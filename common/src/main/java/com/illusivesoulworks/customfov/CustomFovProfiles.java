/*
 * Copyright (C) 2018-2023 Illusive Soulworks
 *
 * Custom FOV is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Custom FOV is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Custom FOV.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.customfov;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mojang.blaze3d.platform.InputConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CustomFovProfiles {

  private static final Map<Integer, Map<String, Object>> PROFILES = new HashMap<>();
  private static int activeProfile = 1;
  private static KeyMapping toggleKey;
  private static int cooldown = 0;
  private static boolean passedInitialRead = false;

  public static KeyMapping registerKeys() {
    toggleKey = new KeyMapping("key.customfov.profile.desc", InputConstants.UNKNOWN.getValue(),
        "key.customfov.category");
    return toggleKey;
  }

  public static void tick() {
    Minecraft mc = Minecraft.getInstance();

    if (mc.player != null && mc.level != null && cooldown <= 0 && toggleKey.isDown()) {
      cooldown = 20;
      Player player = mc.player;
      Map<String, Object> currentSettings = new LinkedHashMap<>();
      Map<String, OptionInstance<?>> options = CustomFovMod.getOptions();
      options.put("fov", Minecraft.getInstance().options.fov());
      options.forEach((key, option) -> currentSettings.put(key, option.get()));
      PROFILES.put(activeProfile, currentSettings);

      if (activeProfile == 1) {
        activeProfile = 2;
      } else if (activeProfile == 2) {
        activeProfile = 1;
      }
      Map<String, Object> map = PROFILES.get(activeProfile);

      if (map != null) {
        map.forEach((key, value) -> {

          if (key.equals("fov")) {
            Minecraft.getInstance().options.fov().set((Integer) value);
          } else {
            ((OptionInstance<Object>) options.get(key)).set(value);
          }
        });
        player.displayClientMessage(
            Component.translatable("key.customfov.profile.switch", activeProfile), true);
      } else {
        CustomFovConstants.LOG.error("Attempted to switch to missing profile {}", activeProfile);
      }
    }

    if (cooldown > 0) {
      cooldown--;
    }
  }

  private static void initProfiles(Map<String, OptionInstance<?>> options) {
    options.forEach((key, option) -> {
      PROFILES.computeIfAbsent(1, (k) -> new LinkedHashMap<>()).put(key, option.get());

      Object def = switch (key) {
        case "fov" -> 70;
        case "fovEffectsMode" -> CustomFovMod.FovEffectsMode.ALL;
        default -> 1.0D;
      };
      PROFILES.computeIfAbsent(2, (k) -> new LinkedHashMap<>()).put(key, def);
    });
  }

  public static void setupProfiles() {
    File file = new File(Minecraft.getInstance().gameDirectory, "customfov.txt");
    Map<String, OptionInstance<?>> options = CustomFovMod.getOptions();
    options.put("fov", Minecraft.getInstance().options.fov());
    int profile = 1;
    passedInitialRead = true;

    if (!file.exists()) {
      initProfiles(options);
      return;
    } else {
      Map<Integer, Map<String, String>> read = new LinkedHashMap<>();

      try (BufferedReader bufferedreader = Files.newReader(file, Charsets.UTF_8)) {
        bufferedreader.lines().forEach((line) -> {
          try {
            String[] split = line.split(":");
            read.computeIfAbsent(Integer.parseInt(split[0]), (k) -> new LinkedHashMap<>())
                .put(split[1], split[2]);
          } catch (Exception exception1) {
            CustomFovConstants.LOG.warn("Skipping malformed profile: {}", line);
          }
        });
      } catch (IOException e) {
        CustomFovConstants.LOG.error("Could not read {}", file, e);
      }
      Map<String, String> profileMap = read.getOrDefault(0, new HashMap<>());

      try {
        profile = Integer.parseInt(profileMap.getOrDefault("profile", "1"));
      } catch (NumberFormatException e) {
        CustomFovConstants.LOG.error("{} is not a valid profile", profileMap.get("profile"));
      }
      read.forEach((prof, map) -> {
        map.forEach((key, value) -> {
          Map<String, Object> prof1 = PROFILES.computeIfAbsent(prof, (k) -> new LinkedHashMap<>());

          try {
            if (key.equals("fov")) {
              prof1.put(key, Integer.parseInt(value));
            } else if (key.equals("fovEffectsMode")) {
              prof1.put(key, CustomFovMod.FovEffectsMode.valueOf(value));
            } else {
              prof1.put(key, Double.parseDouble(value));
            }
          } catch (Exception e) {
            CustomFovConstants.LOG.warn("Skipping malformed value {} for {}", value, key);
          }
        });
      });
    }
    activeProfile = profile;
  }

  public static void saveProfiles() {

    if (!passedInitialRead) {
      return;
    }
    File file = new File(Minecraft.getInstance().gameDirectory, "customfov.txt");

    try (final PrintWriter printwriter = new PrintWriter(
        new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
      printwriter.println("0:profile:" + activeProfile);
      PROFILES.forEach((profile, map) -> {

        if (profile == 0) {
          return;
        }
        map.forEach((key, value) -> printwriter.println(profile + ":" + key + ":" + value));
      });
    } catch (Exception e) {
      CustomFovConstants.LOG.error("Failed to save profiles", e);
    }
  }
}

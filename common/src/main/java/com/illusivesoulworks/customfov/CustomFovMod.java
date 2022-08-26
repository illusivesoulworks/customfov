/*
 * Copyright (C) 2018-2022 Illusive Soulworks
 *
 * Custom FoV is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Custom FoV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Custom FoV.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.customfov;

import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.OptionEnum;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FogType;

public class CustomFovMod {

  private static final OptionInstance<Double> SUBMERGED =
      createFovOption("submergedFovEffectScale");
  private static final OptionInstance<Double> FLYING =
      createFovOption("flyingFovEffectScale");
  private static final OptionInstance<Double> SPRINTING =
      createFovOption("sprintingFovEffectScale");
  private static final OptionInstance<Double> SPEED =
      createFovOption("speedFovEffectScale");
  private static final OptionInstance<Double> AIMING =
      createFovOption("aimingFovEffectScale");
  private static final OptionInstance<FovEffectsMode> FOV_EFFECTS_MODE =
      new OptionInstance<>("customfov.options.fovEffectsMode", (client) -> {
        List<FormattedCharSequence> list1 = splitTooltip(client,
            Component.translatable("customfov.options.fovEffectsMode.none.tooltip"));
        List<FormattedCharSequence> list2 = splitTooltip(client,
            Component.translatable("customfov.options.fovEffectsMode.vanillaOnly.tooltip"));
        List<FormattedCharSequence> list3 = splitTooltip(client,
            Component.translatable("customfov.options.fovEffectsMode.moddedOnly.tooltip"));
        List<FormattedCharSequence> list4 = splitTooltip(client,
            Component.translatable("customfov.options.fovEffectsMode.all.tooltip"));
        return (val) -> switch (val) {
          case NONE -> list1;
          case VANILLA_ONLY -> list2;
          case MODDED_ONLY -> list3;
          case ALL -> list4;
        };
      }, OptionInstance.forOptionEnum(),
          new OptionInstance.Enum<>(Arrays.asList(FovEffectsMode.values()),
              Codec.INT.xmap(FovEffectsMode::byId, FovEffectsMode::getId)),
          FovEffectsMode.ALL, (val) -> {
      });

  private static List<FormattedCharSequence> splitTooltip(Minecraft client, Component component) {
    return client.font.split(component, 200);
  }

  private static OptionInstance<Double> createFovOption(String keyIn) {
    String key = "customfov.options." + keyIn;
    return new OptionInstance<>(key,
        OptionInstance.cachedConstantTooltip(Component.translatable(key + ".tooltip")),
        (component, val) -> val == 0.0D ? Component.translatable("options.generic_value", component,
            CommonComponents.OPTION_OFF) :
            Component.translatable("options.percent_value", component, (int) (val * 100.0D)),
        OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt),
        Codec.doubleRange(0.0D, 1.0D), 1.0D, (val) -> {
    });
  }

  public static Map<String, OptionInstance<?>> getOptions() {
    Map<String, OptionInstance<?>> result = new LinkedHashMap<>();
    result.put("fovEffectsMode", FOV_EFFECTS_MODE);
    result.put("speedFovEffectScale", SPEED);
    result.put("sprintingFovEffectScale", SPRINTING);
    result.put("aimingFovEffectScale", AIMING);
    result.put("flyingFovEffectScale", FLYING);
    result.put("submergedFovEffectScale", SUBMERGED);
    return result;
  }

  public static OptionInstance<?>[] getList() {
    return getOptions().values().toArray(new OptionInstance[]{});
  }

  private static boolean isScoping() {
    Minecraft mc = Minecraft.getInstance();
    LocalPlayer player = mc.player;
    return player != null && mc.options.getCameraType().isFirstPerson() && player.isScoping();
  }

  private static Float fovModifier;

  public static float preComputeFovModifier(float currentFovModifier, boolean scale) {
    FovEffectsMode mode = FOV_EFFECTS_MODE.get();
    float result = 1.0F;

    if (mode == FovEffectsMode.VANILLA_ONLY || mode == FovEffectsMode.ALL) {
      fovModifier = getCustomFovModifier(currentFovModifier);

      if (scale) {
        fovModifier = (float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F,
            fovModifier);
      }
      result = fovModifier;
    }
    return result;
  }

  public static float postComputeFovModifier(float currentFovModifier, boolean scale) {
    float result =
        FOV_EFFECTS_MODE.get() == FovEffectsMode.VANILLA_ONLY ? fovModifier : currentFovModifier;

    if (scale) {
      result =
          (float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, result);
    }
    return result;
  }

  public static Optional<Double> computeFov(Camera camera, double currentFov) {
    FogType fogType = camera.getFluidInCamera();

    if (fogType != FogType.LAVA && fogType != FogType.WATER) {
      return Optional.empty();
    }
    FovEffectsMode mode = FOV_EFFECTS_MODE.get();
    float originalModifier =
        (float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0D,
            0.85714287F);
    double originalFOV = currentFov / originalModifier;

    if (mode == FovEffectsMode.NONE || mode == FovEffectsMode.MODDED_ONLY) {
      return Optional.of(originalFOV);
    }
    return Optional.of(
        originalFOV * (1.0F - (1.0F - originalModifier) * SUBMERGED.get()));
  }

  public static float getCustomFovModifier(float currentFovModifier) {
    Player player = Minecraft.getInstance().player;

    if (player == null) {
      return currentFovModifier;
    }
    float modifier = 1.0F;

    if (player.getAbilities().flying) {
      modifier *= 1.0F + 0.1F * FLYING.get();
    }
    AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

    if (attribute != null) {
      float value = (float) attribute.getValue();
      float walkingSpeed = player.getAbilities().getWalkingSpeed();

      if (value != walkingSpeed) {
        double speedModifier = SPEED.get();

        if (player.isSprinting()) {
          float effects = (value / 1.30000001192092896F) - walkingSpeed;
          float sprint = 0.30000001192092896F;
          effects *= speedModifier;
          sprint *= SPRINTING.get();
          double modified = (walkingSpeed + effects) * (1.0F + sprint);
          modifier = (float) ((double) modifier * (modified / walkingSpeed + 1.0F) / 2.0F);
        } else {
          float effects =
              (float) ((value - walkingSpeed) * speedModifier);
          modifier =
              (float) ((double) modifier * ((effects + walkingSpeed) / walkingSpeed + 1.0F) / 2.0F);
        }
      }
    }

    if (player.getAbilities().getWalkingSpeed() == 0.0F || Float.isNaN(modifier) || Float
        .isInfinite(modifier)) {
      modifier = 1.0F;
    }
    ItemStack itemstack = player.getUseItem();

    if (player.isUsingItem()) {

      if (itemstack.is(Items.BOW)) {
        int i = player.getTicksUsingItem();
        float f1 = (float) i / 20.0F;

        if (f1 > 1.0F) {
          f1 = 1.0F;
        } else {
          f1 = f1 * f1;
        }
        modifier *= 1.0F - (f1 * 0.15F) * AIMING.get();
      } else if (isScoping()) {
        modifier *= 0.1F;
      }
    }
    return modifier;
  }

  public enum FovEffectsMode implements OptionEnum {
    NONE(0, "customfov.options.fovEffectsMode.none"),
    VANILLA_ONLY(1, "customfov.options.fovEffectsMode.vanillaOnly"),
    MODDED_ONLY(2, "customfov.options.fovEffectsMode.moddedOnly"),
    ALL(3, "customfov.options.fovEffectsMode.all");

    private static final FovEffectsMode[] BY_ID =
        Arrays.stream(values()).sorted(Comparator.comparingInt(FovEffectsMode::getId))
            .toArray(FovEffectsMode[]::new);
    private final int id;
    private final String key;

    FovEffectsMode(int id, String key) {
      this.id = id;
      this.key = key;
    }

    public int getId() {
      return this.id;
    }

    @Nonnull
    public String getKey() {
      return this.key;
    }

    public static FovEffectsMode byId(int id) {
      return BY_ID[Mth.positiveModulo(id, BY_ID.length)];
    }
  }
}

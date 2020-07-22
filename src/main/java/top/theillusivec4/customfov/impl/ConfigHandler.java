/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Custom FoV, a mod made for Minecraft.
 *
 * Custom FoV is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Custom FoV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Custom FoV.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.customfov.impl;

import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigTreeBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import top.theillusivec4.customfov.config.CustomFovConfig;
import top.theillusivec4.customfov.config.CustomFovConfig.FovPermission;

public class ConfigHandler {

  static final PropertyMirror<FovPermission> FOV_PERMISSION = PropertyMirror
      .create(ConfigTypes.makeEnum(FovPermission.class));

  static final PropertyMirror<Double> FLYING_MODIFIER = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> FLYING_MAX = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> FLYING_MIN = PropertyMirror.create(ConfigTypes.DOUBLE);

  static final PropertyMirror<Double> AIMING_MODIFIER = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> AIMING_MAX = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> AIMING_MIN = PropertyMirror.create(ConfigTypes.DOUBLE);

  static final PropertyMirror<Double> SPRINTING_MODIFIER = PropertyMirror
      .create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> SPRINTING_MAX = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> SPRINTING_MIN = PropertyMirror.create(ConfigTypes.DOUBLE);

  static final PropertyMirror<Double> EFFECTS_MODIFIER = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> EFFECTS_MAX = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> EFFECTS_MIN = PropertyMirror.create(ConfigTypes.DOUBLE);

  static final PropertyMirror<Double> UNDERWATER_MODIFIER = PropertyMirror
      .create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> UNDERWATER_MAX = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> UNDERWATER_MIN = PropertyMirror.create(ConfigTypes.DOUBLE);

  private static final ConfigTree INSTANCE;

  static {
    ConfigTreeBuilder builder = ConfigTree.builder();

    builder
        .beginValue("fovPermission", ConfigTypes.makeEnum(FovPermission.class), FovPermission.ALL)
        .withComment(
            "Determines which source is allowed to change FoV" + "\nNONE - No FoV changes allowed"
                + "\nVANILLA - Only vanilla FoV changes will be applied"
                + "\nMODDED - Only modded FoV changes will be applied"
                + "\nALL - All FoV changes will be applied").finishValue(FOV_PERMISSION::mirror);

    ConfigTreeBuilder flying = builder.fork("flying");

    flying.beginValue("flyingModifier", ConfigTypes.DOUBLE, 1.0D)
        .withComment("The modifier to multiply by the original FoV modifier")
        .finishValue(FLYING_MODIFIER::mirror);

    flying.beginValue("flyingMax", ConfigTypes.DOUBLE, 10.0D)
        .withComment("The maximum FoV flying modifier value").finishValue(FLYING_MAX::mirror);

    flying.beginValue("flyingMin", ConfigTypes.DOUBLE, -10.0D)
        .withComment("The minimum FoV flying modifier value").finishValue(FLYING_MIN::mirror);

    flying.finishBranch();

    ConfigTreeBuilder aiming = builder.fork("aiming");

    aiming.beginValue("aimingModifier", ConfigTypes.DOUBLE, 1.0D)
        .withComment("The modifier to multiply by the original FoV modifier")
        .finishValue(AIMING_MODIFIER::mirror);

    aiming.beginValue("aimingMax", ConfigTypes.DOUBLE, 10.0D)
        .withComment("The maximum FoV aiming modifier value").finishValue(AIMING_MAX::mirror);

    aiming.beginValue("aimingMin", ConfigTypes.DOUBLE, -10.0D)
        .withComment("The minimum FoV flying modifier value").finishValue(AIMING_MIN::mirror);

    aiming.finishBranch();

    ConfigTreeBuilder sprinting = builder.fork("sprinting");

    sprinting.beginValue("sprintingModifier", ConfigTypes.DOUBLE, 1.0D)
        .withComment("The modifier to multiply by the original FoV modifier")
        .finishValue(SPRINTING_MODIFIER::mirror);

    sprinting.beginValue("sprintingMax", ConfigTypes.DOUBLE, 10.0D)
        .withComment("The maximum FoV sprinting modifier value").finishValue(SPRINTING_MAX::mirror);

    sprinting.beginValue("sprintingMin", ConfigTypes.DOUBLE, -10.0D)
        .withComment("The minimum FoV sprinting modifier value").finishValue(SPRINTING_MIN::mirror);

    sprinting.finishBranch();

    ConfigTreeBuilder effects = builder.fork("effects");

    effects.beginValue("effectsModifier", ConfigTypes.DOUBLE, 1.0D)
        .withComment("The modifier to multiply by the original FoV modifier")
        .finishValue(EFFECTS_MODIFIER::mirror);

    effects.beginValue("effectsMax", ConfigTypes.DOUBLE, 10.0D)
        .withComment("The maximum FoV effects modifier value").finishValue(EFFECTS_MAX::mirror);

    effects.beginValue("aimingMin", ConfigTypes.DOUBLE, -10.0D)
        .withComment("The minimum FoV effects modifier value").finishValue(EFFECTS_MIN::mirror);

    effects.finishBranch();

    ConfigTreeBuilder underwater = builder.fork("underwater");

    underwater.beginValue("underwaterModifier", ConfigTypes.DOUBLE, 1.0D)
        .withComment("The modifier to multiply by the original FoV modifier")
        .finishValue(UNDERWATER_MODIFIER::mirror);

    underwater.beginValue("underwaterMax", ConfigTypes.DOUBLE, 10.0D)
        .withComment("The maximum FoV underwater modifier value")
        .finishValue(UNDERWATER_MAX::mirror);

    underwater.beginValue("underwaterMin", ConfigTypes.DOUBLE, -10.0D)
        .withComment("The minimum FoV underwater modifier value")
        .finishValue(UNDERWATER_MIN::mirror);

    underwater.finishBranch();

    INSTANCE = builder.build();
  }

  public static void init() {
    JanksonValueSerializer serializer = new JanksonValueSerializer(false);
    Path path = Paths.get("config", CustomFovMod.MODID + ".json5");

    try (OutputStream stream = new BufferedOutputStream(
        Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW))) {
      FiberSerialization.serialize(INSTANCE, stream, serializer);
    } catch (FileAlreadyExistsException e) {
      CustomFovMod.LOGGER.debug("Custom FoV config already exists, skipping new config creation...");
    } catch (IOException e) {
      CustomFovMod.LOGGER.error("Error serializing new config!");
      e.printStackTrace();
    }

    try (InputStream stream = new BufferedInputStream(
        Files.newInputStream(path, StandardOpenOption.READ, StandardOpenOption.CREATE))) {
      FiberSerialization.deserialize(INSTANCE, stream, serializer);
    } catch (IOException | ValueDeserializationException e) {
      CustomFovMod.LOGGER.error("Error deserializing config!");
      e.printStackTrace();
    }
    bake();
  }

  private static void bake() {
    CustomFovConfig.setFovPermission(FOV_PERMISSION.getValue());

    CustomFovConfig.setFlyingModifier(FLYING_MODIFIER.getValue());
    CustomFovConfig.setFlyingMax(FLYING_MAX.getValue());
    CustomFovConfig.setFlyingMin(FLYING_MIN.getValue());

    CustomFovConfig.setAimingModifier(AIMING_MODIFIER.getValue());
    CustomFovConfig.setAimingMax(AIMING_MAX.getValue());
    CustomFovConfig.setAimingMin(AIMING_MIN.getValue());

    CustomFovConfig.setSprintingModifier(SPRINTING_MODIFIER.getValue());
    CustomFovConfig.setSprintingMax(SPRINTING_MAX.getValue());
    CustomFovConfig.setSprintingMin(SPRINTING_MIN.getValue());

    CustomFovConfig.setEffectsModifier(EFFECTS_MODIFIER.getValue());
    CustomFovConfig.setEffectsMax(EFFECTS_MAX.getValue());
    CustomFovConfig.setEffectsMin(EFFECTS_MIN.getValue());

    CustomFovConfig.setUnderwaterModifier(UNDERWATER_MODIFIER.getValue());
    CustomFovConfig.setUnderwaterMax(UNDERWATER_MAX.getValue());
    CustomFovConfig.setUnderwaterMin(UNDERWATER_MIN.getValue());
  }
}

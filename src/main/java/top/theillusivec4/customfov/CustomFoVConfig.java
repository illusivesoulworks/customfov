/*
 * Copyright (C) 2018-2019  C4
 *
 * This file is part of CustomFoV, a mod made for Minecraft.
 *
 * CustomFoV is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CustomFoV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with CustomFoV.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.customfov;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import org.apache.commons.lang3.tuple.Pair;

public class CustomFoVConfig {

  static final ForgeConfigSpec CONFIG_SPEC;
  static final Config CONFIG;
  private static final String CONFIG_PREFIX = "gui." + CustomFoV.MODID + ".config.";

  static {
    final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
        .configure(Config::new);
    CONFIG_SPEC = specPair.getRight();
    CONFIG = specPair.getLeft();
  }

  public static FovChangePermission fovChangePermission;
  //Flying
  public static double flyingModifier;
  public static double flyingMax;
  public static double flyingMin;
  //Aiming
  public static double aimingModifier;
  public static double aimingMax;
  public static double aimingMin;
  //Sprinting
  public static double sprintingModifier;
  public static double sprintingMax;
  public static double sprintingMin;
  //Effects
  public static double effectsModifier;
  public static double effectsMax;
  public static double effectsMin;
  //Underwater
  public static double underwaterModifier;
  public static double underwaterMax;
  public static double underwaterMin;

  public static void bake() {
    fovChangePermission = CONFIG.fovChangePermission.get();

    flyingModifier = CONFIG.flyingModifier.get();
    flyingMax = CONFIG.flyingMax.get();
    flyingMin = CONFIG.flyingMin.get();

    aimingModifier = CONFIG.aimingModifier.get();
    aimingMax = CONFIG.aimingMax.get();
    aimingMin = CONFIG.aimingMin.get();

    sprintingModifier = CONFIG.sprintingModifier.get();
    sprintingMax = CONFIG.sprintingMax.get();
    sprintingMin = CONFIG.sprintingMin.get();

    effectsModifier = CONFIG.effectsModifier.get();
    effectsMax = CONFIG.effectsMax.get();
    effectsMin = CONFIG.effectsMin.get();

    underwaterModifier = CONFIG.underwaterModifier.get();
    underwaterMax = CONFIG.underwaterMax.get();
    underwaterMin = CONFIG.underwaterMin.get();
  }

  public static class Config {

    final EnumValue<FovChangePermission> fovChangePermission;
    //Flying
    final DoubleValue flyingModifier;
    final DoubleValue flyingMax;
    final DoubleValue flyingMin;
    //Aiming
    final DoubleValue aimingModifier;
    final DoubleValue aimingMax;
    final DoubleValue aimingMin;
    //Sprinting
    final DoubleValue sprintingModifier;
    final DoubleValue sprintingMax;
    final DoubleValue sprintingMin;
    //Effects
    final DoubleValue effectsModifier;
    final DoubleValue effectsMax;
    final DoubleValue effectsMin;
    //Underwater
    final DoubleValue underwaterModifier;
    final DoubleValue underwaterMax;
    final DoubleValue underwaterMin;

    public Config(ForgeConfigSpec.Builder builder) {

      fovChangePermission = builder.comment(
          "Determines which source is allowed to change FoV" + "\nNONE - No FoV changes allowed"
              + "\nVANILLA - Only vanilla FoV changes will be applied"
              + "\nMODDED - Only modded FoV changes will be applied"
              + "\nALL - All FoV changes will be applied")
          .translation(CONFIG_PREFIX + "fovChangePermission")
          .defineEnum("fovChangePermission", FovChangePermission.ALL);

      builder.push("flying");

      flyingModifier = builder.comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "flyingModifier")
          .defineInRange("flyingModifier", 1.0d, 0.0d, 10.0d);

      flyingMax = builder.comment("The maximum FoV flying modifier value")
          .translation(CONFIG_PREFIX + "flyingMax")
          .defineInRange("flyingMax", 10.0d, -10.0d, 10.0d);

      flyingMin = builder.comment("The minimum FoV flying modifier value")
          .translation(CONFIG_PREFIX + "flyingMin")
          .defineInRange("flyingMin", -10.0d, -10.0d, 10.0d);

      builder.pop();

      builder.push("aiming");

      aimingModifier = builder.comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "aimingModifier")
          .defineInRange("aimingModifier", 1.0d, 0.0d, 10.0d);

      aimingMax = builder.comment("The maximum FoV aiming modifier value")
          .translation(CONFIG_PREFIX + "aimingMax")
          .defineInRange("aimingMax", 10.0d, -10.0d, 10.0d);

      aimingMin = builder.comment("The minimum FoV aiming modifier value")
          .translation(CONFIG_PREFIX + "aimingMin")
          .defineInRange("aimingMin", -10.0d, -10.0d, 10.0d);

      builder.pop();

      builder.push("sprinting");

      sprintingModifier = builder.comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "sprintingModifier")
          .defineInRange("sprintingModifier", 1.0d, 0.0d, 10.0d);

      sprintingMax = builder.comment("The maximum FoV sprinting modifier value")
          .translation(CONFIG_PREFIX + "sprintingMax")
          .defineInRange("sprintingMax", 10.0d, -10.0d, 10.0d);

      sprintingMin = builder.comment("The minimum FoV sprinting modifier value")
          .translation(CONFIG_PREFIX + "sprintingMin")
          .defineInRange("sprintingMin", -10.0d, -10.0d, 10.0d);

      builder.pop();

      builder.push("speedEffects");

      effectsModifier = builder.comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "effectsModifier")
          .defineInRange("effectsModifier", 1.0d, 0.0d, 10.0d);

      effectsMax = builder.comment("The maximum FoV effects modifier value")
          .translation(CONFIG_PREFIX + "effectsMax")
          .defineInRange("effectsMax", 10.0d, -10.0d, 10.0d);

      effectsMin = builder.comment("The minimum FoV effects modifier value")
          .translation(CONFIG_PREFIX + "effectsMin")
          .defineInRange("effectsMin", -10.0d, -10.0d, 10.0d);

      builder.pop();

      builder.push("underwater");

      underwaterModifier = builder.comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "underwaterModifier")
          .defineInRange("underwaterModifier", 1.0d, 0.0d, 10.0d);

      underwaterMax = builder.comment("The maximum FoV underwater modifier value")
          .translation(CONFIG_PREFIX + "underwaterMax")
          .defineInRange("underwaterMax", 10.0d, -10.0d, 10.0d);

      underwaterMin = builder.comment("The minimum FoV underwater modifier value")
          .translation(CONFIG_PREFIX + "underwaterMin")
          .defineInRange("underwaterMin", -10.0d, -10.0d, 10.0d);

      builder.pop();
    }
  }

  public enum FovChangePermission {
    NONE, VANILLA, MODDED, ALL
  }
}

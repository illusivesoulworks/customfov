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

package top.theillusivec4.customfov.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import top.theillusivec4.customfov.CustomFoV;

public class FoVConfig {

  public static final ForgeConfigSpec spec;
  static final BooleanValue staticFoV;
  static final BooleanValue superStaticFoV;
  //Flying
  static final DoubleValue flyingModifier;
  static final DoubleValue flyingMax;
  static final DoubleValue flyingMin;
  //Aiming
  static final DoubleValue aimingModifier;
  static final DoubleValue aimingMax;
  static final DoubleValue aimingMin;
  //Sprinting
  static final DoubleValue sprintingModifier;
  static final DoubleValue sprintingMax;
  static final DoubleValue sprintingMin;
  //Effects
  static final DoubleValue effectsModifier;
  static final DoubleValue effectsMax;
  static final DoubleValue effectsMin;
  //Underwater
  static final DoubleValue underwaterModifier;
  static final DoubleValue underwaterMax;
  static final DoubleValue underwaterMin;
  private static final String CONFIG_PREFIX = "gui." + CustomFoV.MODID + ".config.";

  static {
    Builder builder = new Builder();

    {
      staticFoV = builder
          .comment("Set to true to disable all vanilla FoV modifiers")
          .translation(CONFIG_PREFIX + "staticFoV")
          .define("staticFoV", false);

      superStaticFoV = builder
          .comment("Set to true to disable all FoV modifiers, including modded ones")
          .translation(CONFIG_PREFIX + "superStaticFoV")
          .define("superStaticFoV", false);
    }

    {
      builder.push("Flying");

      flyingModifier = builder
          .comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "flyingModifier")
          .defineInRange("flyingModifier", 1.0d, 0.0d, 10.0d);

      flyingMax = builder
          .comment("The maximum FoV flying modifier value")
          .translation(CONFIG_PREFIX + "flyingMax")
          .defineInRange("flyingMax", 10.0d, -10.0d, 10.0d);

      flyingMin = builder
          .comment("The minimum FoV flying modifier value")
          .translation(CONFIG_PREFIX + "flyingMin")
          .defineInRange("flyingMin", -10.0d, -10.0d, 10.0d);

      builder.pop();
    }

    {
      builder.push("Aiming");

      aimingModifier = builder
          .comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "aimingModifier")
          .defineInRange("aimingModifier", 1.0d, 0.0d, 10.0d);

      aimingMax = builder
          .comment("The maximum FoV aiming modifier value")
          .translation(CONFIG_PREFIX + "aimingMax")
          .defineInRange("aimingMax", 10.0d, -10.0d, 10.0d);

      aimingMin = builder
          .comment("The minimum FoV aiming modifier value")
          .translation(CONFIG_PREFIX + "aimingMin")
          .defineInRange("aimingMin", -10.0d, -10.0d, 10.0d);

      builder.pop();
    }

    {
      builder.push("Sprinting");

      sprintingModifier = builder
          .comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "sprintingModifier")
          .defineInRange("sprintingModifier", 1.0d, 0.0d, 10.0d);

      sprintingMax = builder
          .comment("The maximum FoV sprinting modifier value")
          .translation(CONFIG_PREFIX + "sprintingMax")
          .defineInRange("sprintingMax", 10.0d, -10.0d, 10.0d);

      sprintingMin = builder
          .comment("The minimum FoV sprinting modifier value")
          .translation(CONFIG_PREFIX + "sprintingMin")
          .defineInRange("sprintingMin", -10.0d, -10.0d, 10.0d);

      builder.pop();
    }

    {
      builder.push("Speed Effects");

      effectsModifier = builder
          .comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "effectsModifier")
          .defineInRange("effectsModifier", 1.0d, 0.0d, 10.0d);

      effectsMax = builder
          .comment("The maximum FoV effects modifier value")
          .translation(CONFIG_PREFIX + "effectsMax")
          .defineInRange("effectsMax", 10.0d, -10.0d, 10.0d);

      effectsMin = builder
          .comment("The minimum FoV effects modifier value")
          .translation(CONFIG_PREFIX + "effectsMin")
          .defineInRange("effectsMin", -10.0d, -10.0d, 10.0d);

      builder.pop();
    }

    {
      builder.push("Underwater");

      underwaterModifier = builder
          .comment("The modifier to multiply by the original FoV modifier")
          .translation(CONFIG_PREFIX + "underwaterModifier")
          .defineInRange("underwaterModifier", 1.0d, 0.0d, 10.0d);

      underwaterMax = builder
          .comment("The maximum FoV underwater modifier value")
          .translation(CONFIG_PREFIX + "underwaterMax")
          .defineInRange("underwaterMax", 10.0d, -10.0d, 10.0d);

      underwaterMin = builder
          .comment("The minimum FoV underwater modifier value")
          .translation(CONFIG_PREFIX + "underwaterMin")
          .defineInRange("underwaterMin", -10.0d, -10.0d, 10.0d);

      builder.pop();
    }

    spec = builder.build();
  }
}

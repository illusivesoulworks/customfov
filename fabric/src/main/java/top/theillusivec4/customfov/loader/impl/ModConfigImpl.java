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

package top.theillusivec4.customfov.loader.impl;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.customfov.core.CustomFov;
import top.theillusivec4.customfov.core.ModConfig;

@Config(name = CustomFov.MODID)
public class ModConfigImpl implements ConfigData, ModConfig {

  @ConfigEntry.Gui.Tooltip(count = 5)
  @Comment("Determines which source is allowed to change FoV\n" + "NONE: No FoV changes allowed\n"
      + "VANILLA: Only vanilla FoV changes will be allowed\n"
      + "MODDED: Only modded FoV changes will be allowed\n"
      + "ALL: All FoV changes will be applied")
  public FovPermission fovPermission = FovPermission.ALL;

  @ConfigEntry.Gui.CollapsibleObject
  @ConfigEntry.Gui.Tooltip
  @Comment("FoV settings for flying")
  public FovConfig flying = new FovConfig();

  @ConfigEntry.Gui.CollapsibleObject
  @ConfigEntry.Gui.Tooltip
  @Comment("FoV settings for sprinting")
  public FovConfig sprinting = new FovConfig();

  @ConfigEntry.Gui.CollapsibleObject
  @ConfigEntry.Gui.Tooltip
  @Comment("FoV settings for speed effects")
  public FovConfig effects = new FovConfig();

  @ConfigEntry.Gui.CollapsibleObject
  @ConfigEntry.Gui.Tooltip
  @Comment("FoV settings for aiming")
  public FovConfig aiming = new FovConfig();

  @ConfigEntry.Gui.CollapsibleObject
  @ConfigEntry.Gui.Tooltip
  @Comment("FoV settings for underwater vision")
  public FovConfig underwater = new FovConfig();

  @Override
  public FovPermission getFovPermission() {
    return fovPermission;
  }

  @Override
  public double getBoundFov(double value, FovType fovType) {
    FovConfig config = null;

    switch (fovType) {
      case AIMING:
        config = aiming;
        break;
      case FLYING:
        config = flying;
        break;
      case SPRINTING:
        config = sprinting;
        break;
      case EFFECTS:
        config = effects;
        break;
      case UNDERWATER:
        config = underwater;
        break;
      default:
        break;
    }
    return getBoundValue(value, config);
  }

  private double getBoundValue(double value, FovConfig config) {
    return MathHelper.clamp(value * config.modifier, config.min, config.max);
  }

  private static class FovConfig {

    @ConfigEntry.Gui.Tooltip
    @Comment("The modifier to multiply by the original FoV modifier")
    private double modifier = 1.0D;
    @ConfigEntry.Gui.Tooltip
    @Comment("The minimum FoV modifier value")
    private double min = -10.0D;
    @ConfigEntry.Gui.Tooltip
    @Comment("The maximum FoV modifier value")
    private double max = 10.0D;
  }
}

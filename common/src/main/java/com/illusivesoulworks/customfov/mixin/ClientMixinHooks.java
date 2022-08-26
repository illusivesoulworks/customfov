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

package com.illusivesoulworks.customfov.mixin;

import com.illusivesoulworks.customfov.CustomFovMod;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import org.apache.commons.lang3.ArrayUtils;

public class ClientMixinHooks {

  public static void addFovOptions(OptionsList list) {
    list.addSmall(CustomFovMod.getList());
  }

  public static OptionInstance<?>[] addFovOptions(OptionInstance<?>[] smallOptions) {
    return ArrayUtils.addAll(smallOptions, CustomFovMod.getList());
  }
}

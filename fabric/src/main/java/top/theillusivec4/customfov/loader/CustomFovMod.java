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

package top.theillusivec4.customfov.loader;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import top.theillusivec4.customfov.core.CustomFov;
import top.theillusivec4.customfov.core.ModConfig;
import top.theillusivec4.customfov.loader.impl.ModConfigImpl;

public class CustomFovMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ModConfig config =
        AutoConfig.register(ModConfigImpl.class, JanksonConfigSerializer::new).getConfig();
    CustomFov.getInstance().setConfig(config);
  }
}

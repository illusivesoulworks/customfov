/*
 * Copyright (C) 2018-2021 C4
 *
 * This file is part of Custom FoV.
 *
 * Custom FoV is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Custom FoV is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Custom FoV.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package top.theillusivec4.customfov;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

@Mod(CustomFovMod.MOD_ID)
public class CustomFovMod {

  public static final String MOD_ID = "customfov";

  public CustomFovMod() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::setupClient);
    eventBus.addListener(this::config);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CustomFovConfig.CONFIG_SPEC);
    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY,
            (a, b) -> true));
  }

  private void setupClient(final FMLClientSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(new CustomFovEventListener());
  }

  private void config(final ModConfigEvent evt) {

    if (evt.getConfig().getModId().equals(MOD_ID)) {
      CustomFovConfig.bake();
    }
  }
}

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

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CustomFovConstants.MOD_ID)
public class CustomFovForgeMod {

  public CustomFovForgeMod(FMLJavaModLoadingContext context) {
    FMLClientSetupEvent.getBus(context.getModBusGroup()).addListener(this::clientSetup);
    RegisterKeyMappingsEvent.BUS.addListener(this::registerKeys);
    context.registerExtensionPoint(IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY,
            (a, b) -> true));
  }

  private void registerKeys(final RegisterKeyMappingsEvent evt) {
    evt.register(CustomFovProfiles.registerKeys());
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(ClientEventsListener.class);
    CustomFovProfiles.setupProfiles();
  }
}
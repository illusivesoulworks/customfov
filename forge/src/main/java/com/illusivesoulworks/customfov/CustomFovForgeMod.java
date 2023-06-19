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

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

@Mod(CustomFovConstants.MOD_ID)
public class CustomFovForgeMod {

  public CustomFovForgeMod() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::registerKeys);
    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY,
            (a, b) -> true));
  }

  private void registerKeys(final RegisterKeyMappingsEvent evt) {
    evt.register(CustomFovMod.registerKeys());
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    ClientEventsListener.setup();
    CustomFovMod.setupProfiles();
  }
}
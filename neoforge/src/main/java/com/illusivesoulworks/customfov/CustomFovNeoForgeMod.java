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

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(CustomFovConstants.MOD_ID)
public class CustomFovNeoForgeMod {

  public CustomFovNeoForgeMod(IEventBus eventBus) {
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::registerKeys);
    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY,
            (a, b) -> true));
  }

  private void registerKeys(final RegisterKeyMappingsEvent evt) {
    evt.register(CustomFovProfiles.registerKeys());
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    ClientEventsListener.setup();
    CustomFovProfiles.setupProfiles();
  }
}
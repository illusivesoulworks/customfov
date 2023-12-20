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

import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;

public class ClientEventsListener {

  public static void setup() {
    NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ClientEventsListener::preComputeFov);
    NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ClientEventsListener::postComputeFov);
    NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ClientEventsListener::viewportFov);
    NeoForge.EVENT_BUS.addListener(ClientEventsListener::tick);
  }

  private static void tick(final TickEvent.ClientTickEvent evt) {
    CustomFovProfiles.tick();
  }

  private static void preComputeFov(final ComputeFovModifierEvent evt) {
    evt.setNewFovModifier(CustomFovMod.preComputeFovModifier(evt.getFovModifier(), true));
  }

  private static void postComputeFov(final ComputeFovModifierEvent evt) {
    evt.setNewFovModifier(CustomFovMod.postComputeFovModifier(evt.getNewFovModifier(), false));
  }

  private static void viewportFov(final ViewportEvent.ComputeFov evt) {
    CustomFovMod.computeFov(evt.getCamera(), evt.getFOV()).ifPresent(evt::setFOV);
  }
}

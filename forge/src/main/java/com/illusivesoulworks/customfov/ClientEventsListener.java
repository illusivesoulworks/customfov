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

import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;

public class ClientEventsListener {

  public static void setup() {
    MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST,
        ClientEventsListener::preComputeFov);
    MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,
        ClientEventsListener::postComputeFov);
    MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ClientEventsListener::viewportFov);
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

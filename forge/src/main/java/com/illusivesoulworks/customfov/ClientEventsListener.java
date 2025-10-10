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

import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

public class ClientEventsListener {

  @SubscribeEvent
  public static void tick(final TickEvent.ClientTickEvent.Pre evt) {
    CustomFovProfiles.tick();
  }

  @SubscribeEvent(priority = Priority.HIGHEST)
  public static void preComputeFov(final ComputeFovModifierEvent evt) {
    evt.setNewFovModifier(CustomFovMod.preComputeFovModifier(evt.getFovModifier(), true));
  }

  @SubscribeEvent(priority = Priority.LOWEST)
  public static void postComputeFov(final ComputeFovModifierEvent evt) {
    evt.setNewFovModifier(CustomFovMod.postComputeFovModifier(evt.getNewFovModifier(), false));
  }

  @SubscribeEvent(priority = Priority.HIGHEST)
  public static void viewportFov(final ViewportEvent.ComputeFov evt) {
    CustomFovMod.computeFov(evt.getCamera(), evt.getFOV()).ifPresent(evt::setFOV);
  }
}

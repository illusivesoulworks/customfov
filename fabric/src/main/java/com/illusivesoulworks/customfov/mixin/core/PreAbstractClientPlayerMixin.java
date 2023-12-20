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

package com.illusivesoulworks.customfov.mixin.core;

import com.illusivesoulworks.customfov.CustomFovMod;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = AbstractClientPlayer.class, priority = 100)
public class PreAbstractClientPlayerMixin {

  @ModifyVariable(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/client/Options.fovEffectScale()Lnet/minecraft/client/OptionInstance;"
      ),
      method = "getFieldOfViewModifier",
      ordinal = 0
  )
  private float customfov$getFieldOfViewModifier(float fovModifier) {
    return CustomFovMod.preComputeFovModifier(fovModifier, false);
  }
}

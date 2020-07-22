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

package top.theillusivec4.customfov.impl.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.customfov.CustomFovHooks;

@Mixin(value = AbstractClientPlayerEntity.class, priority = 10000)
public abstract class PostAbstractClientPlayerEntityMixin extends PlayerEntity {

  public PostAbstractClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
    super(world, world.getSpawnPos(), profile);
  }

  @Inject(at = @At("TAIL"), method = "getSpeed()F", cancellable = true)
  private void getSpeed(CallbackInfoReturnable<Float> cb) {
    CustomFovHooks.getResetSpeed().ifPresent(cb::setReturnValue);
  }

  @Shadow
  public abstract boolean isSpectator();

  @Shadow
  public abstract boolean isCreative();
}

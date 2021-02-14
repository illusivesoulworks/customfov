/*
 * Copyright (C) 2018-2019  C4
 *
 * This file is part of CustomFoV, a mod made for Minecraft.
 *
 * CustomFoV is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CustomFoV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with CustomFoV.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.customfov;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.customfov.CustomFoVConfig.FovChangePermission;

public class CustomFoVEventHandler {

  private static float fovModifier;

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void foVUpdatePre(FOVUpdateEvent evt) {

    if (CustomFoVConfig.fovChangePermission != FovChangePermission.NONE) {

      if (CustomFoVConfig.fovChangePermission == FovChangePermission.MODDED) {
        evt.setNewfov(1.0F);
      } else {
        fovModifier = getNewFovModifier();
        evt.setNewfov(fovModifier);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void fovUpdatePost(FOVUpdateEvent evt) {

    if (CustomFoVConfig.fovChangePermission == FovChangePermission.NONE) {
      evt.setNewfov(1.0F);
    } else if (CustomFoVConfig.fovChangePermission == FovChangePermission.VANILLA) {
      evt.setNewfov(fovModifier);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onFoVModifier(EntityViewRenderEvent.FOVModifier evt) {
    FluidState fluidstate = evt.getInfo().getFluidState();

    if (fluidstate.isEmpty()) {
      return;
    }
    float originalModifier = 60.0F / 70.0F;
    double originalFOV = evt.getFOV() / originalModifier;

    if (CustomFoVConfig.fovChangePermission == FovChangePermission.NONE
        || CustomFoVConfig.fovChangePermission == FovChangePermission.MODDED) {
      evt.setFOV(originalFOV);
    } else {
      evt.setFOV(originalFOV * (1.0F - getConfiguredValue((1.0F - originalModifier),
          CustomFoVConfig.underwaterModifier, CustomFoVConfig.underwaterMax,
          CustomFoVConfig.underwaterMin)));
    }
  }

  private float getNewFovModifier() {
    PlayerEntity player = Minecraft.getInstance().player;
    float modifier = 1.0F;

    if (player == null) {
      return 0.0F;
    }

    if (player.abilities.isFlying) {
      modifier *=
          1.0F + getConfiguredValue(0.1F, CustomFoVConfig.flyingModifier, CustomFoVConfig.flyingMax,
              CustomFoVConfig.flyingMin);
    }
    ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

    if (attribute != null) {
      float value = (float) attribute.getValue();
      float walkingSpeed = player.abilities.getWalkSpeed();

      if (value != walkingSpeed) {

        if (player.isSprinting()) {
          float effects = (value / 1.30000001192092896F) - walkingSpeed;
          float sprint = 0.30000001192092896F;
          effects = getConfiguredValue(effects, CustomFoVConfig.effectsModifier,
              CustomFoVConfig.effectsMax, CustomFoVConfig.effectsMin);
          sprint = getConfiguredValue(sprint, CustomFoVConfig.sprintingModifier,
              CustomFoVConfig.sprintingMax, CustomFoVConfig.sprintingMin);
          double modified = (walkingSpeed + effects) * (1.0F + sprint);
          modifier = (float) ((double) modifier * (modified / walkingSpeed + 1.0F) / 2.0F);
        } else {
          float effects = getConfiguredValue(value - walkingSpeed, CustomFoVConfig.effectsModifier,
              CustomFoVConfig.effectsMax, CustomFoVConfig.effectsMin);
          modifier =
              (float) ((double) modifier * ((effects + walkingSpeed) / walkingSpeed + 1.0F) /
                  2.0F);
        }
      }
    }

    if (player.abilities.getWalkSpeed() == 0.0F || Float.isNaN(modifier) || Float
        .isInfinite(modifier)) {
      modifier = 1.0F;
    }

    if (player.isHandActive() && player.getActiveItemStack().getItem() == Items.BOW) {
      int i = player.getItemInUseMaxCount();
      float f1 = (float) i / 20.0F;

      if (f1 > 1.0F) {
        f1 = 1.0F;
      } else {
        f1 = f1 * f1;
      }
      modifier *= 1.0F - getConfiguredValue(f1 * 0.15F, CustomFoVConfig.aimingModifier,
          CustomFoVConfig.aimingMax, CustomFoVConfig.aimingMin);
    }
    return modifier;
  }

  private float getConfiguredValue(float original, double modifier, double max, double min) {
    return (float) MathHelper.clamp(original * modifier, min, max);
  }
}

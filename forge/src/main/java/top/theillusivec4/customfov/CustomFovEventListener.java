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

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.customfov.CustomFovConfig.FovChangePermission;

@SuppressWarnings("unused")
public class CustomFovEventListener {

  private static float fovModifier;

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void foVUpdatePre(FOVUpdateEvent evt) {

    if (CustomFovConfig.fovChangePermission != FovChangePermission.NONE) {

      if (CustomFovConfig.fovChangePermission == FovChangePermission.MODDED) {
        evt.setNewfov(1.0F);
      } else {
        fovModifier = getNewFovModifier();
        evt.setNewfov(fovModifier);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void fovUpdatePost(FOVUpdateEvent evt) {

    if (CustomFovConfig.fovChangePermission == FovChangePermission.NONE) {
      evt.setNewfov(1.0F);
    } else if (CustomFovConfig.fovChangePermission == FovChangePermission.VANILLA) {
      evt.setNewfov(fovModifier);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onFoVModifier(EntityViewRenderEvent.FOVModifier evt) {
    FogType fogType = evt.getInfo().getFluidInCamera();

    if (fogType != FogType.LAVA && fogType != FogType.WATER) {
      return;
    }
    float originalModifier = 60.0F / 70.0F;
    double originalFOV = evt.getFOV() / originalModifier;

    if (CustomFovConfig.fovChangePermission == FovChangePermission.NONE
        || CustomFovConfig.fovChangePermission == FovChangePermission.MODDED) {
      evt.setFOV(originalFOV);
    } else {
      evt.setFOV(originalFOV * (1.0F -
          getConfiguredValue((1.0F - originalModifier), CustomFovConfig.underwaterModifier,
              CustomFovConfig.underwaterMax, CustomFovConfig.underwaterMin)));
    }
  }

  private float getNewFovModifier() {
    Player player = Minecraft.getInstance().player;
    float modifier = 1.0F;

    if (player == null) {
      return 0.0F;
    }

    if (player.getAbilities().flying) {
      modifier *= 1.0F +
          getConfiguredValue(0.1F, CustomFovConfig.flyingModifier, CustomFovConfig.flyingMax,
              CustomFovConfig.flyingMin);
    }
    AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

    if (attribute != null) {
      float value = (float) attribute.getValue();
      float walkingSpeed = player.getAbilities().getWalkingSpeed();

      if (value != walkingSpeed) {

        if (player.isSprinting()) {
          float effects = (value / 1.30000001192092896F) - walkingSpeed;
          float sprint = 0.30000001192092896F;
          effects = getConfiguredValue(effects, CustomFovConfig.effectsModifier,
              CustomFovConfig.effectsMax, CustomFovConfig.effectsMin);
          sprint = getConfiguredValue(sprint, CustomFovConfig.sprintingModifier,
              CustomFovConfig.sprintingMax, CustomFovConfig.sprintingMin);
          double modified = (walkingSpeed + effects) * (1.0F + sprint);
          modifier = (float) ((double) modifier * (modified / walkingSpeed + 1.0F) / 2.0F);
        } else {
          float effects = getConfiguredValue(value - walkingSpeed, CustomFovConfig.effectsModifier,
              CustomFovConfig.effectsMax, CustomFovConfig.effectsMin);
          modifier =
              (float) ((double) modifier * ((effects + walkingSpeed) / walkingSpeed + 1.0F) /
                  2.0F);
        }
      }
    }

    if (player.getAbilities().getWalkingSpeed() == 0.0F || Float.isNaN(modifier) || Float
        .isInfinite(modifier)) {
      modifier = 1.0F;
    }
    ItemStack itemstack = player.getUseItem();

    if (player.isUsingItem()) {

      if (itemstack.is(Items.BOW)) {
        int i = player.getTicksUsingItem();
        float f1 = (float) i / 20.0F;

        if (f1 > 1.0F) {
          f1 = 1.0F;
        } else {
          f1 = f1 * f1;
        }
        modifier *= 1.0F - getConfiguredValue(f1 * 0.15F, CustomFovConfig.aimingModifier,
            CustomFovConfig.aimingMax, CustomFovConfig.aimingMin);
      } else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.isScoping()) {
        modifier *= 0.1F;
      }
    }
    return modifier;
  }

  private float getConfiguredValue(float original, double modifier, double max, double min) {
    return (float) Mth.clamp(original * modifier, min, max);
  }
}

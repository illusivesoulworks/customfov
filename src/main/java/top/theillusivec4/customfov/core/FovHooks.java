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

package top.theillusivec4.customfov.core;

import java.util.Optional;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Items;
import top.theillusivec4.customfov.core.ModConfig.FovPermission;
import top.theillusivec4.customfov.core.ModConfig.FovType;

public class FovHooks {

  private static float modifiedSpeed;

  public static Optional<Float> getResetSpeed() {
    ModConfig config = CustomFov.getInstance().getConfig();
    FovPermission fovPermission = config.getFovPermission();

    if (fovPermission == FovPermission.NONE) {
      return Optional.of(1.0F);
    } else if (fovPermission == FovPermission.VANILLA) {
      return Optional.of(modifiedSpeed);
    }
    return Optional.empty();
  }

  public static Optional<Double> getModifiedFov(Camera camera, double fov) {
    FluidState fluidstate = camera.getSubmergedFluidState();

    if (fluidstate.isEmpty()) {
      return Optional.empty();
    }
    float originalModifier = 60.0F / 70.0F;
    double originalFOV = fov / originalModifier;
    ModConfig config = CustomFov.getInstance().getConfig();
    FovPermission fovPermission = config.getFovPermission();

    if (fovPermission == FovPermission.NONE || fovPermission == FovPermission.MODDED) {
      return Optional.of(originalFOV);
    } else {
      return Optional.of(originalFOV * (1.0F - config
          .getBoundFov(1.0F - originalModifier, FovType.UNDERWATER)));
    }
  }

  public static Optional<Float> getModifiedSpeed(PlayerEntity playerEntity) {
    ModConfig config = CustomFov.getInstance().getConfig();
    FovPermission fovPermission = config.getFovPermission();

    if (fovPermission != FovPermission.NONE) {

      if (fovPermission == FovPermission.MODDED) {
        return Optional.of(1.0F);
      } else {
        float modifier = 1.0F;

        if (playerEntity.abilities.flying) {
          modifier *= 1.0F + config.getBoundFov(0.1F, FovType.FLYING);
        }
        EntityAttributeInstance attribute = playerEntity
            .getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        if (attribute != null) {
          float value = (float) attribute.getValue();
          float walkingSpeed = playerEntity.abilities.getWalkSpeed();

          if (value != walkingSpeed) {

            if (playerEntity.isSprinting()) {
              double effects = (value / 1.30000001192092896F) - walkingSpeed;
              double sprint = 0.30000001192092896F;
              effects = config.getBoundFov(effects, FovType.EFFECTS);
              sprint = config.getBoundFov(sprint, FovType.SPRINTING);
              double modified = (walkingSpeed + effects) * (1.0F + sprint);
              modifier = (float) ((double) modifier * (modified / walkingSpeed + 1.0F) / 2.0F);
            } else {
              double effects = config.getBoundFov(value - walkingSpeed, FovType.EFFECTS);
              modifier =
                  (float) ((double) modifier * ((effects + walkingSpeed) / walkingSpeed + 1.0F) /
                      2.0F);
            }
          }
        }

        if (playerEntity.abilities.getWalkSpeed() == 0.0F || Float.isNaN(modifier) || Float
            .isInfinite(modifier)) {
          modifier = 1.0F;
        }

        if (playerEntity.isUsingItem() && playerEntity.getActiveItem().getItem() == Items.BOW) {
          int i = playerEntity.getItemUseTime();
          float g = (float) i / 20.0F;

          if (g > 1.0F) {
            g = 1.0F;
          } else {
            g *= g;
          }
          modifier *= 1.0F - config.getBoundFov(g * 0.15F, FovType.AIMING);
        }
        modifiedSpeed = modifier;
        return Optional.of(modifiedSpeed);
      }
    }
    return Optional.empty();
  }
}

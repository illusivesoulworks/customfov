package top.theillusivec4.customfov;

import java.util.Optional;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.customfov.config.CustomFovConfig;
import top.theillusivec4.customfov.config.CustomFovConfig.FovPermission;

public class CustomFovHooks {

  private static float modifiedSpeed;

  public static Optional<Float> getResetSpeed() {

    if (CustomFovConfig.getFovPermission() == FovPermission.NONE) {
      return Optional.of(1.0F);
    } else if (CustomFovConfig.getFovPermission() == FovPermission.VANILLA) {
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

    if (CustomFovConfig.getFovPermission() == FovPermission.NONE
        || CustomFovConfig.getFovPermission() == FovPermission.MODDED) {
      return Optional.of(originalFOV);
    } else {
      return Optional.of(originalFOV * (1.0F - getConfiguredValue((1.0F - originalModifier),
          CustomFovConfig.getUnderwaterModifier(), CustomFovConfig.getUnderwaterMax(),
          CustomFovConfig.getUnderwaterMin())));
    }
  }

  public static Optional<Float> getModifiedSpeed(PlayerEntity playerEntity) {

    if (CustomFovConfig.getFovPermission() != FovPermission.NONE) {

      if (CustomFovConfig.getFovPermission() == FovPermission.MODDED) {
        return Optional.of(1.0F);
      } else {
        float modifier = 1.0F;

        if (playerEntity.abilities.flying) {
          modifier *= 1.0F + getConfiguredValue(0.1F, CustomFovConfig.getFlyingModifier(),
              CustomFovConfig.getFlyingMax(), CustomFovConfig.getFlyingMin());
        }
        EntityAttributeInstance attribute = playerEntity
            .getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        if (attribute != null) {
          float speedModifier = (float) (
              (attribute.getValue() / (double) playerEntity.abilities.getWalkSpeed() + 1.0D)
                  / 2.0D);
          float value = (float) attribute.getValue();
          float effPercent = Math.abs(
              (value / (playerEntity.isSprinting() ? 1.3F : 1.0F) - playerEntity.abilities
                  .getWalkSpeed()) / (value - playerEntity.abilities.getWalkSpeed()));
          float configModifier = getConfiguredValue(effPercent * (speedModifier - 1),
              CustomFovConfig.getEffectsModifier(), CustomFovConfig.getEffectsMax(),
              CustomFovConfig.getEffectsMin());

          if (playerEntity.isSprinting()) {
            float sprintPercent = 1.0F - effPercent;
            float sprintModifier = getConfiguredValue(sprintPercent * (speedModifier - 1),
                CustomFovConfig.getSprintingModifier(), CustomFovConfig.getSprintingMax(),
                CustomFovConfig.getSprintingMin());
            configModifier += sprintModifier;
          }
          modifier = (float) ((double) modifier * (1.0D + configModifier));
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
          modifier *= 1.0F - getConfiguredValue(g * 0.15F, CustomFovConfig.getAimingModifier(),
              CustomFovConfig.getAimingMax(), CustomFovConfig.getAimingMin());
        }
        modifiedSpeed = modifier;
        return Optional.of(modifiedSpeed);
      }
    }
    return Optional.empty();
  }

  private static float getConfiguredValue(float original, double modifier, double max, double min) {
    return (float) MathHelper.clamp(original * modifier, min, max);
  }
}

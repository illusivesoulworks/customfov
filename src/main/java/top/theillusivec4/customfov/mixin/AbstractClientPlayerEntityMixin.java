package top.theillusivec4.customfov.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.customfov.CustomFoVConfig;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

  public AbstractClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
    super(world, world.getSpawnPos(), profile);
  }

  @Inject(at = @At("TAIL"), method = "getSpeed()F", cancellable = true)
  private void getSpeed(CallbackInfoReturnable<Float> cb) {
    float modifier = 1.0F;

    if (this.abilities.flying) {
      modifier *=
          1.0F + getConfiguredValue(0.1F, CustomFoVConfig.flyingModifier, CustomFoVConfig.flyingMax,
              CustomFoVConfig.flyingMin);
    }
    modifier = (float) ((double) modifier * (
        (this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) / (double) this.abilities
            .getWalkSpeed() + 1.0D) / 2.0D));

    if (this.abilities.getWalkSpeed() == 0.0F || Float.isNaN(modifier) || Float
        .isInfinite(modifier)) {
      modifier = 1.0F;
    }

    if (this.isUsingItem() && this.getActiveItem().getItem() == Items.BOW) {
      int i = this.getItemUseTime();
      float g = (float) i / 20.0F;

      if (g > 1.0F) {
        g = 1.0F;
      } else {
        g *= g;
      }

      modifier *= 1.0F - g * 0.15F;
    }
    cb.setReturnValue(modifier);
  }

  @Shadow
  public abstract boolean isSpectator();

  @Shadow
  public abstract boolean isCreative();

  private float getConfiguredValue(float original, double modifier, double max, double min) {
    return (float) MathHelper.clamp(original * modifier, min, max);
  }
}

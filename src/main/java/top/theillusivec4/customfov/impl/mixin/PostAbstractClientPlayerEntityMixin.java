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

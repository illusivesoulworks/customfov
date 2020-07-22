package top.theillusivec4.customfov.impl.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.customfov.CustomFovHooks;

@Mixin(value = GameRenderer.class, priority = 10)
public class GameRendererMixin {

  @Inject(at = @At("TAIL"), method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", cancellable = true)
  public void getFov(Camera camera, float tickDelta, boolean changingFov,
      CallbackInfoReturnable<Double> cb) {
    CustomFovHooks.getModifiedFov(camera, cb.getReturnValue()).ifPresent(cb::setReturnValue);
  }
}

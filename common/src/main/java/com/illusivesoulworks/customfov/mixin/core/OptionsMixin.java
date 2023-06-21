package com.illusivesoulworks.customfov.mixin.core;

import com.illusivesoulworks.customfov.CustomFovProfiles;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {

  @Inject(at = @At("TAIL"), method = "save")
  private void customfov$save(CallbackInfo ci) {
    CustomFovProfiles.saveProfiles();
  }
}

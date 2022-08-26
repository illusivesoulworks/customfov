/*
 * Copyright (C) 2018-2022 Illusive Soulworks
 *
 * Custom FoV is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Custom FoV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Custom FoV.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.customfov.mixin.core;

import com.illusivesoulworks.customfov.mixin.ClientMixinHooks;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleOptionsSubScreen.class)
public class SimpleOptionsSubScreenMixin {

  @Shadow
  @Final
  @Mutable
  protected OptionInstance<?>[] smallOptions;

  @Inject(at = @At("TAIL"), method = "<init>")
  private void customfov$editOptions(Screen screen, Options options, Component title, OptionInstance<?>[] smallOptions, CallbackInfo ci) {

    if ((Object) this instanceof AccessibilityOptionsScreen) {
      this.smallOptions = ClientMixinHooks.addFovOptions(this.smallOptions);
    }
  }

//  @Inject(at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screens/SimpleOptionsSubScreen.addWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"), method = "init")
//  private void customfov$addAccessibilityOptions(CallbackInfo ci) {
//    ClientMixinHooks.addFovOptions((SimpleOptionsSubScreen) (Object) this, this.list);
//  }
}

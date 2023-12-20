/*
 * Copyright (C) 2018-2023 Illusive Soulworks
 *
 * Custom FOV is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Custom FOV is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Custom FOV.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.customfov.mixin.core;

import com.illusivesoulworks.customfov.mixin.ClientMixinHooks;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
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
public abstract class SimpleOptionsSubScreenMixin extends OptionsSubScreen {

  @Shadow
  @Final
  @Mutable
  protected OptionInstance<?>[] smallOptions;

  public SimpleOptionsSubScreenMixin(Screen $$0, Options $$1, Component $$2) {
    super($$0, $$1, $$2);
  }

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "<init>")
  private void customfov$editOptions(Screen screen, Options options, Component title, OptionInstance<?>[] smallOptions, CallbackInfo ci) {

    if (((Object) this) instanceof AccessibilityOptionsScreen) {
      this.smallOptions = ClientMixinHooks.addFovOptions(this.smallOptions);
    }
  }
}

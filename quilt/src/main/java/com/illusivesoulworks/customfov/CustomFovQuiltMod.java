package com.illusivesoulworks.customfov;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public class CustomFovQuiltMod implements ClientModInitializer {

  @Override
  public void onInitializeClient(ModContainer modContainer) {
    KeyBindingHelper.registerKeyBinding(CustomFovProfiles.registerKeys());
    ClientTickEvents.END.register(client -> CustomFovProfiles.tick());
    ClientLifecycleEvents.READY.register(client -> CustomFovProfiles.setupProfiles());
  }
}

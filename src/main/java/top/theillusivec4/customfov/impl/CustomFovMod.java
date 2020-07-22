package top.theillusivec4.customfov.impl;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomFovMod implements ClientModInitializer {

  public static final String MODID = "customfov";
  public static final Logger LOGGER = LogManager.getLogger();

  @Override
  public void onInitializeClient() {
    ConfigHandler.init();
  }
}

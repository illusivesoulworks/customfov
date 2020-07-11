package top.theillusivec4.customfov;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomFoV implements ClientModInitializer {

  public static final String MODID = "customfov";
  public static final Logger LOGGER = LogManager.getLogger();

  @Override
  public void onInitializeClient() {
    CustomFoVConfig.init();
  }
}

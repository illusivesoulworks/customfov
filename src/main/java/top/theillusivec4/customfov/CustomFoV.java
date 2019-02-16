package top.theillusivec4.customfov;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.customfov.core.EventHandler;
import top.theillusivec4.customfov.core.FoVConfig;

@Mod(CustomFoV.MODID)
public class CustomFoV {

    public static final String MODID = "customfov";

    public CustomFoV() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FoVConfig.spec);
    }

    private void setupClient(final FMLClientSetupEvent evt) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}

package c4.customfov;

import c4.customfov.core.EventHandlerClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Level;

@Mod(   modid = CustomFoV.MODID,
        name = CustomFoV.NAME,
        version = "@VERSION@",
        dependencies = "required-after:forge@[14.23.5.2768,)",
        acceptedMinecraftVersions = "[1.12, 1.13)",
        certificateFingerprint = "@FINGERPRINT@",
        clientSideOnly = true,
        guiFactory = "c4.customfov.client.gui.SortedGuiConfigFactory")
public class CustomFoV {

    public static final String MODID = "customfov";
    public static final String NAME = "Custom FoV";

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }

    @EventHandler
    public void onFingerPrintViolation(FMLFingerprintViolationEvent evt) {
        FMLLog.log.log(Level.ERROR, "Invalid fingerprint detected! The file " + evt.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}

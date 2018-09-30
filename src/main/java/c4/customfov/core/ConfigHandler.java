package c4.customfov.core;

import c4.customfov.CustomFoV;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = CustomFoV.MODID)
public class ConfigHandler {

    @Name("Static FoV")
    @Comment("Set to true to disable all vanilla FoV modifiers")
    public static boolean staticFoV = false;

    @Name("Flying")
    @Comment("Configure flying FoV settings")
    public static final Flying flying = new Flying();

    @Name("Aiming")
    @Comment("Configure aiming FoV settings")
    public static final Aiming aiming = new Aiming();

    @Name("Speed")
    @Comment("Configure speed FoV settings")
    public static final Speed speed = new Speed();

    public static class Flying {

        @Name("Modifier")
        @Comment("The modifier to multiply by the original FoV modifier")
        @RangeDouble(min = 0.0D)
        public double modifier = 1.0D;

        @Name("Maximum Value")
        @Comment("The maximum FoV flying modifier value, -1 for no maximum")
        @RangeDouble(min = -1.0D)
        public double maxValue = -1.0D;
    }

    public static class Aiming {

        @Name("Modifier")
        @Comment("The modifier to multiply by the original FoV modifier")
        @RangeDouble(min = 0.0D)
        public double modifier = 1.0D;

        @Name("Maximum Value")
        @Comment("The maximum FoV aiming modifier value, -1 for no maximum")
        @RangeDouble(min = -1.0D)
        public double maxValue = -1.0D;
    }

    public static class Speed {

        @Name("Sprinting")
        @Comment("Configure sprinting FoV settings")
        public final Sprinting sprinting = new Sprinting();

        @Name("Effects")
        @Comment("Configure speed potion effects FoV settings")
        public final Effects effects = new Effects();

        public class Sprinting {

            @Name("Modifier")
            @Comment("The modifier to multiply by the original FoV modifier")
            @RangeDouble(min = 0.0D)
            public double modifier = 1.0D;

            @Name("Maximum Value")
            @Comment("The maximum FoV sprinting modifier value, -1 for no maximum")
            @RangeDouble(min = -1.0D)
            public double maxValue = -1.0D;
        }

        public class Effects {

            @Name("Modifier")
            @Comment("The modifier to multiply by the original FoV modifier")
            @RangeDouble(min = 0.0D)
            public double modifier = 1.0D;

            @Name("Maximum Value")
            @Comment("The maximum FoV effects modifier value, -1 for no maximum")
            @RangeDouble(min = -1.0D)
            public double maxValue = -1.0D;
        }
    }

    @Mod.EventBusSubscriber(modid = CustomFoV.MODID)
    private static class ConfigEventHandler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent evt) {
            if (evt.getModID().equals(CustomFoV.MODID)) {
                ConfigManager.sync(CustomFoV.MODID, Config.Type.INSTANCE);
            }
        }
    }
}

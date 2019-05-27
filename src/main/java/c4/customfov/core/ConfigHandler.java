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

    @Name("Super Static FoV")
    @Comment("Set to true to disable all FoV modifiers, including modded ones")
    public static boolean superStaticFoV = false;

    @Name("Flying")
    @Comment("Configure flying FoV settings")
    public static final Flying flying = new Flying();

    @Name("Aiming")
    @Comment("Configure aiming FoV settings")
    public static final Aiming aiming = new Aiming();

    @Name("Speed")
    @Comment("Configure speed FoV settings")
    public static final Speed speed = new Speed();

    @Name("Underwater")
    @Comment("Configure underwater FoV settings")
    public static final Underwater underwater = new Underwater();

    public static class Flying {

        @Name("Modifier")
        @Comment("The modifier to multiply by the original FoV modifier")
        public double modifier = 1.0D;

        @Name("Maximum Value")
        @Comment("The maximum FoV flying modifier value")
        @RangeDouble(min = -Double.MAX_VALUE)
        public double maxValue = 10.0D;

        @Name("Minimum Value")
        @Comment("The minimum FoV flying modifier value")
        @RangeDouble(min = -Double.MAX_VALUE)
        public double minValue = -10.0D;
    }

    public static class Aiming {

        @Name("Modifier")
        @Comment("The modifier to multiply by the original FoV modifier")
        public double modifier = 1.0D;

        @Name("Maximum Value")
        @Comment("The maximum FoV aiming modifier value")
        @RangeDouble(min = -Double.MAX_VALUE)
        public double maxValue = 10.0D;

        @Name("Minimum Value")
        @Comment("The minimum FoV aiming modifier value")
        @RangeDouble(min = -Double.MAX_VALUE)
        public double minValue = -10.0D;
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
            public double modifier = 1.0D;

            @Name("Maximum Value")
            @Comment("The maximum FoV sprinting modifier value")
            @RangeDouble(min = -Double.MAX_VALUE)
            public double maxValue = 10.0D;

            @Name("Minimum Value")
            @Comment("The minimum FoV sprinting modifier value")
            @RangeDouble(min = -Double.MAX_VALUE)
            public double minValue = -10.0D;
        }

        public class Effects {

            @Name("Modifier")
            @Comment("The modifier to multiply by the original FoV modifier")
            public double modifier = 1.0D;

            @Name("Maximum Value")
            @Comment("The maximum FoV effects modifier value")
            @RangeDouble(min = -Double.MAX_VALUE)
            public double maxValue = 10.0D;

            @Name("Minimum Value")
            @Comment("The minimum FoV effects modifier value")
            @RangeDouble(min = -Double.MAX_VALUE)
            public double minValue = -10.0D;
        }
    }

    public static class Underwater {

        @Name("Modifier")
        @Comment("The modifier to multiply by the original FoV modifier")
        public double modifier = 1.0D;

        @Name("Maximum Value")
        @Comment("The maximum FoV underwater modifier value")
        @RangeDouble(min = -Double.MAX_VALUE)
        public double maxValue = 10.0D;

        @Name("Minimum Value")
        @Comment("The minimum FoV underwater modifier value")
        @RangeDouble(min = -Double.MAX_VALUE)
        public double minValue = -10.0D;
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

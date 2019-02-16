package top.theillusivec4.customfov.core;

import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import org.apache.commons.lang3.tuple.Pair;
import top.theillusivec4.customfov.CustomFoV;

public class FoVConfig {

    static final BooleanValue staticFoV;

    //Flying
    static final DoubleValue flyingModifier;
    static final DoubleValue flyingMax;
    static final DoubleValue flyingMin;

    //Aiming
    static final DoubleValue aimingModifier;
    static final DoubleValue aimingMax;
    static final DoubleValue aimingMin;

    //Sprinting
    static final DoubleValue sprintingModifier;
    static final DoubleValue sprintingMax;
    static final DoubleValue sprintingMin;

    //Effects
    static final DoubleValue effectsModifier;
    static final DoubleValue effectsMax;
    static final DoubleValue effectsMin;

    //Underwater
    static final DoubleValue underwaterModifier;
    static final DoubleValue underwaterMax;
    static final DoubleValue underwaterMin;

    public static final ForgeConfigSpec spec;

    private static final String CONFIG_PREFIX = "gui." + CustomFoV.MODID + ".config.";

    static {
        Builder builder = new Builder();

        {
            staticFoV = builder
                    .comment("Set to true to disable all vanilla FoV modifiers")
                    .translation(CONFIG_PREFIX + "staticFoV")
                    .define("staticFoV", false);
        }

        {
            builder.push("Flying");

            flyingModifier = builder
                    .comment("The modifier to multiply by the original FoV modifier")
                    .translation(CONFIG_PREFIX + "flyingModifier")
                    .defineInRange("flyingModifier", 1.0d, 0.0d, 10.0d);

            flyingMax = builder
                    .comment("The maximum FoV flying modifier value")
                    .translation(CONFIG_PREFIX + "flyingMax")
                    .defineInRange("flyingMax", 10.0d, -10.0d, 10.0d);

            flyingMin = builder
                    .comment("The minimum FoV flying modifier value")
                    .translation(CONFIG_PREFIX + "flyingMin")
                    .defineInRange("flyingMin", -10.0d, -10.0d, 10.0d);

            builder.pop();
        }

        {
            builder.push("Aiming");

            aimingModifier = builder
                    .comment("The modifier to multiply by the original FoV modifier")
                    .translation(CONFIG_PREFIX + "aimingModifier")
                    .defineInRange("aimingModifier", 1.0d, 0.0d, 10.0d);

            aimingMax = builder
                    .comment("The maximum FoV aiming modifier value")
                    .translation(CONFIG_PREFIX + "aimingMax")
                    .defineInRange("aimingMax", 10.0d, -10.0d, 10.0d);

            aimingMin = builder
                    .comment("The minimum FoV aiming modifier value")
                    .translation(CONFIG_PREFIX + "aimingMin")
                    .defineInRange("aimingMin", -10.0d, -10.0d, 10.0d);

            builder.pop();
        }

        {
            builder.push("Sprinting");

            sprintingModifier = builder
                    .comment("The modifier to multiply by the original FoV modifier")
                    .translation(CONFIG_PREFIX + "sprintingModifier")
                    .defineInRange("sprintingModifier", 1.0d, 0.0d, 10.0d);

            sprintingMax = builder
                    .comment("The maximum FoV sprinting modifier value")
                    .translation(CONFIG_PREFIX + "sprintingMax")
                    .defineInRange("sprintingMax", 10.0d, -10.0d, 10.0d);

            sprintingMin = builder
                    .comment("The minimum FoV sprinting modifier value")
                    .translation(CONFIG_PREFIX + "sprintingMin")
                    .defineInRange("sprintingMin", -10.0d, -10.0d, 10.0d);

            builder.pop();
        }

        {
            builder.push("Effects");

            effectsModifier = builder
                    .comment("The modifier to multiply by the original FoV modifier")
                    .translation(CONFIG_PREFIX + "effectsModifier")
                    .defineInRange("effectsModifier", 1.0d, 0.0d, 10.0d);

            effectsMax = builder
                    .comment("The maximum FoV effects modifier value")
                    .translation(CONFIG_PREFIX + "effectsMax")
                    .defineInRange("effectsMax", 10.0d, -10.0d, 10.0d);

            effectsMin = builder
                    .comment("The minimum FoV effects modifier value")
                    .translation(CONFIG_PREFIX + "effectsMin")
                    .defineInRange("effectsMin", -10.0d, -10.0d, 10.0d);

            builder.pop();
        }

        {
            builder.push("Underwater");

            underwaterModifier = builder
                    .comment("The modifier to multiply by the original FoV modifier")
                    .translation(CONFIG_PREFIX + "underwaterModifier")
                    .defineInRange("underwaterModifier", 1.0d, 0.0d, 10.0d);

            underwaterMax = builder
                    .comment("The maximum FoV underwater modifier value")
                    .translation(CONFIG_PREFIX + "underwaterMax")
                    .defineInRange("underwaterMax", 10.0d, -10.0d, 10.0d);

            underwaterMin = builder
                    .comment("The minimum FoV underwater modifier value")
                    .translation(CONFIG_PREFIX + "underwaterMin")
                    .defineInRange("underwaterMin", -10.0d, -10.0d, 10.0d);

            builder.pop();
        }

        spec = builder.build();
    }
}

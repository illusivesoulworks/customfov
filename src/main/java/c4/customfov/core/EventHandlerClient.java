package c4.customfov.core;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerClient {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFoVUpdate(FOVUpdateEvent evt) {
        evt.setNewfov(getNewFovModifier());
    }

    private float getNewFovModifier() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        float modifier = 1.0F;

        if (ConfigHandler.staticFoV) {
            return modifier;
        }

        if (player.capabilities.isFlying) {
            modifier *= 1.0F + getConfiguredValue(0.1F, ConfigHandler.flying.modifier,
                    ConfigHandler.flying.maxValue, ConfigHandler.flying.minValue);
        }

        IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        float speedModifier = (float)((iattributeinstance.getAttributeValue()
                / (double)player.capabilities.getWalkSpeed() + 1.0D) / 2.0D);
        float value = (float)iattributeinstance.getAttributeValue();
        float effPercent = Math.abs((value / (player.isSprinting() ? 1.3F : 1.0F) - player.capabilities.getWalkSpeed())
                / (value - player.capabilities.getWalkSpeed()));
        float configModifier = getConfiguredValue(effPercent * (speedModifier - 1),
                ConfigHandler.speed.effects.modifier, ConfigHandler.speed.effects.maxValue,
                ConfigHandler.speed.effects.minValue);

        if (player.isSprinting()) {
            float sprintPercent = 1.0F - effPercent;
            float sprintModifier = getConfiguredValue(sprintPercent * (speedModifier - 1),
                    ConfigHandler.speed.sprinting.modifier, ConfigHandler.speed.sprinting.maxValue,
                    ConfigHandler.speed.sprinting.minValue);
            configModifier += sprintModifier;
        }
        modifier = (float) ((double) modifier * (1.0D + configModifier));

        if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(modifier) || Float.isInfinite(modifier)) {
            modifier = 1.0F;
        }

        if (player.isHandActive() && player.getActiveItemStack().getItem() == Items.BOW) {
            int i = player.getItemInUseMaxCount();
            float f1 = (float)i / 20.0F;

            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            modifier *= 1.0F - getConfiguredValue(f1 * 0.15F, ConfigHandler.aiming.modifier,
                    ConfigHandler.aiming.maxValue, ConfigHandler.aiming.minValue);
        }

        return modifier;
    }

    private float getConfiguredValue(float original, double modifier, double max, double min) {
        return (float)MathHelper.clamp(original * modifier, min, max);
    }
}

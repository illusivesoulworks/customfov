/*
 * Copyright (C) 2018-2019  C4
 *
 * This file is part of CustomFoV, a mod made for Minecraft.
 *
 * CustomFoV is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CustomFoV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with CustomFoV.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.customfov.core;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFoVUpdate(FOVUpdateEvent evt) {
        evt.setNewfov(getNewFovModifier());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFoVModifier(EntityViewRenderEvent.FOVModifier evt) {
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(Minecraft.getInstance().world, evt.getEntity(),
                (float)evt.getRenderPartialTicks());

        if (iblockstate.getMaterial() == Material.WATER) {
            float originalModifier = 60.0F / 70.0F;
            evt.setFOV(evt.getFOV() / originalModifier * getConfiguredValue(originalModifier, FoVConfig.underwaterModifier.get(),
                    FoVConfig.underwaterMax.get(), FoVConfig.underwaterMin.get()));
        }
    }

    private float getNewFovModifier() {
        EntityPlayer player = Minecraft.getInstance().player;
        float modifier = 1.0F;

        if (FoVConfig.staticFoV.get()) {
            return modifier;
        }

        if (player.abilities.isFlying) {
            modifier *= 1.0F + getConfiguredValue(0.1F, FoVConfig.flyingModifier.get(), FoVConfig.flyingMax.get(),
                    FoVConfig.flyingMin.get());
        }

        IAttributeInstance iattributeinstance = player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        float speedModifier = (float)((iattributeinstance.getValue() / (double)player.abilities.getWalkSpeed() + 1.0D) / 2.0D);
        float value = (float)iattributeinstance.getValue();
        float effPercent = Math.abs((value / (player.isSprinting() ? 1.3F : 1.0F) - player.abilities.getWalkSpeed())
                / (value - player.abilities.getWalkSpeed()));
        float configModifier = getConfiguredValue(effPercent * (speedModifier - 1), FoVConfig.effectsModifier.get(),
                FoVConfig.effectsMax.get(), FoVConfig.effectsMin.get());

        if (player.isSprinting()) {
            float sprintPercent = 1.0F - effPercent;
            float sprintModifier = getConfiguredValue(sprintPercent * (speedModifier - 1), FoVConfig.sprintingModifier.get(),
                    FoVConfig.sprintingMax.get(), FoVConfig.sprintingMin.get());
            configModifier += sprintModifier;
        }
        modifier = (float) ((double) modifier * (1.0D + configModifier));

        if (player.abilities.getWalkSpeed() == 0.0F || Float.isNaN(modifier) || Float.isInfinite(modifier)) {
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

            modifier *= 1.0F - getConfiguredValue(f1 * 0.15F, FoVConfig.aimingModifier.get(), FoVConfig.aimingMax.get(),
                    FoVConfig.aimingMin.get());
        }

        return modifier;
    }

    private float getConfiguredValue(float original, double modifier, double max, double min) {
        return (float)MathHelper.clamp(original * modifier, min, max);
    }
}

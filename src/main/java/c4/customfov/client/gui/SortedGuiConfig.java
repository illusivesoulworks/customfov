package c4.customfov.client.gui;

import c4.customfov.CustomFoV;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class SortedGuiConfig extends GuiConfig {

    public SortedGuiConfig(GuiScreen parentScreen) {
        this(parentScreen, CustomFoV.MODID, false, false, CustomFoV.NAME,
                ConfigManager.getModConfigClasses(CustomFoV.MODID));
    }

    public SortedGuiConfig(GuiScreen parentScreen, String modID, boolean allRequireWorldRestart,
                           boolean allRequireMcRestart, String title, Class<?>... configClasses) {
        super(parentScreen, collectConfigElements(configClasses), modID, null, allRequireWorldRestart,
                allRequireMcRestart, title, null);
    }

    private static List<IConfigElement> collectConfigElements(Class<?>[] configClasses)
    {
        List<IConfigElement> toReturn;
        if(configClasses.length == 1)
        {
            toReturn = ConfigElement.from(configClasses[0]).getChildElements();
        }
        else
        {
            toReturn = new ArrayList<>();
            for(Class<?> clazz : configClasses)
            {
                toReturn.add(ConfigElement.from(clazz));
            }
        }
        return toReturn;
    }
}

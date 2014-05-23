package bspkrs.floatingruins.fml.gui;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.floatingruins.fml.Reference;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.gui.GuiConfig;

public class GuiFRConfig extends GuiConfig
{
    public GuiFRConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, (new ConfigProperty(Reference.config.getCategory(Configuration.CATEGORY_GENERAL))).getConfigPropertiesList(false),
                true, Reference.MODID, true, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
    }
}

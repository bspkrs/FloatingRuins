package bspkrs.floatingruins.fml.gui;

import java.util.Set;

import bspkrs.floatingruins.fml.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class ModGuiFactoryHandler implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft minecraftInstance)
    {

    }

    @Override
    public boolean hasConfigGui()
    {
    	return true;
    }
    
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        return new ConfigGui(parentScreen);
    }

    public static class ConfigGui extends GuiConfig
    {
        public ConfigGui(GuiScreen parent)
        {
        	super(parent, (new ConfigElement(Reference.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
                    Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
        }
    }
    
    //unfortunantly this was removed. it seems the contents of guiFRConfig needs to be moved in here...
//    @Override
//    public Class<? extends GuiScreen> mainConfigGuiClass()
//    {
//        return GuiFRConfig.class;
//    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    // unable to find a use for this method, cant find any reference to getHandlerFor() in old versions of forge
    // deprecated in 1.11.2?
//    @Override
//    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
//    {
//        return null;
//    }
}

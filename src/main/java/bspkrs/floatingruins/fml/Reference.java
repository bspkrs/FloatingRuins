package bspkrs.floatingruins.fml;

import net.minecraftforge.common.config.Configuration;

public class Reference
{
    public static final String  MODID            = "FloatingRuins";
    public static final String  NAME             = "FloatingRuins";
    public static final String  PROXY_COMMON     = "bspkrs.floatingruins.fml.CommonProxy";
    public static final String  PROXY_CLIENT     = "bspkrs.floatingruins.fml.ClientProxy";
    public static final String  GUI_FACTORY      = "bspkrs.floatingruins.fml.gui.ModGuiFactoryHandler";
    
    public static final String  CONFIG_VERSION   = "1.0";
    
    public static final String  CTGY_GEN         = "general";
    public static final String  CTGY_ISLANDS     = CTGY_GEN + ".island_settings";
    public static final String  CTGY_ISLAND_SIZE = CTGY_ISLANDS + ".island_size";
    public static final String  CTGY_DUNGEONS    = CTGY_GEN + ".dungeon_settings";
    
    public static Configuration config           = null;
}
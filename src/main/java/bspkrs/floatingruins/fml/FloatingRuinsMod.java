package bspkrs.floatingruins.fml;

import java.io.File;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.floatingruins.FloatingRuins;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)", useMetadata = true,
        guiFactory = "bspkrs.floatingruins.fml.gui.ModGuiFactoryHandler")
public class FloatingRuinsMod
{
    public ModVersionChecker       versionChecker;
    private final String           versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/floatingRuinsForge.version";
    private final String           mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    @Metadata(value = Reference.MODID)
    public static ModMetadata      metadata;
    
    @Instance(value = Reference.MODID)
    public static FloatingRuinsMod instance;
    
    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy      proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            FloatingRuins.rarity = 44;
          //            FloatingRuins.harderDungeons = true;
          //            FloatingRuins.allowDebugLogging = true;
          //            FloatingRuins.allowInSuperFlat = true;
          //            FloatingRuins.biomeIDBlacklist = "";// "0;1;3;4;5;6;7;8;9;13;17;";
          //            FloatingRuins.baseDepth = 30;
          //            if (file.exists())
          //                file.delete();
        }
        
        FloatingRuins.initConfig(file);
        
        if (!CommonUtils.isObfuscatedEnv())
            FloatingRuins.allowDebugLogging = true;
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // hopefully a million is high enough to be last?
        GameRegistry.registerWorldGenerator(new FloatingRuinsWorldGenerator(), 1000000);
        
        FMLCommonHandler.instance().bus().register(instance);
        
        proxy.registerTickHandler();
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        //event.registerServerCommand(new CommandFRGen());
    }
    
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.modID.equals(Reference.MODID))
        {
            Reference.config.save();
            FloatingRuins.syncConfig(false);
        }
    }
}

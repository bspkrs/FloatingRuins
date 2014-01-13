package bspkrs.floatingruins.fml;

import java.io.File;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.floatingruins.FloatingRuins;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(name = "FloatingRuins", modid = "FloatingRuins", version = FloatingRuins.VERSION_NUMBER, dependencies = "required-after:bspkrsCore", useMetadata = true)
public class FloatingRuinsMod
{
    public ModVersionChecker       versionChecker;
    private final String           versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/floatingRuinsForge.version";
    private final String           mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    @Metadata(value = "FloatingRuins")
    public static ModMetadata      metadata;
    
    @Instance(value = "FloatingRuins")
    public static FloatingRuinsMod instance;
    
    @SidedProxy(clientSide = "bspkrs.floatingruins.fml.ClientProxy", serverSide = "bspkrs.floatingruins.fml.CommonProxy")
    public static CommonProxy      proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            FloatingRuins.rarity = 44;
          //            //FloatingRuins.harderDungeons = true;
          //            FloatingRuins.allowDebugLogging = true;
          //            FloatingRuins.allowInSuperFlat = true;
          //            //FloatingRuins.biomeIDBlacklist = "";// "0;1;3;4;5;6;7;8;9;13;17;";
          //            FloatingRuins.baseDepth = 30;
          //            if (file.exists())
          //                file.delete();
        }
        
        FloatingRuins.loadConfig(file);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // fuck it... hopefully a million is high enough to be last?
        GameRegistry.registerWorldGenerator(new FloatingRuinsWorldGenerator(), 1000000);
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
}

package bspkrs.floatingruins.fml;

import java.io.File;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.floatingruins.FloatingRuins;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;

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

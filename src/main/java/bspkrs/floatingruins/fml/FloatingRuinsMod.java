package bspkrs.floatingruins.fml;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
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
import bspkrs.floatingruins.FloatingRuins;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)", useMetadata = true,
        guiFactory = "bspkrs.floatingruins.fml.gui.ModGuiFactoryHandler", updateJSON = Const.VERSION_URL_BASE + Reference.MODID + Const.VERSION_URL_EXT)
public class FloatingRuinsMod
{

    @Metadata(value = Reference.MODID)
    public static ModMetadata      metadata;

    @Instance(value = Reference.MODID)
    public static FloatingRuinsMod instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy      proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
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

        MinecraftForge.EVENT_BUS.register(instance);

        proxy.registerTickHandler();
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

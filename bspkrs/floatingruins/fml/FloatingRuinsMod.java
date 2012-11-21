package bspkrs.floatingruins.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.Configuration;
import bspkrs.floatingruins.FloatingRuins;
import bspkrs.fml.util.Config;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

@Mod(name = "FloatingRuins", modid = "FloatingRuins", version = "Forge 1.4.5.r01", useMetadata = true)
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class FloatingRuinsMod
{
    private static ModVersionChecker versionChecker;
    private final String             versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.5/floatingRuinsForge.version";
    private final String             mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    @SideOnly(Side.CLIENT)
    public static Minecraft          mcClient;
    
    public ModMetadata               metadata;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        metadata.version = "Forge " + FloatingRuins.versionNumber;
        versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic, FMLLog.getLogger());
        versionChecker.checkVersionWithLogging();
        
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        
        config.load();
        FloatingRuins.allowUpdateCheck = Config.getBoolean(config, "allowUpdateCheck", Configuration.CATEGORY_GENERAL, FloatingRuins.allowUpdateCheck, FloatingRuins.allowUpdateCheckDesc);
        FloatingRuins.allowInSuperFlat = Config.getBoolean(config, "allowInSuperFlat", Configuration.CATEGORY_GENERAL, FloatingRuins.allowInSuperFlat, FloatingRuins.allowInSuperFlatDesc);
        FloatingRuins.rarity = Config.getInt(config, "rarity", Configuration.CATEGORY_GENERAL, FloatingRuins.rarity, 1, Integer.MAX_VALUE, FloatingRuins.rarityDesc);
        FloatingRuins.baseHeight = Config.getInt(config, "baseHeight", Configuration.CATEGORY_GENERAL, FloatingRuins.baseHeight, 80, 240, FloatingRuins.baseHeightDesc);
        FloatingRuins.heightVariation = Config.getInt(config, "heightVariation", Configuration.CATEGORY_GENERAL, FloatingRuins.heightVariation, 0, 160, FloatingRuins.heightVariationDesc);
        FloatingRuins.baseRadius = Config.getInt(config, "baseRadius", Configuration.CATEGORY_GENERAL, FloatingRuins.baseRadius, 6, 50, FloatingRuins.baseRadiusDesc);
        FloatingRuins.radiusVariation = Config.getInt(config, "radiusVariation", Configuration.CATEGORY_GENERAL, FloatingRuins.radiusVariation, 0, 50, FloatingRuins.radiusVariationDesc);
        FloatingRuins.baseDepth = Config.getInt(config, "baseDepth", Configuration.CATEGORY_GENERAL, FloatingRuins.baseDepth, 2, 50, FloatingRuins.baseDepthDesc);
        FloatingRuins.depthVariation = Config.getInt(config, "depthVariation", Configuration.CATEGORY_GENERAL, FloatingRuins.depthVariation, 0, 50, FloatingRuins.depthVariationDesc);
        FloatingRuins.numberOfItems = Config.getInt(config, "numberOfItems", Configuration.CATEGORY_GENERAL, FloatingRuins.numberOfItems, 1, 27, FloatingRuins.numberOfItemsDesc);
        FloatingRuins.stringOfIds = Config.getString(config, "stringOfIds", Configuration.CATEGORY_GENERAL, FloatingRuins.stringOfIds, FloatingRuins.stringOfIdsDesc);
        FloatingRuins.blockIDBlacklist = Config.getString(config, "blockIDBlacklist", Configuration.CATEGORY_GENERAL, FloatingRuins.blockIDBlacklist, FloatingRuins.blockIDBlacklistDesc);
        FloatingRuins.spawnerDefault = Config.getString(config, "spawnerDefault", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerDefault, FloatingRuins.spawnerDefaultDesc);
        FloatingRuins.spawnerDesert = Config.getString(config, "spawnerDesert", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerDesert, FloatingRuins.spawnerDesertDesc);
        FloatingRuins.spawnerForest = Config.getString(config, "spawnerForest", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerForest, FloatingRuins.spawnerForestDesc);
        FloatingRuins.spawnerHills = Config.getString(config, "spawnerHills", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerHills, FloatingRuins.spawnerHillsDesc);
        FloatingRuins.spawnerIceBiomes = Config.getString(config, "spawnerIceBiomes", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerIceBiomes, FloatingRuins.spawnerIceBiomesDesc);
        FloatingRuins.spawnerJungle = Config.getString(config, "spawnerJungle", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerJungle, FloatingRuins.spawnerJungleDesc);
        FloatingRuins.spawnerMushroom = Config.getString(config, "spawnerMushroom", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerMushroom, FloatingRuins.spawnerMushroomDesc);
        FloatingRuins.spawnerOcean = Config.getString(config, "spawnerOcean", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerOcean, FloatingRuins.spawnerOceanDesc);
        FloatingRuins.spawnerPlains = Config.getString(config, "spawnerPlains", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerPlains, FloatingRuins.spawnerPlainsDesc);
        FloatingRuins.spawnerRiver = Config.getString(config, "spawnerRiver", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerRiver, FloatingRuins.spawnerRiverDesc);
        FloatingRuins.spawnerSwampland = Config.getString(config, "spawnerSwampland", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerSwampland, FloatingRuins.spawnerSwamplandDesc);
        FloatingRuins.spawnerTaiga = Config.getString(config, "spawnerTaiga", Configuration.CATEGORY_GENERAL, FloatingRuins.spawnerTaiga, FloatingRuins.spawnerTaigaDesc);
        config.save();
    }
    
    @Init
    public void init(FMLInitializationEvent event)
    {
        if (event.getSide().equals(Side.CLIENT))
        {
            TickRegistry.registerTickHandler(new FloatingRuinsTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
            this.mcClient = FMLClientHandler.instance().getClient();
        }
        
        GameRegistry.registerWorldGenerator(new FloatingRuinsWorldGenerator());
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart)
        {
            return true;
        }
        
        if (mcClient != null && mcClient.thePlayer != null)
        {
            if (FloatingRuins.allowUpdateCheck)
                if (!versionChecker.isCurrentVersion())
                    for (String msg : versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
            return false;
        }
        
        return true;
    }
}

package bspkrs.floatingruins.fml;

import java.io.File;

import net.minecraft.src.mod_bspkrsCore;
import net.minecraftforge.common.Configuration;
import bspkrs.floatingruins.FloatingRuins;
import bspkrs.fml.util.Config;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(name = "FloatingRuins", modid = "FloatingRuins", version = "Forge " + FloatingRuins.VERSION_NUMBER, dependencies = "required-after:mod_bspkrsCore", useMetadata = true)
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class FloatingRuinsMod
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL = "http://bspk.rs/Minecraft/1.5.0/floatingRuinsForge.version";
    private final String            mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    @Metadata(value = "FloatingRuins")
    public static ModMetadata       metadata;
    
    @Instance(value = "FloatingRuins")
    public static FloatingRuinsMod  instance;
    
    @SidedProxy(clientSide = "bspkrs.floatingruins.fml.ClientProxy", serverSide = "bspkrs.floatingruins.fml.CommonProxy")
    public static CommonProxy       proxy;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        //        if (Block.class.getSimpleName().equalsIgnoreCase("Block"))
        //        { // debug settings for deobfuscated execution
        //            FloatingRuins.rarity = 100;
        //            FloatingRuins.harderDungeons = true;
        //            FloatingRuins.allowDebugLogging = true;
        //            FloatingRuins.allowInSuperFlat = true;
        //            FloatingRuins.spawnerPlains = "Zombie, Skeleton";
        //            FloatingRuins.biomeIDBlacklist = "";// "0;1;3;4;5;6;7;8;9;13;17;";
        //            if (file.exists())
        //                file.delete();
        //        }
        
        Configuration config = new Configuration(file);
        
        config.load();
        
        FloatingRuins.allowDebugLogging = Config.getBoolean(config, "allowDebugLogging", ctgyGen, FloatingRuins.allowDebugLogging, FloatingRuins.allowDebugLoggingDesc);
        FloatingRuins.allowInSuperFlat = Config.getBoolean(config, "allowInSuperFlat", ctgyGen, FloatingRuins.allowInSuperFlat, FloatingRuins.allowInSuperFlatDesc);
        FloatingRuins.allowMultiMobSpawners = Config.getBoolean(config, "allowMultiMobSpawners", ctgyGen, FloatingRuins.allowMultiMobSpawners, FloatingRuins.allowMultiMobSpawnersDesc);
        FloatingRuins.harderDungeons = Config.getBoolean(config, "harderDungeons", ctgyGen, FloatingRuins.harderDungeons, FloatingRuins.harderDungeonsDesc);
        FloatingRuins.rarity = Config.getInt(config, "rarity", ctgyGen, FloatingRuins.rarity, 1, Integer.MAX_VALUE, FloatingRuins.rarityDesc);
        FloatingRuins.baseHeight = Config.getInt(config, "baseHeight", ctgyGen, FloatingRuins.baseHeight, 80, 240, FloatingRuins.baseHeightDesc);
        FloatingRuins.heightVariation = Config.getInt(config, "heightVariation", ctgyGen, FloatingRuins.heightVariation, 0, 160, FloatingRuins.heightVariationDesc);
        FloatingRuins.baseRadius = Config.getInt(config, "baseRadius", ctgyGen, FloatingRuins.baseRadius, 6, 50, FloatingRuins.baseRadiusDesc);
        FloatingRuins.radiusVariation = Config.getInt(config, "radiusVariation", ctgyGen, FloatingRuins.radiusVariation, 0, 50, FloatingRuins.radiusVariationDesc);
        FloatingRuins.baseDepth = Config.getInt(config, "baseDepth", ctgyGen, FloatingRuins.baseDepth, 2, 50, FloatingRuins.baseDepthDesc);
        FloatingRuins.depthVariation = Config.getInt(config, "depthVariation", ctgyGen, FloatingRuins.depthVariation, 0, 50, FloatingRuins.depthVariationDesc);
        FloatingRuins.numberOfItems = Config.getInt(config, "numberOfItems", ctgyGen, FloatingRuins.numberOfItems, 1, 27, FloatingRuins.numberOfItemsDesc);
        FloatingRuins.stringOfIds = Config.getString(config, "stringOfIds", ctgyGen, FloatingRuins.stringOfIds, FloatingRuins.stringOfIdsDesc);
        FloatingRuins.blockIDBlacklist = Config.getString(config, "blockIDBlacklist", ctgyGen, FloatingRuins.blockIDBlacklist, FloatingRuins.blockIDBlacklistDesc);
        FloatingRuins.dimensionIDBlacklist = Config.getString(config, "dimensionIDBlacklist", ctgyGen, FloatingRuins.dimensionIDBlacklist, FloatingRuins.dimensionIDBlacklistDesc);
        FloatingRuins.biomeIDBlacklist = Config.getString(config, "biomeIDBlacklist", ctgyGen, FloatingRuins.biomeIDBlacklist, FloatingRuins.biomeIDBlacklistDesc);
        FloatingRuins.spawnerDefault = Config.getString(config, "spawnerDefault", ctgyGen, FloatingRuins.spawnerDefault, FloatingRuins.spawnerDefaultDesc);
        FloatingRuins.spawnerDesert = Config.getString(config, "spawnerDesert", ctgyGen, FloatingRuins.spawnerDesert, FloatingRuins.spawnerDesertDesc);
        FloatingRuins.spawnerForest = Config.getString(config, "spawnerForest", ctgyGen, FloatingRuins.spawnerForest, FloatingRuins.spawnerForestDesc);
        FloatingRuins.spawnerHills = Config.getString(config, "spawnerHills", ctgyGen, FloatingRuins.spawnerHills, FloatingRuins.spawnerHillsDesc);
        FloatingRuins.spawnerIceBiomes = Config.getString(config, "spawnerIceBiomes", ctgyGen, FloatingRuins.spawnerIceBiomes, FloatingRuins.spawnerIceBiomesDesc);
        FloatingRuins.spawnerJungle = Config.getString(config, "spawnerJungle", ctgyGen, FloatingRuins.spawnerJungle, FloatingRuins.spawnerJungleDesc);
        FloatingRuins.spawnerMushroom = Config.getString(config, "spawnerMushroom", ctgyGen, FloatingRuins.spawnerMushroom, FloatingRuins.spawnerMushroomDesc);
        FloatingRuins.spawnerOcean = Config.getString(config, "spawnerOcean", ctgyGen, FloatingRuins.spawnerOcean, FloatingRuins.spawnerOceanDesc);
        FloatingRuins.spawnerPlains = Config.getString(config, "spawnerPlains", ctgyGen, FloatingRuins.spawnerPlains, FloatingRuins.spawnerPlainsDesc);
        FloatingRuins.spawnerRiver = Config.getString(config, "spawnerRiver", ctgyGen, FloatingRuins.spawnerRiver, FloatingRuins.spawnerRiverDesc);
        FloatingRuins.spawnerSwampland = Config.getString(config, "spawnerSwampland", ctgyGen, FloatingRuins.spawnerSwampland, FloatingRuins.spawnerSwamplandDesc);
        FloatingRuins.spawnerTaiga = Config.getString(config, "spawnerTaiga", ctgyGen, FloatingRuins.spawnerTaiga, FloatingRuins.spawnerTaigaDesc);
        FloatingRuins.spawnerNearLava = Config.getString(config, "spawnerNearLava", ctgyGen, FloatingRuins.spawnerNearLava, FloatingRuins.spawnerNearLavaDesc);
        
        config.save();
        
        if (mod_bspkrsCore.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic, FMLLog.getLogger());
            versionChecker.checkVersionWithLoggingBySubStringAsFloat(metadata.version.length() - 1, metadata.version.length());
        }
    }
    
    @Init
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerWorldGenerator(new FloatingRuinsWorldGenerator());
        proxy.registerTickHandler();
    }
}

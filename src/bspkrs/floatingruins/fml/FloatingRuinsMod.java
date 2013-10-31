package bspkrs.floatingruins.fml;

import java.io.File;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.floatingruins.FloatingRuins;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
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
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(name = "FloatingRuins", modid = "FloatingRuins", version = "Forge " + FloatingRuins.VERSION_NUMBER, dependencies = "required-after:bspkrsCore", useMetadata = true)
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class FloatingRuinsMod
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/floatingRuinsForge.version";
    private final String            mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    @Metadata(value = "FloatingRuins")
    public static ModMetadata       metadata;
    
    @Instance(value = "FloatingRuins")
    public static FloatingRuinsMod  instance;
    
    @SidedProxy(clientSide = "bspkrs.floatingruins.fml.ClientProxy", serverSide = "bspkrs.floatingruins.fml.CommonProxy")
    public static CommonProxy       proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
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
        
        Configuration config = new Configuration(file);
        
        config.load();
        
        FloatingRuins.allowDebugLogging = config.getBoolean("allowDebugLogging", ctgyGen, FloatingRuins.allowDebugLogging, FloatingRuins.allowDebugLoggingDesc);
        FloatingRuins.allowInSuperFlat = config.getBoolean("allowInSuperFlat", ctgyGen, FloatingRuins.allowInSuperFlat, FloatingRuins.allowInSuperFlatDesc);
        FloatingRuins.allowMultiMobSpawners = config.getBoolean("allowMultiMobSpawners", ctgyGen, FloatingRuins.allowMultiMobSpawners, FloatingRuins.allowMultiMobSpawnersDesc);
        FloatingRuins.harderDungeons = config.getBoolean("harderDungeons", ctgyGen, FloatingRuins.harderDungeons, FloatingRuins.harderDungeonsDesc);
        FloatingRuins.rarity = config.getInt("rarity", ctgyGen, FloatingRuins.rarity, 1, Integer.MAX_VALUE, FloatingRuins.rarityDesc);
        FloatingRuins.baseHeight = config.getInt("baseHeight", ctgyGen, FloatingRuins.baseHeight, 80, 215, FloatingRuins.baseHeightDesc);
        FloatingRuins.heightVariation = config.getInt("heightVariation", ctgyGen, FloatingRuins.heightVariation, 0, 160, FloatingRuins.heightVariationDesc);
        FloatingRuins.baseRadius = config.getInt("baseRadius", ctgyGen, FloatingRuins.baseRadius, 6, 50, FloatingRuins.baseRadiusDesc);
        FloatingRuins.radiusVariation = config.getInt("radiusVariation", ctgyGen, FloatingRuins.radiusVariation, 0, 50, FloatingRuins.radiusVariationDesc);
        FloatingRuins.baseDepth = config.getInt("baseDepth", ctgyGen, FloatingRuins.baseDepth, 2, 50, FloatingRuins.baseDepthDesc);
        FloatingRuins.depthVariation = config.getInt("depthVariation", ctgyGen, FloatingRuins.depthVariation, 0, 50, FloatingRuins.depthVariationDesc);
        FloatingRuins.numberOfItems = config.getInt("numberOfItems", ctgyGen, FloatingRuins.numberOfItems, 1, 27, FloatingRuins.numberOfItemsDesc);
        FloatingRuins.stringOfIds = config.getString("stringOfIds", ctgyGen, FloatingRuins.stringOfIds, FloatingRuins.stringOfIdsDesc);
        FloatingRuins.blockIDBlacklist = config.getString("blockIDBlacklist", ctgyGen, FloatingRuins.blockIDBlacklist, FloatingRuins.blockIDBlacklistDesc);
        FloatingRuins.dimensionIDBlacklist = config.getString("dimensionIDBlacklist", ctgyGen, FloatingRuins.dimensionIDBlacklist, FloatingRuins.dimensionIDBlacklistDesc);
        FloatingRuins.biomeIDBlacklist = config.getString("biomeIDBlacklist", ctgyGen, FloatingRuins.biomeIDBlacklist, FloatingRuins.biomeIDBlacklistDesc);
        FloatingRuins.spawnerDefault = config.getString("spawnerDefault", ctgyGen, FloatingRuins.spawnerDefault, FloatingRuins.spawnerDefaultDesc);
        FloatingRuins.spawnerDesert = config.getString("spawnerDesert", ctgyGen, FloatingRuins.spawnerDesert, FloatingRuins.spawnerDesertDesc);
        FloatingRuins.spawnerForest = config.getString("spawnerForest", ctgyGen, FloatingRuins.spawnerForest, FloatingRuins.spawnerForestDesc);
        FloatingRuins.spawnerHills = config.getString("spawnerHills", ctgyGen, FloatingRuins.spawnerHills, FloatingRuins.spawnerHillsDesc);
        FloatingRuins.spawnerIceBiomes = config.getString("spawnerIceBiomes", ctgyGen, FloatingRuins.spawnerIceBiomes, FloatingRuins.spawnerIceBiomesDesc);
        FloatingRuins.spawnerJungle = config.getString("spawnerJungle", ctgyGen, FloatingRuins.spawnerJungle, FloatingRuins.spawnerJungleDesc);
        FloatingRuins.spawnerMushroom = config.getString("spawnerMushroom", ctgyGen, FloatingRuins.spawnerMushroom, FloatingRuins.spawnerMushroomDesc);
        FloatingRuins.spawnerOcean = config.getString("spawnerOcean", ctgyGen, FloatingRuins.spawnerOcean, FloatingRuins.spawnerOceanDesc);
        FloatingRuins.spawnerPlains = config.getString("spawnerPlains", ctgyGen, FloatingRuins.spawnerPlains, FloatingRuins.spawnerPlainsDesc);
        FloatingRuins.spawnerRiver = config.getString("spawnerRiver", ctgyGen, FloatingRuins.spawnerRiver, FloatingRuins.spawnerRiverDesc);
        FloatingRuins.spawnerSwampland = config.getString("spawnerSwampland", ctgyGen, FloatingRuins.spawnerSwampland, FloatingRuins.spawnerSwamplandDesc);
        FloatingRuins.spawnerTaiga = config.getString("spawnerTaiga", ctgyGen, FloatingRuins.spawnerTaiga, FloatingRuins.spawnerTaigaDesc);
        FloatingRuins.spawnerNearLava = config.getString("spawnerNearLava", ctgyGen, FloatingRuins.spawnerNearLava, FloatingRuins.spawnerNearLavaDesc);
        
        config.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerWorldGenerator(new FloatingRuinsWorldGenerator());
        proxy.registerTickHandler();
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
    }
}

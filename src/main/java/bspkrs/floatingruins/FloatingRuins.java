package bspkrs.floatingruins;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;
import bspkrs.floatingruins.fml.Reference;
import bspkrs.util.CommonUtils;
import bspkrs.util.Coord;
import cpw.mods.fml.common.registry.GameData;

// Test Seed: 5460896710218081688
// 1470679938 (large biomes)
public final class FloatingRuins
{
    private final static boolean enabledDefault               = true;
    public static boolean        enabled                      = enabledDefault;
    private final static boolean allowDebugLoggingDefault     = false;
    public static boolean        allowDebugLogging            = allowDebugLoggingDefault;
    private final static boolean allowInSuperFlatDefault      = false;
    public static boolean        allowInSuperFlat             = allowInSuperFlatDefault;
    private final static boolean allowMultiMobSpawnersDefault = true;
    public static boolean        allowMultiMobSpawners        = allowMultiMobSpawnersDefault;
    private final static boolean harderDungeonsDefault        = false;
    public static boolean        harderDungeons               = harderDungeonsDefault;
    private final static int     rarityDefault                = 800;
    public static int            rarity                       = rarityDefault;
    private final static int     rarityDungeonDefault         = 1;
    public static int            rarityDungeon                = rarityDungeonDefault;
    private final static int     heightMaxDefault             = 225;
    public static int            heightMax                    = heightMaxDefault;
    private final static int     heightMeanDefault            = 100;
    public static int            heightMean                   = heightMeanDefault;
    private final static int     heightMinDefault             = 80;
    public static int            heightMin                    = heightMinDefault;
    private final static int     heightNormDefault            = 3;
    public static int            heightNorm                   = heightNormDefault;
    private final static int     radiusMaxDefault             = 23;
    public static int            radiusMax                    = radiusMaxDefault;
    private final static int     radiusMeanDefault            = 10;
    public static int            radiusMean                   = radiusMeanDefault;
    private final static int     radiusMinDefault             = 7;
    public static int            radiusMin                    = radiusMinDefault;
    private final static int     radiusNormDefault            = 5;
    public static int            radiusNorm                   = radiusNormDefault;
    private final static int     depthMaxDefault              = 45;
    public static int            depthMax                     = depthMaxDefault;
    private final static int     depthMeanDefault             = 9;
    public static int            depthMean                    = depthMeanDefault;
    private final static int     depthMinDefault              = 5;
    public static int            depthMin                     = depthMinDefault;
    private final static int     depthNormDefault             = 3;
    public static int            depthNorm                    = depthNormDefault;
    private final static int     shapeSpheroidWeightDefault   = 21;
    public static int            shapeSpheroidWeight          = shapeSpheroidWeightDefault;
    private final static int     shapeConeWeightDefault       = 14;
    public static int            shapeConeWeight              = shapeConeWeightDefault;
    private final static int     shapeJetsonsWeightDefault    = 1;
    public static int            shapeJetsonsWeight           = shapeJetsonsWeightDefault;
    private final static int     shapeStalactiteWeightDefault = 0;
    public static int            shapeStalactiteWeight        = shapeStalactiteWeightDefault;
    private final static int     numberOfItemsDefault         = 4;
    public static int            numberOfItems                = numberOfItemsDefault;
    private final static String  blockIDBlacklistDefault;
    public static String         blockIDBlacklist;
    private final static String  dimensionIDBlacklistDefault  = "-1;1";
    public static String         dimensionIDBlacklist         = dimensionIDBlacklistDefault;
    private final static String  biomeIDBlacklistDefault      = "7;8;9;11;15;16;";
    public static String         biomeIDBlacklist             = biomeIDBlacklistDefault;
    private final static boolean useCustomItemListDefault     = false;
    public static boolean        useCustomItemList            = useCustomItemListDefault;
    private final static String  stringOfIdsDefault;
    public static String         stringOfIds;
    private final static String  spawnerDefaultDefault        = "Creeper, Skeleton, Zombie, CaveSpider";
    public static String         spawnerDefault               = spawnerDefaultDefault;
    private final static String  spawnerDesertDefault         = "WitherSkeleton, PigZombie, Blaze";
    public static String         spawnerDesert                = spawnerDesertDefault;
    private final static String  spawnerForestDefault         = "Witch, CaveSpider";
    public static String         spawnerForest                = spawnerForestDefault;
    private final static String  spawnerPlainsDefault         = "Spider, Zombie, Creeper";
    public static String         spawnerPlains                = spawnerPlainsDefault;
    private final static String  spawnerSwamplandDefault      = "Creeper, CaveSpider, Witch";
    public static String         spawnerSwampland             = spawnerSwamplandDefault;
    private final static String  spawnerTaigaDefault          = "Zombie, Creeper, Wolf";
    public static String         spawnerTaiga                 = spawnerTaigaDefault;
    private final static String  spawnerHillsDefault          = "Default";
    public static String         spawnerHills                 = spawnerHillsDefault;
    private final static String  spawnerOceanDefault          = "Silverfish, ChargedCreeper";
    public static String         spawnerOcean                 = spawnerOceanDefault;
    private final static String  spawnerRiverDefault          = "Silverfish, Creeper";
    public static String         spawnerRiver                 = spawnerRiverDefault;
    private final static String  spawnerJungleDefault         = "Skeleton, CaveSpider";
    public static String         spawnerJungle                = spawnerJungleDefault;
    private final static String  spawnerIceBiomesDefault      = "Zombie, Skeleton, Wolf";
    public static String         spawnerIceBiomes             = spawnerIceBiomesDefault;
    private final static String  spawnerMushroomDefault       = "MushroomCow";
    public static String         spawnerMushroom              = spawnerMushroomDefault;
    private final static String  spawnerNearLavaDefault       = "Blaze, LavaSlime, WitherSkeleton, PigZombie";
    public static String         spawnerNearLava              = spawnerNearLavaDefault;
    
    private static int           chunksToRetry                = 0;
    
    public static void initConfig(File file)
    {
        Reference.config = new Configuration(file, Reference.CONFIG_VERSION);
        
        if (!Reference.CONFIG_VERSION.equals(Reference.config.getLoadedConfigVersion()))
        {
            FRLog.info("Your FloatingRuins config file is out of date and will be altered.");
            for (ConfigElement ce : ConfigElement.values())
                if (Reference.config.moveProperty(Reference.CTGY_GEN, ce.key(), ce.ctgy()))
                    FRLog.debug("Property %s moved from %s to %s", ce.key(), Reference.CTGY_GEN, ce.ctgy());
        }
        
        syncConfig(true);
    }
    
    public static void syncConfig(boolean init)
    {
        if (!init)
            Reference.config.load();
        
        Reference.config.setCategoryComment(Reference.CTGY_GEN, "ATTENTION: Editing this file manually is no longer necessary. \n" +
                "On the Mods list screen select the entry for FloatingRuins, then click the Config button to modify these settings.");
        
        HashMap<String, List<String>> orderedKeys = new HashMap<String, List<String>>();
        orderedKeys.put(Reference.CTGY_GEN, new ArrayList<String>());
        orderedKeys.put(Reference.CTGY_ISLANDS, new ArrayList<String>());
        orderedKeys.put(Reference.CTGY_ISLAND_SIZE, new ArrayList<String>());
        orderedKeys.put(Reference.CTGY_DUNGEONS, new ArrayList<String>());
        
        enabled = Reference.config.getBoolean(ConfigElement.ENABLED.key(), ConfigElement.ENABLED.ctgy(), enabledDefault,
                ConfigElement.ENABLED.desc(), ConfigElement.ENABLED.languageKey());
        orderedKeys.get(ConfigElement.ENABLED.ctgy()).add(ConfigElement.ENABLED.key());
        allowDebugLogging = Reference.config.getBoolean(ConfigElement.ALLOW_DEBUG_LOGGING.key(), ConfigElement.ALLOW_DEBUG_LOGGING.ctgy(), allowDebugLoggingDefault,
                ConfigElement.ALLOW_DEBUG_LOGGING.desc(), ConfigElement.ALLOW_DEBUG_LOGGING.languageKey());
        orderedKeys.get(ConfigElement.ALLOW_DEBUG_LOGGING.ctgy()).add(ConfigElement.ALLOW_DEBUG_LOGGING.key());
        allowInSuperFlat = Reference.config.getBoolean(ConfigElement.ALLOW_IN_SUPERFLAT.key(), ConfigElement.ALLOW_IN_SUPERFLAT.ctgy(), allowInSuperFlatDefault,
                ConfigElement.ALLOW_IN_SUPERFLAT.desc(), ConfigElement.ALLOW_IN_SUPERFLAT.languageKey());
        orderedKeys.get(ConfigElement.ALLOW_IN_SUPERFLAT.ctgy()).add(ConfigElement.ALLOW_IN_SUPERFLAT.key());
        allowMultiMobSpawners = Reference.config.getBoolean(ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.key(), ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.ctgy(), allowMultiMobSpawnersDefault,
                ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.desc(), ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.languageKey());
        orderedKeys.get(ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.ctgy()).add(ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.key());
        rarity = Reference.config.getInt(ConfigElement.RARITY.key(), ConfigElement.RARITY.ctgy(), rarityDefault, 1, Integer.MAX_VALUE,
                ConfigElement.RARITY.desc(), ConfigElement.RARITY.languageKey());
        orderedKeys.get(ConfigElement.RARITY.ctgy()).add(ConfigElement.RARITY.key());
        rarityDungeon = Reference.config.getInt(ConfigElement.RARITY_DUNGEON.key(), ConfigElement.RARITY_DUNGEON.ctgy(), rarityDungeonDefault, 1, Integer.MAX_VALUE,
                ConfigElement.RARITY_DUNGEON.desc(), ConfigElement.RARITY_DUNGEON.languageKey());
        orderedKeys.get(ConfigElement.RARITY_DUNGEON.ctgy()).add(ConfigElement.RARITY_DUNGEON.key());
        harderDungeons = Reference.config.getBoolean(ConfigElement.HARDER_DUNGEONS.key(), ConfigElement.HARDER_DUNGEONS.ctgy(), harderDungeonsDefault,
                ConfigElement.HARDER_DUNGEONS.desc(), ConfigElement.HARDER_DUNGEONS.languageKey());
        orderedKeys.get(ConfigElement.HARDER_DUNGEONS.ctgy()).add(ConfigElement.HARDER_DUNGEONS.key());
        heightMean = Reference.config.getInt(ConfigElement.HEIGHT_MEAN.key(), ConfigElement.HEIGHT_MEAN.ctgy(), heightMeanDefault, heightMin, heightMax,
                ConfigElement.HEIGHT_MEAN.desc(), ConfigElement.HEIGHT_MEAN.languageKey());
        orderedKeys.get(ConfigElement.HEIGHT_MEAN.ctgy()).add(ConfigElement.HEIGHT_MEAN.key());
        heightMin = Reference.config.getInt(ConfigElement.HEIGHT_MIN.key(), ConfigElement.HEIGHT_MIN.ctgy(), heightMinDefault, 80, heightMean,
                ConfigElement.HEIGHT_MIN.desc(), ConfigElement.HEIGHT_MIN.languageKey());
        orderedKeys.get(ConfigElement.HEIGHT_MIN.ctgy()).add(ConfigElement.HEIGHT_MIN.key());
        heightMax = Reference.config.getInt(ConfigElement.HEIGHT_MAX.key(), ConfigElement.HEIGHT_MAX.ctgy(), heightMaxDefault, heightMean, 240,
                ConfigElement.HEIGHT_MAX.desc(), ConfigElement.HEIGHT_MAX.languageKey());
        orderedKeys.get(ConfigElement.HEIGHT_MAX.ctgy()).add(ConfigElement.HEIGHT_MAX.key());
        heightNorm = Reference.config.getInt(ConfigElement.HEIGHT_NORM.key(), ConfigElement.HEIGHT_NORM.ctgy(), heightNormDefault, 1, 10,
                ConfigElement.HEIGHT_NORM.desc(), ConfigElement.HEIGHT_NORM.languageKey());
        orderedKeys.get(ConfigElement.HEIGHT_NORM.ctgy()).add(ConfigElement.HEIGHT_NORM.key());
        radiusMean = Reference.config.getInt(ConfigElement.RADIUS_MEAN.key(), ConfigElement.RADIUS_MEAN.ctgy(), radiusMeanDefault, radiusMin, radiusMax,
                ConfigElement.RADIUS_MEAN.desc(), ConfigElement.RADIUS_MEAN.languageKey());
        orderedKeys.get(ConfigElement.RADIUS_MEAN.ctgy()).add(ConfigElement.RADIUS_MEAN.key());
        radiusMax = Reference.config.getInt(ConfigElement.RADIUS_MAX.key(), ConfigElement.RADIUS_MAX.ctgy(), radiusMaxDefault, radiusMean, 50,
                ConfigElement.RADIUS_MAX.desc(), ConfigElement.RADIUS_MAX.languageKey());
        orderedKeys.get(ConfigElement.RADIUS_MAX.ctgy()).add(ConfigElement.RADIUS_MAX.key());
        radiusMin = Reference.config.getInt(ConfigElement.RADIUS_MIN.key(), ConfigElement.RADIUS_MIN.ctgy(), radiusMinDefault, 5, radiusMean,
                ConfigElement.RADIUS_MIN.desc(), ConfigElement.RADIUS_MIN.languageKey());
        orderedKeys.get(ConfigElement.RADIUS_MIN.ctgy()).add(ConfigElement.RADIUS_MIN.key());
        radiusNorm = Reference.config.getInt(ConfigElement.RADIUS_NORM.key(), ConfigElement.RADIUS_NORM.ctgy(), radiusNormDefault, 1, 10,
                ConfigElement.RADIUS_NORM.desc(), ConfigElement.RADIUS_NORM.languageKey());
        orderedKeys.get(ConfigElement.RADIUS_NORM.ctgy()).add(ConfigElement.RADIUS_NORM.key());
        depthMean = Reference.config.getInt(ConfigElement.DEPTH_MEAN.key(), ConfigElement.DEPTH_MEAN.ctgy(), depthMeanDefault, depthMin, depthMax,
                ConfigElement.DEPTH_MEAN.desc(), ConfigElement.DEPTH_MEAN.languageKey());
        orderedKeys.get(ConfigElement.DEPTH_MEAN.ctgy()).add(ConfigElement.DEPTH_MEAN.key());
        depthMin = Reference.config.getInt(ConfigElement.DEPTH_MIN.key(), ConfigElement.DEPTH_MIN.ctgy(), depthMinDefault, 5, depthMean,
                ConfigElement.DEPTH_MIN.desc(), ConfigElement.DEPTH_MIN.languageKey());
        orderedKeys.get(ConfigElement.DEPTH_MIN.ctgy()).add(ConfigElement.DEPTH_MIN.key());
        depthMax = Reference.config.getInt(ConfigElement.DEPTH_MAX.key(), ConfigElement.DEPTH_MAX.ctgy(), depthMaxDefault, depthMean, 45,
                ConfigElement.DEPTH_MAX.desc(), ConfigElement.DEPTH_MAX.languageKey());
        orderedKeys.get(ConfigElement.DEPTH_MAX.ctgy()).add(ConfigElement.DEPTH_MAX.key());
        depthNorm = Reference.config.getInt(ConfigElement.DEPTH_NORM.key(), ConfigElement.DEPTH_NORM.ctgy(), depthNormDefault, 1, 10,
                ConfigElement.DEPTH_NORM.desc(), ConfigElement.DEPTH_NORM.languageKey());
        orderedKeys.get(ConfigElement.DEPTH_NORM.ctgy()).add(ConfigElement.DEPTH_NORM.key());
        shapeSpheroidWeight = Reference.config.getInt(ConfigElement.SHAPE_SPHEROID_WEIGHT.key(), ConfigElement.SHAPE_SPHEROID_WEIGHT.ctgy(), shapeSpheroidWeightDefault, 0, Integer.MAX_VALUE,
                ConfigElement.SHAPE_SPHEROID_WEIGHT.desc(), ConfigElement.SHAPE_SPHEROID_WEIGHT.languageKey());
        orderedKeys.get(ConfigElement.SHAPE_SPHEROID_WEIGHT.ctgy()).add(ConfigElement.SHAPE_SPHEROID_WEIGHT.key());
        shapeConeWeight = Reference.config.getInt(ConfigElement.SHAPE_CONE_WEIGHT.key(), ConfigElement.SHAPE_CONE_WEIGHT.ctgy(), shapeConeWeightDefault, 0, Integer.MAX_VALUE,
                ConfigElement.SHAPE_CONE_WEIGHT.desc(), ConfigElement.SHAPE_CONE_WEIGHT.languageKey());
        orderedKeys.get(ConfigElement.SHAPE_CONE_WEIGHT.ctgy()).add(ConfigElement.SHAPE_CONE_WEIGHT.key());
        shapeJetsonsWeight = Reference.config.getInt(ConfigElement.SHAPE_JETSONS_WEIGHT.key(), ConfigElement.SHAPE_JETSONS_WEIGHT.ctgy(), shapeJetsonsWeightDefault, 0, Integer.MAX_VALUE,
                ConfigElement.SHAPE_JETSONS_WEIGHT.desc(), ConfigElement.SHAPE_JETSONS_WEIGHT.languageKey());
        orderedKeys.get(ConfigElement.SHAPE_JETSONS_WEIGHT.ctgy()).add(ConfigElement.SHAPE_JETSONS_WEIGHT.key());
        // shapeStalactiteWeight = Reference.config.getInt("shapeStalactiteWeight", ctgyGen, shapeStalactiteWeight, 0, 0, shapeStalactiteWeightDesc);
        useCustomItemList = Reference.config.getBoolean(ConfigElement.USE_CUSTOM_ITEM_LIST.key(), ConfigElement.USE_CUSTOM_ITEM_LIST.ctgy(), useCustomItemListDefault,
                ConfigElement.USE_CUSTOM_ITEM_LIST.desc(), ConfigElement.USE_CUSTOM_ITEM_LIST.languageKey());
        orderedKeys.get(ConfigElement.USE_CUSTOM_ITEM_LIST.ctgy()).add(ConfigElement.USE_CUSTOM_ITEM_LIST.key());
        stringOfIds = Reference.config.getString(ConfigElement.STRING_OF_IDS.key(), ConfigElement.STRING_OF_IDS.ctgy(), stringOfIdsDefault,
                ConfigElement.STRING_OF_IDS.desc(), ConfigElement.STRING_OF_IDS.languageKey());
        orderedKeys.get(ConfigElement.STRING_OF_IDS.ctgy()).add(ConfigElement.STRING_OF_IDS.key());
        numberOfItems = Reference.config.getInt(ConfigElement.NUMBER_OF_ITEMS.key(), ConfigElement.NUMBER_OF_ITEMS.ctgy(), numberOfItemsDefault, 1, 27,
                ConfigElement.NUMBER_OF_ITEMS.desc(), ConfigElement.NUMBER_OF_ITEMS.languageKey());
        orderedKeys.get(ConfigElement.NUMBER_OF_ITEMS.ctgy()).add(ConfigElement.NUMBER_OF_ITEMS.key());
        blockIDBlacklist = Reference.config.getString(ConfigElement.BLOCK_ID_BLACKLIST.key(), ConfigElement.BLOCK_ID_BLACKLIST.ctgy(), blockIDBlacklistDefault,
                ConfigElement.BLOCK_ID_BLACKLIST.desc(), ConfigElement.BLOCK_ID_BLACKLIST.languageKey());
        orderedKeys.get(ConfigElement.BLOCK_ID_BLACKLIST.ctgy()).add(ConfigElement.BLOCK_ID_BLACKLIST.key());
        dimensionIDBlacklist = Reference.config.getString(ConfigElement.DIMENSION_ID_BLACKLIST.key(), ConfigElement.DIMENSION_ID_BLACKLIST.ctgy(), dimensionIDBlacklistDefault,
                ConfigElement.DIMENSION_ID_BLACKLIST.desc(), ConfigElement.DIMENSION_ID_BLACKLIST.languageKey());
        orderedKeys.get(ConfigElement.DIMENSION_ID_BLACKLIST.ctgy()).add(ConfigElement.DIMENSION_ID_BLACKLIST.key());
        biomeIDBlacklist = Reference.config.getString(ConfigElement.BIOME_ID_BLACKLIST.key(), ConfigElement.BIOME_ID_BLACKLIST.ctgy(), biomeIDBlacklistDefault,
                ConfigElement.BIOME_ID_BLACKLIST.desc(), ConfigElement.BIOME_ID_BLACKLIST.languageKey());
        orderedKeys.get(ConfigElement.BIOME_ID_BLACKLIST.ctgy()).add(ConfigElement.BIOME_ID_BLACKLIST.key());
        spawnerDefault = Reference.config.getString(ConfigElement.SPAWNER_DEFAULT.key(), ConfigElement.SPAWNER_DEFAULT.ctgy(), spawnerDefaultDefault,
                ConfigElement.SPAWNER_DEFAULT.desc(), ConfigElement.SPAWNER_DEFAULT.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_DEFAULT.ctgy()).add(ConfigElement.SPAWNER_DEFAULT.key());
        spawnerDesert = Reference.config.getString(ConfigElement.SPAWNER_DESERT.key(), ConfigElement.SPAWNER_DESERT.ctgy(), spawnerDesertDefault,
                ConfigElement.SPAWNER_DESERT.desc(), ConfigElement.SPAWNER_DESERT.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_DESERT.ctgy()).add(ConfigElement.SPAWNER_DESERT.key());
        spawnerForest = Reference.config.getString(ConfigElement.SPAWNER_FOREST.key(), ConfigElement.SPAWNER_FOREST.ctgy(), spawnerForestDefault,
                ConfigElement.SPAWNER_FOREST.desc(), ConfigElement.SPAWNER_FOREST.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_FOREST.ctgy()).add(ConfigElement.SPAWNER_FOREST.key());
        spawnerHills = Reference.config.getString(ConfigElement.SPAWNER_HILLS.key(), ConfigElement.SPAWNER_HILLS.ctgy(), spawnerHillsDefault,
                ConfigElement.SPAWNER_HILLS.desc(), ConfigElement.SPAWNER_HILLS.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_HILLS.ctgy()).add(ConfigElement.SPAWNER_HILLS.key());
        spawnerIceBiomes = Reference.config.getString(ConfigElement.SPAWNER_ICE_BIOMES.key(), ConfigElement.SPAWNER_ICE_BIOMES.ctgy(), spawnerIceBiomesDefault,
                ConfigElement.SPAWNER_ICE_BIOMES.desc(), ConfigElement.SPAWNER_ICE_BIOMES.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_ICE_BIOMES.ctgy()).add(ConfigElement.SPAWNER_ICE_BIOMES.key());
        spawnerJungle = Reference.config.getString(ConfigElement.SPAWNER_JUNGLE.key(), ConfigElement.SPAWNER_JUNGLE.ctgy(), spawnerJungleDefault,
                ConfigElement.SPAWNER_JUNGLE.desc(), ConfigElement.SPAWNER_JUNGLE.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_JUNGLE.ctgy()).add(ConfigElement.SPAWNER_JUNGLE.key());
        spawnerMushroom = Reference.config.getString(ConfigElement.SPAWNER_MUSHROOM.key(), ConfigElement.SPAWNER_MUSHROOM.ctgy(), spawnerMushroomDefault,
                ConfigElement.SPAWNER_MUSHROOM.desc(), ConfigElement.SPAWNER_MUSHROOM.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_MUSHROOM.ctgy()).add(ConfigElement.SPAWNER_MUSHROOM.key());
        spawnerOcean = Reference.config.getString(ConfigElement.SPAWNER_OCEAN.key(), ConfigElement.SPAWNER_OCEAN.ctgy(), spawnerOceanDefault,
                ConfigElement.SPAWNER_OCEAN.desc(), ConfigElement.SPAWNER_OCEAN.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_OCEAN.ctgy()).add(ConfigElement.SPAWNER_OCEAN.key());
        spawnerPlains = Reference.config.getString(ConfigElement.SPAWNER_PLAINS.key(), ConfigElement.SPAWNER_PLAINS.ctgy(), spawnerPlainsDefault,
                ConfigElement.SPAWNER_PLAINS.desc(), ConfigElement.SPAWNER_PLAINS.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_PLAINS.ctgy()).add(ConfigElement.SPAWNER_PLAINS.key());
        spawnerRiver = Reference.config.getString(ConfigElement.SPAWNER_RIVER.key(), ConfigElement.SPAWNER_RIVER.ctgy(), spawnerRiverDefault,
                ConfigElement.SPAWNER_RIVER.desc(), ConfigElement.SPAWNER_RIVER.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_RIVER.ctgy()).add(ConfigElement.SPAWNER_RIVER.key());
        spawnerSwampland = Reference.config.getString(ConfigElement.SPAWNER_SWAMPLAND.key(), ConfigElement.SPAWNER_SWAMPLAND.ctgy(), spawnerSwamplandDefault,
                ConfigElement.SPAWNER_SWAMPLAND.desc(), ConfigElement.SPAWNER_SWAMPLAND.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_SWAMPLAND.ctgy()).add(ConfigElement.SPAWNER_SWAMPLAND.key());
        spawnerTaiga = Reference.config.getString(ConfigElement.SPAWNER_TAIGA.key(), ConfigElement.SPAWNER_TAIGA.ctgy(), spawnerTaigaDefault,
                ConfigElement.SPAWNER_TAIGA.desc(), ConfigElement.SPAWNER_TAIGA.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_TAIGA.ctgy()).add(ConfigElement.SPAWNER_TAIGA.key());
        spawnerNearLava = Reference.config.getString(ConfigElement.SPAWNER_NEAR_LAVA.key(), ConfigElement.SPAWNER_NEAR_LAVA.ctgy(), spawnerNearLavaDefault,
                ConfigElement.SPAWNER_NEAR_LAVA.desc(), ConfigElement.SPAWNER_NEAR_LAVA.languageKey());
        orderedKeys.get(ConfigElement.SPAWNER_NEAR_LAVA.ctgy()).add(ConfigElement.SPAWNER_NEAR_LAVA.key());
        
        for (Entry<String, List<String>> entry : orderedKeys.entrySet())
        {
            Reference.config.setCategoryPropertyOrder(entry.getKey(), entry.getValue());
            Reference.config.setCategoryLanguageKey(entry.getKey(), "bspkrs.fr.configgui.ctgy." + entry.getKey());
        }
        
        Reference.config.save();
    }
    
    /**
     * Method used during world generation that calculates all necessary generation variables and determines if this x,z location is
     * suitable for island generation. If conditions are met doGenerateSurface() is called.
     */
    public static void generateSurface(World world, Random random, int x, int z, boolean isWorldGen)
    {
        if ((world.getWorldInfo().getTerrainType() != WorldType.FLAT || allowInSuperFlat))
        {
            if (!CommonUtils.isIDInList(world.provider.dimensionId, dimensionIDBlacklist))
            {
                random = getRandom(world, x, z);
                int tgtY = getWeightedInt(heightMin, heightMean, heightMax, heightNorm, random);
                
                if (isWorldGen)
                {
                    x += random.nextInt(16);
                    z += random.nextInt(16);
                }
                
                WorldGenFloatingIsland islandGenerator = getFloatingIslandGenerator(world, random, x, tgtY, z);
                
                if (!isWorldGen || random.nextInt(rarity) == 0)
                {
                    String debug = "";
                    int biomeID = getBlacklistBiomeIDWithinRange(world, x, z, islandGenerator.radius);
                    if (isWorldGen && biomeID > -1)
                    {
                        debug = "Location %d,%d skipped due to proximity of a biomeID (%d) in the biomeIDBlackList";
                        if (isWorldGen)
                        {
                            debug += ". \"Retry\" count incremented (%d).";
                            chunksToRetry++;
                        }
                        debug(debug, x, z, biomeID, chunksToRetry);
                    }
                    else if (isWorldGen && isVillageNearby(world, x, islandGenerator.yGround, z, islandGenerator.radius))
                    {
                        debug = "Location %d, %d skipped due to a village being found nearby";
                        if (isWorldGen)
                        {
                            debug += ". \"Retry\" count incremented (%d).";
                            chunksToRetry++;
                        }
                        debug(debug, x, z, chunksToRetry);
                    }
                    else if (!doGenerateSurface(world, random, x, tgtY, z, islandGenerator))
                    {
                        debug = "Location %d, %d failed during island generation";
                        if (isWorldGen)
                        {
                            debug += ". \"Retry\" count incremented (%d).";
                            chunksToRetry++;
                        }
                        debug(debug, x, z, chunksToRetry);
                    }
                }
                else if (isWorldGen && chunksToRetry > 0 && random.nextInt(128) == 0)
                {
                    if (getBlacklistBiomeIDWithinRange(world, x, z, islandGenerator.radius) == -1
                            && !isVillageNearby(world, x, islandGenerator.yGround, z, islandGenerator.radius))
                    {
                        if (doGenerateSurface(world, random, x, tgtY, z, islandGenerator))
                        {
                            chunksToRetry--;
                            debug("Successfully generated a \"retry\" floating island at %d,%d. \"Retry\" count decremented (%d).", x, z, chunksToRetry);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Accepts a WorldGenFloatingIsland object (or gets a new one) and executes the WorldGenFloatingIsland.generate() method
     */
    public static boolean doGenerateSurface(World world, Random random, int x, int tgtY, int z, WorldGenFloatingIsland islandGenerator)
    {
        if (islandGenerator == null)
            islandGenerator = getFloatingIslandGenerator(world, random, x, tgtY, z);
        
        return islandGenerator.generate(world, random, x, tgtY, z);
    }
    
    /**
     * Randomly calculates the variables needed to create a WorldGenFloatingIsland object and returns a new WorldGenFloatingIsland object
     */
    public static WorldGenFloatingIsland getFloatingIslandGenerator(World world, Random random, int x, int tgtY, int z)
    {
        int radius = getWeightedInt(radiusMin, radiusMean, radiusMax, radiusNorm, random);
        int yGround = CommonUtils.getHighestGroundBlock(world, x, tgtY, z);
        
        int depth = getWeightedInt(depthMin, depthMean, depthMax, depthNorm, random);
        int islandType = getWeightedIslandType(random);
        
        WorldType wt = world.getWorldInfo().getTerrainType();
        if (depth > yGround - (wt == WorldType.FLAT ? 1 : 5))
            depth = yGround - (wt == WorldType.FLAT ? 1 : 5);
        
        return new WorldGenFloatingIsland(radius, depth, yGround, islandType);
    }
    
    /**
     * Returns a new Random that's seeded based on the world seed and the x,z position
     */
    public static Random getRandom(World world, int x, int z)
    {
        Random random = new Random(world.getSeed());
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;
        random.setSeed(x * l + z * l1 ^ world.getSeed());
        return random;
    }
    
    @SuppressWarnings("unchecked")
    public static boolean isVillageNearby(World world, int x, int y, int z, int radius)
    {
        if (world.villageCollectionObj != null)
            for (Village village : (List<Village>) world.villageCollectionObj.getVillageList())
            {
                if (Math.sqrt(village.getCenter().getDistanceSquared(x, y, z)) < village.getVillageRadius() + radius)
                    return true;
            }
        return false;
    }
    
    public static int getBlacklistBiomeIDWithinRange(World world, int x, int z, int radius)
    {
        float reciprocalRootOf2 = 0.70710678f;
        int adjRadius = Math.round(radius * reciprocalRootOf2);
        Coord pos = new Coord(x, 64, z);
        ForgeDirection[] NSEW = { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST };
        
        int biomeID = pos.getBiomeGenBase(world).biomeID;
        if (CommonUtils.isIDInList(biomeID, biomeIDBlacklist))
            return biomeID;
        
        for (ForgeDirection fd : NSEW)
        {
            for (int i = radius; i > 0; i = i - 2)
            {
                biomeID = pos.getOffsetCoord(fd, i).getBiomeGenBase(world).biomeID;
                if (CommonUtils.isIDInList(biomeID, biomeIDBlacklist))
                    return biomeID;
            }
        }
        
        for (int ns = 0; ns < 2; ns++)
            for (int ew = 2; ew < 4; ew++)
                for (int r = adjRadius; r > 0; r = r - 2)
                {
                    biomeID = pos.getOffsetCoord(NSEW[ns], r).getOffsetCoord(NSEW[ew], r).getBiomeGenBase(world).biomeID;
                    if (CommonUtils.isIDInList(biomeID, biomeIDBlacklist))
                        return biomeID;
                }
        
        return -1;
    }
    
    public static void debug(String msg, Object... args)
    {
        if (allowDebugLogging)
            FRLog.info("[DEBUG] " + msg, args);
    }
    
    /**
     * Gets a random int that is weighted so as to create a bell curve like distribution lying between min and max 50% of ints will be
     * greater than the input mean, 50% of ints will be less than mean the higher the norm var is, the more likely output ints will be close
     * to mean
     */
    public static int getWeightedInt(int min, int mean, int max, int norm, Random random)
    {
        float deviation = 0.0f;
        float step;
        int weightedInt;
        
        //The magic happens here. It creates a var (deviation) 0 - 99 that is weighted based on input norm
        for (int i = 0; i < norm; i++)
        {
            deviation += (float) random.nextInt(99) / (float) norm;
        }
        
        //This here skews the data to account for mean not necessarily being in the middle of min and max
        if (deviation <= 50)
        {
            step = (mean - min) / 50F;
            min = min + Math.round(step * (deviation));
        }
        else
        {
            deviation -= 50;
            
            step = (max - mean) / 50F;
            min = mean + Math.round(step * (deviation));
        }
        
        //Calculates the final random
        weightedInt = min + random.nextInt(Math.abs((int) Math.ceil(step)));
        
        // If crappy inputs are fed in, make sure we don't return a negative value!
        return Math.abs(weightedInt);
    }
    
    public static int getWeightedIslandType(Random random)
    {
        int totalWeight = shapeSpheroidWeight + shapeConeWeight + shapeJetsonsWeight + shapeStalactiteWeight;
        int choice = random.nextInt(totalWeight);
        
        if (choice >= totalWeight - shapeStalactiteWeight)
            return WorldGenFloatingIsland.STALACTITE;
        else if (choice >= totalWeight - shapeStalactiteWeight - shapeJetsonsWeight)
            return WorldGenFloatingIsland.JETSONS;
        else if (choice >= totalWeight - shapeStalactiteWeight - shapeJetsonsWeight - shapeConeWeight)
            return WorldGenFloatingIsland.CONE;
        else
            return WorldGenFloatingIsland.SPHEROID;
    }
    
    static
    {
        blockIDBlacklistDefault = GameData.getBlockRegistry().getNameForObject(Blocks.bedrock) + ";";
        stringOfIdsDefault = GameData.getItemRegistry().getNameForObject(Items.arrow) + ", 10; " +
                GameData.getItemRegistry().getNameForObject(Items.arrow) + ", 16; " +
                GameData.getItemRegistry().getNameForObject(Items.coal) + ", 6; " +
                GameData.getItemRegistry().getNameForObject(Items.diamond) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.iron_ingot) + ", 3; " +
                GameData.getItemRegistry().getNameForObject(Items.gold_ingot) + ", 2; " +
                GameData.getItemRegistry().getNameForObject(Items.mushroom_stew) + ", 2; " +
                GameData.getItemRegistry().getNameForObject(Items.feather) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.chainmail_helmet) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.chainmail_chestplate) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.chainmail_leggings) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.chainmail_boots) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.painting) + ", 2; " +
                GameData.getItemRegistry().getNameForObject(Items.painting) + ", 5; " +
                GameData.getItemRegistry().getNameForObject(Items.golden_apple) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.golden_apple) + ", 3; " +
                GameData.getItemRegistry().getNameForObject(Items.bucket) + ", 2; " +
                GameData.getItemRegistry().getNameForObject(Items.lava_bucket) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.milk_bucket) + ", 1; " +
                GameData.getItemRegistry().getNameForObject(Items.book) + ", 4; " +
                GameData.getItemRegistry().getNameForObject(Items.slime_ball) + ", 6; " +
                GameData.getItemRegistry().getNameForObject(Items.egg) + ", 4; " +
                GameData.getItemRegistry().getNameForObject(Items.egg) + ", 8; " +
                GameData.getItemRegistry().getNameForObject(Items.glowstone_dust) + ", 12; " +
                GameData.getItemRegistry().getNameForObject(Items.glowstone_dust) + ", 8; " +
                GameData.getItemRegistry().getNameForObject(Items.cooked_fished) + ", 3; " +
                GameData.getItemRegistry().getNameForObject(Items.dye) + ", 5, 0; " +
                GameData.getItemRegistry().getNameForObject(Items.cake) + ", 2; " +
                GameData.getItemRegistry().getNameForObject(Items.blaze_rod) + ", 2; " +
                GameData.getItemRegistry().getNameForObject(Items.nether_wart) + ", 6; " +
                GameData.getItemRegistry().getNameForObject(Items.emerald) + ", 4; " +
                GameData.getItemRegistry().getNameForObject(Items.emerald) + ", 6; " +
                GameData.getItemRegistry().getNameForObject(Items.quartz) + ", 6; " +
                GameData.getItemRegistry().getNameForObject(Items.map) + ", 1; " +
                GameData.getBlockRegistry().getNameForObject(Blocks.obsidian) + ", 4; " +
                GameData.getBlockRegistry().getNameForObject(Blocks.ice) + ", 3;";
        
    }
}

package bspkrs.floatingruins;

import java.io.File;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.util.ForgeDirection;
import bspkrs.helpers.block.BlockHelper;
import bspkrs.helpers.item.ItemHelper;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.Coord;
import bspkrs.util.config.Configuration;

// Test Seed: 5460896710218081688
// 1470679938 (large biomes)
public final class FloatingRuins
{
    public final static String   VERSION_NUMBER               = Const.MCVERSION + ".r04";
    
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
    private final static int     radiusMaxDefault             = 50;
    public static int            radiusMax                    = radiusMaxDefault;
    private final static int     radiusMeanDefault            = 10;
    public static int            radiusMean                   = radiusMeanDefault;
    private final static int     radiusMinDefault             = 7;
    public static int            radiusMin                    = radiusMinDefault;
    private final static int     radiusNormDefault            = 3;
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
    
    public static Configuration  config;
    
    public static Configuration getConfig()
    {
        return config;
    }
    
    static
    {
        blockIDBlacklistDefault = BlockHelper.getUniqueID(Blocks.bedrock) + ";";
        stringOfIdsDefault = ItemHelper.getUniqueID(Items.arrow) + ", 10; " +
                ItemHelper.getUniqueID(Items.arrow) + ", 16; " +
                ItemHelper.getUniqueID(Items.coal) + ", 6; " +
                ItemHelper.getUniqueID(Items.diamond) + ", 1; " +
                ItemHelper.getUniqueID(Items.iron_ingot) + ", 3; " +
                ItemHelper.getUniqueID(Items.gold_ingot) + ", 2; " +
                ItemHelper.getUniqueID(Items.mushroom_stew) + ", 2; " +
                ItemHelper.getUniqueID(Items.feather) + ", 1; " +
                ItemHelper.getUniqueID(Items.chainmail_helmet) + ", 1; " +
                ItemHelper.getUniqueID(Items.chainmail_chestplate) + ", 1; " +
                ItemHelper.getUniqueID(Items.chainmail_leggings) + ", 1; " +
                ItemHelper.getUniqueID(Items.chainmail_boots) + ", 1; " +
                ItemHelper.getUniqueID(Items.painting) + ", 2; " +
                ItemHelper.getUniqueID(Items.painting) + ", 5; " +
                ItemHelper.getUniqueID(Items.golden_apple) + ", 1; " +
                ItemHelper.getUniqueID(Items.golden_apple) + ", 3; " +
                ItemHelper.getUniqueID(Items.bucket) + ", 2; " +
                ItemHelper.getUniqueID(Items.lava_bucket) + ", 1; " +
                ItemHelper.getUniqueID(Items.milk_bucket) + ", 1; " +
                ItemHelper.getUniqueID(Items.book) + ", 4; " +
                ItemHelper.getUniqueID(Items.slime_ball) + ", 6; " +
                ItemHelper.getUniqueID(Items.egg) + ", 4; " +
                ItemHelper.getUniqueID(Items.egg) + ", 8; " +
                ItemHelper.getUniqueID(Items.glowstone_dust) + ", 12; " +
                ItemHelper.getUniqueID(Items.glowstone_dust) + ", 8; " +
                ItemHelper.getUniqueID(Items.cooked_fished) + ", 3; " +
                ItemHelper.getUniqueID(Items.dye) + ", 5, 0; " +
                ItemHelper.getUniqueID(Items.cake) + ", 2; " +
                ItemHelper.getUniqueID(Items.blaze_rod) + ", 2; " +
                ItemHelper.getUniqueID(Items.nether_wart) + ", 6; " +
                ItemHelper.getUniqueID(Items.emerald) + ", 4; " +
                ItemHelper.getUniqueID(Items.emerald) + ", 6; " +
                ItemHelper.getUniqueID(Items.quartz) + ", 6; " +
                ItemHelper.getUniqueID(Items.map) + ", 1; " +
                BlockHelper.getUniqueID(Blocks.obsidian) + ", 4; " +
                BlockHelper.getUniqueID(Blocks.ice) + ", 3;";
        
    }
    
    public static void loadConfig(File file)
    {
        config = new Configuration(file);
        
        syncConfig();
    }
    
    public static void syncConfig()
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        config.load();
        
        config.addCustomCategoryComment(ctgyGen, "ATTENTION: Editing this file manually is no longer necessary. \n" +
                "On the Mods list screen select the entry for FloatingRuins, then click the Config button to modify these settings.");
        
        enabled = config.getBoolean(ConfigElement.ENABLED.key(), ctgyGen, enabledDefault, ConfigElement.ENABLED.desc(), ConfigElement.ENABLED.languageKey());
        allowDebugLogging = config.getBoolean(ConfigElement.ALLOW_DEBUG_LOGGING.key(), ctgyGen, allowDebugLoggingDefault, ConfigElement.ALLOW_DEBUG_LOGGING.desc(), ConfigElement.ALLOW_DEBUG_LOGGING.languageKey());
        allowInSuperFlat = config.getBoolean(ConfigElement.ALLOW_IN_SUPERFLAT.key(), ctgyGen, allowInSuperFlatDefault, ConfigElement.ALLOW_IN_SUPERFLAT.desc(), ConfigElement.ALLOW_IN_SUPERFLAT.languageKey());
        allowMultiMobSpawners = config.getBoolean(ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.key(), ctgyGen, allowMultiMobSpawnersDefault, ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.desc(), ConfigElement.ALLOW_MULTI_MOB_SPAWNERS.languageKey());
        harderDungeons = config.getBoolean(ConfigElement.HARDER_DUNGEONS.key(), ctgyGen, harderDungeonsDefault, ConfigElement.HARDER_DUNGEONS.desc(), ConfigElement.HARDER_DUNGEONS.languageKey());
        rarity = config.getInt(ConfigElement.RARITY.key(), ctgyGen, rarityDefault, 1, Integer.MAX_VALUE, ConfigElement.RARITY.desc(), ConfigElement.RARITY.languageKey());
        rarityDungeon = config.getInt(ConfigElement.RARITY_DUNGEON.key(), ctgyGen, rarityDungeonDefault, 1, Integer.MAX_VALUE, ConfigElement.RARITY_DUNGEON.desc(), ConfigElement.RARITY_DUNGEON.languageKey());
        heightMean = config.getInt(ConfigElement.HEIGHT_MEAN.key(), ctgyGen, heightMeanDefault, heightMin, heightMax, ConfigElement.HEIGHT_MEAN.desc(), ConfigElement.HEIGHT_MEAN.languageKey());
        heightMin = config.getInt(ConfigElement.HEIGHT_MIN.key(), ctgyGen, heightMinDefault, 80, heightMean, ConfigElement.HEIGHT_MIN.desc(), ConfigElement.HEIGHT_MIN.languageKey());
        heightMax = config.getInt(ConfigElement.HEIGHT_MAX.key(), ctgyGen, heightMaxDefault, heightMean, 240, ConfigElement.HEIGHT_MAX.desc(), ConfigElement.HEIGHT_MAX.languageKey());
        heightNorm = config.getInt(ConfigElement.HEIGHT_NORM.key(), ctgyGen, heightNormDefault, 1, 10, ConfigElement.HEIGHT_NORM.desc(), ConfigElement.HEIGHT_NORM.languageKey());
        radiusMean = config.getInt(ConfigElement.RADIUS_MEAN.key(), ctgyGen, radiusMeanDefault, radiusMin, radiusMax, ConfigElement.RADIUS_MEAN.desc(), ConfigElement.RADIUS_MEAN.languageKey());
        radiusMax = config.getInt(ConfigElement.RADIUS_MAX.key(), ctgyGen, radiusMaxDefault, radiusMean, 50, ConfigElement.RADIUS_MAX.desc(), ConfigElement.RADIUS_MAX.languageKey());
        radiusMin = config.getInt(ConfigElement.RADIUS_MIN.key(), ctgyGen, radiusMinDefault, 5, radiusMean, ConfigElement.RADIUS_MIN.desc(), ConfigElement.RADIUS_MIN.languageKey());
        radiusNorm = config.getInt(ConfigElement.RADIUS_NORM.key(), ctgyGen, radiusNormDefault, 1, 10, ConfigElement.RADIUS_NORM.desc(), ConfigElement.RADIUS_NORM.languageKey());
        depthMean = config.getInt(ConfigElement.DEPTH_MEAN.key(), ctgyGen, depthMeanDefault, depthMin, depthMax, ConfigElement.DEPTH_MEAN.desc(), ConfigElement.DEPTH_MEAN.languageKey());
        depthMin = config.getInt(ConfigElement.DEPTH_MIN.key(), ctgyGen, depthMinDefault, 5, depthMean, ConfigElement.DEPTH_MIN.desc(), ConfigElement.DEPTH_MIN.languageKey());
        depthMax = config.getInt(ConfigElement.DEPTH_MAX.key(), ctgyGen, depthMaxDefault, depthMean, 45, ConfigElement.DEPTH_MAX.desc(), ConfigElement.DEPTH_MAX.languageKey());
        depthNorm = config.getInt(ConfigElement.DEPTH_NORM.key(), ctgyGen, depthNormDefault, 1, 10, ConfigElement.DEPTH_NORM.desc(), ConfigElement.DEPTH_NORM.languageKey());
        shapeSpheroidWeight = config.getInt(ConfigElement.SHAPE_SPHEROID_WEIGHT.key(), ctgyGen, shapeSpheroidWeightDefault, 0, Integer.MAX_VALUE, ConfigElement.SHAPE_SPHEROID_WEIGHT.desc(), ConfigElement.SHAPE_SPHEROID_WEIGHT.languageKey());
        shapeConeWeight = config.getInt(ConfigElement.SHAPE_CONE_WEIGHT.key(), ctgyGen, shapeConeWeightDefault, 0, Integer.MAX_VALUE, ConfigElement.SHAPE_CONE_WEIGHT.desc(), ConfigElement.SHAPE_CONE_WEIGHT.languageKey());
        shapeJetsonsWeight = config.getInt(ConfigElement.SHAPE_JETSONS_WEIGHT.key(), ctgyGen, shapeJetsonsWeightDefault, 0, Integer.MAX_VALUE, ConfigElement.SHAPE_JETSONS_WEIGHT.desc(), ConfigElement.SHAPE_JETSONS_WEIGHT.languageKey());
        // shapeStalactiteWeight = config.getInt("shapeStalactiteWeight", ctgyGen, shapeStalactiteWeight, 0, 0, shapeStalactiteWeightDesc);
        numberOfItems = config.getInt(ConfigElement.NUMBER_OF_ITEMS.key(), ctgyGen, numberOfItemsDefault, 1, 27, ConfigElement.NUMBER_OF_ITEMS.desc(), ConfigElement.NUMBER_OF_ITEMS.languageKey());
        stringOfIds = config.getString(ConfigElement.STRING_OF_IDS.key(), ctgyGen, stringOfIdsDefault, ConfigElement.STRING_OF_IDS.desc(), ConfigElement.STRING_OF_IDS.languageKey());
        blockIDBlacklist = config.getString(ConfigElement.BLOCK_ID_BLACKLIST.key(), ctgyGen, blockIDBlacklistDefault, ConfigElement.BLOCK_ID_BLACKLIST.desc(), ConfigElement.BLOCK_ID_BLACKLIST.languageKey());
        dimensionIDBlacklist = config.getString(ConfigElement.DIMENSION_ID_BLACKLIST.key(), ctgyGen, dimensionIDBlacklistDefault, ConfigElement.DIMENSION_ID_BLACKLIST.desc(), ConfigElement.DIMENSION_ID_BLACKLIST.languageKey());
        biomeIDBlacklist = config.getString(ConfigElement.BIOME_ID_BLACKLIST.key(), ctgyGen, biomeIDBlacklistDefault, ConfigElement.BIOME_ID_BLACKLIST.desc(), ConfigElement.BIOME_ID_BLACKLIST.languageKey());
        spawnerDefault = config.getString(ConfigElement.SPAWNER_DEFAULT.key(), ctgyGen, spawnerDefaultDefault, ConfigElement.SPAWNER_DEFAULT.desc(), ConfigElement.SPAWNER_DEFAULT.languageKey());
        spawnerDesert = config.getString(ConfigElement.SPAWNER_DESERT.key(), ctgyGen, spawnerDesertDefault, ConfigElement.SPAWNER_DESERT.desc(), ConfigElement.SPAWNER_DESERT.languageKey());
        spawnerForest = config.getString(ConfigElement.SPAWNER_FOREST.key(), ctgyGen, spawnerForestDefault, ConfigElement.SPAWNER_FOREST.desc(), ConfigElement.SPAWNER_FOREST.languageKey());
        spawnerHills = config.getString(ConfigElement.SPAWNER_HILLS.key(), ctgyGen, spawnerHillsDefault, ConfigElement.SPAWNER_HILLS.desc(), ConfigElement.SPAWNER_HILLS.languageKey());
        spawnerIceBiomes = config.getString(ConfigElement.SPAWNER_ICE_BIOMES.key(), ctgyGen, spawnerIceBiomesDefault, ConfigElement.SPAWNER_ICE_BIOMES.desc(), ConfigElement.SPAWNER_ICE_BIOMES.languageKey());
        spawnerJungle = config.getString(ConfigElement.SPAWNER_JUNGLE.key(), ctgyGen, spawnerJungleDefault, ConfigElement.SPAWNER_JUNGLE.desc(), ConfigElement.SPAWNER_JUNGLE.languageKey());
        spawnerMushroom = config.getString(ConfigElement.SPAWNER_MUSHROOM.key(), ctgyGen, spawnerMushroomDefault, ConfigElement.SPAWNER_MUSHROOM.desc(), ConfigElement.SPAWNER_MUSHROOM.languageKey());
        spawnerOcean = config.getString(ConfigElement.SPAWNER_OCEAN.key(), ctgyGen, spawnerOceanDefault, ConfigElement.SPAWNER_OCEAN.desc(), ConfigElement.SPAWNER_OCEAN.languageKey());
        spawnerPlains = config.getString(ConfigElement.SPAWNER_PLAINS.key(), ctgyGen, spawnerPlainsDefault, ConfigElement.SPAWNER_PLAINS.desc(), ConfigElement.SPAWNER_PLAINS.languageKey());
        spawnerRiver = config.getString(ConfigElement.SPAWNER_RIVER.key(), ctgyGen, spawnerRiverDefault, ConfigElement.SPAWNER_RIVER.desc(), ConfigElement.SPAWNER_RIVER.languageKey());
        spawnerSwampland = config.getString(ConfigElement.SPAWNER_SWAMPLAND.key(), ctgyGen, spawnerSwamplandDefault, ConfigElement.SPAWNER_SWAMPLAND.desc(), ConfigElement.SPAWNER_SWAMPLAND.languageKey());
        spawnerTaiga = config.getString(ConfigElement.SPAWNER_TAIGA.key(), ctgyGen, spawnerTaigaDefault, ConfigElement.SPAWNER_TAIGA.desc(), ConfigElement.SPAWNER_TAIGA.languageKey());
        spawnerNearLava = config.getString(ConfigElement.SPAWNER_NEAR_LAVA.key(), ctgyGen, spawnerNearLavaDefault, ConfigElement.SPAWNER_NEAR_LAVA.desc(), ConfigElement.SPAWNER_NEAR_LAVA.languageKey());
        
        config.save();
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
        weightedInt = min + random.nextInt((int) Math.ceil(step));
        
        return weightedInt;
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
}

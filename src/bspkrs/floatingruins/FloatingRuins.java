package bspkrs.floatingruins;

import java.io.File;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.ForgeDirection;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
import bspkrs.util.Const;
import bspkrs.util.Coord;

// Test Seed: 5460896710218081688
// 1470679938 (large biomes)
public final class FloatingRuins
{
    public final static String  VERSION_NUMBER            = Const.MCVERSION + ".r02";
    
    public final static String  allowDebugLoggingDesc     = "Set to true if you want FloatingRuins to log info about what it's doing, false to disable";
    public static boolean       allowDebugLogging         = false;
    public final static String  allowInSuperFlatDesc      = "Set to true to allow generation of floating ruins on superflat maps, false to disable.";
    public static boolean       allowInSuperFlat          = false;
    public final static String  allowMultiMobSpawnersDesc = "When set to true spawners will be able to spawn any of the mobs for the biome the floating island generated in, set to false to use the old logic of randomly picking just one mob.";
    public static boolean       allowMultiMobSpawners     = true;
    public final static String  harderDungeonsDesc        = "Set to true to generate harder dungeons (roof is bedrock, chest is harder to get to, mobs spawn more aggressively), set to false to generate normal dungeons.";
    public static boolean       harderDungeons            = false;
    public final static String  rarityDesc                = "The probability of a floating island generating is 1 in each 'rarity' number of chunks.";
    public static int           rarity                    = 800;
    public final static String  baseHeightDesc            = "The base world height for floating ruins (Min=80, Max=225).";
    public static int           baseHeight                = 100;
    public final static String  heightVariationDesc       = "The amount of height variation allowed above baseHeight (Min=0, Max=240-baseHeight).";
    public static int           heightVariation           = 20;
    public final static String  baseRadiusDesc            = "The minimum radius of each island.  I recommend keeping this at 7 or more (Min=5, Max=50).";
    public static int           baseRadius                = 9;
    public final static String  radiusVariationDesc       = "The amount of floating island radius variation.  Making this value too large will result in very large chunks of your world turning into floating islands (Min=0, Max=50).";
    public static int           radiusVariation           = 6;
    public final static String  baseDepthDesc             = "The minimum depth/thickness of islands (Min=2, Max=50).";
    public static int           baseDepth                 = 5;
    public final static String  depthVariationDesc        = "The amount of variation allowed for the depth/thickness of islands (Min=0, Max=50).";
    public static int           depthVariation            = 45;
    public final static String  numberOfItemsDesc         = "The number of items in a ruin's chest.";
    public static int           numberOfItems             = 4;
    public final static String  blockIDBlacklistDesc      = "Add block IDs to this list if you don't want them to be moved when a floating island is generated.  Format used: \",\" separates between id and metadata and \";\" separates between each block.";
    public static String        blockIDBlacklist          = Block.bedrock.blockID + ";";
    public final static String  dimensionIDBlacklistDesc  = "Add dimension IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each dimension ID.";
    public static String        dimensionIDBlacklist      = "-1;1";
    public final static String  biomeIDBlacklistDesc      = "Add biome IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each biome ID.";
    public static String        biomeIDBlacklist          = "7;8;9;11;15;16;";
    public final static String  stringOfIdsDesc           = "The ids for items found in chests. Format used: \",\" separates between item id, quantity, and metadata and \";\" separates between each item.";
    public static String        stringOfIds               = "262, 10; 262, 16; 263, 6; 264, 1; 265, 3; 266, 2; 282, 2; 288, 1; 302, 1; 303, 1; 304, 1; 305, 1; 321, 2; 321, 5; 322, 1; 322, 3; 325, 2; 326, 1; 335, 1; 340, 1; 341, 2; 341, 4; 344, 2; 344, 4; 348, 12; 348, 8; 350, 1; 351, 5, 0; 354, 2; 369, 2; 372, 6; 388, 1; 388, 4; 46, 4; 79, 2";
    public final static String  spawnerDefaultDesc        = "Mob spawners can be configured using the mobs' names, each separated by a comma. Using \"Default\" will make the specified biome use the same settings as 'spawnerDefault'.";
    public static String        spawnerDefault            = "Creeper, Skeleton, Zombie, CaveSpider";
    public final static String  spawnerDesertDesc         = "";
    public static String        spawnerDesert             = "WitherSkeleton, PigZombie, Blaze";
    public final static String  spawnerForestDesc         = "";
    public static String        spawnerForest             = "Witch, CaveSpider";
    public final static String  spawnerPlainsDesc         = "";
    public static String        spawnerPlains             = "Spider, Zombie, Creeper";
    public final static String  spawnerSwamplandDesc      = "";
    public static String        spawnerSwampland          = "Creeper, CaveSpider, Witch";
    public final static String  spawnerTaigaDesc          = "";
    public static String        spawnerTaiga              = "Zombie, Creeper, Wolf";
    public final static String  spawnerHillsDesc          = "";
    public static String        spawnerHills              = "Default";
    public final static String  spawnerOceanDesc          = "";
    public static String        spawnerOcean              = "Silverfish, ChargedCreeper";
    public final static String  spawnerRiverDesc          = "";
    public static String        spawnerRiver              = "Silverfish, Creeper";
    public final static String  spawnerJungleDesc         = "";
    public static String        spawnerJungle             = "Skeleton, CaveSpider";
    public final static String  spawnerIceBiomesDesc      = "";
    public static String        spawnerIceBiomes          = "Zombie, Skeleton, Wolf";
    public final static String  spawnerMushroomDesc       = "";
    public static String        spawnerMushroom           = "MushroomCow";
    public final static String  spawnerNearLavaDesc       = "If the dungeon is close enough to lava, the spawner will use these mobs:";
    public static String        spawnerNearLava           = "Blaze, LavaSlime, WitherSkeleton, PigZombie";
    
    private static int          chunksToRetry             = 0;
    
    public static Configuration config;
    
    public static void loadConfig(File file)
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        config = new Configuration(file);
        
        config.load();
        
        allowDebugLogging = config.getBoolean("allowDebugLogging", ctgyGen, allowDebugLogging, allowDebugLoggingDesc);
        allowInSuperFlat = config.getBoolean("allowInSuperFlat", ctgyGen, allowInSuperFlat, allowInSuperFlatDesc);
        allowMultiMobSpawners = config.getBoolean("allowMultiMobSpawners", ctgyGen, allowMultiMobSpawners, allowMultiMobSpawnersDesc);
        harderDungeons = config.getBoolean("harderDungeons", ctgyGen, harderDungeons, harderDungeonsDesc);
        rarity = config.getInt("rarity", ctgyGen, rarity, 1, Integer.MAX_VALUE, rarityDesc);
        baseHeight = config.getInt("baseHeight", ctgyGen, baseHeight, 80, 215, baseHeightDesc);
        heightVariation = config.getInt("heightVariation", ctgyGen, heightVariation, 0, 160, heightVariationDesc);
        baseRadius = config.getInt("baseRadius", ctgyGen, baseRadius, 6, 50, baseRadiusDesc);
        radiusVariation = config.getInt("radiusVariation", ctgyGen, radiusVariation, 0, 50, radiusVariationDesc);
        baseDepth = config.getInt("baseDepth", ctgyGen, baseDepth, 2, 50, baseDepthDesc);
        depthVariation = config.getInt("depthVariation", ctgyGen, depthVariation, 0, 50, depthVariationDesc);
        numberOfItems = config.getInt("numberOfItems", ctgyGen, numberOfItems, 1, 27, numberOfItemsDesc);
        stringOfIds = config.getString("stringOfIds", ctgyGen, stringOfIds, stringOfIdsDesc);
        blockIDBlacklist = config.getString("blockIDBlacklist", ctgyGen, blockIDBlacklist, blockIDBlacklistDesc);
        dimensionIDBlacklist = config.getString("dimensionIDBlacklist", ctgyGen, dimensionIDBlacklist, dimensionIDBlacklistDesc);
        biomeIDBlacklist = config.getString("biomeIDBlacklist", ctgyGen, biomeIDBlacklist, biomeIDBlacklistDesc);
        spawnerDefault = config.getString("spawnerDefault", ctgyGen, spawnerDefault, spawnerDefaultDesc);
        spawnerDesert = config.getString("spawnerDesert", ctgyGen, spawnerDesert, spawnerDesertDesc);
        spawnerForest = config.getString("spawnerForest", ctgyGen, spawnerForest, spawnerForestDesc);
        spawnerHills = config.getString("spawnerHills", ctgyGen, spawnerHills, spawnerHillsDesc);
        spawnerIceBiomes = config.getString("spawnerIceBiomes", ctgyGen, spawnerIceBiomes, spawnerIceBiomesDesc);
        spawnerJungle = config.getString("spawnerJungle", ctgyGen, spawnerJungle, spawnerJungleDesc);
        spawnerMushroom = config.getString("spawnerMushroom", ctgyGen, spawnerMushroom, spawnerMushroomDesc);
        spawnerOcean = config.getString("spawnerOcean", ctgyGen, spawnerOcean, spawnerOceanDesc);
        spawnerPlains = config.getString("spawnerPlains", ctgyGen, spawnerPlains, spawnerPlainsDesc);
        spawnerRiver = config.getString("spawnerRiver", ctgyGen, spawnerRiver, spawnerRiverDesc);
        spawnerSwampland = config.getString("spawnerSwampland", ctgyGen, spawnerSwampland, spawnerSwamplandDesc);
        spawnerTaiga = config.getString("spawnerTaiga", ctgyGen, spawnerTaiga, spawnerTaigaDesc);
        spawnerNearLava = config.getString("spawnerNearLava", ctgyGen, spawnerNearLava, spawnerNearLavaDesc);
        
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
                int tgtY = baseHeight + random.nextInt(heightVariation);
                
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
                    if (biomeID > -1)
                    {
                        debug = "Location %d,%d skipped due to proximity of a biomeID (%d) in the biomeIDBlackList";
                        if (isWorldGen)
                        {
                            debug += ". \"Retry\" count incremented (%d).";
                            chunksToRetry++;
                        }
                        debug(debug, x, z, biomeID, chunksToRetry);
                    }
                    else if (isVillageNearby(world, x, islandGenerator.yGround, z, islandGenerator.radius))
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
        int radius = baseRadius + random.nextInt(radiusVariation);
        int yGround = CommonUtils.getHighestGroundBlock(world, x, tgtY, z);
        
        Random random2 = getRandom(world, x, z);
        
        float depthRatio = random2.nextInt(5) == 0 ? random2.nextFloat() * 0.5F + 2.0F : random2.nextFloat() * 0.2F + 0.4F;
        int depth = (int) Math.ceil(radius * depthRatio);
        int islandType = WorldGenFloatingIsland.islandTypes[random.nextInt(WorldGenFloatingIsland.islandTypes.length)];
        
        if (depth > baseDepth + depthVariation || depth < baseDepth || depth > yGround - 5)
        {
            WorldType wt = world.getWorldInfo().getTerrainType();
            depth = Math.min(yGround - (wt == WorldType.FLAT ? 1 : 5), baseDepth + random2.nextInt(depthVariation));
            depthRatio = depth / (float) radius;
        }
        
        return new WorldGenFloatingIsland(radius, depthRatio, yGround, islandType);
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
                biomeID = pos.getDirectionallyOffsetCoord(fd, i).getBiomeGenBase(world).biomeID;
                if (CommonUtils.isIDInList(biomeID, biomeIDBlacklist))
                    return biomeID;
            }
        }
        
        for (int ns = 0; ns < 2; ns++)
            for (int ew = 2; ew < 4; ew++)
                for (int r = adjRadius; r > 0; r = r - 2)
                {
                    biomeID = pos.getDirectionallyOffsetCoord(NSEW[ns], r).getDirectionallyOffsetCoord(NSEW[ew], r).getBiomeGenBase(world).biomeID;
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
}

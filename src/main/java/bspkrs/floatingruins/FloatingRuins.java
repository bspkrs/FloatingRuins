package bspkrs.floatingruins;

import java.io.File;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;
import bspkrs.helpers.block.BlockHelper;
import bspkrs.helpers.item.ItemHelper;
import bspkrs.util.BSConfiguration;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.Coord;

// Test Seed: 5460896710218081688
// 1470679938 (large biomes)
public final class FloatingRuins
{
    public final static String    VERSION_NUMBER            = Const.MCVERSION + ".r01";
    
    public final static String    allowDebugLoggingDesc     = "Set to true if you want FloatingRuins to log info about what it's doing, false to disable";
    public static boolean         allowDebugLogging         = false;
    public final static String    allowInSuperFlatDesc      = "Set to true to allow generation of floating ruins on superflat maps, false to disable.";
    public static boolean         allowInSuperFlat          = false;
    public final static String    allowMultiMobSpawnersDesc = "When set to true spawners will be able to spawn any of the mobs for the biome the floating island generated in, set to false to use the old logic of randomly picking just one mob.";
    public static boolean         allowMultiMobSpawners     = true;
    public final static String    harderDungeonsDesc        = "Set to true to generate harder dungeons (roof is bedrock, chest is harder to get to, mobs spawn more aggressively), set to false to generate normal dungeons.";
    public static boolean         harderDungeons            = false;
    public final static String    rarityDesc                = "The probability of a floating island generating is 1 in each 'rarity' number of chunks.";
    public static int             rarity                    = 800;
    public final static String    rarityDungeonDesc         = "The probability of a floating island having a dungeon on it is 1 in each 'dungeonRarity' number of islands.";
    public static int             rarityDungeon             = 1;
    public final static String    heightMaxDesc      		= "The maximum world height for floating ruins (Min=heightMean, Max=240).";
    public static int             heightMax                 = 240;
    public final static String    heightMeanDesc        	= "The average island height. Half of the islands will be below heightMean, half of the islands will be above heightMean. (Min=heightMin, Max=heightMax).";
    public static int             heightMean            	= 100;
    public final static String    heightMinDesc             = "The minimum world height for floating ruins (Min=80, Max=heightMax).";
    public static int             heightMin                 = 80;
    public final static String    heightNormDesc        	= "How strongly island height should tend to lie around heightMean. (Min=1, Max=10).";
    public static int             heightNorm            	= 3;
    public final static String    radiusMaxDesc             = "The maximum radius of each island.  Making this value too large will result in very large chunks of your world turning into floating islands (Min=radiusMean, Max=50).";
    public static int             radiusMax       	        = 50;
    public final static String    radiusMeanDesc         	= "The average island radius. Half of the islands will be smaller than radiusMean, half of the islands will be larger than radiusMean. (Min=radiusMin, Max=radiusMax).";
    public static int             radiusMean          	    = 9;
    public final static String    radiusMinDesc             = "The minimum radius of each island.  I recommend keeping this at 7 or more (Min=5, Max=radiusMean).";
    public static int             radiusMin                 = 5;
    public final static String    radiusNormDesc      	    = "How strongly island radius should tend to lie around radiusMean. (Min=1, Max=10).";
    public static int             radiusNorm          	    = 3;
    public final static String    depthMaxDesc  			= "The maximum depth/thickness of islands (Min=depthMean, Max=50).";
    public static int             depthMax                  = 45;
    public final static String    depthMeanDesc        	    = "The average island depth. Half of the islands will be shallower than depthMean, half of the islands will be deeper than depthMean. (Min=depthMin, Max=depthMax).";
    public static int             depthMean           	    = 9;
    public final static String    depthMinDesc              = "The minimum depth/thickness of islands (Min=2, Max=depthMean).";
    public static int             depthMin                  = 5;
    public final static String    depthNormDesc      	  	= "How strongly island depth should tend to lie around depthMean. (Min=1, Max=10).";
    public static int             depthNorm          	    = 3;  
    public final static String    shapeSpheroidWeightDesc	= "The relative chance of an island having the spheroid shape.";
    public static int             shapeSpheroidWeight  	    = 21;      
    public final static String    shapeConeWeightDesc		= "The relative chance of an island having the cone shape.";
    public static int             shapeConeWeight  	    	= 14;        
    public final static String    shapeJetsonsWeightDesc	= "The relative chance of an island having the jetsons shape.";
    public static int             shapeJetsonsWeight  	    = 1;         
    public final static String    shapeStalactiteWeightDesc	= "UNIMPLEMENTED and IGNORED: The relative chance of an island having the stalactite shape.";
    public static int             shapeStalactiteWeight  	= 0;   
    public final static String    numberOfItemsDesc         = "The number of items in a ruin's chest.";
    public static int             numberOfItems             = 4;
    public final static String    blockIDBlacklistDesc      = "Add block IDs to this list if you don't want them to be moved when a floating island is generated.  Format used: \",\" separates between id and metadata and \";\" separates between each block.";
    public static String          blockIDBlacklist;
    public final static String    dimensionIDBlacklistDesc  = "Add dimension IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each dimension ID.";
    public static String          dimensionIDBlacklist      = "-1;1";
    public final static String    biomeIDBlacklistDesc      = "Add biome IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each biome ID.";
    public static String          biomeIDBlacklist          = "7;8;9;11;15;16;";
    public final static String    stringOfIdsDesc           = "The ids for items found in chests. Format used: \",\" separates between item id, quantity, and metadata and \";\" separates between each item.";
    public static String          stringOfIds;
    public final static String    spawnerDefaultDesc        = "Mob spawners can be configured using the mobs' names, each separated by a comma. Using \"Default\" will make the specified biome use the same settings as 'spawnerDefault'.";
    public static String          spawnerDefault            = "Creeper, Skeleton, Zombie, CaveSpider";
    public final static String    spawnerDesertDesc         = "";
    public static String          spawnerDesert             = "WitherSkeleton, PigZombie, Blaze";
    public final static String    spawnerForestDesc         = "";
    public static String          spawnerForest             = "Witch, CaveSpider";
    public final static String    spawnerPlainsDesc         = "";
    public static String          spawnerPlains             = "Spider, Zombie, Creeper";
    public final static String    spawnerSwamplandDesc      = "";
    public static String          spawnerSwampland          = "Creeper, CaveSpider, Witch";
    public final static String    spawnerTaigaDesc          = "";
    public static String          spawnerTaiga              = "Zombie, Creeper, Wolf";
    public final static String    spawnerHillsDesc          = "";
    public static String          spawnerHills              = "Default";
    public final static String    spawnerOceanDesc          = "";
    public static String          spawnerOcean              = "Silverfish, ChargedCreeper";
    public final static String    spawnerRiverDesc          = "";
    public static String          spawnerRiver              = "Silverfish, Creeper";
    public final static String    spawnerJungleDesc         = "";
    public static String          spawnerJungle             = "Skeleton, CaveSpider";
    public final static String    spawnerIceBiomesDesc      = "";
    public static String          spawnerIceBiomes          = "Zombie, Skeleton, Wolf";
    public final static String    spawnerMushroomDesc       = "";
    public static String          spawnerMushroom           = "MushroomCow";
    public final static String    spawnerNearLavaDesc       = "If the dungeon is close enough to lava, the spawner will use these mobs:";
    public static String          spawnerNearLava           = "Blaze, LavaSlime, WitherSkeleton, PigZombie";
    
    private static int            chunksToRetry             = 0;
    
    public static BSConfiguration config;
    
    static
    {
        blockIDBlacklist = BlockHelper.getUniqueID(Blocks.bedrock) + ";";
        stringOfIds = ItemHelper.getUniqueID(Items.arrow) + ", 10;" +
                ItemHelper.getUniqueID(Items.arrow) + ", 16;" +
                ItemHelper.getUniqueID(Items.coal) + ", 6;" +
                ItemHelper.getUniqueID(Items.diamond) + ", 1;" +
                ItemHelper.getUniqueID(Items.iron_ingot) + ", 3;" +
                ItemHelper.getUniqueID(Items.gold_ingot) + ", 2;" +
                ItemHelper.getUniqueID(Items.mushroom_stew) + ", 2;" +
                ItemHelper.getUniqueID(Items.feather) + ", 1;" +
                ItemHelper.getUniqueID(Items.chainmail_helmet) + ", 1;" +
                ItemHelper.getUniqueID(Items.chainmail_chestplate) + ", 1;" +
                ItemHelper.getUniqueID(Items.chainmail_leggings) + ", 1;" +
                ItemHelper.getUniqueID(Items.chainmail_boots) + ", 1;" +
                ItemHelper.getUniqueID(Items.painting) + ", 2;" +
                ItemHelper.getUniqueID(Items.painting) + ", 5;" +
                ItemHelper.getUniqueID(Items.golden_apple) + ", 1;" +
                ItemHelper.getUniqueID(Items.golden_apple) + ", 3;" +
                ItemHelper.getUniqueID(Items.bucket) + ", 2;" +
                ItemHelper.getUniqueID(Items.lava_bucket) + ", 1;" +
                ItemHelper.getUniqueID(Items.milk_bucket) + ", 1;" +
                ItemHelper.getUniqueID(Items.book) + ", 4;" +
                ItemHelper.getUniqueID(Items.slime_ball) + ", 6;" +
                ItemHelper.getUniqueID(Items.egg) + ", 4;" +
                ItemHelper.getUniqueID(Items.egg) + ", 8;" +
                ItemHelper.getUniqueID(Items.glowstone_dust) + ", 12;" +
                ItemHelper.getUniqueID(Items.glowstone_dust) + ", 8;" +
                ItemHelper.getUniqueID(Items.cooked_fished) + ", 3;" +
                ItemHelper.getUniqueID(Items.dye) + ", 5, 0;" +
                ItemHelper.getUniqueID(Items.cake) + ", 2;" +
                ItemHelper.getUniqueID(Items.blaze_rod) + ", 2;" +
                ItemHelper.getUniqueID(Items.nether_wart) + ", 6;" +
                ItemHelper.getUniqueID(Items.emerald) + ", 4;" +
                ItemHelper.getUniqueID(Items.emerald) + ", 6;" +
                ItemHelper.getUniqueID(Items.quartz) + ", 6;" +
                ItemHelper.getUniqueID(Items.map) + ", 1;" +
                BlockHelper.getUniqueID(Blocks.obsidian) + ", 4;" +
                BlockHelper.getUniqueID(Blocks.ice) + ", 3;";
        
    }
    
    public static void loadConfig(File file)
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        config = new BSConfiguration(file);
        
        config.load();
        
        allowDebugLogging = config.getBoolean("allowDebugLogging", ctgyGen, allowDebugLogging, allowDebugLoggingDesc);
        allowInSuperFlat = config.getBoolean("allowInSuperFlat", ctgyGen, allowInSuperFlat, allowInSuperFlatDesc);
        allowMultiMobSpawners = config.getBoolean("allowMultiMobSpawners", ctgyGen, allowMultiMobSpawners, allowMultiMobSpawnersDesc);
        harderDungeons = config.getBoolean("harderDungeons", ctgyGen, harderDungeons, harderDungeonsDesc);
        rarity = config.getInt("rarity", ctgyGen, rarity, 1, Integer.MAX_VALUE, rarityDesc);
        rarityDungeon = config.getInt("rarityDungeon", ctgyGen, rarityDungeon, 1, Integer.MAX_VALUE, rarityDungeonDesc);		
        heightMax = config.getInt("heightMax", ctgyGen, heightMax, heightMean, 240, heightMaxDesc);
        heightMean = config.getInt("heightMean", ctgyGen, heightMean, heightMin, heightMax, heightMeanDesc);
        heightMin = config.getInt("heightMin", ctgyGen, heightMin, 80, heightMean, heightMinDesc);
        heightNorm = config.getInt("heightNorm", ctgyGen, heightNorm, 1, 10, heightNormDesc);
        radiusMax = config.getInt("radiusMax", ctgyGen, radiusMax, radiusMean, 50, radiusMaxDesc);
        radiusMean = config.getInt("radiusMean", ctgyGen, radiusMean, radiusMin, radiusMax, radiusMeanDesc);
        radiusMin = config.getInt("radiusMin", ctgyGen, radiusMin, 5, radiusMean, radiusMinDesc);
        radiusNorm = config.getInt("radiusNorm", ctgyGen, radiusNorm, 1, 10, radiusNormDesc);
        depthMax = config.getInt("depthMax", ctgyGen, depthMax, depthMean, 45, depthMaxDesc);
        depthMean = config.getInt("depthMean", ctgyGen, depthMean, depthMin, depthMax, depthMeanDesc);
        depthMin = config.getInt("depthMin", ctgyGen, depthMin, 5, depthMean, depthMinDesc); 
        depthNorm = config.getInt("depthNorm", ctgyGen, depthNorm, 1, 10, depthNormDesc);   
        shapeSpheroidWeight = config.getInt("shapeSpheroidWeight", ctgyGen, shapeSpheroidWeight, 0, Integer.MAX_VALUE, shapeSpheroidWeightDesc);   
        shapeConeWeight = config.getInt("shapeConeWeight", ctgyGen, shapeConeWeight, 0, Integer.MAX_VALUE, shapeConeWeightDesc);   
        shapeJetsonsWeight = config.getInt("shapeJetsonsWeight", ctgyGen, shapeJetsonsWeight, 0, Integer.MAX_VALUE, shapeJetsonsWeightDesc);   
        shapeStalactiteWeight = config.getInt("shapeStalactiteWeight", ctgyGen, shapeStalactiteWeight, 0, 0, shapeStalactiteWeightDesc);
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
                int tgtY = GetWeightedInt(heightMin, heightMean, heightMax, heightNorm, random);
                
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
    	int radius = GetWeightedInt(radiusMin, radiusMean, radiusMax, radiusNorm, random);
        int yGround = CommonUtils.getHighestGroundBlock(world, x, tgtY, z);
        
        int depth = GetWeightedInt(depthMin, depthMean, depthMax, depthNorm, random);
        int islandType = GetWeightedIslandType(random);
        
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
	* Gets a random int that is weighted so as to create a bell curve like distribution lying between min and max
	* 50% of ints will be greater than the input mean, 50% of ints will be less than mean
	* the higher the norm var is, the more likely output ints will be close to mean
	*/
    public static int GetWeightedInt(int min, int mean, int max, int norm, Random random)
    {    	
    	float deviation = 0.0f;
    	float step;
    	int weightedInt;
    	
		//The magic happens here. It creates a var (deviation) 0 - 99 that is weighted based on input norm
    	for (int i = 0; i < norm; i++)
    	{
    		deviation += (float)random.nextInt(99)/(float)norm;
    	}
    	
		//This here skews the data to account for mean not necessarily being in the middle of min and max
    	if (deviation <= 50)
    	{
    		step = (mean - min)/50F;
    		min = min + Math.round(step * (deviation));
    	}
    	else
    	{
    		deviation -= 50;
    		
    		step = (max - mean)/50F;
    		min = mean + Math.round(step * (deviation));
    	}
    	
		//Calculates the final random
    	weightedInt = min + random.nextInt((int)Math.ceil(step));
    	
    	return weightedInt;   
    }
    
    public static int GetWeightedIslandType(Random random)
    {
    	int totalWeight = shapeSpheroidWeight + shapeConeWeight + shapeJetsonsWeight + shapeStalactiteWeight;
    	int choice = random.nextInt(totalWeight);
    	
    	if(choice >= totalWeight - shapeStalactiteWeight)
    		return WorldGenFloatingIsland.STALACTITE;
    	else if(choice >= totalWeight - shapeStalactiteWeight - shapeJetsonsWeight)
    		return WorldGenFloatingIsland.JETSONS;
    	else if(choice >= totalWeight - shapeStalactiteWeight - shapeJetsonsWeight - shapeConeWeight)
    		return WorldGenFloatingIsland.CONE;
    	else
    		return WorldGenFloatingIsland.SPHEROID;
    }
}

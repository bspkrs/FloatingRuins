package bspkrs.floatingruins;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import bspkrs.util.CommonUtils;

// Test Seed: 5460896710218081688
// 1470679938 (large biomes)
public final class FloatingRuins
{
    public final static String VERSION_NUMBER            = "1.4.6.r05";
    
    public final static String allowDebugLoggingDesc     = "Set to true if you want FloatingRuins to log info about what it's doing, false to disable";
    public static boolean      allowDebugLogging         = false;
    public final static String allowUpdateCheckDesc      = "Set to true to allow checking for mod updates, false to disable";
    public static boolean      allowUpdateCheck          = true;
    public final static String allowInSuperFlatDesc      = "Set to true to allow generation of floating ruins on superflat maps, false to disable.";
    public static boolean      allowInSuperFlat          = false;
    public final static String allowMultiMobSpawnersDesc = "When set to true spawners will be able to spawn any of the mobs for the biome the floating island generated in, set to false to use the old logic of randomly picking just one mob.";
    public static boolean      allowMultiMobSpawners     = true;
    public final static String harderDungeonsDesc        = "Set to true to generate harder dungeons (roof is bedrock, chest is harder to get to, mobs spawn more aggressively), set to false to generate normal dungeons.";
    public static boolean      harderDungeons            = false;
    public final static String rarityDesc                = "The probability of a floating island generating is 1 in each 'rarity' number of chunks.";
    public static int          rarity                    = 800;
    public final static String baseHeightDesc            = "The base world height for floating ruins (Min=80, Max=240).";
    public static int          baseHeight                = 100;
    public final static String heightVariationDesc       = "The amount of height variation allowed above baseHeight (Min=0, Max=240-baseHeight).";
    public static int          heightVariation           = 20;
    public final static String baseRadiusDesc            = "The minimum radius of each island.  I recommend keeping this at 7 or more (Min=5, Max=50).";
    public static int          baseRadius                = 9;
    public final static String radiusVariationDesc       = "The amount of floating island radius variation.  Making this value too large will result in very large chunks of your world turning into floating islands (Min=0, Max=50).";
    public static int          radiusVariation           = 6;
    public final static String baseDepthDesc             = "The minimum depth/thickness of islands (Min=2, Max=50).";
    public static int          baseDepth                 = 5;
    public final static String depthVariationDesc        = "The amount of variation allowed for the depth/thickness of islands (Min=0, Max=50).";
    public static int          depthVariation            = 45;
    public final static String numberOfItemsDesc         = "The number of items in a ruin's chest.";
    public static int          numberOfItems             = 4;
    public final static String blockIDBlacklistDesc      = "Add block IDs to this list if you don't want them to be moved when a floating island is generated.  Format used: \",\" separates between id and metadata and \";\" separates between each block.";
    public static String       blockIDBlacklist          = Block.bedrock.blockID + ";";
    public final static String dimensionIDBlacklistDesc  = "Add dimension IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each dimension ID.";
    public static String       dimensionIDBlacklist      = "-1;1";
    public final static String biomeIDBlacklistDesc      = "Add biome IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each biome ID.";
    public static String       biomeIDBlacklist          = "8;9;";
    public final static String stringOfIdsDesc           = "The ids for items found in chests. Format used: \",\" separates between item id, quantity, and metadata and \";\" separates between each item.";
    public static String       stringOfIds               = "262, 10; 262, 16; 263, 6; 264, 1; 265, 3; 266, 2; 282, 2; 288, 1; 302, 1; 303, 1; 304, 1; 305, 1; 321, 2; 321, 5; 322, 1; 322, 3; 325, 2; 326, 1; 335, 1; 340, 1; 341, 2; 341, 4; 344, 2; 344, 4; 348, 12; 348, 8; 350, 1; 351, 5, 0; 354, 2; 369, 2; 372, 6; 388, 1; 388, 4; 46, 4; 79, 2";
    public final static String spawnerDefaultDesc        = "Mob spawners can be configured using the mobs' names, each separated by a comma. Using \"Default\" will make the specified biome use the same settings as 'spawnerDefault'.";
    public static String       spawnerDefault            = "Creeper, Skeleton, Zombie, CaveSpider";
    public final static String spawnerDesertDesc         = "";
    public static String       spawnerDesert             = "WitherSkeleton, PigZombie, Blaze";
    public final static String spawnerForestDesc         = "";
    public static String       spawnerForest             = "Witch, CaveSpider";
    public final static String spawnerPlainsDesc         = "";
    public static String       spawnerPlains             = "Spider, Zombie, ChargedCreeper";
    public final static String spawnerSwamplandDesc      = "";
    public static String       spawnerSwampland          = "Creeper, CaveSpider, Witch";
    public final static String spawnerTaigaDesc          = "";
    public static String       spawnerTaiga              = "Zombie, ChargedCreeper";
    public final static String spawnerHillsDesc          = "";
    public static String       spawnerHills              = "Default";
    public final static String spawnerOceanDesc          = "";
    public static String       spawnerOcean              = "Silverfish, ChargedCreeper";
    public final static String spawnerRiverDesc          = "";
    public static String       spawnerRiver              = "Silverfish";
    public final static String spawnerJungleDesc         = "";
    public static String       spawnerJungle             = "Skeleton, CaveSpider";
    public final static String spawnerIceBiomesDesc      = "";
    public static String       spawnerIceBiomes          = "Zombie, Skeleton";
    public final static String spawnerMushroomDesc       = "";
    public static String       spawnerMushroom           = "MushroomCow";
    public final static String spawnerNearLavaDesc       = "If the dungeon is close enough to lava, the spawner will use one of these mobs:";
    public static String       spawnerNearLava           = "Blaze, LavaSlime, WitherSkeleton, PigZombie";
    
    public static void generateSurface(World world, Random random, int x, int z)
    {
        if ((world.getWorldInfo().getTerrainType() != WorldType.FLAT || allowInSuperFlat)
                && !CommonUtils.isIDInList(world.provider.dimensionId, FloatingRuins.dimensionIDBlacklist)
                && !CommonUtils.isIDInList(world.getBiomeGenForCoords(x, z).biomeID, FloatingRuins.biomeIDBlacklist))
        {
            random = new Random(world.getSeed());
            long l = (random.nextLong() / 2L) * 2L + 1L;
            long l1 = (random.nextLong() / 2L) * 2L + 1L;
            random.setSeed(x * l + z * l1 ^ world.getSeed());
            
            int bH = Math.max(80, Math.min(240, baseHeight));
            int hV = Math.max(0, Math.min(240 - baseHeight, heightVariation));
            int y = bH + random.nextInt(hV);
            if (random.nextInt(rarity) == 0)
                (new WorldGenFloatingIsland()).generate(world, random, x, y, z);
        }
    }
    
    public static void debug(String msg, Object... args)
    {
        if (allowDebugLogging)
            FRLog.info("[DEBUG] " + msg, args);
    }
}

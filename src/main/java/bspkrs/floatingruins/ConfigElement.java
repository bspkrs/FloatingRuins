package bspkrs.floatingruins;

import static net.minecraftforge.common.config.Property.Type.BOOLEAN;
import static net.minecraftforge.common.config.Property.Type.INTEGER;
import static net.minecraftforge.common.config.Property.Type.STRING;
import net.minecraftforge.common.config.Property;
import bspkrs.floatingruins.fml.Reference;

public enum ConfigElement
{
    ENABLED("enabled", Reference.CTGY_GEN, "bspkrs.fr.configgui.enabled", "Enables or disables Floating Ruins generation.", BOOLEAN),
    ALLOW_DEBUG_LOGGING("allowDebugLogging", Reference.CTGY_GEN, "bspkrs.fr.configgui.allowDebugLogging",
            "Set to true if you want FloatingRuins to log info about what it's doing, false to disable", BOOLEAN),
    ALLOW_IN_SUPERFLAT("allowInSuperFlat", Reference.CTGY_GEN, "bspkrs.fr.configgui.allowInSuperFlat",
            "Set to true to allow generation of floating ruins on superflat maps, false to disable.", BOOLEAN),
    ALLOW_MULTI_MOB_SPAWNERS("allowMultiMobSpawners", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.allowMultiMobSpawners",
            "When set to true spawners will be able to spawn any of the mobs for the biome the floating island generated in, set to false to use the old logic of randomly picking just one mob.", BOOLEAN),
    HARDER_DUNGEONS("harderDungeons", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.harderDungeons",
            "Set to true to generate harder dungeons (roof is bedrock, chest is harder to get to, mobs spawn more aggressively), set to false to generate normal dungeons.", BOOLEAN),
    RARITY("rarity", Reference.CTGY_ISLANDS, "bspkrs.fr.configgui.rarity",
            "The probability of a floating island generating is 1 in each 'rarity' number of chunks.", INTEGER),
    RARITY_DUNGEON("rarityDungeon", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.rarityDungeon",
            "The probability of a floating island having a dungeon on it is 1 in each 'rarityDungeon' number of islands.", INTEGER),
    HEIGHT_MAX("heightMax", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.heightMax",
            "The maximum world height for floating ruins (Min=heightMean, Max=225).", INTEGER),
    HEIGHT_MEAN("heightMean", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.heightMean",
            "The average island height. Half of the islands will be below heightMean, half of the islands will be above heightMean. (Min=heightMin, Max=heightMax).", INTEGER),
    HEIGHT_MIN("heightMin", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.heightMin",
            "The minimum world height for floating ruins (Min=80, Max=heightMax).", INTEGER),
    HEIGHT_NORM("heightNorm", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.heightNorm",
            "How strongly island height should tend to lie around heightMean. (Min=1, Max=10).", INTEGER),
    RADIUS_MAX("radiusMax", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.radiusMax",
            "The maximum radius of each island.  Making this value too large will result in very large chunks of your world turning into floating islands (Min=radiusMean, Max=50).", INTEGER),
    RADIUS_MEAN("radiusMean", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.radiusMean",
            "The average island radius. Half of the islands will be smaller than radiusMean, half of the islands will be larger than radiusMean. (Min=radiusMin, Max=radiusMax).", INTEGER),
    RADIUS_MIN("radiusMin", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.radiusMin",
            "The minimum radius of each island.  I recommend keeping this at 7 or more (Min=5, Max=radiusMean).", INTEGER),
    RADIUS_NORM("radiusNorm", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.radiusNorm",
            "How strongly island radius should tend to lie around radiusMean. (Min=1, Max=10).", INTEGER),
    DEPTH_MAX("depthMax", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.depthMax",
            "The maximum depth/thickness of islands (Min=depthMean, Max=50).", INTEGER),
    DEPTH_MEAN("depthMean", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.depthMean",
            "The average island depth. Half of the islands will be shallower than depthMean, half of the islands will be deeper than depthMean. (Min=depthMin, Max=depthMax).", INTEGER),
    DEPTH_MIN("depthMin", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.depthMin",
            "The minimum depth/thickness of islands (Min=2, Max=depthMean).", INTEGER),
    DEPTH_NORM("depthNorm", Reference.CTGY_ISLAND_SIZE, "bspkrs.fr.configgui.depthNorm",
            "How strongly island depth should tend to lie around depthMean. (Min=1, Max=10).", INTEGER),
    SHAPE_SPHEROID_WEIGHT("shapeSpheroidWeight", Reference.CTGY_ISLANDS, "bspkrs.fr.configgui.shapeSpheroidWeight",
            "The relative chance of an island having the spheroid shape.", INTEGER),
    SHAPE_CONE_WEIGHT("shapeConeWeight", Reference.CTGY_ISLANDS, "bspkrs.fr.configgui.shapeConeWeight",
            "The relative chance of an island having the cone shape.", INTEGER),
    SHAPE_JETSONS_WEIGHT("shapeJetsonsWeight", Reference.CTGY_ISLANDS, "bspkrs.fr.configgui.shapeJetsonsWeight",
            "The relative chance of an island having the jetsons shape.", INTEGER),
    //    SHAPE_STALACTITE_WEIGHT("shapeStalactiteWeight", "bspkrs.fr.configgui.shapeStalactiteWeight",
    //            "UNIMPLEMENTED and IGNORED: The relative chance of an island having the stalactite shape.", INTEGER),
    NUMBER_OF_ITEMS("numberOfItems", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.numberOfItems",
            "The number of items in a ruin's chest.", INTEGER),
    BLOCK_ID_BLACKLIST("blockIDBlacklist", Reference.CTGY_GEN, "bspkrs.fr.configgui.blockIDBlacklist",
            "Add block IDs to this list if you don't want them to be moved when a floating island is generated.  Format used: \",\" separates between id and metadata and \";\" separates between each block.", STRING),
    DIMENSION_ID_BLACKLIST("dimensionIDBlacklist", Reference.CTGY_GEN, "bspkrs.fr.configgui.dimensionIDBlacklist",
            "Add dimension IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each dimension ID.", STRING),
    BIOME_ID_BLACKLIST("biomeIDBlacklist", Reference.CTGY_GEN, "bspkrs.fr.configgui.biomeIDBlacklist",
            "Add biome IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each biome ID.", STRING),
    USE_CUSTOM_ITEM_LIST("useCustomItemList", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.useCustomItemList",
            "Whether or not to use the custom list of items. When true the custom list below will be used, when false items will be chosen from the various lists used to choose random chest items for regular dungeons/desert ruins/etc.", BOOLEAN),
    STRING_OF_IDS("stringOfIds", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.stringOfIds",
            "The ids for items found in chests. Format used: \",\" separates between item id, quantity, and metadata and \";\" separates between each item.", STRING),
    SPAWNER_DEFAULT("spawnerDefault", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerDefault",
            "Mob spawners can be configured using the mobs' names, each separated by a comma. Using \"Default\" will make the specified biome use the same settings as 'spawnerDefault'.", STRING),
    SPAWNER_DESERT("spawnerDesert", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerDesert", "", STRING),
    SPAWNER_FOREST("spawnerForest", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerForest", "", STRING),
    SPAWNER_PLAINS("spawnerPlains", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerPlains", "", STRING),
    SPAWNER_SWAMPLAND("spawnerSwampland", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerSwampland", "", STRING),
    SPAWNER_TAIGA("spawnerTaiga", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerTaiga", "", STRING),
    SPAWNER_HILLS("spawnerHills", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerHills", "", STRING),
    SPAWNER_OCEAN("spawnerOcean", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerOcean", "", STRING),
    SPAWNER_RIVER("spawnerRiver", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerRiver", "", STRING),
    SPAWNER_JUNGLE("spawnerJungle", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerJungle", "", STRING),
    SPAWNER_ICE_BIOMES("spawnerIceBiomes", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerIceBiomes", "", STRING),
    SPAWNER_MUSHROOM("spawnerMushroom", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerMushroom", "", STRING),
    SPAWNER_NEAR_LAVA("spawnerNearLava", Reference.CTGY_DUNGEONS, "bspkrs.fr.configgui.spawnerNearLava",
            "If the dungeon is close enough to lava, the spawner will use these mobs.", STRING);
    
    private String        key;
    private String        ctgy;
    private String        langKey;
    private String        desc;
    private Property.Type propertyType;
    private String[]      validStrings;
    
    private ConfigElement(String key, String ctgy, String langKey, String desc, Property.Type propertyType, String[] validStrings)
    {
        this.key = key;
        this.ctgy = ctgy;
        this.langKey = langKey;
        this.desc = desc;
        this.propertyType = propertyType;
        this.validStrings = validStrings;
    }
    
    private ConfigElement(String key, String ctgy, String langKey, String desc, Property.Type propertyType)
    {
        this(key, ctgy, langKey, desc, propertyType, new String[0]);
    }
    
    public String key()
    {
        return key;
    }
    
    public String ctgy()
    {
        return ctgy;
    }
    
    public String languageKey()
    {
        return langKey;
    }
    
    public String desc()
    {
        return desc;
    }
    
    public Property.Type propertyType()
    {
        return propertyType;
    }
    
    public String[] validStrings()
    {
        return validStrings;
    }
}
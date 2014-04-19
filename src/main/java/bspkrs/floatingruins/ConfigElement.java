package bspkrs.floatingruins;

import static bspkrs.util.config.Property.Type.BIOME_LIST;
import static bspkrs.util.config.Property.Type.BLOCK_LIST;
import static bspkrs.util.config.Property.Type.BOOLEAN;
import static bspkrs.util.config.Property.Type.DIMENSION_LIST;
import static bspkrs.util.config.Property.Type.ENTITY_LIST;
import static bspkrs.util.config.Property.Type.INTEGER;
import static bspkrs.util.config.Property.Type.ITEMSTACK_LIST;
import bspkrs.util.config.Property;

public enum ConfigElement
{
    ENABLED("enabled", "bspkrs.fr.configgui.enabled", "Enables or disables Floating Ruins generation.", BOOLEAN),
    ALLOW_DEBUG_LOGGING("allowDebugLogging", "bspkrs.fr.configgui.allowDebugLogging",
            "Set to true if you want FloatingRuins to log info about what it's doing, false to disable", BOOLEAN),
    ALLOW_IN_SUPERFLAT("allowInSuperFlat", "bspkrs.fr.configgui.allowInSuperFlat",
            "Set to true to allow generation of floating ruins on superflat maps, false to disable.", BOOLEAN),
    ALLOW_MULTI_MOB_SPAWNERS("allowMultiMobSpawners", "bspkrs.fr.configgui.allowMultiMobSpawners",
            "When set to true spawners will be able to spawn any of the mobs for the biome the floating island generated in, set to false to use the old logic of randomly picking just one mob.", BOOLEAN),
    HARDER_DUNGEONS("harderDungeons", "bspkrs.fr.configgui.harderDungeons",
            "Set to true to generate harder dungeons (roof is bedrock, chest is harder to get to, mobs spawn more aggressively), set to false to generate normal dungeons.", BOOLEAN),
    RARITY("rarity", "bspkrs.fr.configgui.rarity",
            "The probability of a floating island generating is 1 in each 'rarity' number of chunks.", INTEGER),
    RARITY_DUNGEON("rarityDungeon", "bspkrs.fr.configgui.rarityDungeon",
            "The probability of a floating island having a dungeon on it is 1 in each 'rarityDungeon' number of islands.", INTEGER),
    HEIGHT_MAX("heightMax", "bspkrs.fr.configgui.heightMax",
            "The maximum world height for floating ruins (Min=heightMean, Max=225).", INTEGER),
    HEIGHT_MEAN("heightMean", "bspkrs.fr.configgui.heightMean",
            "The average island height. Half of the islands will be below heightMean, half of the islands will be above heightMean. (Min=heightMin, Max=heightMax).", INTEGER),
    HEIGHT_MIN("heightMin", "bspkrs.fr.configgui.heightMin",
            "The minimum world height for floating ruins (Min=80, Max=heightMax).", INTEGER),
    HEIGHT_NORM("heightNorm", "bspkrs.fr.configgui.heightNorm",
            "How strongly island height should tend to lie around heightMean. (Min=1, Max=10).", INTEGER),
    RADIUS_MAX("radiusMax", "bspkrs.fr.configgui.radiusMax",
            "The maximum radius of each island.  Making this value too large will result in very large chunks of your world turning into floating islands (Min=radiusMean, Max=50).", INTEGER),
    RADIUS_MEAN("radiusMean", "bspkrs.fr.configgui.radiusMean",
            "The average island radius. Half of the islands will be smaller than radiusMean, half of the islands will be larger than radiusMean. (Min=radiusMin, Max=radiusMax).", INTEGER),
    RADIUS_MIN("radiusMin", "bspkrs.fr.configgui.radiusMin",
            "The minimum radius of each island.  I recommend keeping this at 7 or more (Min=5, Max=radiusMean).", INTEGER),
    RADIUS_NORM("radiusNorm", "bspkrs.fr.configgui.radiusNorm",
            "How strongly island radius should tend to lie around radiusMean. (Min=1, Max=10).", INTEGER),
    DEPTH_MAX("depthMax", "bspkrs.fr.configgui.depthMax",
            "The maximum depth/thickness of islands (Min=depthMean, Max=50).", INTEGER),
    DEPTH_MEAN("depthMean", "bspkrs.fr.configgui.depthMean",
            "The average island depth. Half of the islands will be shallower than depthMean, half of the islands will be deeper than depthMean. (Min=depthMin, Max=depthMax).", INTEGER),
    DEPTH_MIN("depthMin", "bspkrs.fr.configgui.depthMin",
            "The minimum depth/thickness of islands (Min=2, Max=depthMean).", INTEGER),
    DEPTH_NORM("depthNorm", "bspkrs.fr.configgui.depthNorm",
            "How strongly island depth should tend to lie around depthMean. (Min=1, Max=10).", INTEGER),
    SHAPE_SPHEROID_WEIGHT("shapeSpheroidWeight", "bspkrs.fr.configgui.shapeSpheroidWeight",
            "The relative chance of an island having the spheroid shape.", INTEGER),
    SHAPE_CONE_WEIGHT("shapeConeWeight", "bspkrs.fr.configgui.shapeConeWeight",
            "The relative chance of an island having the cone shape.", INTEGER),
    SHAPE_JETSONS_WEIGHT("shapeJetsonsWeight", "bspkrs.fr.configgui.shapeJetsonsWeight",
            "The relative chance of an island having the jetsons shape.", INTEGER),
    //    SHAPE_STALACTITE_WEIGHT("shapeStalactiteWeight", "bspkrs.fr.configgui.shapeStalactiteWeight",
    //            "UNIMPLEMENTED and IGNORED: The relative chance of an island having the stalactite shape.", INTEGER),
    NUMBER_OF_ITEMS("numberOfItems", "bspkrs.fr.configgui.numberOfItems",
            "The number of items in a ruin's chest.", INTEGER),
    BLOCK_ID_BLACKLIST("blockIDBlacklist", "bspkrs.fr.configgui.blockIDBlacklist",
            "Add block IDs to this list if you don't want them to be moved when a floating island is generated.  Format used: \",\" separates between id and metadata and \";\" separates between each block.", BLOCK_LIST),
    DIMENSION_ID_BLACKLIST("dimensionIDBlacklist", "bspkrs.fr.configgui.dimensionIDBlacklist",
            "Add dimension IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each dimension ID.", DIMENSION_LIST),
    BIOME_ID_BLACKLIST("biomeIDBlacklist", "bspkrs.fr.configgui.biomeIDBlacklist",
            "Add biome IDs where you do not want Floating Ruins to generate.  Format used: \";\" separates between each biome ID.", BIOME_LIST),
    STRING_OF_IDS("stringOfIds", "bspkrs.fr.configgui.stringOfIds",
            "The ids for items found in chests. Format used: \",\" separates between item id, quantity, and metadata and \";\" separates between each item.", ITEMSTACK_LIST),
    SPAWNER_DEFAULT("spawnerDefault", "bspkrs.fr.configgui.spawnerDefault",
            "Mob spawners can be configured using the mobs' names, each separated by a comma. Using \"Default\" will make the specified biome use the same settings as 'spawnerDefault'.", ENTITY_LIST),
    SPAWNER_DESERT("spawnerDesert", "bspkrs.fr.configgui.spawnerDesert", "", ENTITY_LIST),
    SPAWNER_FOREST("spawnerForest", "bspkrs.fr.configgui.spawnerForest", "", ENTITY_LIST),
    SPAWNER_PLAINS("spawnerPlains", "bspkrs.fr.configgui.spawnerPlains", "", ENTITY_LIST),
    SPAWNER_SWAMPLAND("spawnerSwampland", "bspkrs.fr.configgui.spawnerSwampland", "", ENTITY_LIST),
    SPAWNER_TAIGA("spawnerTaiga", "bspkrs.fr.configgui.spawnerTaiga", "", ENTITY_LIST),
    SPAWNER_HILLS("spawnerHills", "bspkrs.fr.configgui.spawnerHills", "", ENTITY_LIST),
    SPAWNER_OCEAN("spawnerOcean", "bspkrs.fr.configgui.spawnerOcean", "", ENTITY_LIST),
    SPAWNER_RIVER("spawnerRiver", "bspkrs.fr.configgui.spawnerRiver", "", ENTITY_LIST),
    SPAWNER_JUNGLE("spawnerJungle", "bspkrs.fr.configgui.spawnerJungle", "", ENTITY_LIST),
    SPAWNER_ICE_BIOMES("spawnerIceBiomes", "bspkrs.fr.configgui.spawnerIceBiomes", "", ENTITY_LIST),
    SPAWNER_MUSHROOM("spawnerMushroom", "bspkrs.fr.configgui.spawnerMushroom", "", ENTITY_LIST),
    SPAWNER_NEAR_LAVA("spawnerNearLava", "bspkrs.fr.configgui.spawnerNearLava",
            "If the dungeon is close enough to lava, the spawner will use these mobs.", ENTITY_LIST);
    
    private String        key;
    private String        langKey;
    private String        desc;
    private Property.Type propertyType;
    private String[]      validStrings;
    
    private ConfigElement(String key, String langKey, String desc, Property.Type propertyType, String[] validStrings)
    {
        this.key = key;
        this.langKey = langKey;
        this.desc = desc;
        this.propertyType = propertyType;
        this.validStrings = validStrings;
    }
    
    private ConfigElement(String key, String langKey, String desc, Property.Type propertyType)
    {
        this(key, langKey, desc, propertyType, new String[0]);
    }
    
    public String key()
    {
        return key;
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
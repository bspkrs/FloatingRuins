package bspkrs.floatingruins;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import bspkrs.helpers.item.ItemHelper;
import bspkrs.helpers.tileentity.TileEntityHelper;
import bspkrs.helpers.world.WorldHelper;
import bspkrs.util.BlockNotifyType;
import bspkrs.util.CommonUtils;

public class WorldGenFloatingIslandRuin extends WorldGenerator
{
    private final int                numberOfItems;
    private final String             stringOfIds;
    private final String             spawnerDefault;
    private final String             spawnerDesert;
    private final String             spawnerForest;
    private final String             spawnerHills;
    private final String             spawnerPlains;
    private final String             spawnerSwampland;
    private final String             spawnerTaiga;
    private final String             spawnerOcean;
    private final String             spawnerRiver;
    private final String             spawnerJungle;
    private final String             spawnerIceBiomes;
    private final String             spawnerMushroom;
    private final String             spawnerNearLava;
    private boolean                  isLavaNearby;
    
    private static final ItemStack[] helms       = { null,
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.diamond_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.leather_helmet, 1, 0),
                                                 new ItemStack(Items.leather_helmet, 1, 0),
                                                 new ItemStack(Items.leather_helmet, 1, 0),
                                                 new ItemStack(Items.leather_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 new ItemStack(Items.chainmail_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.diamond_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 new ItemStack(Items.golden_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.leather_helmet, 1, 0),
                                                 new ItemStack(Items.leather_helmet, 1, 0),
                                                 new ItemStack(Items.leather_helmet, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0),
                                                 new ItemStack(Items.iron_helmet, 1, 0) };
    
    private static final ItemStack[] plates      = { null,
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.diamond_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.leather_chestplate, 1, 0),
                                                 new ItemStack(Items.leather_chestplate, 1, 0),
                                                 new ItemStack(Items.leather_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 new ItemStack(Items.chainmail_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.diamond_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 new ItemStack(Items.golden_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.leather_chestplate, 1, 0),
                                                 new ItemStack(Items.leather_chestplate, 1, 0),
                                                 new ItemStack(Items.leather_chestplate, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0),
                                                 new ItemStack(Items.iron_chestplate, 1, 0) };
    
    private static final ItemStack[] legs        = { null,
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.diamond_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.leather_leggings, 1, 0),
                                                 new ItemStack(Items.leather_leggings, 1, 0),
                                                 new ItemStack(Items.leather_leggings, 1, 0),
                                                 new ItemStack(Items.leather_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 new ItemStack(Items.chainmail_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.diamond_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 new ItemStack(Items.golden_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.leather_leggings, 1, 0),
                                                 new ItemStack(Items.leather_leggings, 1, 0),
                                                 new ItemStack(Items.leather_leggings, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0),
                                                 new ItemStack(Items.iron_leggings, 1, 0) };
    
    private static final ItemStack[] boots       = { null,
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.diamond_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.leather_boots, 1, 0),
                                                 new ItemStack(Items.leather_boots, 1, 0),
                                                 new ItemStack(Items.leather_boots, 1, 0),
                                                 new ItemStack(Items.leather_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 new ItemStack(Items.chainmail_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.diamond_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 new ItemStack(Items.golden_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.leather_boots, 1, 0),
                                                 new ItemStack(Items.leather_boots, 1, 0),
                                                 new ItemStack(Items.leather_boots, 1, 0),
                                                 new ItemStack(Items.leather_boots, 1, 0),
                                                 null,
                                                 null,
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0),
                                                 new ItemStack(Items.iron_boots, 1, 0) };
    
    private static final ItemStack[] skel_weap   = { new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.diamond_sword, 1, 0),
                                                 new ItemStack(Items.diamond_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.bow, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0) };
    
    private static final ItemStack[] zombie_weap = { new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.diamond_sword, 1, 0),
                                                 new ItemStack(Items.diamond_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.wooden_sword, 1, 0),
                                                 new ItemStack(Items.wooden_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.stone_sword, 1, 0),
                                                 new ItemStack(Items.diamond_sword, 1, 0),
                                                 new ItemStack(Items.diamond_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.golden_sword, 1, 0),
                                                 new ItemStack(Items.wooden_sword, 1, 0),
                                                 new ItemStack(Items.wooden_sword, 1, 0),
                                                 new ItemStack(Items.wooden_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0),
                                                 new ItemStack(Items.iron_sword, 1, 0) };
    
    public WorldGenFloatingIslandRuin(boolean isLavaNearby)
    {
        numberOfItems = FloatingRuins.numberOfItems;
        stringOfIds = FloatingRuins.stringOfIds;
        spawnerDefault = FloatingRuins.spawnerDefault;
        spawnerDesert = FloatingRuins.spawnerDesert;
        spawnerForest = FloatingRuins.spawnerForest;
        spawnerHills = FloatingRuins.spawnerHills;
        spawnerPlains = FloatingRuins.spawnerPlains;
        spawnerSwampland = FloatingRuins.spawnerSwampland;
        spawnerTaiga = FloatingRuins.spawnerTaiga;
        spawnerOcean = FloatingRuins.spawnerOcean;
        spawnerRiver = FloatingRuins.spawnerOcean;
        spawnerJungle = FloatingRuins.spawnerJungle;
        spawnerIceBiomes = FloatingRuins.spawnerIceBiomes;
        spawnerMushroom = FloatingRuins.spawnerMushroom;
        spawnerNearLava = FloatingRuins.spawnerNearLava;
        
        this.isLavaNearby = isLavaNearby;
    }
    
    @Override
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        setDungeon(world, random, x, y, z);
        return true;
    }
    
    private void setDungeon(World world, Random random, int x, int y, int z)
    {
        BiomeGenBase biomegenbase = world.getWorldChunkManager().getBiomeGenAt(x, z);
        Block dungeonBlock = getDungeonBlock(biomegenbase);
        if (dungeonBlock.equals(Blocks.snow))
            setIgloo(world, x, y, z, 5, dungeonBlock);
        else if (dungeonBlock.equals(Blocks.sandstone))
            setPyramid(world, x, y, z, 6, dungeonBlock);
        else if (dungeonBlock.equals(Blocks.red_mushroom_block))
            CommonUtils.setHugeMushroom(world, random, x, y, z, dungeonBlock);
        else
            setBox(world, random, x, y, z, 4, 4, getDungeonBlock(biomegenbase));
        
        setChest(world, random, x, y, z);
        setSpawner(world, biomegenbase, x, y + 2, z);
    }
    
    private void setChest(World world, Random random, int x, int y, int z)
    {
        WorldHelper.setBlock(world, x, y, z, Blocks.chest, 0, BlockNotifyType.ALL);
        TileEntityChest tileentitychest = (TileEntityChest) WorldHelper.getBlockTileEntity(world, x, y, z);
        addItems(tileentitychest, random);
        
        Block blockingBlock = (FloatingRuins.harderDungeons ? Blocks.bedrock : Blocks.obsidian);
        
        WorldHelper.setBlock(world, x + 1, y, z, blockingBlock, 0, BlockNotifyType.ALL);
        WorldHelper.setBlock(world, x - 1, y, z, blockingBlock, 0, BlockNotifyType.ALL);
        WorldHelper.setBlock(world, x, y, z + 1, blockingBlock, 0, BlockNotifyType.ALL);
        WorldHelper.setBlock(world, x, y, z - 1, blockingBlock, 0, BlockNotifyType.ALL);
        WorldHelper.setBlock(world, x, y - 1, z, blockingBlock, 0, BlockNotifyType.ALL);
        
        if (FloatingRuins.harderDungeons)
            WorldHelper.setBlock(world, x, y + 1, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
        else
            WorldHelper.setBlock(world, x, y + 1, z, Blocks.cobblestone, 0, BlockNotifyType.ALL);
    }
    
    private void setSpawner(World world, BiomeGenBase biomegenbase, int x, int y, int z)
    {
        WorldHelper.setBlock(world, x, y, z, Blocks.mob_spawner, 0, BlockNotifyType.ALL);
        
        WorldHelper.setBlock(world, x + 1, y, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
        WorldHelper.setBlock(world, x - 1, y, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
        WorldHelper.setBlock(world, x, y, z + 1, Blocks.obsidian, 0, BlockNotifyType.ALL);
        WorldHelper.setBlock(world, x, y, z - 1, Blocks.obsidian, 0, BlockNotifyType.ALL);
        if (FloatingRuins.harderDungeons)
        {
            WorldHelper.setBlock(world, x, y - 1, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
            WorldHelper.setBlock(world, x, y + 1, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
        }
        
        TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner) WorldHelper.getBlockTileEntity(world, x, y, z);
        if (tileEntityMobSpawner != null)
        {
            String[] mobIDList;
            if (FloatingRuins.allowMultiMobSpawners)
                mobIDList = getSpawnerMobList(world, biomegenbase);
            else
            {
                mobIDList = new String[1];
                mobIDList[0] = getSpawnerType(world, biomegenbase, x, z);
            }
            
            if (mobIDList.length == 1 && mobIDList[0].trim().equalsIgnoreCase("Default"))
                mobIDList = FloatingRuins.spawnerDefault.split(",");
            
            // get rid of extra whitespace from list to avoid NPEs
            for (int i = 0; i < mobIDList.length; i++)
                mobIDList[i] = mobIDList[i].trim();
            
            NBTTagCompound spawnerNBT = new NBTTagCompound();
            TileEntityHelper.writeToNBT(tileEntityMobSpawner, spawnerNBT);
            
            NBTTagCompound properties;
            NBTTagList spawnPotentials = new NBTTagList();
            
            for (int i = 0; i < mobIDList.length; i++)
            {
                properties = new NBTTagCompound();
                
                NBTTagCompound potentialSpawn = new NBTTagCompound();
                potentialSpawn.setInteger("Weight", world.rand.nextInt(4) + 1);
                String debug = "  +" + mobIDList[i] + " wt(" + potentialSpawn.getInteger("Weight") + ") ";
                
                if (mobIDList[i].equals("WitherSkeleton"))
                {
                    NBTTagList equipment = new NBTTagList();
                    debug += "+E:" + this.applyEquipment(equipment, zombie_weap, world.rand, true, world.rand.nextBoolean());
                    properties.setTag("Equipment", equipment);
                    
                    properties.setByte("SkeletonType", (byte) 1);
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", "Skeleton");
                    spawnerNBT.setString("EntityId", "Skeleton");
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("Wolf"))
                {
                    properties.setByte("Angry", (byte) 1);
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("ChargedCreeper"))
                {
                    properties.setByte("powered", (byte) 1);
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", "Creeper");
                    spawnerNBT.setString("EntityId", "Creeper");
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("PigZombie"))
                {
                    properties.setShort("Anger", (short) (400 + world.rand.nextInt(400)));
                    
                    NBTTagList equipment = new NBTTagList();
                    debug += "+E:" + applyEquipment(equipment, zombie_weap, world.rand, true, false);
                    properties.setTag("Equipment", equipment);
                    
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("Zombie"))
                {
                    boolean flag = world.rand.nextBoolean();
                    properties.setByte("isVillager", (byte) (flag ? 1 : 0));
                    if (flag)
                    {
                        properties.setInteger("ConversionTime", -1);
                        debug += "+V ";
                    }
                    
                    flag = world.rand.nextBoolean();
                    if (flag)
                    {
                        NBTTagList equipment = new NBTTagList();
                        debug += "+E:" + applyEquipment(equipment, zombie_weap, world.rand, flag, world.rand.nextBoolean());
                        properties.setTag("Equipment", equipment);
                    }
                    
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("Skeleton"))
                {
                    NBTTagList equipment = new NBTTagList();
                    debug += "+E:" + applyEquipment(equipment, skel_weap, world.rand, true, world.rand.nextBoolean());
                    properties.setTag("Equipment", equipment);
                    
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else
                {
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                
                spawnPotentials.appendTag(potentialSpawn);
                FloatingRuins.debug(debug);
            }
            
            spawnerNBT.setTag("SpawnPotentials", spawnPotentials);
            
            if (FloatingRuins.harderDungeons)
            {
                spawnerNBT.setShort("MinSpawnDelay", (short) 80);
                spawnerNBT.setShort("MaxSpawnDelay", (short) 200);
                spawnerNBT.setShort("SpawnCount", (short) 6);
                spawnerNBT.setShort("MaxNearbyEntities", (short) 16);
                spawnerNBT.setShort("SpawnRange", (short) 7);
            }
            
            TileEntityHelper.readFromNBT(tileEntityMobSpawner, spawnerNBT);
        }
    }
    
    private String applyEquipment(NBTTagList equipment, ItemStack[] weaponList, Random random, boolean giveWeapon, boolean giveArmor)
    {
        String debug = "";
        NBTTagCompound Items = new NBTTagCompound();
        
        ItemStack equip = weaponList[random.nextInt(weaponList.length)];
        if (giveWeapon)
        {
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
        }
        
        equipment.appendTag(Items);
        
        if (giveArmor)
        {
            Items = new NBTTagCompound();
            equip = boots[random.nextInt(boots.length)];
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
            equipment.appendTag(Items);
            
            Items = new NBTTagCompound();
            equip = legs[random.nextInt(legs.length)];
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
            equipment.appendTag(Items);
            
            Items = new NBTTagCompound();
            equip = plates[random.nextInt(plates.length)];
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
            equipment.appendTag(Items);
            
            Items = new NBTTagCompound();
            equip = helms[random.nextInt(helms.length)];
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
            equipment.appendTag(Items);
        }
        else
        {
            for (int j = 0; j < 4; j++)
            {
                Items = new NBTTagCompound();
                equipment.appendTag(Items);
            }
        }
        
        return debug;
    }
    
    private Block getDungeonBlock(BiomeGenBase biomegenbase)
    {
        if (biomegenbase.equals(BiomeGenBase.desert) || biomegenbase.equals(BiomeGenBase.desertHills))
            return Blocks.sandstone;
        
        if (biomegenbase.equals(BiomeGenBase.taiga) || biomegenbase.equals(BiomeGenBase.taigaHills) || isIceBiome(biomegenbase))
            return Blocks.snow;
        
        if (biomegenbase.equals(BiomeGenBase.forest) || biomegenbase.equals(BiomeGenBase.forestHills))
            return Blocks.cobblestone;
        
        if (biomegenbase.equals(BiomeGenBase.extremeHills) || biomegenbase.equals(BiomeGenBase.extremeHillsEdge))
            return Blocks.stone;
        
        if (biomegenbase.equals(BiomeGenBase.plains))
            return Blocks.planks;
        
        if (biomegenbase.equals(BiomeGenBase.swampland))
            return Blocks.mossy_cobblestone;
        
        if (biomegenbase.equals(BiomeGenBase.ocean))
            return Blocks.stonebrick;
        
        if (biomegenbase.equals(BiomeGenBase.river))
            return Blocks.planks;
        
        if (biomegenbase.equals(BiomeGenBase.jungle) || biomegenbase.equals(BiomeGenBase.jungleHills))
            return Blocks.mossy_cobblestone;
        
        if (isMushroomBiome(biomegenbase))
            return Blocks.red_mushroom_block;
        else
            return Blocks.brick_block;
    }
    
    private boolean isIceBiome(BiomeGenBase biomegenbase)
    {
        return biomegenbase.equals(BiomeGenBase.frozenOcean) || biomegenbase.equals(BiomeGenBase.frozenRiver) || biomegenbase.equals(BiomeGenBase.icePlains) || biomegenbase.equals(BiomeGenBase.iceMountains);
    }
    
    private boolean isMushroomBiome(BiomeGenBase biomegenbase)
    {
        return biomegenbase.equals(BiomeGenBase.mushroomIsland) || biomegenbase.equals(BiomeGenBase.mushroomIslandShore);
    }
    
    private void addItems(TileEntityChest tileentitychest, Random random)
    {
        int limit = random.nextInt(4) + numberOfItems;
        int i = 0;
        do
        {
            tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), getItems(random));
        }
        while (++i <= limit);
    }
    
    private ItemStack getItems(Random random)
    {
        String itemStack[] = stringOfIds.split(";")[random.nextInt(stringOfIds.split(";").length)].split(",");
        String id = ItemHelper.getUniqueID(Items.egg);
        int size = 1;
        int meta = 0;
        if (itemStack.length > 0)
            id = itemStack[0].trim();
        
        if (itemStack.length > 1)
            size = CommonUtils.parseInt(itemStack[1].trim());
        
        if (itemStack.length > 2)
            meta = CommonUtils.parseInt(itemStack[2].trim());
        
        Item item = ItemHelper.getItem(id);
        
        if (item == null)
            item = ItemHelper.getItem(ItemHelper.getUniqueID(Items.egg));
        
        if (!item.getHasSubtypes() && meta != 0)
            meta = 0;
        
        return new ItemStack(item, size, meta);
    }
    
    public String getSpawnerType(World world, BiomeGenBase biomegenbase, int x, int z)
    {
        if (isLavaNearby)
            return getMobString(spawnerNearLava, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.desert) || biomegenbase.equals(BiomeGenBase.desertHills))
            return getMobString(spawnerDesert, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.forest) || biomegenbase.equals(BiomeGenBase.forestHills))
            return getMobString(spawnerForest, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.plains))
            return getMobString(spawnerPlains, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.swampland))
            return getMobString(spawnerSwampland, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.taiga) || biomegenbase.equals(BiomeGenBase.taigaHills))
            return getMobString(spawnerTaiga, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.extremeHills) || biomegenbase.equals(BiomeGenBase.extremeHillsEdge))
            return getMobString(spawnerHills, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.ocean))
            return getMobString(spawnerOcean, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.river))
            return getMobString(spawnerRiver, world, x, z);
        
        if (biomegenbase.equals(BiomeGenBase.jungle) || biomegenbase.equals(BiomeGenBase.jungleHills))
            return getMobString(spawnerJungle, world, x, z);
        
        if (isIceBiome(biomegenbase))
            return getMobString(spawnerIceBiomes, world, x, z);
        
        if (isMushroomBiome(biomegenbase))
            return getMobString(spawnerMushroom, world, x, z);
        else
            return getMobString(spawnerDefault, world, x, z);
    }
    
    public String[] getSpawnerMobList(World world, BiomeGenBase biomegenbase)
    {
        if (isLavaNearby)
            return spawnerNearLava.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.desert) || biomegenbase.equals(BiomeGenBase.desertHills))
            return spawnerDesert.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.forest) || biomegenbase.equals(BiomeGenBase.forestHills))
            return spawnerForest.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.plains))
            return spawnerPlains.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.swampland))
            return spawnerSwampland.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.taiga) || biomegenbase.equals(BiomeGenBase.taigaHills))
            return spawnerTaiga.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.extremeHills) || biomegenbase.equals(BiomeGenBase.extremeHillsEdge))
            return spawnerHills.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.ocean))
            return spawnerOcean.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.river))
            return spawnerRiver.split(",");
        
        if (biomegenbase.equals(BiomeGenBase.jungle) || biomegenbase.equals(BiomeGenBase.jungleHills))
            return spawnerJungle.split(",");
        
        if (isIceBiome(biomegenbase))
            return spawnerIceBiomes.split(",");
        
        if (isMushroomBiome(biomegenbase))
            return spawnerMushroom.split(",");
        else
            return spawnerDefault.split(",");
    }
    
    private String getMobString(String s, World world, int x, int z)
    {
        Random random = new Random();
        if (s.equalsIgnoreCase("default"))
            return getMobString(spawnerDefault, world, x, z);
        else
        {
            String mob = s.split(",")[random.nextInt(s.split(",").length)].trim();
            if (mob.equalsIgnoreCase("slime") && !(new EntitySlime(world)).getCanSpawnHere())
                return getMobString(spawnerDefault, world, x, z);
            
            return mob;
        }
    }
    
    public void setBox(World world, Random random, int xIn, int yIn, int zIn, int width, int height, Block block)
    {
        for (int x = -width; x <= width; x++)
            for (int y = height; y >= -height; y--)
                for (int z = -width; z <= width; z++)
                {
                    if (y == height || (Math.abs(x) == width && Math.abs(z) == width && y >= 0))
                    {
                        WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, Blocks.stonebrick, 0, BlockNotifyType.ALL);
                        WorldHelper.setBlock(world, x + xIn, y + yIn + 1, z + zIn, (FloatingRuins.harderDungeons ? Blocks.bedrock : Blocks.stonebrick), 0, BlockNotifyType.ALL);
                    }
                    
                    if (y >= 1 && ((Math.abs(x) == width) ^ (Math.abs(z) == width)))
                        WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, block, 0, BlockNotifyType.ALL);
                    
                    if (y > 0 && y < height && Math.abs(z) < width && Math.abs(x) < width)
                        WorldHelper.setBlockToAir(world, x + xIn, y + yIn, z + zIn);
                    
                    if (y == -1 || y == 0)
                        WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, Blocks.stonebrick, 0, BlockNotifyType.ALL);
                    
                    if (y < -1)
                    {
                        int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                        if ((Math.abs(x) == width || Math.abs(z) == width) && !WorldHelper.isBlockNormalCube(world, x + xIn, y + yIn, z + zIn, false) && yg < y + yIn && yg >= yIn - height)
                            WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, Blocks.stonebrick, 0, BlockNotifyType.ALL);
                    }
                }
    }
    
    private void setIgloo(World world, int xIn, int yIn, int zIn, int range, Block blockID)
    {
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    int dist = (int) Math.round(Math.sqrt(CommonUtils.sqr(x) + CommonUtils.sqr(y) + CommonUtils.sqr(z)));
                    if (dist <= range)
                    {
                        if (y >= 0)
                        {
                            if (dist == range)
                                WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, (FloatingRuins.harderDungeons && y > 2 ? Blocks.bedrock : blockID), 0, BlockNotifyType.ALL);
                            
                            // For wolves
                            if (y == 0 && dist < range)
                                WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, Blocks.grass, 0, BlockNotifyType.ALL);
                            
                            if (y > 0 && dist < range)
                            {
                                WorldHelper.setBlockToAir(world, x + xIn, y + yIn, z + zIn);
                                if (y == 1)
                                    WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, Blocks.snow, 0, BlockNotifyType.ALL);
                            }
                        }
                        else
                        {
                            if (y == -1)
                                WorldHelper.setBlock(world, x + xIn, yIn - 1, z + zIn, blockID, 0, BlockNotifyType.ALL);
                            
                            int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                            if (dist == range && !WorldHelper.isBlockNormalCube(world, x + xIn, y + yIn, z + zIn, false) && yg < y + yIn && yg >= yIn - range)
                                WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, blockID, 0, BlockNotifyType.ALL);
                        }
                    }
                }
    }
    
    private void setPyramid(World world, int xIn, int yIn, int zIn, int range, Block block)
    {
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    if (y >= 0)
                    {
                        if ((Math.abs(x) == range - y && Math.abs(x) >= Math.abs(z)) || (Math.abs(z) == range - y && Math.abs(z) >= Math.abs(x)) || y == 0)
                            WorldHelper.setBlock(world, xIn + x, y + yIn, zIn + z, (FloatingRuins.harderDungeons && y > 2 ? Blocks.bedrock : block), 0, BlockNotifyType.ALL);
                        else if ((Math.abs(x) < range - y && Math.abs(x) >= Math.abs(z)) || (Math.abs(z) < range - y && Math.abs(z) >= Math.abs(x)))
                            WorldHelper.setBlockToAir(world, xIn + x, y + yIn, zIn + z);
                    }
                    else
                    {
                        if (y == -1)
                            WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, block, 0, BlockNotifyType.ALL);
                        
                        int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                        if ((Math.abs(x) == range || Math.abs(z) == range) && !WorldHelper.isBlockNormalCube(world, x + xIn, y + yIn, z + zIn, false) && yg < y + yIn && yg >= yIn - range)
                            WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, block, 0, BlockNotifyType.ALL);
                    }
                }
    }
}

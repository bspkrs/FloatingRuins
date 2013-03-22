package bspkrs.floatingruins;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import bspkrs.util.CommonUtils;

public class WorldGenFloatingIslandRuin extends WorldGenerator
{
    private final int          numberOfItems;
    private final String       stringOfIds;
    private final String       spawnerDefault;
    private final String       spawnerDesert;
    private final String       spawnerForest;
    private final String       spawnerHills;
    private final String       spawnerPlains;
    private final String       spawnerSwampland;
    private final String       spawnerTaiga;
    private final String       spawnerOcean;
    private final String       spawnerRiver;
    private final String       spawnerJungle;
    private final String       spawnerIceBiomes;
    private final String       spawnerMushroom;
    private final String       spawnerNearLava;
    private boolean            isLavaNearby;
    
    private static ItemStack[] helms       = { null,
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetDiamond.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetLeather.itemID, 1, 0),
                                           new ItemStack(Item.helmetLeather.itemID, 1, 0),
                                           new ItemStack(Item.helmetLeather.itemID, 1, 0),
                                           new ItemStack(Item.helmetLeather.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           new ItemStack(Item.helmetChain.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetDiamond.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           new ItemStack(Item.helmetGold.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetLeather.itemID, 1, 0),
                                           new ItemStack(Item.helmetLeather.itemID, 1, 0),
                                           new ItemStack(Item.helmetLeather.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0),
                                           new ItemStack(Item.helmetSteel.itemID, 1, 0) };
    
    private static ItemStack[] plates      = { null,
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateDiamond.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateLeather.itemID, 1, 0),
                                           new ItemStack(Item.plateLeather.itemID, 1, 0),
                                           new ItemStack(Item.plateLeather.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           new ItemStack(Item.plateChain.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateDiamond.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           new ItemStack(Item.plateGold.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateLeather.itemID, 1, 0),
                                           new ItemStack(Item.plateLeather.itemID, 1, 0),
                                           new ItemStack(Item.plateLeather.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0),
                                           new ItemStack(Item.plateSteel.itemID, 1, 0) };
    
    private static ItemStack[] legs        = { null,
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsDiamond.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsLeather.itemID, 1, 0),
                                           new ItemStack(Item.legsLeather.itemID, 1, 0),
                                           new ItemStack(Item.legsLeather.itemID, 1, 0),
                                           new ItemStack(Item.legsLeather.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           new ItemStack(Item.legsChain.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsDiamond.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           new ItemStack(Item.legsGold.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsLeather.itemID, 1, 0),
                                           new ItemStack(Item.legsLeather.itemID, 1, 0),
                                           new ItemStack(Item.legsLeather.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0),
                                           new ItemStack(Item.legsSteel.itemID, 1, 0) };
    
    private static ItemStack[] boots       = { null,
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsDiamond.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsLeather.itemID, 1, 0),
                                           new ItemStack(Item.bootsLeather.itemID, 1, 0),
                                           new ItemStack(Item.bootsLeather.itemID, 1, 0),
                                           new ItemStack(Item.bootsLeather.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           new ItemStack(Item.bootsChain.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsDiamond.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           new ItemStack(Item.bootsGold.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsLeather.itemID, 1, 0),
                                           new ItemStack(Item.bootsLeather.itemID, 1, 0),
                                           new ItemStack(Item.bootsLeather.itemID, 1, 0),
                                           new ItemStack(Item.bootsLeather.itemID, 1, 0),
                                           null,
                                           null,
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0),
                                           new ItemStack(Item.bootsSteel.itemID, 1, 0) };
    
    private static ItemStack[] skel_weap   = { new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.swordDiamond.itemID, 1, 0),
                                           new ItemStack(Item.swordDiamond.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.bow.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0) };
    
    private static ItemStack[] zombie_weap = { new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordDiamond.itemID, 1, 0),
                                           new ItemStack(Item.swordDiamond.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordWood.itemID, 1, 0),
                                           new ItemStack(Item.swordWood.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordStone.itemID, 1, 0),
                                           new ItemStack(Item.swordDiamond.itemID, 1, 0),
                                           new ItemStack(Item.swordDiamond.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordGold.itemID, 1, 0),
                                           new ItemStack(Item.swordWood.itemID, 1, 0),
                                           new ItemStack(Item.swordWood.itemID, 1, 0),
                                           new ItemStack(Item.swordWood.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0),
                                           new ItemStack(Item.swordSteel.itemID, 1, 0) };
    
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
        int dungeonBlock = getDungeonBlock(biomegenbase);
        if (dungeonBlock == Block.blockSnow.blockID)
            setIgloo(world, x, y, z, 5, dungeonBlock);
        else if (dungeonBlock == Block.sandStone.blockID)
            setPyramid(world, x, y, z, 6, dungeonBlock);
        else if (dungeonBlock == Block.mushroomCapRed.blockID)
            CommonUtils.setHugeMushroom(world, random, x, y, z, dungeonBlock);
        else
            setBox(world, random, x, y, z, 4, 4, getDungeonBlock(biomegenbase));
        
        setChest(world, random, x, y, z);
        setSpawner(world, biomegenbase, x, y + 2, z);
    }
    
    private void setChest(World world, Random random, int x, int y, int z)
    {
        world.setBlock(x, y, z, Block.chest.blockID, 0, 3);
        TileEntityChest tileentitychest = (TileEntityChest) world.getBlockTileEntity(x, y, z);
        addItems(tileentitychest, random);
        
        int blockingBlockID = (FloatingRuins.harderDungeons ? Block.bedrock.blockID : Block.obsidian.blockID);
        
        world.setBlock(x + 1, y, z, blockingBlockID, 0, 3);
        world.setBlock(x - 1, y, z, blockingBlockID, 0, 3);
        world.setBlock(x, y, z + 1, blockingBlockID, 0, 3);
        world.setBlock(x, y, z - 1, blockingBlockID, 0, 3);
        world.setBlock(x, y - 1, z, blockingBlockID, 0, 3);
        
        if (FloatingRuins.harderDungeons)
            world.setBlock(x, y + 1, z, Block.obsidian.blockID, 0, 3);
        else
            world.setBlock(x, y + 1, z, Block.cobblestone.blockID, 0, 3);
    }
    
    private void setSpawner(World world, BiomeGenBase biomegenbase, int x, int y, int z)
    {
        world.setBlock(x, y, z, Block.mobSpawner.blockID, 0, 3);
        
        world.setBlock(x + 1, y, z, Block.obsidian.blockID, 0, 3);
        world.setBlock(x - 1, y, z, Block.obsidian.blockID, 0, 3);
        world.setBlock(x, y, z + 1, Block.obsidian.blockID, 0, 3);
        world.setBlock(x, y, z - 1, Block.obsidian.blockID, 0, 3);
        if (FloatingRuins.harderDungeons)
            world.setBlock(x, y - 1, z, Block.obsidian.blockID, 0, 3);
        
        TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);
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
            tileEntityMobSpawner.writeToNBT(spawnerNBT);
            
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
                    properties.setByte("SkeletonType", (byte) 1);
                    potentialSpawn.setCompoundTag("Properties", properties);
                    potentialSpawn.setString("Type", "Skeleton");
                    spawnerNBT.setString("EntityId", "Skeleton");
                    spawnerNBT.setCompoundTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("Wolf"))
                {
                    properties.setByte("Angry", (byte) 1);
                    potentialSpawn.setCompoundTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setCompoundTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("ChargedCreeper") || (mobIDList[i].equals("Creeper") && biomegenbase.canSpawnLightningBolt()))
                {
                    properties.setByte("powered", (byte) 1);
                    potentialSpawn.setCompoundTag("Properties", properties);
                    potentialSpawn.setString("Type", "Creeper");
                    spawnerNBT.setString("EntityId", "Creeper");
                    spawnerNBT.setCompoundTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("PigZombie"))
                {
                    properties.setShort("Anger", (short) (400 + world.rand.nextInt(400)));
                    potentialSpawn.setCompoundTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setCompoundTag("SpawnData", properties);
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
                        NBTTagCompound item = new NBTTagCompound();
                        debug += "+E:";
                        
                        ItemStack equip = zombie_weap[world.rand.nextInt(zombie_weap.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        item = new NBTTagCompound();
                        equip = boots[world.rand.nextInt(boots.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        item = new NBTTagCompound();
                        equip = legs[world.rand.nextInt(legs.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        item = new NBTTagCompound();
                        equip = plates[world.rand.nextInt(plates.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        item = new NBTTagCompound();
                        equip = helms[world.rand.nextInt(helms.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        properties.setTag("Equipment", equipment);
                    }
                    
                    potentialSpawn.setCompoundTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setCompoundTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("Skeleton"))
                {
                    boolean flag = world.rand.nextBoolean();
                    if (flag)
                    {
                        NBTTagList equipment = new NBTTagList();
                        NBTTagCompound item = new NBTTagCompound();
                        debug += "+E:";
                        
                        ItemStack equip = skel_weap[world.rand.nextInt(skel_weap.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        item = new NBTTagCompound();
                        equip = boots[world.rand.nextInt(boots.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        item = new NBTTagCompound();
                        equip = legs[world.rand.nextInt(legs.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        item = new NBTTagCompound();
                        equip = plates[world.rand.nextInt(plates.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        item = new NBTTagCompound();
                        equip = helms[world.rand.nextInt(helms.length)];
                        if (equip != null)
                        {
                            equip.writeToNBT(item);
                            debug += equip.getItemName() + ";";
                        }
                        equipment.appendTag(item);
                        
                        properties.setTag("Equipment", equipment);
                    }
                    
                    potentialSpawn.setCompoundTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setCompoundTag("SpawnData", properties);
                }
                else
                {
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setCompoundTag("SpawnData", properties);
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
            
            tileEntityMobSpawner.readFromNBT(spawnerNBT);
        }
    }
    
    private int getDungeonBlock(BiomeGenBase biomegenbase)
    {
        if (biomegenbase.equals(BiomeGenBase.desert) || biomegenbase.equals(BiomeGenBase.desertHills))
            return Block.sandStone.blockID;
        
        if (biomegenbase.equals(BiomeGenBase.taiga) || biomegenbase.equals(BiomeGenBase.taigaHills) || isIceBiome(biomegenbase))
            return Block.blockSnow.blockID;
        
        if (biomegenbase.equals(BiomeGenBase.forest) || biomegenbase.equals(BiomeGenBase.forestHills))
            return Block.cobblestone.blockID;
        
        if (biomegenbase.equals(BiomeGenBase.extremeHills) || biomegenbase.equals(BiomeGenBase.extremeHillsEdge))
            return Block.stone.blockID;
        
        if (biomegenbase.equals(BiomeGenBase.plains))
            return Block.planks.blockID;
        
        if (biomegenbase.equals(BiomeGenBase.swampland))
            return Block.cobblestoneMossy.blockID;
        
        if (biomegenbase.equals(BiomeGenBase.ocean))
            return Block.stoneBrick.blockID;
        
        if (biomegenbase.equals(BiomeGenBase.river))
            return Block.planks.blockID;
        
        if (biomegenbase.equals(BiomeGenBase.jungle) || biomegenbase.equals(BiomeGenBase.jungleHills))
            return Block.cobblestoneMossy.blockID;
        
        if (isMushroomBiome(biomegenbase))
            return Block.mushroomCapRed.blockID;
        else
            return Block.brick.blockID;
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
        int i = 0;
        do
        {
            tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), getItems(random));
        }
        while (++i <= numberOfItems);
    }
    
    private ItemStack getItems(Random random)
    {
        String itemStack[] = stringOfIds.split(";")[random.nextInt(stringOfIds.split(";").length)].split(",");
        int id = 344;
        int size = 1;
        int meta = 0;
        if (itemStack.length > 0)
            id = CommonUtils.parseInt(itemStack[0].trim());
        
        if (itemStack.length > 1)
            size = CommonUtils.parseInt(itemStack[1].trim());
        
        if (itemStack.length > 2)
            meta = CommonUtils.parseInt(itemStack[2].trim());
        
        if (Item.itemsList[id] == null)
            id = 344;
        
        if (!Item.itemsList[id].getHasSubtypes() && meta != 0)
            meta = 0;
        
        return new ItemStack(id, size, meta);
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
    
    public void setBox(World world, Random random, int xIn, int yIn, int zIn, int width, int height, int blockID)
    {
        for (int x = -width; x <= width; x++)
            for (int y = height; y >= -height; y--)
                for (int z = -width; z <= width; z++)
                {
                    if (y == height || (Math.abs(x) == width && Math.abs(z) == width && y >= 0))
                    {
                        world.setBlock(x + xIn, y + yIn, z + zIn, Block.stoneBrick.blockID, 0, 3);
                        world.setBlock(x + xIn, y + yIn + 1, z + zIn, (FloatingRuins.harderDungeons ? Block.bedrock.blockID : Block.stoneBrick.blockID), 0, 3);
                    }
                    
                    if (y >= 1 && ((Math.abs(x) == width) ^ (Math.abs(z) == width)))
                        world.setBlock(x + xIn, y + yIn, z + zIn, blockID, 0, 3);
                    
                    if (y > 0 && y < height && Math.abs(z) < width && Math.abs(x) < width)
                        world.setBlock(x + xIn, y + yIn, z + zIn, 0, 0, 3);
                    
                    if (y == -1 || y == 0)
                        world.setBlock(x + xIn, y + yIn, z + zIn, Block.stoneBrick.blockID, 0, 3);
                    
                    if (y < -1)
                    {
                        int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                        if ((Math.abs(x) == width || Math.abs(z) == width) && !world.isBlockNormalCube(x + xIn, y + yIn, z + zIn) && yg < y + yIn && yg >= yIn - height)
                            world.setBlock(x + xIn, y + yIn, z + zIn, Block.stoneBrick.blockID, 0, 3);
                    }
                }
    }
    
    private void setIgloo(World world, int xIn, int yIn, int zIn, int range, int blockID)
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
                                world.setBlock(x + xIn, y + yIn, z + zIn, (FloatingRuins.harderDungeons && y > 2 ? Block.bedrock.blockID : blockID), 0, 3);
                            
                            if (y == 0)
                                world.setBlock(x + xIn, y + yIn, z + zIn, blockID, 0, 3);
                            
                            if (y > 0 && dist < range)
                            {
                                world.setBlock(x + xIn, y + yIn, z + zIn, 0, 0, 3);
                                if (y == 1)
                                    world.setBlock(x + xIn, y + yIn, z + zIn, Block.snow.blockID, 0, 3);
                            }
                        }
                        else
                        {
                            if (y == -1)
                                world.setBlock(x + xIn, yIn - 1, z + zIn, blockID, 0, 3);
                            
                            int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                            if (dist == range && !world.isBlockNormalCube(x + xIn, y + yIn, z + zIn) && yg < y + yIn && yg >= yIn - range)
                                world.setBlock(x + xIn, y + yIn, z + zIn, blockID, 0, 3);
                        }
                    }
                }
    }
    
    private void setPyramid(World world, int xIn, int yIn, int zIn, int range, int blockID)
    {
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    if (y >= 0)
                    {
                        if ((Math.abs(x) == range - y && Math.abs(x) >= Math.abs(z)) || (Math.abs(z) == range - y && Math.abs(z) >= Math.abs(x)) || y == 0)
                            world.setBlock(xIn + x, y + yIn, zIn + z, (FloatingRuins.harderDungeons && y > 2 ? Block.bedrock.blockID : blockID), 0, 3);
                        else if ((Math.abs(x) < range - y && Math.abs(x) >= Math.abs(z)) || (Math.abs(z) < range - y && Math.abs(z) >= Math.abs(x)))
                            world.setBlock(xIn + x, y + yIn, zIn + z, 0, 0, 3);
                    }
                    else
                    {
                        if (y == -1)
                            world.setBlock(x + xIn, y + yIn, z + zIn, blockID, 0, 3);
                        
                        int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                        if ((Math.abs(x) == range || Math.abs(z) == range) && !world.isBlockNormalCube(x + xIn, y + yIn, z + zIn) && yg < y + yIn && yg >= yIn - range)
                            world.setBlock(x + xIn, y + yIn, z + zIn, blockID, 0, 3);
                    }
                }
    }
}

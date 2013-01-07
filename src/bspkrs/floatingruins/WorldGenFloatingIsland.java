package bspkrs.floatingruins;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.feature.WorldGenerator;
import bspkrs.util.CommonUtils;

public class WorldGenFloatingIsland extends WorldGenerator
{
    private boolean   isLavaNearby;
    private final int SPHEROID    = 0;
    private final int CONE        = 1;
    private final int POINT       = 2;
    private final int STALACTITE  = 3;
    private int[]     islandTypes = { SPHEROID, CONE, SPHEROID, SPHEROID, CONE };
    
    @Override
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        FloatingRuins.baseRadius = Math.max(6, Math.min(50, FloatingRuins.baseRadius));
        FloatingRuins.radiusVariation = Math.max(0, Math.min(50, FloatingRuins.radiusVariation));
        int r = FloatingRuins.baseRadius + random.nextInt(FloatingRuins.radiusVariation);
        isLavaNearby = false;
        if (world.isAirBlock(x, y, z) && (world.isAirBlock(x, y - r, z) || world.getBlockId(x, y - r, z) == Block.waterStill.blockID)
                && genIsland(world, r, x, y, z))
            (new WorldGenFloatingIslandRuin(isLavaNearby)).generate(world, random, x, y, z);
        
        return true;
    }
    
    private boolean isBlockInRange(int islandType, World world, int x, int y, int z, float depthRatio, int depth, int radius)
    {
        float distToCenterColumn = Math.round(Math.sqrt(CommonUtils.sqr(x) + CommonUtils.sqr(z)));
        float distToOrigin = Math.round(Math.sqrt(CommonUtils.sqr(x) + CommonUtils.sqr(z) + (y > 0 ? 0.0D : CommonUtils.sqr(y / depthRatio))));
        
        if (islandType == CONE)
        {
            if (y >= -1)
                return distToCenterColumn <= radius;
            else
                return distToCenterColumn <= (1.0F - Math.abs((y + 1.0F) / (depth - 1.0F))) * radius;
        }
        else if (islandType == POINT)
        {
            return distToCenterColumn <= (y >= -1 ? radius * 1.0F : y == -2 ? Math.ceil(radius * 0.8F) : (((float) depth / Math.abs(y)) - 1.0F));
        }
        else if (islandType == STALACTITE)
        {
            if (y >= -2)
                return distToCenterColumn <= radius;
            else if (Math.abs(y) <= depth * 0.4F)
                return distToOrigin <= radius;
            else if ((new Random((x + z) / (z == 0 ? 1 : z))).nextInt(Math.abs(x * z)) == 0)
                return distToOrigin <= radius;
            else
                return false;
        }
        
        // default to SPHEROID
        return distToOrigin <= radius;
    }
    
    public boolean genIsland(World world, int radius, int xIn, int yIn, int zIn)
    {
        FloatingRuins.baseDepth = Math.max(2, Math.min(50, FloatingRuins.baseDepth));
        FloatingRuins.depthVariation = Math.max(0, Math.min(50, FloatingRuins.depthVariation));
        int specialOre = getSpecialOre();
        Random random = new Random();
        float depthRatio = random.nextInt(5) == 0 ? random.nextFloat() * 0.5F + 2.0F : random.nextFloat() * 0.2F + 0.4F;
        int depth = (int) Math.ceil(radius * depthRatio);
        int yg = CommonUtils.getHighestGroundBlock(world, xIn, yIn, zIn);
        
        if (depth > FloatingRuins.baseDepth + FloatingRuins.depthVariation || depth < FloatingRuins.baseDepth || depth > yg - 5)
        {
            WorldType wt = world.getWorldInfo().getTerrainType();
            depth = Math.min(yg - (wt == WorldType.FLAT ? 1 : 5), Math.max(FloatingRuins.baseDepth, Math.min(depth, FloatingRuins.baseDepth + world.rand.nextInt(FloatingRuins.depthVariation))));
            depthRatio = (float) depth / (float) radius;
        }
        
        int islandType = islandTypes[(new Random(xIn * yIn * zIn)).nextInt(islandTypes.length)];
        
        for (int x = -radius; x <= radius; x++)
            for (int y = 15; y >= -depth; y--)
                for (int z = -radius; z <= radius; z++)
                {
                    int blockID = world.getBlockId(x + xIn, yg + y, z + zIn);
                    if (isBlockInRange(islandType, world, x, y, z, depthRatio, depth, radius))
                    {
                        int metadata = world.getBlockMetadata(x + xIn, yg + y, z + zIn);
                        if (((y <= 0) || (blockID != Block.waterStill.blockID && blockID != Block.waterMoving.blockID && (world.isBlockNormalCube(x + xIn, yg + y, z + zIn) || blockID == Block.leaves.blockID))) && !CommonUtils.isIDInList(blockID, metadata, FloatingRuins.blockIDBlacklist))
                        {
                            if (blockID == Block.mobSpawner.blockID)
                            {
                                TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getBlockTileEntity(x + xIn, yg + y, z + zIn);
                                if (spawner != null)
                                {
                                    world.setBlockWithNotify(x + xIn, y + yIn, z + zIn, Block.mobSpawner.blockID);
                                    NBTTagCompound spawnerNBT = new NBTTagCompound();
                                    spawner.writeToNBT(spawnerNBT);
                                    
                                    spawner = (TileEntityMobSpawner) world.getBlockTileEntity(x + xIn, y + yIn, z + zIn);
                                    if (spawner != null)
                                        spawner.readFromNBT(spawnerNBT);
                                }
                            }
                            else if (blockID == Block.chest.blockID)
                            {
                                TileEntityChest chest = (TileEntityChest) world.getBlockTileEntity(x + xIn, yg + y, z + zIn);
                                if (chest != null)
                                {
                                    world.setBlockAndMetadataWithNotify(x + xIn, y + yIn, z + zIn, Block.chest.blockID, metadata);
                                    NBTTagCompound chestNBT = new NBTTagCompound();
                                    chest.writeToNBT(chestNBT);
                                    
                                    chest = (TileEntityChest) world.getBlockTileEntity(x + xIn, y + yIn, z + zIn);
                                    if (chest != null)
                                        chest.readFromNBT(chestNBT);
                                }
                            }
                            else
                                world.setBlockAndMetadata(x + xIn, y + yIn, z + zIn, blockID, metadata);
                            
                            if (y >= -8 && (blockID == Block.lavaStill.blockID || blockID == Block.lavaMoving.blockID))
                                isLavaNearby = true;
                            
                            world.setBlockWithNotify(x + xIn, yg + y, z + zIn, 0);
                        }
                    }
                    if (random.nextInt(3) == 0 && world.getBlockId(x + xIn, y + yIn, z + zIn) == Block.stone.blockID && Math.abs(x) <= 1 && Math.abs(z) <= 1 && Math.abs(y + depth / 2) <= 2)
                        world.setBlock(x + xIn, y + yIn, z + zIn, specialOre);
                }
        
        // if (depthRatio < 1.0F)
        for (int x = -radius; x <= radius; x++)
            for (int y = 5; y >= -depth; y--)
                for (int z = -radius; z <= radius; z++)
                {
                    int block = world.getBlockId(x + xIn, y + yIn, z + zIn);
                    if (block != 0 && world.isAirBlock(x + xIn, (y + yIn) - 1, z + zIn))
                        if (block == Block.gravel.blockID)
                            world.setBlock(x + xIn, y + yIn, z + zIn, Block.stone.blockID);
                        else if (block == Block.sand.blockID)
                            world.setBlock(x + xIn, y + yIn, z + zIn, Block.sandStone.blockID);
                }
        
        return true;
    }
    
    private int getSpecialOre()
    {
        switch ((new Random()).nextInt(8))
        {
            case 0:
                return Block.oreDiamond.blockID;
                
            case 1:
                return Block.oreGold.blockID;
                
            case 2:
                return Block.oreIron.blockID;
                
            case 3:
                return Block.oreLapis.blockID;
                
            case 4:
                return Block.oreRedstone.blockID;
                
            case 5:
                return Block.oreEmerald.blockID;
                
            case 6:
                return Block.oreIron.blockID;
        }
        return Block.oreCoal.blockID;
    }
}
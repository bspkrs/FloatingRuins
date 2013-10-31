package bspkrs.floatingruins;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ForgeDirection;
import bspkrs.util.BlockNotifyType;
import bspkrs.util.CommonUtils;
import bspkrs.util.Coord;

public class WorldGenFloatingIsland extends WorldGenerator
{
    private boolean   isLavaNearby;
    private final int SPHEROID    = 0;
    private final int CONE        = 1;
    private final int JETSONS     = 2;
    private final int STALACTITE  = 3;
    private int[]     islandTypes = { SPHEROID, CONE, SPHEROID, SPHEROID, CONE };
    
    //    private final Coord srcOrigin;
    //    private final Coord tgtOrigin;
    //    private int         depth;
    //    private final int   radius;
    //    private float       depthRatio;
    //    private int         yGround;
    
    public WorldGenFloatingIsland()
    {   
        
    }
    
    @Override
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        boolean ran = false;
        FloatingRuins.baseRadius = Math.max(6, Math.min(50, FloatingRuins.baseRadius));
        FloatingRuins.radiusVariation = Math.max(0, Math.min(50, FloatingRuins.radiusVariation));
        int r = FloatingRuins.baseRadius + random.nextInt(FloatingRuins.radiusVariation);
        isLavaNearby = false;
        if (world.isAirBlock(x, y, z) && (world.isAirBlock(x, y - r, z) || world.getBlockId(x, y - r, z) == Block.waterStill.blockID)
                && genIsland(world, r, x, y, z))
            ran = (new WorldGenFloatingIslandRuin(isLavaNearby)).generate(world, random, x, y, z);
        
        return ran;
    }
    
    private boolean isBlockInRange(int islandType, World world, int x, int y, int z, float depthRatio, int depth, int radius)
    {
        float distToCenterColumn = Math.round(Math.sqrt(CommonUtils.sqr(x) + CommonUtils.sqr(z)));
        float distToOrigin = Math.round(Math.sqrt(CommonUtils.sqr(x) + CommonUtils.sqr(z) + (y > 10 ? -2.0D : y > 5 ? -1.0D : y > 0 ? 0.0D : CommonUtils.sqr(y / depthRatio))));
        
        if (islandType == CONE)
        {
            if (y >= -1)
                return distToCenterColumn <= radius + (y > 9 ? 3.5D : (y > 5 ? 2.5D : (y > 1 ? 1.5D : 0)));
            else
                return distToCenterColumn <= (1.0F - Math.abs((y + 1.0F) / (depth - 1.0F))) * radius;
        }
        else if (islandType == JETSONS)
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
    
    private boolean isSpecialMoveableBlock(int blockID)
    {
        return blockID == Block.stoneSingleSlab.blockID || blockID == Block.stairsWoodOak.blockID || blockID == Block.stairsCobblestone.blockID
                || blockID == Block.pressurePlatePlanks.blockID || blockID == Block.pressurePlateIron.blockID || blockID == Block.pressurePlateStone.blockID
                || blockID == Block.fence.blockID || blockID == Block.thinGlass.blockID || BlockLeavesBase.class.isAssignableFrom(Block.blocksList[blockID].getClass());
    }
    
    public boolean genIsland(World world, int radius, int xIn, int yIn, int zIn)
    {
        FloatingRuins.baseDepth = Math.max(2, Math.min(50, FloatingRuins.baseDepth));
        FloatingRuins.depthVariation = Math.max(0, Math.min(50, FloatingRuins.depthVariation));
        Random random = new Random(xIn + zIn << 8 + yIn << 16);
        float depthRatio = random.nextInt(5) == 0 ? random.nextFloat() * 0.5F + 2.0F : random.nextFloat() * 0.2F + 0.4F;
        int depth = (int) Math.ceil(radius * depthRatio);
        int yg = CommonUtils.getHighestGroundBlock(world, xIn, yIn, zIn);
        
        if (yg == 0)
            return false;
        
        Coord srcOrigin = new Coord(xIn, yg, zIn);
        Coord tgtOrigin = new Coord(xIn, yIn, zIn);
        
        if (depth > FloatingRuins.baseDepth + FloatingRuins.depthVariation || depth < FloatingRuins.baseDepth || depth > yg - 5)
        {
            WorldType wt = world.getWorldInfo().getTerrainType();
            depth = Math.min(yg - (wt == WorldType.FLAT ? 1 : 5), Math.max(FloatingRuins.baseDepth, Math.min(depth, FloatingRuins.baseDepth + world.rand.nextInt(FloatingRuins.depthVariation))));
            depthRatio = (float) depth / (float) radius;
        }
        
        int islandType = islandTypes[(new Random(xIn * yIn * zIn)).nextInt(islandTypes.length)];
        
        String debug = "Island: ";
        if (islandType == CONE)
            debug += "Cone ";
        else
            debug += "Spheroid ";
        
        debug += String.format("r(%d) d(%d) @%d,%d,%d ", radius, depth, xIn, yIn, zIn);
        
        int specialOre = getSpecialOre();
        
        for (int x = -radius - 4; x <= radius + 4; x++)
            for (int y = 40; y >= -depth; y--)
                for (int z = -radius - 4; z <= radius + 4; z++)
                {
                    Coord delta = new Coord(x, y, z);
                    Coord src = srcOrigin.add(delta);
                    int blockID = src.getBlockID(world);
                    if (blockID > 0 && isBlockInRange(islandType, world, x, y, z, depthRatio, depth, radius))
                    {
                        int metadata = src.getBlockMetadata(world);
                        if ((y <= 0)
                                || (blockID != Block.waterStill.blockID && blockID != Block.waterMoving.blockID &&
                                (src.isBlockNormalCube(world) || (blockID != 0 && isSpecialMoveableBlock(blockID)))
                                )
                                && !CommonUtils.isIDInList(blockID, metadata, FloatingRuins.blockIDBlacklist))
                        {
                            if (blockID == Block.mobSpawner.blockID)
                                debug += "+S(" + (x + xIn) + "," + (y + yIn) + "," + (z + zIn) + ") ";
                            else if (blockID == Block.chest.blockID)
                                debug += "+C(" + (x + xIn) + "," + (y + yIn) + "," + (z + zIn) + ") ";
                            else if (y >= -8 && !isLavaNearby && (blockID == Block.lavaStill.blockID || blockID == Block.lavaMoving.blockID))
                            {
                                isLavaNearby = true;
                                debug += "+L ";
                            }
                            
                            Coord tgt = tgtOrigin.add(delta);
                            
                            Coord.moveBlock(world, src, tgt, true);
                        }
                    }
                    if (random.nextInt(3) == 0 && blockID == Block.stone.blockID && Math.abs(x) <= 1 && Math.abs(z) <= 1 && Math.abs(y + depth / 2) <= 2)
                        world.setBlock(x + xIn, y + yIn, z + zIn, specialOre, 0, BlockNotifyType.NONE);
                }
        
        // if (depthRatio < 1.0F)
        for (int x = -radius; x <= radius; x++)
            for (int y = 5; y >= -depth; y--)
                for (int z = -radius; z <= radius; z++)
                {
                    Coord delta = new Coord(x, y, z);
                    Coord tgt = tgtOrigin.add(delta);
                    int blockID = tgt.getBlockID(world);
                    if (blockID != 0 && tgt.getAdjacentCoord(ForgeDirection.DOWN).isAirBlock(world))
                        if (blockID == Block.gravel.blockID)
                            world.setBlock(tgt.x, tgt.y, tgt.z, Block.stone.blockID, 0, BlockNotifyType.ALL);
                        else if (blockID == Block.sand.blockID)
                            world.setBlock(tgt.x, tgt.y, tgt.z, Block.sandStone.blockID, 0, BlockNotifyType.ALL);
                }
        
        FloatingRuins.debug(debug);
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
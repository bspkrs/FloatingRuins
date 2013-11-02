package bspkrs.floatingruins;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ForgeDirection;
import bspkrs.util.BlockNotifyType;
import bspkrs.util.CommonUtils;
import bspkrs.util.Coord;

public class WorldGenFloatingIsland extends WorldGenerator
{
    private boolean           isLavaNearby;
    private static final int  SPHEROID    = 0;
    private static final int  CONE        = 1;
    private static final int  JETSONS     = 2;
    private static final int  STALACTITE  = 3;
    public static final int[] islandTypes = { SPHEROID, CONE, SPHEROID, SPHEROID, CONE };
    
    private Coord             srcOrigin;
    private Coord             tgtOrigin;
    private final int         depth;
    public final int          radius;
    private final float       depthRatio;
    public final int          yGround;
    private final int         islandType;
    private Random            random;
    
    public WorldGenFloatingIsland(int radius, float depthRatio, int yGround, int islandType)
    {
        this.radius = radius;
        this.depthRatio = depthRatio;
        this.yGround = yGround;
        this.islandType = islandType;
        this.depth = (int) Math.ceil(radius * depthRatio);
    }
    
    @Override
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        boolean ran = false;
        
        this.random = FloatingRuins.getRandom(world, x, z);
        
        if (yGround == 0)
            return false;
        
        srcOrigin = new Coord(x, yGround, z);
        tgtOrigin = new Coord(x, y, z);
        
        isLavaNearby = false;
        if (tgtOrigin.isAirBlock(world) && (world.isAirBlock(x, y - depth, z) || world.getBlockId(x, y - depth, z) == Block.waterStill.blockID)
                && genIsland(world, radius, x, y, z))
            ran = (new WorldGenFloatingIslandRuin(isLavaNearby)).generate(world, random, x, y, z);
        
        return ran;
    }
    
    private boolean genIsland(World world, int radius, int xIn, int yIn, int zIn)
    {
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
                            
                            Coord tgt = tgtOrigin.add(delta);
                            if (blockID == Block.mobSpawner.blockID)
                                debug += "+S(" + tgt + ") ";
                            else if (blockID == Block.chest.blockID)
                                debug += "+C(" + tgt + ") ";
                            else if (y >= -8 && !isLavaNearby && (blockID == Block.lavaStill.blockID || blockID == Block.lavaMoving.blockID))
                            {
                                isLavaNearby = true;
                                debug += "+L ";
                            }
                            
                            Coord.moveBlock(world, src, tgt, true);
                        }
                    }
                    if (random.nextInt(3) == 0 && blockID == Block.stone.blockID && Math.abs(x) <= 1 && Math.abs(z) <= 1 && Math.abs(y + depth / 2) <= 2)
                        world.setBlock(x + xIn, y + yIn, z + zIn, specialOre, 0, BlockNotifyType.NONE);
                }
        
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
                || blockID == Block.fence.blockID || blockID == Block.thinGlass.blockID || BlockLeavesBase.class.isAssignableFrom(Block.blocksList[blockID].getClass())
                || blockID == Block.tilledField.blockID;
    }
    
    private int getSpecialOre()
    {
        switch (random.nextInt(8))
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
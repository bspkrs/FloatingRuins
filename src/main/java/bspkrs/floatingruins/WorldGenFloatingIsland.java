package bspkrs.floatingruins;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;
import bspkrs.helpers.block.BlockHelper;
import bspkrs.helpers.world.WorldHelper;
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
    public static final int[] islandTypes = { SPHEROID, CONE, SPHEROID, SPHEROID, CONE, SPHEROID, CONE, SPHEROID, SPHEROID, CONE, SPHEROID, CONE, SPHEROID, SPHEROID, CONE, SPHEROID, CONE, SPHEROID, JETSONS, SPHEROID, CONE, SPHEROID, CONE, SPHEROID, SPHEROID, CONE, SPHEROID, CONE, SPHEROID, SPHEROID, CONE, SPHEROID, CONE, SPHEROID, SPHEROID, CONE };
    
    private Coord             srcOrigin;
    private Coord             tgtOrigin;
    private final int         depth;
    public final int          radius;
    public final int          yGround;
    private final int         islandType;
    private Random            random;
    
    public WorldGenFloatingIsland(int radius, int depth, int yGround, int islandType)
    {
        this.radius = radius;
        this.depth = depth;
        this.yGround = yGround;
        this.islandType = islandType;
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
        
        //Added a check for dungeon rarity config
        if (isTgtSuitableForGeneration(world, tgtOrigin) && genIsland(world, radius, x, y, z) && random.nextInt(FloatingRuins.rarityDungeon) == 0)
            ran = (new WorldGenFloatingIslandRuin(isLavaNearby)).generate(world, random, x, y, z);
        
        return ran;
    }
    
    public boolean isTgtSuitableForGeneration(World world, Coord tgtOrigin)
    {
        //float reciprocalRootOf2 = 0.70710678f;
        //int adjRadius = Math.round(radius * reciprocalRootOf2);
        
        LinkedList<SimpleEntry<Coord, Boolean>> toCheck = new LinkedList<SimpleEntry<Coord, Boolean>>();
        
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin, Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius + 4), Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius + 4), Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.EAST, radius + 4), Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.WEST, radius + 4), Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius + 4).getOffsetCoord(ForgeDirection.EAST, radius + 4), Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius + 4).getOffsetCoord(ForgeDirection.WEST, radius + 4), Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius + 4).getOffsetCoord(ForgeDirection.EAST, radius + 4), Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius + 4).getOffsetCoord(ForgeDirection.WEST, radius + 4), Boolean.FALSE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, depth), Boolean.TRUE));
        
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius + 4).getOffsetCoord(ForgeDirection.UP, 40), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius + 4).getOffsetCoord(ForgeDirection.UP, 40), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.EAST, radius + 4).getOffsetCoord(ForgeDirection.UP, 40), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.WEST, radius + 4).getOffsetCoord(ForgeDirection.UP, 40), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius + 4).getOffsetCoord(ForgeDirection.EAST, radius + 4).getOffsetCoord(ForgeDirection.UP, 40), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius + 4).getOffsetCoord(ForgeDirection.WEST, radius + 4).getOffsetCoord(ForgeDirection.UP, 40), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius + 4).getOffsetCoord(ForgeDirection.EAST, radius + 4).getOffsetCoord(ForgeDirection.UP, 40), Boolean.TRUE));
        toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius + 4).getOffsetCoord(ForgeDirection.WEST, radius + 4).getOffsetCoord(ForgeDirection.UP, 40), Boolean.TRUE));
        
        if (depth > 15)
        {
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, depth / 2), Boolean.TRUE));
        }
        
        if (depth > 31)
        {
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth / 4.0F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.NORTH, radius).getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.EAST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
            toCheck.add(new SimpleEntry<Coord, Boolean>(tgtOrigin.getOffsetCoord(ForgeDirection.SOUTH, radius).getOffsetCoord(ForgeDirection.WEST, radius).getOffsetCoord(ForgeDirection.DOWN, Math.round(depth * 0.75F)), Boolean.TRUE));
        }
        
        for (SimpleEntry<Coord, Boolean> entry : toCheck)
            if (!isTgtCoordReplaceable(world, entry.getKey(), entry.getValue()))
                return false;
        
        return true;
    }
    
    public boolean isTgtCoordReplaceable(World world, Coord tgt, boolean allowNonAirSpecialBlocks)
    {
        return tgt.isAirBlock(world) || (allowNonAirSpecialBlocks && tgt.getBlock(world).equals(Blocks.water))
                || (allowNonAirSpecialBlocks && !tgt.isBlockOpaqueCube(world))
                || (allowNonAirSpecialBlocks && tgt.isWood(world))
                || (allowNonAirSpecialBlocks && tgt.isLeaves(world));
    }
    
    private boolean genIsland(World world, int radius, int xIn, int yIn, int zIn)
    {
        int blocksMoved = 0;
        int groundBlocksMoved = 0;
        String debug = "Floating Island: ";
        if (islandType == CONE)
            debug += "Cone ";
        else if (islandType == JETSONS)
            debug += "Jetsons ";
        else
            debug += "Spheroid ";
        
        debug += String.format("r(%d) d(%d) @%d,%d,%d ", radius, depth, xIn, yIn, zIn);
        
        Block specialOre = getSpecialOre();
        
        for (int x = -radius - 4; x <= radius + 4; x++)
            for (int y = 40; y >= -depth; y--)
                for (int z = -radius - 4; z <= radius + 4; z++)
                {
                    Coord delta = new Coord(x, y, z);
                    Coord src = srcOrigin.add(delta);
                    Block block = src.getBlock(world);
                    if (!src.isAirBlock(world) && isBlockInRange(islandType, world, x, y, z, depth, radius))
                    {
                        int metadata = src.getBlockMetadata(world);
                        if ((y <= 0) || (!block.equals(Blocks.water) && !block.equals(Blocks.flowing_water))
                                && !CommonUtils.isIDInList(BlockHelper.getUniqueID(block), metadata, FloatingRuins.blockIDBlacklist))
                        {
                            
                            Coord tgt = tgtOrigin.add(delta);
                            if (block.equals(Blocks.mob_spawner))
                                debug += "+S(" + tgt + ") ";
                            else if (block.equals(Blocks.chest))
                                debug += "+C(" + tgt + ") ";
                            else if (y >= -8 && !isLavaNearby && (block.equals(Blocks.lava) || block.equals(Blocks.flowing_lava)))
                            {
                                isLavaNearby = true;
                                debug += "+L ";
                            }
                            
                            Coord.moveBlock(world, src, tgt, true);
                            
                            if (y <= 0)
                                groundBlocksMoved++;
                            
                            blocksMoved++;
                        }
                        if (random.nextInt(3) == 0 && block.equals(Blocks.stone) && Math.abs(x) <= 1 && Math.abs(z) <= 1 && Math.abs(y + depth / 4) <= 2)
                            WorldHelper.setBlock(world, x + xIn, y + yIn, z + zIn, specialOre, 0, BlockNotifyType.NONE);
                    }
                }
        
        for (int x = -radius; x <= radius; x++)
            for (int y = 5; y >= -depth; y--)
                for (int z = -radius; z <= radius; z++)
                {
                    Coord delta = new Coord(x, y, z);
                    Coord tgt = tgtOrigin.add(delta);
                    Block block = tgt.getBlock(world);
                    if (!tgt.isAirBlock(world) && tgt.getAdjacentCoord(ForgeDirection.DOWN).isAirBlock(world))
                        if (block.equals(Blocks.gravel))
                            WorldHelper.setBlock(world, tgt.x, tgt.y, tgt.z, Blocks.stone, 0, BlockNotifyType.ALL);
                        else if (block.equals(Blocks.sand))
                            WorldHelper.setBlock(world, tgt.x, tgt.y, tgt.z, Blocks.sandstone, 0, BlockNotifyType.ALL);
                }
        
        debug += "Blocks Moved: " + blocksMoved + " (" + groundBlocksMoved + " at or below origin)";
        
        FloatingRuins.debug(debug);
        return true;
    }
    
    private boolean isBlockInRange(int islandType, World world, int x, int y, int z, int depth, int radius)
    {
    	float depthRatio = (float)depth/(float)radius;
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
            float jetDist = (((float) depth / Math.abs(y)) - 1.0F);
            return distToCenterColumn <= (y >= -1 ? radius : y == -2 ? Math.min(Math.ceil(radius * 0.9F), jetDist) :
                    y == -3 ? Math.min(Math.ceil(radius * 0.8F), jetDist) : jetDist);
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
    
    private Block getSpecialOre()
    {
        switch (random.nextInt(8))
        {
            case 0:
                return Blocks.diamond_ore;
                
            case 1:
                return Blocks.gold_ore;
                
            case 2:
                return Blocks.iron_ore;
                
            case 3:
                return Blocks.lapis_ore;
                
            case 4:
                return Blocks.redstone_ore;
                
            case 5:
                return Blocks.emerald_ore;
                
            case 6:
                return Blocks.iron_ore;
        }
        return Blocks.coal_ore;
    }
}
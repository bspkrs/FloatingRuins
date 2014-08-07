package bspkrs.floatingruins;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;
import bspkrs.util.BlockNotifyType;
import bspkrs.util.CommonUtils;
import bspkrs.util.Coord;
import cpw.mods.fml.common.registry.GameData;

public class WorldGenFloatingIsland extends WorldGenerator
{
    private boolean         isLavaNearby;
    public static final int SPHEROID   = 0;
    public static final int CONE       = 1;
    public static final int JETSONS    = 2;
    public static final int STALACTITE = 3;
    
    private Coord           srcOrigin;
    private Coord           tgtOrigin;
    private final int       depth;
    public final int        radius;
    public final int        yGround;
    private final int       islandType;
    private Random          random;
    
    public WorldGenFloatingIsland(int radius, int depth, int yGround, int islandType)
    {
        this.radius = radius;
        this.depth = depth;
        this.yGround = yGround;
        if (islandType == JETSONS && depth < 30)
            this.islandType = CONE;
        else
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
        
        // Added a check for dungeon rarity config
        if (isTgtSuitableForGeneration(world, tgtOrigin))
        {
            ran = genIsland(world, radius, x, y, z);
            if (ran && random.nextInt(FloatingRuins.rarityDungeon) == 0)
                (new WorldGenFloatingIslandRuin(isLavaNearby)).generate(world, random, x, y, z);
        }
        
        return ran;
    }
    
    public boolean isTgtSuitableForGeneration(World world, Coord tgtOrigin)
    {
        FRLog.debug("Checking target area for generation suitability...");
        
        for (int y = 40; y >= -depth; y--)
            for (int x = -radius - 4; x <= radius + 4; x++)
                for (int z = -radius - 4; z <= radius + 4; z++)
                {
                    if (y == 40 || y == -depth || Math.abs(x) == radius || Math.abs(z) == radius || Math.abs(x) == radius + 4 || Math.abs(z) == radius + 4)
                    {
                        Coord delta = new Coord(x, y, z);
                        Coord tgt = tgtOrigin.add(delta);
                        if (!isTgtCoordReplaceable(world, tgt, y < tgtOrigin.y))
                        {
                            FRLog.debug("Target coord is not replaceable: %s", tgt.toString());
                            return false;
                        }
                    }
                }
        
        FRLog.debug("Target area found to be suitable.");
        return true;
    }
    
    public boolean isTgtCoordReplaceable(World world, Coord tgt,
            boolean allowNonAirSpecialBlocks)
    {
        return tgt.chunkExists(world) && (tgt.isAirBlock(world)
                || (allowNonAirSpecialBlocks && tgt.getBlock(world).equals(Blocks.water))
                || (allowNonAirSpecialBlocks && !tgt.isBlockOpaqueCube(world))
                || (allowNonAirSpecialBlocks && tgt.isWood(world))
                || (allowNonAirSpecialBlocks && tgt.isLeaves(world)));
    }
    
    private boolean genIsland(World world, int radius, int xIn, int yIn, int zIn)
    {
        int blocksMoved = 0;
        int groundBlocksMoved = 0;
        int blockNotifications = 0;
        float range;
        String debug = "Floating Island: ";
        if (islandType == CONE)
            debug += "Cone ";
        else if (islandType == JETSONS)
            debug += "Jetsons ";
        else
            debug += "Spheroid ";
        
        debug += String.format("r(%d) d(%d) @%d,%d,%d ", radius, depth, xIn, yIn, zIn);
        
        Block specialOre = getSpecialOre();
        
        for (int y = 40; y >= -depth; y--)
        {
            if (y >= 0)
                range = radius;
            else
                switch (islandType)
                {
                    case CONE:
                        if (y >= -1)
                            range = (float) (radius + (y > 9 ? 3.5D : (y > 5 ? 2.5D : (y > 1 ? 1.5D : 0))));
                        else
                            range = (1.0F - Math.abs((y + 1.0F) / (depth - 1.0F))) * radius;
                        break;
                    case JETSONS:
                        float jetDist = (((float) depth / Math.abs(y)) - 1.0F);
                        if (y >= -1)
                            range = radius;
                        else if (y == -2)
                            range = (int) Math.round(Math.min(Math.ceil(radius * 0.9F), jetDist));
                        else if (y == -3)
                            range = (int) Math.round(Math.min(Math.ceil(radius * 0.8F), jetDist));
                        else
                            range = Math.round(jetDist);
                        break;
                    default: // Ellipsoid
                        range = Math.round(Math.sqrt(CommonUtils.sqr(radius) * (1.0F - (float) CommonUtils.sqr(y) / (float) CommonUtils.sqr(depth)))); // Derived from ellipse equation
                        break;
                
                }
            
            if (range <= 0.0F)
                range = -4.0F;
            
            //sqrRange = CommonUtils.sqr(range);
            
            for (int x = (int) (-range - 4); x <= range + 4; x++)
                for (int z = (int) (-range - 4); z <= range + 4; z++)
                {
                    Coord delta = new Coord(x, y, z);
                    Coord src = srcOrigin.add(delta);
                    if (!src.isAirBlock(world)
                            //&& ((CommonUtils.sqr(x) + CommonUtils.sqr(z)) <= sqrRange))
                            && isBlockInRange(islandType, world, delta, depth, radius))
                    {
                        
                        Block block = src.getBlock(world);
                        int metadata = src.getBlockMetadata(world);
                        if (((y <= 0)
                                || (!block.equals(Blocks.water) && !block.equals(Blocks.flowing_water)))
                                && !CommonUtils.isIDInList(GameData.getBlockRegistry().getNameForObject(block), metadata, FloatingRuins.blockIDBlacklist))
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
                            
                            if (y >= 0 || !isBlockInRange(islandType, world, delta.getAdjacentCoord(ForgeDirection.DOWN), depth, radius)
                                    || !isBlockInRange(islandType, world, delta.getAdjacentCoord(ForgeDirection.NORTH), depth, radius)
                                    || !isBlockInRange(islandType, world, delta.getAdjacentCoord(ForgeDirection.SOUTH), depth, radius)
                                    || !isBlockInRange(islandType, world, delta.getAdjacentCoord(ForgeDirection.EAST), depth, radius)
                                    || !isBlockInRange(islandType, world, delta.getAdjacentCoord(ForgeDirection.WEST), depth, radius))
                            {
                                Coord.moveBlock(world, src, tgt, true);
                                blockNotifications++;
                            }
                            else
                                Coord.moveBlock(world, src, tgt, true, BlockNotifyType.NONE);
                            
                            if (y <= 0)
                                groundBlocksMoved++;
                            
                            blocksMoved++;
                        }
                        if (random.nextInt(3) == 0 && block.equals(Blocks.stone) && Math.abs(x) <= 1 && Math.abs(z) <= 1 && Math.abs(y + depth / 4) <= 2)
                            world.setBlock(x + xIn, y + yIn, z + zIn, specialOre, 0, BlockNotifyType.NONE);
                    }
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
                            world.setBlock(tgt.x, tgt.y, tgt.z, Blocks.stone, 0, BlockNotifyType.ALL);
                        else if (block.equals(Blocks.sand))
                            if (tgt.getBlockMetadata(world) == 1)
                                world.setBlock(tgt.x, tgt.y, tgt.z, Blocks.hardened_clay, 0, BlockNotifyType.ALL);
                            else
                                world.setBlock(tgt.x, tgt.y, tgt.z, Blocks.sandstone, 0, BlockNotifyType.ALL);
                }
        
        debug += "Blocks Moved: " + blocksMoved + " (" + groundBlocksMoved + " at or below origin, " + blockNotifications + " block notifications)";
        
        FloatingRuins.debug(debug);
        return true;
    }
    
    private boolean isBlockInRange(int islandType, World world, Coord delta, int depth, int radius)
    {
        float depthRatio = (float) depth / (float) radius;
        float distToCenterColumn = Math.round(Math.sqrt(CommonUtils.sqr(delta.x) + CommonUtils.sqr(delta.z)));
        float distToOrigin = Math.round(Math.sqrt(CommonUtils.sqr(delta.x) + CommonUtils.sqr(delta.z) + (delta.y > 10 ? -2.0D : delta.y > 5 ? -1.0D :
                delta.y > 0 ? 0.0D : CommonUtils.sqr(delta.y / depthRatio))));
        
        if (islandType == CONE)
        {
            if (delta.y >= -1)
                return distToCenterColumn <= radius + (delta.y > 9 ? 3.5D : (delta.y > 5 ? 2.5D : (delta.y > 1 ? 1.5D : 0)));
            else
                return distToCenterColumn <= (1.0F - Math.abs((delta.y + 1.0F) / (depth - 1.0F))) * radius;
        }
        else if (islandType == JETSONS)
        {
            float jetDist = (((float) depth / Math.abs(delta.y)) - 1.0F);
            return distToCenterColumn <= (delta.y >= -1 ? radius : delta.y == -2 ? Math.min(Math.ceil(radius * 0.9F), jetDist) : delta.y == -3 ? Math.min(Math.ceil(radius * 0.8F), jetDist) : jetDist);
        }
        else if (islandType == STALACTITE)
        {
            if (delta.y >= -2)
                return distToCenterColumn <= radius;
            else if (Math.abs(delta.y) <= depth * 0.4F)
                return distToOrigin <= radius;
            else if ((new Random((delta.x + delta.z) / (delta.z == 0 ? 1 : delta.z))).nextInt(Math
                    .abs(delta.x * delta.z)) == 0)
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
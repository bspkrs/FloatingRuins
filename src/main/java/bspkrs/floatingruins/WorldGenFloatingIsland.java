package bspkrs.floatingruins;

import java.util.Random;

import bspkrs.util.BlockNotifyType;
import bspkrs.util.CommonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
//import net.minecraftforge.registries.GameData;
//proper?: import net.minecraftforge.fml.common.registry.GameRegistry;
//old: import net.minecraftforge.fml.common.registry.GameData;

public class WorldGenFloatingIsland extends WorldGenerator
{
    private boolean         isLavaNearby;
    public static final int SPHEROID      = 0;
    public static final int CONE          = 1;
    public static final int JETSONS       = 2;
    public static final int STALACTITE    = 3;

    private BlockPos        srcOrigin;
    private BlockPos        tgtOrigin;
    private final int       depth;
    public final int        radius;
    public final int        yGround;
    private final int       islandType;
    private Random          random;

    private String          failureReason = "";

    public WorldGenFloatingIsland(int radius, int depth, int yGround, int islandType)
    {
        this.radius = radius;
        this.depth = depth;
        this.yGround = yGround;
        if ((islandType == JETSONS) && (depth < 30))
            this.islandType = CONE;
        else
            this.islandType = islandType;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos)
    {
        boolean ran = false;

        this.random = FloatingRuins.getRandom(world, pos);

        if (yGround == 0)
            return false;

        srcOrigin = new BlockPos(pos.getX(), yGround, pos.getZ());
        tgtOrigin = new BlockPos(pos);

        isLavaNearby = false;

        // Added a check for dungeon rarity config
        if (isTgtSuitableForGeneration(world, tgtOrigin))
        {
            ran = genIsland(world, radius, pos);
            if (ran && (random.nextInt(FloatingRuins.rarityDungeon) == 0))
                (new WorldGenFloatingIslandRuin(isLavaNearby)).generate(world, random, pos);
        }

        return ran;
    }

    public boolean isTgtSuitableForGeneration(World world, BlockPos tgtOrigin)
    {
        FRLog.debug("Checking target area for generation suitability...");

        for (int y = 40; y >= -depth; y--)
            for (int x = -radius - 4; x <= (radius + 4); x++)
                for (int z = -radius - 4; z <= (radius + 4); z++)
                {
                    if ((y == 40) || (y == -depth) || (Math.abs(x) == radius) || (Math.abs(z) == radius) || (Math.abs(x) == (radius + 4)) || (Math.abs(z) == (radius + 4)))
                    {
                        BlockPos delta = new BlockPos(x, y, z);
                        BlockPos tgt = tgtOrigin.add(delta);
                        if (!isTgtBlockPosReplaceable(world, tgt, y < tgtOrigin.getY()))
                        {
                            FRLog.debug("Target BlockPos is not replaceable: %s", tgt.toString());
                            FRLog.debug(failureReason);
                            return false;
                        }
                    }
                }

        FRLog.debug("Target area found to be suitable.");
        return true;
    }

    public boolean isTgtBlockPosReplaceable(World world, BlockPos tgt,
            boolean allowNonAirSpecialBlocks)
    {
        IBlockState state = world.getBlockState(tgt);
        Block block = state.getBlock();
        if (!world.isBlockLoaded(tgt, false))
            failureReason = "Chunk does not exist.";
        else if (!world.isAirBlock(tgt)
                && !(allowNonAirSpecialBlocks && block.equals(Blocks.WATER))
                && !(allowNonAirSpecialBlocks && !world.isBlockNormalCube(tgt, true))
                && !(allowNonAirSpecialBlocks && block.isWood(world, tgt))
                && !(allowNonAirSpecialBlocks && block.isLeaves(state, world, tgt)))
            failureReason = "Block at target is not replaceable.";

        return failureReason.isEmpty();
    }

    private boolean genIsland(World world, int radius, BlockPos posIn)
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

        debug += String.format("r(%d) d(%d) @%s ", radius, depth, posIn.toString());

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
                        range = Math.round(Math.sqrt(CommonUtils.sqr(radius) * (1.0F - ((float) CommonUtils.sqr(y) / (float) CommonUtils.sqr(depth))))); // Derived from ellipse equation
                        break;

                }

            if (range <= 0.0F)
                range = -4.0F;

            for (int x = (int) (-range - 4); x <= (range + 4); x++)
                for (int z = (int) (-range - 4); z <= (range + 4); z++)
                {
                    BlockPos delta = new BlockPos(x, y, z);
                    BlockPos src = srcOrigin.add(delta);
                    if (!world.isAirBlock(src)
                            && isBlockInRange(islandType, world, delta, depth, radius))
                    {
                        IBlockState state = world.getBlockState(src);
                        Block block = state.getBlock();
                        int metadata = block.getMetaFromState(state);
                        String SBlock = block.toString();

                        BlockPos tgt = tgtOrigin.add(delta);
                        if (((y <= 0)
                                || (!block.equals(Blocks.WATER) && !block.equals(Blocks.FLOWING_WATER)))
                        		//TODO: again, have to fix getBlockRegistry
                        		// i think it is fixed, i need to test it once i can compile
                                && !CommonUtils.isIDInList(SBlock, metadata, FloatingRuins.blockIDBlacklist))
                        {
                            if (block.equals(Blocks.MOB_SPAWNER))
                                debug += "+S(" + tgt + ") ";
                            else if (block.equals(Blocks.CHEST))
                                debug += "+C(" + tgt + ") ";
                            else if ((y >= -8) && !isLavaNearby && (block.equals(Blocks.LAVA) || block.equals(Blocks.FLOWING_LAVA)))
                            {
                                isLavaNearby = true;
                                debug += "+L ";
                            }

                            if ((y >= 0) || !isBlockInRange(islandType, world, delta.down(), depth, radius)
                                    || !isBlockInRange(islandType, world, delta.north(), depth, radius)
                                    || !isBlockInRange(islandType, world, delta.south(), depth, radius)
                                    || !isBlockInRange(islandType, world, delta.east(), depth, radius)
                                    || !isBlockInRange(islandType, world, delta.west(), depth, radius))
                            {
                                CommonUtils.moveBlock(world, src, tgt, true);
                                blockNotifications++;
                            }
                            else
                                CommonUtils.moveBlock(world, src, tgt, true, BlockNotifyType.NONE);

                            if (y <= 0)
                                groundBlocksMoved++;

                            blocksMoved++;
                        }
                        if ((random.nextInt(3) == 0) && block.equals(Blocks.STONE) && (Math.abs(x) <= 1) && (Math.abs(z) <= 1) && (Math.abs(y + (depth / 4)) <= 2))
                            world.setBlockState(tgt, specialOre.getDefaultState(), BlockNotifyType.NONE);
                    }
                }
        }

        for (int x = -radius; x <= radius; x++)
            for (int y = 5; y >= -depth; y--)
                for (int z = -radius; z <= radius; z++)
                {
                    BlockPos delta = new BlockPos(x, y, z);
                    BlockPos tgt = tgtOrigin.add(delta);
                    IBlockState state = world.getBlockState(tgt);
                    Block block = state.getBlock();
                    if (!world.isAirBlock(tgt) && world.isAirBlock(tgt.down()))
                        if (block.equals(Blocks.GRAVEL))
                            world.setBlockState(tgt, Blocks.STONE.getDefaultState(), BlockNotifyType.ALL);
                        else if (block.equals(Blocks.SAND))
                            if (block.getMetaFromState(state) == 1)
                                world.setBlockState(tgt, Blocks.HARDENED_CLAY.getDefaultState(), BlockNotifyType.ALL);
                            else
                                world.setBlockState(tgt, Blocks.SANDSTONE.getDefaultState(), BlockNotifyType.ALL);
                }

        debug += "Blocks Moved: " + blocksMoved + " (" + groundBlocksMoved + " at or below origin, " + blockNotifications + " block notifications)";

        FloatingRuins.debug(debug);
        return true;
    }

    private boolean isBlockInRange(int islandType, World world, BlockPos delta, int depth, int radius)
    {
        float depthRatio = (float) depth / (float) radius;
        float distToCenterColumn = Math.round(Math.sqrt(CommonUtils.sqr(delta.getX()) + CommonUtils.sqr(delta.getZ())));
        float distToOrigin = Math.round(Math.sqrt(CommonUtils.sqr(delta.getX()) + CommonUtils.sqr(delta.getZ()) + (delta.getY() > 10 ? -2.0D : delta.getY() > 5 ? -1.0D :
                delta.getY() > 0 ? 0.0D : CommonUtils.sqr(delta.getY() / depthRatio))));

        if (islandType == CONE)
        {
            if (delta.getY() >= -1)
                return distToCenterColumn <= (radius + (delta.getY() > 9 ? 3.5D : (delta.getY() > 5 ? 2.5D : (delta.getY() > 1 ? 1.5D : 0))));
            else
                return distToCenterColumn <= ((1.0F - Math.abs((delta.getY() + 1.0F) / (depth - 1.0F))) * radius);
        }
        else if (islandType == JETSONS)
        {
            float jetDist = (((float) depth / Math.abs(delta.getY())) - 1.0F);
            return distToCenterColumn <= (delta.getY() >= -1 ? radius : delta.getY() == -2 ? Math.min(Math.ceil(radius * 0.9F), jetDist) : delta.getY() == -3 ? Math.min(Math.ceil(radius * 0.8F), jetDist) : jetDist);
        }
        else if (islandType == STALACTITE)
        {
            if (delta.getY() >= -2)
                return distToCenterColumn <= radius;
            else if (Math.abs(delta.getY()) <= (depth * 0.4F))
                return distToOrigin <= radius;
            else if ((new Random((delta.getX() + delta.getZ()) / (delta.getZ() == 0 ? 1 : delta.getZ()))).nextInt(Math.abs(delta.getX() * delta.getZ())) == 0)
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
                return Blocks.DIAMOND_ORE;

            case 1:
                return Blocks.GOLD_ORE;

            case 2:
                return Blocks.IRON_ORE;

            case 3:
                return Blocks.LAPIS_ORE;

            case 4:
                return Blocks.REDSTONE_ORE;

            case 5:
                return Blocks.EMERALD_ORE;

            case 6:
                return Blocks.IRON_ORE;
        }
        return Blocks.COAL_ORE;
    }
}
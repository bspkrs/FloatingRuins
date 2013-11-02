package bspkrs.floatingruins.fml;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class FloatingRuinsWorldGenerator implements IWorldGenerator
{
    public FloatingRuinsWorldGenerator()
    {}
    
    @Override
    public void generate(Random random, int x, int z, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        TickRegistry.registerTickHandler(new DelayedWorldGenTicker(EnumSet.of(TickType.SERVER), 10, world, random, x << 4, z << 4), Side.SERVER);
    }
    
}

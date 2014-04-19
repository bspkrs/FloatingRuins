package bspkrs.floatingruins.fml;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import bspkrs.floatingruins.FloatingRuins;
import cpw.mods.fml.common.IWorldGenerator;

public class FloatingRuinsWorldGenerator implements IWorldGenerator
{
    public FloatingRuinsWorldGenerator()
    {}
    
    @Override
    public void generate(Random random, int x, int z, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (FloatingRuins.enabled)
            new DelayedWorldGenTicker(10, world, random, x << 4, z << 4);
    }
    
}

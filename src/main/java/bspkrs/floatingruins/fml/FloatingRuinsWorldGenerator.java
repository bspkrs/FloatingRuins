package bspkrs.floatingruins.fml;

import java.util.Random;

import bspkrs.floatingruins.FloatingRuins;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class FloatingRuinsWorldGenerator implements IWorldGenerator
{
    public FloatingRuinsWorldGenerator()
    {}

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (FloatingRuins.enabled)
            FloatingRuins.generateSurface(world, random, chunkX << 4, chunkZ << 4, true);
        //new DelayedWorldGenTicker(10, world, random, chunkX << 4, chunkZ << 4);
    }

}

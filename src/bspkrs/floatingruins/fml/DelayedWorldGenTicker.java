package bspkrs.floatingruins.fml;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.world.World;
import bspkrs.floatingruins.FloatingRuins;
import bspkrs.fml.util.DelayedActionTicker;
import cpw.mods.fml.common.TickType;

public class DelayedWorldGenTicker extends DelayedActionTicker
{
    private World  world;
    private Random random;
    private int    x;
    private int    z;
    
    public DelayedWorldGenTicker(EnumSet<TickType> tickTypes, int delayTicks, World world, Random random, int x, int z)
    {
        super(tickTypes, delayTicks);
        this.world = world;
        this.random = random;
        this.x = x;
        this.z = z;
    }
    
    @Override
    public String getLabel()
    {
        return "DelayedWorldGenTicker";
    }
    
    @Override
    protected void onDelayCompletion()
    {
        FloatingRuins.generateSurface(world, random, x, z, true);
    }
    
}

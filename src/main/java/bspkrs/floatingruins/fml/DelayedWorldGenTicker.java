package bspkrs.floatingruins.fml;

import java.util.Random;

import net.minecraft.world.World;
import bspkrs.floatingruins.FloatingRuins;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class DelayedWorldGenTicker
{
    private World  world;
    private Random random;
    private int    x;
    private int    z;
    private int    delayTicks;
    
    public DelayedWorldGenTicker(int delayTicks, World world, Random random, int x, int z)
    {
        this.delayTicks = Math.max(delayTicks, 1);
        this.world = world;
        this.random = random;
        this.x = x;
        this.z = z;
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent
    public void onTick(WorldTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;
        
        if (--delayTicks == 0)
        {
            FloatingRuins.generateSurface(world, random, x, z, true);
            FMLCommonHandler.instance().bus().unregister(this);
        }
    }
}

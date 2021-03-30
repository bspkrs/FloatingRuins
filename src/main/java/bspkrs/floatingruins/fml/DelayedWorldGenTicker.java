package bspkrs.floatingruins.fml;

import java.util.Random;

import bspkrs.floatingruins.FloatingRuins;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

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

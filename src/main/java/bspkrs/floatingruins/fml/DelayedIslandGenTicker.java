package bspkrs.floatingruins.fml;

import bspkrs.floatingruins.IslandGenOptions;
import bspkrs.floatingruins.WorldGenFloatingIsland;
import bspkrs.util.CommonUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class DelayedIslandGenTicker
{
    private IslandGenOptions opt;
    private int              delayTicks;
    
    public DelayedIslandGenTicker(int delayTicks, IslandGenOptions opt)
    {
        this.delayTicks = Math.max(delayTicks, 1);
        this.opt = opt;
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent
    public void onTick(ServerTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;
        
        if (--delayTicks == 0)
        {
            new WorldGenFloatingIsland(opt.radius, opt.depth, CommonUtils.getHighestGroundBlock(opt.world, opt.x, opt.y, opt.z), opt.islandType)
                    .generate(opt.world, opt.random, opt.x, opt.y, opt.z);
            FMLCommonHandler.instance().bus().unregister(this);
        }
    }
}

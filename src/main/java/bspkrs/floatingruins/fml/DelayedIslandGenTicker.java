package bspkrs.floatingruins.fml;

import java.util.EnumSet;

import bspkrs.floatingruins.IslandGenOptions;
import bspkrs.floatingruins.WorldGenFloatingIsland;
import bspkrs.fml.util.DelayedActionTicker;
import bspkrs.util.CommonUtils;
import cpw.mods.fml.common.TickType;

public class DelayedIslandGenTicker extends DelayedActionTicker
{
    private IslandGenOptions opt;
    
    public DelayedIslandGenTicker(EnumSet<TickType> tickTypes, int delayTicks, IslandGenOptions opt, boolean isWorldGen)
    {
        super(tickTypes, delayTicks);
        this.opt = opt;
    }
    
    @Override
    public String getLabel()
    {
        return "DelayedIslandGenTicker";
    }
    
    @Override
    protected void onDelayCompletion()
    {
        new WorldGenFloatingIsland(opt.radius, opt.depthRatio, CommonUtils.getHighestGroundBlock(opt.world, opt.x, opt.y, opt.z), opt.islandType)
                .generate(opt.world, opt.random, opt.x, opt.y, opt.z);
    }
    
}

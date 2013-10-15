package bspkrs.floatingruins.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FloatingRuinsTicker implements ITickHandler
{
    private Minecraft         mcClient;
    
    private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);
    
    public FloatingRuinsTicker(EnumSet<TickType> tickTypes)
    {
        this.tickTypes = tickTypes;
        this.mcClient = FMLClientHandler.instance().getClient();
    }
    
    @Override
    public void tickStart(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, true);
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, false);
    }
    
    private void tick(EnumSet<TickType> tickTypes, boolean isStart)
    {
        for (TickType tickType : tickTypes)
        {
            if (!onTick(tickType, isStart))
            {
                this.tickTypes.remove(tickType);
                this.tickTypes.removeAll(tickType.partnerTicks());
            }
        }
    }
    
    public boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart)
        {
            return true;
        }
        
        if (mcClient != null && mcClient.thePlayer != null)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && FloatingRuinsMod.versionChecker != null)
                if (!FloatingRuinsMod.versionChecker.isCurrentVersion())
                    for (String msg : FloatingRuinsMod.versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
            return false;
        }
        
        return true;
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return this.tickTypes;
    }
    
    @Override
    public String getLabel()
    {
        return "FloatingRuinsTicker";
    }
    
}

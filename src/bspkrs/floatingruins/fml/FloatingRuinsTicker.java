package bspkrs.floatingruins.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.fml.util.TickerBase;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FloatingRuinsTicker extends TickerBase
{
    private Minecraft mcClient;
    
    public FloatingRuinsTicker(EnumSet<TickType> tickTypes)
    {
        super(tickTypes);
        this.mcClient = FMLClientHandler.instance().getClient();
    }
    
    @Override
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
    public String getLabel()
    {
        return "FloatingRuinsTicker";
    }
    
}

package bspkrs.floatingruins.fml;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.helpers.entity.player.EntityPlayerHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FloatingRuinsTicker
{
    private static boolean isRegistered = false;
    private Minecraft      mcClient;
    
    public FloatingRuinsTicker()
    {
        mcClient = FMLClientHandler.instance().getClient();
        isRegistered = true;
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;
        
        boolean keepTicking = !(mcClient != null && mcClient.thePlayer != null && mcClient.theWorld != null);
        
        if (bspkrsCoreMod.instance.allowUpdateCheck && !keepTicking)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && FloatingRuinsMod.instance.versionChecker != null)
                if (!FloatingRuinsMod.instance.versionChecker.isCurrentVersion())
                    for (String msg : FloatingRuinsMod.instance.versionChecker.getInGameMessage())
                        EntityPlayerHelper.addChatMessage(mcClient.thePlayer, new ChatComponentText(msg));
            
        }
        
        if (!keepTicking)
        {
            FMLCommonHandler.instance().bus().unregister(this);
            isRegistered = false;
        }
    }
    
    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
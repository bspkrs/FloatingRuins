package bspkrs.floatingruins.fml;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

        if (!keepTicking && isRegistered)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && FloatingRuinsMod.instance.versionChecker != null)
                if (!FloatingRuinsMod.instance.versionChecker.isCurrentVersion())
                    for (String msg : FloatingRuinsMod.instance.versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(new ChatComponentText(msg));

            FMLCommonHandler.instance().bus().unregister(this);
            isRegistered = false;
        }
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}

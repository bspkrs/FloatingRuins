package bspkrs.floatingruins.fml;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerTickHandler()
    {
        new FloatingRuinsTicker();
    }
}

package bspkrs.floatingruins.fml;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerTickHandler()
    {
        new FloatingRuinsTicker();
    }
}

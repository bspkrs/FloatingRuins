package bspkrs.floatingruins.fml;

import java.util.EnumSet;

import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerTickHandler()
    {
        TickRegistry.registerTickHandler(new FloatingRuinsTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
    }
}

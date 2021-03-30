package bspkrs.floatingruins;

import java.util.Random;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandFRGen extends CommandBase
{
    @Override
    public String getName()
    {
        return "frgen";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "commands.frgen.usage";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /*
     *  /frgen <x> <y> <z> [[<radius> <depth>] <shape>]
     *  /frgen random
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws WrongUsageException
    {
        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("random"))
            {
                if (sender instanceof EntityPlayer)
                {
                    // TODO: fix apparent coordinate offset
                    EntityPlayer player = (EntityPlayer) sender;
                    Random random = FloatingRuins.getRandom(player.world, new BlockPos((int) player.posX, 0, (int) player.posZ));
                    FloatingRuins.generateSurface(player.world, random, (int) player.posX, (int) player.posZ, false);
                }
            }
            else
                throw new WrongUsageException("commands.frgen.usage");
        }
        else if (args.length == 3)
        {
            // TODO: island gen with x, y, and z
        }
        else if (args.length == 4)
        {
            // TODO: island gen with x, y, z, and shape
        }
        else if (args.length == 5)
        {
            // TODO: island gen with x, y, z, radius, and depth
        }
        else if (args.length == 6)
        {
            // TODO: island gen with x, y, z, radius, depth, and shape
        }
    }

    @Override
    public int compareTo(ICommand p_compareTo_1_)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}

package bspkrs.floatingruins;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandFRGen extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "frgen";
    }
    
    @Override
    public String getCommandUsage(ICommandSender icommandsender)
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
    public void processCommand(ICommandSender icommandsender, String[] args)
    {
        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("random"))
            {
                // TODO: island gen randomly
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
}

package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
/*Handles tumble-snowDestroysBlocks command
  boolean value assigned to each world
  each command sender can change their own world's value*/
public class SnowDestroysBlocks implements TabExecutor {

    private final Tumble plug;

    public SnowDestroysBlocks(Tumble plug) {

        this.plug = plug;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return strings.length==2?List.of("true","false"):List.of();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //invalidate command if incorrect arguments are detected
        if(args.length!=1 || !(args[0].equalsIgnoreCase("false")||args[0].equalsIgnoreCase("true")))
            return false;
        if(commandSender instanceof Entity e) {
            //change value from entity sender
            PersistentDataContainer container = e.getWorld().getPersistentDataContainer();
            container.set(plug.SNOW_DESTROYS_BLOCKS, PersistentDataType.BOOLEAN,Boolean.parseBoolean(args[0]));
        }
        else if(commandSender instanceof BlockCommandSender b) {
            //change value from block sender
            PersistentDataContainer container = b.getBlock().getWorld().getPersistentDataContainer();
            container.set(plug.SNOW_DESTROYS_BLOCKS, PersistentDataType.BOOLEAN,Boolean.parseBoolean(args[0]));
        }
        else {
            //fail for console command sending (no world)
            return false;
        }
        //success case (command went through and value was changed)
        return true;
    }
}

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        if(args.length<1 || args.length>2 || !(List.of("true","false")).contains(args[0].toLowerCase()))
            return false;
        //prepare persistent data container
        PersistentDataContainer container = null;
        //check if world name was provided
        if(args.length==2&&plug.getServer().getWorlds().stream().map(m->m.getName()).collect(Collectors.toList()).contains(args[1].toLowerCase())) {
            //get world from name
            container = plug.getServer().getWorld(args[1]).getPersistentDataContainer();
        }
        else if(commandSender instanceof Entity e) {
            //get world from entity sender
            container = e.getWorld().getPersistentDataContainer();

        }
        else if(commandSender instanceof BlockCommandSender b) {
            //get world from block sender
            container = b.getBlock().getWorld().getPersistentDataContainer();

        }
        else {
            //fail case for undefined world
            return false;
        }
        //success case (command went through and value can be changed)
        container.set(plug.SNOW_DESTROYS_BLOCKS, PersistentDataType.BOOLEAN,Boolean.parseBoolean(args[0]));
        return true;
    }
}

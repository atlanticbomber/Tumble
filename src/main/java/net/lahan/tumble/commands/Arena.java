package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
/*
* Handles tumble-arena command and its various subcommands to change the arena size and location
* Each arena's coordinates and size are stored in the world's data
* command sender must be in the world of the arena to change it*/
public class Arena implements TabExecutor {
    private final Tumble plug;
    public static final int DEFAULT_SIZE = 51;
    public Arena(Tumble plug) {
        this.plug = plug;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //confirm that command sender has a linked world
        if(sender instanceof Entity||sender instanceof BlockCommandSender) {
            World w = null;
            //extract world from command sender
            if(sender instanceof Entity e) w = e.getWorld();
            else if(sender instanceof BlockCommandSender b) w = b.getBlock().getWorld();
            PersistentDataContainer cont = w.getPersistentDataContainer();
            //declare variables for all cases
            int arenaSize = cont.get(plug.ARENA_SIZE,PersistentDataType.INTEGER);
            int startX;
            int startZ;
            //fail case if no argument provided
            if (args.length == 0) {
                return false;
            //set subcommand: allows user to set arena coordinates
            } else if (args[0].equals("set")) {
                //extract coordinates from player sender if no more args provided
                if (args.length == 1 && sender instanceof Player) {
                    Player player = (Player) sender;
                    startX = player.getLocation().getBlockX();
                    startZ = player.getLocation().getBlockZ();
                    //use coords and arena size to create arena
                    cont.set(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY,
                            new int[]{startX,startZ,startX+arenaSize,startZ+arenaSize});
                    return true;
                //uses 2 numbers as a corner
                } else if (args.length == 3) {
                    //fail if arguments are not numbers
                    try {
                        startX = Integer.parseInt(args[1]);
                        startZ = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    //set arena coordinates and return success if numbers were entered
                    cont.set(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY,
                             new int[]{startX,startZ,startX+arenaSize,startZ+arenaSize});
                    return true;
                }
            //size subcommand (1 additional parameter only)
            } else if (args[0].equals("size") && args.length == 2) {
                int oldSize = cont.get(plug.ARENA_SIZE,PersistentDataType.INTEGER);
                if (args[1].equals("default")) {//set arena size to plugin default
                    cont.set(plug.ARENA_SIZE,PersistentDataType.INTEGER, DEFAULT_SIZE);
                    //update arena coordinates to maintain consistency
                    int[] corners = cont.get(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY);
                    corners[2] += DEFAULT_SIZE-oldSize;
                    corners[3] += DEFAULT_SIZE-oldSize;
                    return true;
                }
                int newSize;
                try {//fail if number cannot be parsed
                    newSize = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
                //set size to nearest odd number to make math easier
                newSize = 2*(newSize/2)+1;
                cont.set(plug.ARENA_SIZE,PersistentDataType.INTEGER, newSize);
                //update arena coordinates to maintain consistency
                int[] corners = cont.get(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY);
                corners[2] += newSize-oldSize;
                corners[3] += newSize-oldSize;
                return true;
            //disable subcommand: collapses this world's arena
            } else if (args[0].equals("disable") && args.length == 1) {
                cont.set(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY,
                        new int[]{0,0,0,0});
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    //tabcomplete recommendations
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length<2) {return List.of("set","size","disable");}
        boolean isPlayer = sender instanceof Player;
        Player player;
        //return empty list for nonplayer senders, otherwise extract player and world
        if(!isPlayer) {return List.of();}
        player = (Player) sender;
        int arenaSize = player.getWorld().getPersistentDataContainer().get(plug.ARENA_SIZE, PersistentDataType.INTEGER);
        if(args[0].equals("set")) {
            //autocompletes set command with player's current coordinates
            if (args.length == 2) {
                return List.of(Integer.toString(player.getLocation().getBlockX()));
            } else if (args.length == 3) {
                return List.of(Integer.toString( player.getLocation().getBlockZ()));
            }
        }
        //autocomplete with current size and Default for size subcommand
        else if(args[0].equals("size")&&args.length==2) {
            return List.of(Integer.toString(arenaSize),"Default");
        }
        //return empty list in other cases (disable subcommand, wrong # of params etc.)
        return List.of();
    }
}

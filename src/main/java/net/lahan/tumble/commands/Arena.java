package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Arena implements TabExecutor {
    private Tumble plug;
    public static final int DEFAULT_SIZE = 50;
    public Arena(Tumble plug) {
        this.plug = plug;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FileConfiguration config = plug.getConfig();
        int arenaSize = config.getInt("gameArea.size");
        int startX;
        int startZ;
        if(args.length==0){
            return false;
        }
        else if(args[0].equals("set")) {
            if(args.length==1&&sender instanceof Player) {
                Player player = (Player) sender;
                startX = player.getLocation().getBlockX();
                startZ = player.getLocation().getBlockZ();
                config.set("gameArea.start.x",startX);
                config.set("gameArea.start.z",startZ);
                config.set("gameArea.end.x",startX+arenaSize);
                config.set("gameArea.end.z",startZ+arenaSize);
                plug.saveConfig();
                return true;
            }
            else if(args.length==3) {
                try {
                    startX = Integer.parseInt(args[1]);
                    startZ = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    return false;
                }
                config.set("gameArea.start.x",startX);
                config.set("gameArea.start.z",startZ);
                config.set("gameArea.end.x",startX+arenaSize);
                config.set("gameArea.end.z",startZ+arenaSize);
                plug.saveConfig();
                return true;
            }
            else if(args.length==5) {
                int endX;
                int endZ;
                try {
                    startX = Integer.parseInt(args[1]);
                    startZ = Integer.parseInt(args[2]);
                    endX = Integer.parseInt(args[3]);
                    endZ = Integer.parseInt(args[4]);
                } catch (NumberFormatException e) {
                    return false;
                }
                config.set("gameArea.start.x",Math.min(startX,endX));
                config.set("gameArea.start.z",Math.min(startZ,endZ));
                config.set("gameArea.end.x",Math.max(startX,endX));
                config.set("gameArea.end.z",Math.max(startZ,endZ));
                plug.saveConfig();
                return true;
            }
        }
        else if(args[0].equals("size")&&args.length==2) {
            int newSize;
            try {newSize = Integer.parseInt(args[1]);}
            catch(NumberFormatException e) {return false;}
            config.set("gameArea.size",newSize);
            return true;
        }
        else if(args[0].equals("disable")&&args.length==1) {
            config.set("gameArea.start.x",0);
            config.set("gameArea.start.z",0);
            config.set("gameArea.end.x",0);
            config.set("gameArea.end.z",0);
            plug.saveConfig();
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length<2) {return List.of("set","size","disable");}
        boolean isPlayer = sender instanceof Player;
        Player player;
        if(!isPlayer) {return List.of();}
        player = (Player) sender;
        int arenaSize = plug.getConfig().getInt("gameArea.size");
        if(args[0].equals("set")) {
            if (args.length == 2) {
                return List.of(Integer.toString(player.getLocation().getBlockX()));
            } else if (args.length == 3) {
                return List.of(Integer.toString( player.getLocation().getBlockZ()));
            } else if (args.length == 4) {
                return List.of(Integer.toString(player.getLocation().getBlockX() + arenaSize));
            } else if (args.length == 5) {
                return List.of(Integer.toString( player.getLocation().getBlockZ() + arenaSize));
            }
        }
        else if(args[0].equals("size")&&args.length==2) {
            return List.of(Integer.toString(arenaSize),"Default");
        }
        return List.of();
    }
}

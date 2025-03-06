package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Arena implements TabExecutor {
    private Tumble plug;
    public Arena(Tumble plug) {
        this.plug = plug;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean isPlayer = sender instanceof Player;
        Player player;
        if(!isPlayer) {return List.of();}
        player = (Player) sender;
        int arenaSize = plug.getConfig().getInt("gameArea.size");
        if(args.length==0) {
            return List.of(Integer.toString(((int)player.getLocation().getX())));
        }
        else if(args.length==1) {
            return List.of(Integer.toString(((int)player.getLocation().getY())));
        }
        else if(args.length==2) {
            return List.of(Integer.toString(((int)player.getLocation().getX())+arenaSize));
        }
        else if(args.length==3) {
            return List.of(Integer.toString(((int)player.getLocation().getY())+arenaSize));
        }
        return List.of();
    }
}

package net.lahan.tumble.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowDestroysBlocks implements TabExecutor {
    private boolean active;
    public SnowDestroysBlocks() {
        active = false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of("true","false");
    }
    public boolean isActive() {
        return active;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length!=1 || !(args[0].equalsIgnoreCase("false")||args[0].equalsIgnoreCase("true")))
            return false;
        active = Boolean.parseBoolean(args[0]);
        return true;
    }
}

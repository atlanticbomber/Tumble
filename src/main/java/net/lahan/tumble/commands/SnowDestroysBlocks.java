package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class SnowDestroysBlocks implements TabExecutor {

    private final Tumble plug;

    public SnowDestroysBlocks(Tumble plug) {

        this.plug = plug;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return strings.length==1?List.of("true","false"):List.of();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length!=1 || !(args[0].equalsIgnoreCase("false")||args[0].equalsIgnoreCase("true")))
            return false;
        FileConfiguration config = plug.getConfig();
        config.set("snowDestroysBlocks",Boolean.parseBoolean(args[0]));
        plug.saveConfig();
        return true;
    }
}

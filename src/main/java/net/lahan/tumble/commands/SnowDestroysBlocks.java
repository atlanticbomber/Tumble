package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class SnowDestroysBlocks implements TabExecutor {

    private Tumble plug;

    public SnowDestroysBlocks(Tumble plug) {

        this.plug = plug;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of("true","false");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length!=1 || !(args[0].equalsIgnoreCase("false")||args[0].equalsIgnoreCase("true")))
            return false;
        FileConfiguration config = YamlConfiguration.loadConfiguration(plug.getData());
        config.set("snowDestroysBlocks",Boolean.parseBoolean(args[0]));
        plug.getLogger().info("argument: "+args[0]+", parsed argument: "+Boolean.parseBoolean(args[0]));
        try {
            config.save(plug.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}

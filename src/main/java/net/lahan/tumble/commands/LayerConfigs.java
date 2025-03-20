package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LayerConfigs implements TabExecutor {
    private final Tumble plug;
    public static final List<String> CONFIGS = List.of("spacing","blockFrequency","circleAdjustment","number","includeSpecialLayers");
    public LayerConfigs(Tumble plug) {
        this.plug = plug;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(args.length==2&&CONFIGS.contains(args[0]))) return false;
        FileConfiguration config = plug.getConfig();
        try {
            switch (args[0]) {
                case "spacing":
                case "number":
                    int valInt = Integer.parseInt(args[1]);
                    config.set("layers."+args[0],valInt);
                    plug.saveConfig();
                    return true;
                case "blockFrequency":
                case "circleAdjustment":
                    double valDouble = Double.parseDouble(args[1]);
                    config.set("layers."+args[0],valDouble);
                    plug.saveConfig();
                    return true;
                case "includeSpecialLayers":
                    boolean valBoolean = Boolean.parseBoolean(args[1]);
                    config.set("layers."+args[0],valBoolean);
                    plug.saveConfig();
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FileConfiguration config = plug.getConfig();
        if(args.length<2) {
            return CONFIGS;
        }
        else if(args.length==2&&CONFIGS.contains(args[0])) {
            return List.of(config.getString("layers."+args[0]));
        }
        else {
            return List.of();
        }
    }
}

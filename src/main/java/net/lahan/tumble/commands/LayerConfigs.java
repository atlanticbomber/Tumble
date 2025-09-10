package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.NamespacedKey;
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
//controls layer generation parameters in the world where cmd is executed
public class LayerConfigs implements TabExecutor {
    private final Tumble plug;
    //list of possible configuration values
    public static final List<String> CONFIGS = List.of("spacing","blockFrequency","circleAdjustment","number","includeSpecialLayers");
    public LayerConfigs(Tumble plug) {
        this.plug = plug;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //validate arg 1 and sender world identity
        if(!(args.length==2&&CONFIGS.contains(args[0]))) return false;
        PersistentDataContainer cont = plug.getServer().getWorlds().get(0).getPersistentDataContainer();
        if(commandSender instanceof Entity e) cont = e.getWorld().getPersistentDataContainer();
        if(commandSender instanceof BlockCommandSender b) cont = b.getBlock().getWorld().getPersistentDataContainer();
        try {
            switch (args[0]) {
                //validate int and change
                case "spacing":
                case "number":
                    int valInt = Integer.parseInt(args[1]);
                    cont.set(new NamespacedKey(plug, "layer-"+args[0]), PersistentDataType.INTEGER,valInt);
                    return true;
                //validate double and change
                case "blockFrequency":
                case "circleAdjustment":
                    double valDouble = Double.parseDouble(args[1]);
                    cont.set(new NamespacedKey(plug, "layer-"+args[0]), PersistentDataType.DOUBLE,valDouble);
                    return true;
                //validate boolean values
                case "includeSpecialLayers":
                    boolean valBoolean = Boolean.parseBoolean(args[1]);
                    cont.set(new NamespacedKey(plug, "layer-"+args[0]), PersistentDataType.BOOLEAN,valBoolean);
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
    //autocomplete parameters
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        PersistentDataContainer cont = plug.getServer().getWorlds().get(0).getPersistentDataContainer();
        if(sender instanceof Entity e) cont = e.getWorld().getPersistentDataContainer();
        if(sender instanceof BlockCommandSender b) cont = b.getBlock().getWorld().getPersistentDataContainer();
        //returns possible configs
        if(args.length<2) {
            return CONFIGS;
        }
        else if(args.length==2&&CONFIGS.contains(args[0])) {
            //returns current config value
            switch(args[0]) {
                case "spacing":
                    return List.of(cont.get(plug.LAYER_SPACING,PersistentDataType.INTEGER).toString());
                case "blockFrequency":
                    return List.of(cont.get(plug.LAYER_BLOCK_FREQUENCY,PersistentDataType.DOUBLE).toString());
                case "circleAdjustment":
                    return List.of(cont.get(plug.LAYER_CIRCLE_ADJUSTMENT,PersistentDataType.DOUBLE).toString());
                case "number":
                    return List.of(cont.get(plug.LAYER_NUMBER,PersistentDataType.INTEGER).toString());
                case "includeSpecialLayers":
                    return List.of(cont.get(plug.LAYER_INCLUDE_SPECIAL_LAYERS,PersistentDataType.BOOLEAN).toString());
                default:
                    return List.of();
            }
        }
        else {
            return List.of();
        }
    }
}

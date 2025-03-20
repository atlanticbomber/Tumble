package net.lahan.tumble;

import net.lahan.tumble.commands.Arena;
import net.lahan.tumble.commands.BuildLayer;
import net.lahan.tumble.commands.LayerConfigs;
import net.lahan.tumble.commands.SnowDestroysBlocks;
import net.lahan.tumble.listeners.SnowBreaker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Tumble extends JavaPlugin {

    public void initConfig() {
        FileConfiguration config = getConfig();
        config.set("snowDestroysBlocks",true);
        config.set("rocketDestroysBlocks",true);
        config.set("gameArea.size", Arena.DEFAULT_SIZE);
        config.set("gameArea.start.x",0);
        config.set("gameArea.start.z",0);
        config.set("gameArea.end.x",0);
        config.set("gameArea.end.z",0);
        config.set("layers.spacing",15);
        config.set("layers.blockFrequency",0.05);
        config.set("layers.circleAdjustment",0.3);
        config.set("layers.number",3);
        config.set("layers.includeSpecialLayers",true);
        saveConfig();
    }

    @Override
    public void onEnable() {

        // Plugin startup logic
        File data = new File(getDataFolder(), "config.yml");
        if(!data.exists()) {
            saveDefaultConfig();
            initConfig();
        }
        this.getCommand("tumble-arena").setExecutor(new Arena(this));
        this.getCommand("tumble-buildLayer").setExecutor(new BuildLayer(this));
        this.getCommand("tumble-snowDestroysBlocks").setExecutor(new SnowDestroysBlocks(this));
        this.getCommand("tumble-layerConfigs").setExecutor(new LayerConfigs(this));
        getServer().getPluginManager().registerEvents(new SnowBreaker(this), this);

    }

    @Override
    public void onDisable() {
    }


}
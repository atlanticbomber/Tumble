package net.lahan.tumble;

import net.lahan.tumble.commands.Arena;
import net.lahan.tumble.commands.BuildLayer;
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
        this.getCommand("snowDestroysBlocks").setExecutor(new SnowDestroysBlocks(this));
        getServer().getPluginManager().registerEvents(new SnowBreaker(this), this);
        this.getCommand("arena").setExecutor(new Arena(this));
        this.getCommand("buildLayer").setExecutor(new BuildLayer(this));
    }

    @Override
    public void onDisable() {
    }


}

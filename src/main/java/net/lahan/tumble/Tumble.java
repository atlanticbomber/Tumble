package net.lahan.tumble;

import net.lahan.tumble.commands.Arena;
import net.lahan.tumble.commands.SnowDestroysBlocks;
import net.lahan.tumble.listeners.SnowBreaker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Tumble extends JavaPlugin {

    private File data;
    public File getData() {
        return data;
    }
    public void initConfig() {
        FileConfiguration config = getConfig();
        config.set("snowDestroysBlocks",true);
        config.set("rocketDestroysBlocks",true);
        config.set("gameArea.size", Arena.DEFAULT_SIZE);
        config.set("gameArea.start.x",0);
        config.set("gameArea.start.z",0);
        config.set("gameArea.end.x",0);
        config.set("gameArea.end.z",0);
        saveConfig();
    }

    @Override
    public void onEnable() {

        // Plugin startup logic
        data = new File(getDataFolder(),"config.yml");
        if(!data.exists()) {
            saveDefaultConfig();
            initConfig();
        }
        this.getCommand("snowDestroysBlocks").setExecutor(new SnowDestroysBlocks(this));
        getServer().getPluginManager().registerEvents(new SnowBreaker(this), this);
        this.getCommand("arena").setExecutor(new Arena(this));
    }

    @Override
    public void onDisable() {
    }


}

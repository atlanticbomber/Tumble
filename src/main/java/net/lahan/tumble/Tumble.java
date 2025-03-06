package net.lahan.tumble;

import net.lahan.tumble.commands.SnowDestroysBlocks;
import net.lahan.tumble.listeners.SnowBreaker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
        config.set("gameArea.size", 50);
        config.set("gameArea.start.x",0);
        config.set("gameArea.start.y",0);
        config.set("gameArea.end.x",0);
        config.set("gameArea.end.y",0);
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
        SnowDestroysBlocks snowCMD = new SnowDestroysBlocks(this);
        this.getCommand("snowDestroysBlocks").setExecutor(snowCMD);
        getServer().getPluginManager().registerEvents(new SnowBreaker(this), this);
    }

    @Override
    public void onDisable() {
    }


}

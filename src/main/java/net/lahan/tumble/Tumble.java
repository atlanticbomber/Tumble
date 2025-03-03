package net.lahan.tumble;

import net.lahan.tumble.commands.SnowDestroysBlocks;
import net.lahan.tumble.listeners.SnowBreaker;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tumble extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        SnowDestroysBlocks snowCMD = new SnowDestroysBlocks();
        this.getCommand("snowDestroysBlocks").setExecutor(snowCMD);
        getServer().getPluginManager().registerEvents(new SnowBreaker(snowCMD), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}

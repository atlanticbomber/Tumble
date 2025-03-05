package net.lahan.tumble.listeners;

import net.lahan.tumble.Tumble;
import net.lahan.tumble.commands.SnowDestroysBlocks;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Objects;

public class SnowBreaker implements Listener {

    private final Tumble plug;

    public SnowBreaker(Tumble plug) {
        this.plug = plug;

    }
    @EventHandler
    public void onSnowHit(ProjectileHitEvent event) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(plug.getData());
        if(configuration.getBoolean("SnowDestroysBlocks")&&event.getEntity().getType()==EntityType.SNOWBALL) {
            Objects.requireNonNull(event.getHitBlock()).breakNaturally();
        }
    }

}

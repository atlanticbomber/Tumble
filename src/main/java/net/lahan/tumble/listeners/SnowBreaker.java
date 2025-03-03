package net.lahan.tumble.listeners;

import net.lahan.tumble.commands.SnowDestroysBlocks;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Objects;

public class SnowBreaker implements Listener {
    private SnowDestroysBlocks cmd;
    public SnowBreaker(SnowDestroysBlocks cmd) {
        this.cmd = cmd;
    }
    @EventHandler
    public void onSnowHit(ProjectileHitEvent event) {
        if(cmd.isActive()&&event.getEntity().getType()==EntityType.SNOWBALL) {
            Objects.requireNonNull(event.getHitBlock()).setType(Material.AIR);
        }
    }

}

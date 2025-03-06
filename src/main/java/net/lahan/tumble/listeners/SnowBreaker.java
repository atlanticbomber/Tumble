package net.lahan.tumble.listeners;

import net.lahan.tumble.Tumble;
import net.lahan.tumble.commands.SnowDestroysBlocks;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SnowBreaker implements Listener {

    private final Tumble plug;

    public SnowBreaker(Tumble plug) {
        this.plug = plug;

    }
    @EventHandler
    public void onSnowHit(ProjectileHitEvent event) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(plug.getData());
        if(configuration.getBoolean("snowDestroysBlocks")&&event.getEntity().getType()==EntityType.SNOWBALL) {
            Block block = Objects.requireNonNull(event.getHitBlock());
            plug.getServer().getWorld("world").
                    playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            block.setType(Material.AIR);
        }
    }

}

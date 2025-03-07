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
        if(snowballConditions(Objects.requireNonNull(event.getHitBlock()))&&event.getEntity().getType()==EntityType.SNOWBALL) {
            Block block = Objects.requireNonNull(event.getHitBlock());
            plug.getServer().getWorld("world").
                    playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            block.setType(Material.AIR);
        }
    }
    public boolean snowballConditions(Block b) {
        FileConfiguration configuration = plug.getConfig();
        int lowX = configuration.getInt("gameArea.start.x");
        int lowZ = configuration.getInt("gameArea.start.z");
        int highX = configuration.getInt("gameArea.end.x");
        int highZ = configuration.getInt("gameArea.end.z");
        boolean inArena = b.getX()>=lowX&&b.getX()<highX&&b.getZ()>=lowZ&&b.getZ()<highZ;
        return configuration.getBoolean("snowDestroysBlocks")||inArena;
    }

}

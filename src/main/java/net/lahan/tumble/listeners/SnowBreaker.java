package net.lahan.tumble.listeners;

import net.lahan.tumble.Tumble;
import net.lahan.tumble.commands.SnowDestroysBlocks;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class SnowBreaker implements Listener {
    //observes whenever a snowball hits a block and destroys it if applicable
    private final Tumble plug;

    public SnowBreaker(Tumble plug) {
        this.plug = plug;

    }
    @EventHandler
    public void onSnowHit(ProjectileHitEvent event) {//triggers on ANY projectile hit
        if(snowballConditions(Objects.requireNonNull(event.getHitBlock()))//ensure that snowball should destroy block
                &&event.getEntity().getType()==EntityType.SNOWBALL) {//ensure that a snowball triggered the event
            Block block = Objects.requireNonNull(event.getHitBlock());
            event.getHitBlock().getWorld().
                    playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());//pretend block was broken naturally
            block.setType(Material.AIR);//prevent item drop
        }
    }
    public boolean snowballConditions(Block b) {
        PersistentDataContainer container = b.getWorld().getPersistentDataContainer();
        int[] coords = container.get(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY);
        //get world arena coordinates
        int lowX = coords[0];
        int lowZ = coords[1];
        int highX = coords[2];
        int highZ = coords[3];
        boolean inArena = b.getX()>=lowX&&b.getX()<highX&&b.getZ()>=lowZ&&b.getZ()<highZ;
        /*Blocks are only destroyed if one or more of the following are fulfilled:
        * The block is inside the tumble arena
        * the global snowDestroysBlocks gamerule is enabled for this world
        */
        return container.get(plug.SNOW_DESTROYS_BLOCKS, PersistentDataType.BOOLEAN)||inArena;
    }

}

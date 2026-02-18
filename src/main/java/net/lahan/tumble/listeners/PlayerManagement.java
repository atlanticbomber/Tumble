package net.lahan.tumble.listeners;

import net.lahan.tumble.Tumble;
import net.lahan.tumble.events.TumbleGameEndEvent;
import net.lahan.tumble.events.TumblePlayerElimEvent;
import net.lahan.tumble.events.TumbleRoundEndEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PlayerManagement implements Listener {
    private final Tumble plug;
    public PlayerManagement(Tumble plug) {
        this.plug = plug;
    }
    @EventHandler
    public void playerJoinConfig(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        World w = p.getWorld();
        PersistentDataContainer worldData = w.getPersistentDataContainer();
        PersistentDataContainer playerData = event.getPlayer().getPersistentDataContainer();
        if(!playerData.has(plug.CONFIGURED_PLAYER)) {
            playerData.set(plug.GAMES_WON, PersistentDataType.INTEGER,0);
            playerData.set(plug.ROUNDS_WON, PersistentDataType.INTEGER,0);
            playerData.set(plug.TOTAL_GAMES, PersistentDataType.INTEGER,0);
            playerData.set(plug.TOTAL_ROUNDS, PersistentDataType.INTEGER,0);
            playerData.set(plug.CONFIGURED_PLAYER, PersistentDataType.BOOLEAN,true);

        }
        playerData.set(plug.IN_GAME, PersistentDataType.BOOLEAN,false);
        playerData.set(plug.SCORE,PersistentDataType.INTEGER,0);
        playerData.set(plug.ALIVE,PersistentDataType.BOOLEAN,false);
    }
    @EventHandler
    public void teleportOutOfArena(PlayerChangedWorldEvent e) {
        World w = e.getPlayer().getWorld();
        PersistentDataContainer worldData = w.getPersistentDataContainer();
        if(worldData.get(plug.HAS_GAME,PersistentDataType.BOOLEAN)) {
            int[] arena = worldData.get(plug.ARENA_COORDINATES, PersistentDataType.INTEGER_ARRAY);
            e.getPlayer().teleport(new Location(w, arena[2]+7, w.getHighestBlockYAt(arena[2]+7,arena[3]+7)+1, arena[3]+7));
        }
    }

    @EventHandler
    public void freezeInput(PlayerInputEvent e) {

    }
    public boolean playerInGame(Player p) {
        int[] arena = p.getWorld().getPersistentDataContainer().get(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY);
        Location loc = p.getLocation();
        boolean inArena = loc.getX()>arena[0]-7&&loc.getZ()>arena[1]-7&&loc.getX()<arena[2]+7&&loc.getZ()<arena[3]+7;
        return (!p.getPersistentDataContainer().get(plug.IN_GAME, PersistentDataType.BOOLEAN))&&p.getWorld().getPersistentDataContainer().get(plug.HAS_GAME,PersistentDataType.BOOLEAN)&&inArena;
    }
    private boolean playerInGameTest(Player p) {
        int[] arena = p.getWorld().getPersistentDataContainer().get(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY);
        Location loc = p.getLocation();
        return loc.getX()>arena[0]-7&&loc.getZ()>arena[1]-7&&loc.getX()<arena[2]+7&&loc.getZ()<arena[3]+7;
    }
    @EventHandler
    public void damagePreventer(@NotNull EntityDamageEvent event) {
        if(event.getEntity() instanceof Player p) {
            if(playerInGameTest(p)) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void inventoryReplenishmentDrop(@NotNull PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if(playerInGameTest(p)) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void inventoryReplenishmentThrow(@NotNull PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if(playerInGameTest(p)) {
            if(event.getAction()== Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK) {
                if(event.getItem().getType()== Material.SNOWBALL) {
                    p.getInventory().getItem(event.getHand()).setAmount(2);

                }
            }
        }
    }
    @EventHandler
    public void eliminatePlayer(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(playerInGameTest(p)&&p.getWorld().getBlockAt(e.getTo().add(0,-1,0)).getType()==Material.POINTED_DRIPSTONE) {
            p.setGameMode(GameMode.SPECTATOR);
            int layers = p.getWorld().getPersistentDataContainer().get(plug.LAYER_NUMBER,PersistentDataType.INTEGER);
            int spacing = p.getWorld().getPersistentDataContainer().get(plug.LAYER_SPACING,PersistentDataType.INTEGER);
            p.teleport(p.getLocation().add(0,layers*spacing-p.getLocation().getY(),0));
            p.getServer().getPluginManager().callEvent(new TumblePlayerElimEvent(e.getPlayer()));

        }
    }
}

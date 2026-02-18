package net.lahan.tumble.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class TumbleRoundEndEvent extends Event {
    private World world;
    private List<Player> players;
    public World getWorld() {
        return world;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public TumbleRoundEndEvent(World world, List<Player> players) {
        this.world = world;
        this.players = players;
    }
    private final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {return HANDLERS;}
}
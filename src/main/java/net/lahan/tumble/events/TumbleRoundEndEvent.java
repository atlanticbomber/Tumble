package net.lahan.tumble.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TumbleRoundEndEvent extends Event {
    private World world;
    public World getWorld() {
        return world;
    }
    public TumbleRoundEndEvent(World world) {
        this.world = world;
    }
    private final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {return HANDLERS;}
}
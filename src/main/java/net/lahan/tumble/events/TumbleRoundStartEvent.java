package net.lahan.tumble.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.List;

public class TumbleRoundStartEvent extends Event {
    private World world;
    public World getWorld() {
        return world;
    }
    public TumbleRoundStartEvent(World world) {
        this.world = world;
    }
    private final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {return HANDLERS;}
}

package net.lahan.tumble.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;

import java.util.List;

public class TumbleRoundStartEvent extends WorldEvent {
    private List<Player> players;
    public List<Player> getPlayers() {
        return players;
    }
    public TumbleRoundStartEvent(World world, List<Player> players) {
        super(world);
        this.players = players;
    }
    private final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {return HANDLERS;}
}

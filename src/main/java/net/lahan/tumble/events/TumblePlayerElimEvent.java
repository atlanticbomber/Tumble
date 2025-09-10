package net.lahan.tumble.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class TumblePlayerElimEvent extends PlayerEvent {
    private final HandlerList HANDLERS = new HandlerList();
    public TumblePlayerElimEvent(@NotNull Player who) {
        super(who);
    }
    public HandlerList getHandlers() {return HANDLERS;}

}

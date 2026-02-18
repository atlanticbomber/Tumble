package net.lahan.tumble.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.NotNull;

public class TumbleInputFreezeUpdateEvent extends WorldEvent {
    private final HandlerList HANDLERS = new HandlerList();
    public TumbleInputFreezeUpdateEvent(@NotNull World where, boolean what) {
        super(where);
    }
    public HandlerList getHandlers() {return HANDLERS;}
}

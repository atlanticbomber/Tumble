package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class stopGame implements CommandExecutor {
    private  final Tumble plug;
    public stopGame(Tumble plug) {
        this.plug = plug;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        World w;
        if(sender instanceof Entity entity) w = entity.getWorld();
        else if(sender instanceof BlockCommandSender b) w = b.getBlock().getWorld();
        else return false;
        PersistentDataContainer worldData = w.getPersistentDataContainer();
        if(!worldData.get(plug.HAS_GAME, PersistentDataType.BOOLEAN)) return false;
        worldData.set(plug.HAS_GAME,PersistentDataType.BOOLEAN,false);
        int[] coords = worldData.get(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY);
        int outX = coords[2]+7;
        int outZ = coords[3]+7;
        int outY = w.getHighestBlockYAt(outX,outZ);
        Location postGame = new Location(w,outX,outY, outZ);
        for(Player p: plug.getServer().getOnlinePlayers()) {
            PersistentDataContainer playerData = p.getPersistentDataContainer();
            if(playerData.get(plug.IN_GAME,PersistentDataType.BOOLEAN)&&p.getWorld()==w) {
                playerData.set(plug.IN_GAME, PersistentDataType.BOOLEAN, false);
                playerData.set(plug.SCORE, PersistentDataType.INTEGER, 0);
                playerData.set(plug.ALIVE, PersistentDataType.BOOLEAN, false);
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(postGame);
            }
        }
        return true;
    }
}

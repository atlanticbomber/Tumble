package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import net.lahan.tumble.events.TumbleGameStartEvent;
import net.lahan.tumble.events.TumbleRoundStartEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class StartGame implements TabExecutor {
    private Tumble plug;
    public StartGame(Tumble plug) {
        this.plug = plug;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        PersistentDataContainer worldData;
        World world;
        if(sender instanceof Entity e) world = e.getWorld();
        else if(sender instanceof BlockCommandSender b) world = b.getBlock().getWorld();
        else return false;
        worldData = world.getPersistentDataContainer();
        int[] coords = worldData.get(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY);
        if(worldData.get(plug.HAS_GAME, PersistentDataType.BOOLEAN)||coords[0]==coords[2]||coords[1]==coords[3]) return false;
        List<Player> playersInGame = new ArrayList<Player>();
        Map<String,Player> onlineNames = new HashMap<>();
        for(Player p: plug.getServer().getOnlinePlayers()) onlineNames.put(p.getName(),p);
        for(String s: args) {
            if(onlineNames.containsKey(s)) {
                Player p = onlineNames.get(s);
                playersInGame.add(p);
            }
        }
        if(playersInGame.size()==0) playersInGame.addAll(world.getPlayers());
        worldData.set(plug.HAS_GAME,PersistentDataType.BOOLEAN,true);
        plug.getServer().getPluginManager().callEvent(new TumbleGameStartEvent(world,playersInGame));
        for(Player p: playersInGame) {
            PersistentDataContainer playerData = p.getPersistentDataContainer();
            playerData.set(plug.IN_GAME,PersistentDataType.BOOLEAN,true);
            playerData.set(plug.TOTAL_GAMES,PersistentDataType.INTEGER,1+playerData.get(plug.TOTAL_GAMES,PersistentDataType.INTEGER));
            Location loc = p.getLocation().clone();
            loc.setWorld(world);
            p.teleport(loc);
            p.setGameMode(GameMode.ADVENTURE);
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> availPlayers = new ArrayList<>();
        availPlayers.addAll(plug.getServer().getOnlinePlayers().stream().map(p->p.getName()).collect(Collectors.toList()));
        availPlayers.removeAll(Arrays.asList(args));
        return availPlayers;
    }
}

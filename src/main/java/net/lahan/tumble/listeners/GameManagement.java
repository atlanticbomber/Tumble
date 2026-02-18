package net.lahan.tumble.listeners;

import net.lahan.tumble.Tumble;
import net.lahan.tumble.events.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class GameManagement implements Listener {
    private final Tumble plug;
    private Dictionary<World,List<Player>> playerLists;
    public GameManagement(Tumble plug) {
        this.plug = plug;
        this.playerLists = new Hashtable<>();
    }

    @EventHandler
    public void gameSetup(TumbleGameStartEvent e) {
        World world = e.getWorld();
        List<Player> playersInGame = e.getPlayers();
        for(Player p: playersInGame) {
            PersistentDataContainer playerData = p.getPersistentDataContainer();
            playerData.set(plug.IN_GAME,PersistentDataType.BOOLEAN,true);
            playerData.set(plug.TOTAL_GAMES,PersistentDataType.INTEGER,1+playerData.get(plug.TOTAL_GAMES,PersistentDataType.INTEGER));
            playerData.set(plug.SCORE,PersistentDataType.INTEGER,0);
            Location loc = p.getLocation().clone();
            loc.setWorld(world);
            p.teleport(loc);
            p.setGameMode(GameMode.ADVENTURE);
        }
        playerLists.put(world,playersInGame);
        plug.getServer().getPluginManager().callEvent(new TumbleRoundStartEvent(world,playersInGame));
    }
    @EventHandler
    public void roundSetup(TumbleRoundStartEvent e) {
        World world = e.getWorld();
        plug.getServer().dispatchCommand(plug.getServer().getConsoleSender(),"tumble-buildLayer "+world.getName());
        PersistentDataContainer worldData = world.getPersistentDataContainer();
        List<Player> playerList = playerLists.get(world);
        int height = -61+worldData.get(plug.LAYER_SPACING,PersistentDataType.INTEGER)*worldData.get(plug.LAYER_NUMBER,PersistentDataType.INTEGER);
        int[] coords = worldData.get(plug.ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY);
        int centerX = (coords[0]+coords[2])/2;
        int centerZ = (coords[1]+coords[3])/2;
        int spawnRad = worldData.get(plug.ARENA_SIZE,PersistentDataType.INTEGER)/4;
        Location center = new Location(world, centerX, height, centerZ);
        for(int i = 0; i<playerList.size(); i++) {
            Player p = playerList.get(i);
            p.getInventory().clear();
            ItemStack sb = new ItemStack(Material.SNOWBALL);
            sb.setAmount(1);
            for(int j = 0; j<9; j++)
                p.getInventory().setItem(j,sb.clone());
            PersistentDataContainer playerData = p.getPersistentDataContainer();
            playerData.set(plug.ALIVE, PersistentDataType.BOOLEAN, true);
            playerData.set(plug.TOTAL_ROUNDS,PersistentDataType.INTEGER,1+playerData.get(plug.TOTAL_ROUNDS,PersistentDataType.INTEGER));
            Location startPos = center.add(new Vector(spawnRad*Math.cos(2*Math.PI*i/playerList.size()),0,spawnRad*Math.sin(2*Math.PI*i/playerList.size())));
            p.setGameMode(GameMode.ADVENTURE);
            p.teleport(startPos);
        }

    }

    @EventHandler
    public void playerElimTrack(TumblePlayerElimEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        playerLists.get(w).remove(p);
        PersistentDataContainer worldData = w.getPersistentDataContainer();
        PersistentDataContainer playerData = p.getPersistentDataContainer();
        playerData.set(plug.ALIVE,PersistentDataType.BOOLEAN,false);
        worldData.set(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER,
                worldData.get(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER)-1);
        if(worldData.get(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER)==1) {
            plug.getServer().getPluginManager().callEvent(new TumbleRoundStartEvent(w,playerLists.get(w)));
        }
    }
    @EventHandler
    public void playerLeaveConfig(PlayerQuitEvent event) {
        World world = event.getPlayer().getWorld();
        PersistentDataContainer playerData = event.getPlayer().getPersistentDataContainer();
        PersistentDataContainer worldData = event.getPlayer().getWorld().getPersistentDataContainer();
        boolean inGame = playerData.get(plug.IN_GAME,PersistentDataType.BOOLEAN);
        boolean alive = playerData.get(plug.ALIVE,PersistentDataType.BOOLEAN);
        if(inGame) worldData.set(plug.NUMBER_PLAYERS,PersistentDataType.INTEGER,
                worldData.get(plug.NUMBER_PLAYERS,PersistentDataType.INTEGER)-1);
        if(alive) worldData.set(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER,
                worldData.get(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER)-1);
        playerData.set(plug.IN_GAME, PersistentDataType.BOOLEAN,false);
        playerData.set(plug.SCORE,PersistentDataType.INTEGER,0);
        playerData.set(plug.ALIVE,PersistentDataType.BOOLEAN,false);
        playerLists.get(world).remove(event.getPlayer());
        if(worldData.get(plug.NUMBER_PLAYERS,PersistentDataType.INTEGER)==1)
            plug.getServer().getPluginManager().callEvent(new TumbleGameEndEvent(world,playerLists.get(world)));
        if(worldData.get(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER)==1)
            plug.getServer().getPluginManager().callEvent(new TumbleRoundEndEvent(world,playerLists.get(world)));
    }

    @EventHandler
    public void playerWorldChangeConfig(PlayerChangedWorldEvent event) {
        World world = event.getFrom();
        PersistentDataContainer playerData = event.getPlayer().getPersistentDataContainer();
        PersistentDataContainer worldData = event.getPlayer().getWorld().getPersistentDataContainer();
        boolean inGame = playerData.get(plug.IN_GAME,PersistentDataType.BOOLEAN);
        boolean alive = playerData.get(plug.ALIVE,PersistentDataType.BOOLEAN);
        if(inGame) worldData.set(plug.NUMBER_PLAYERS,PersistentDataType.INTEGER,
                worldData.get(plug.NUMBER_PLAYERS,PersistentDataType.INTEGER)-1);
        if(alive) worldData.set(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER,
                worldData.get(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER)-1);
        playerData.set(plug.IN_GAME, PersistentDataType.BOOLEAN,false);
        playerData.set(plug.SCORE,PersistentDataType.INTEGER,0);
        playerData.set(plug.ALIVE,PersistentDataType.BOOLEAN,false);
        playerLists.get(world).remove(event.getPlayer());
        if(worldData.get(plug.NUMBER_PLAYERS,PersistentDataType.INTEGER)==1)
            plug.getServer().getPluginManager().callEvent(new TumbleGameEndEvent(world,playerLists.get(world)));
        if(worldData.get(plug.ACTIVE_PLAYERS,PersistentDataType.INTEGER)==1)
            plug.getServer().getPluginManager().callEvent(new TumbleRoundEndEvent(world,playerLists.get(world)));
    }
}

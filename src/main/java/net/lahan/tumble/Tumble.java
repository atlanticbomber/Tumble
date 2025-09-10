package net.lahan.tumble;

import net.lahan.tumble.commands.Arena;
import net.lahan.tumble.commands.BuildLayer;
import net.lahan.tumble.commands.LayerConfigs;
import net.lahan.tumble.commands.SnowDestroysBlocks;
import net.lahan.tumble.listeners.PlayerManagement;
import net.lahan.tumble.listeners.SnowBreaker;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Tumble extends JavaPlugin {
    //World persistent data keys: one-time config
    public final NamespacedKey SNOW_DESTROYS_BLOCKS = new NamespacedKey(this, "snowDestroysBlocks");
    public final NamespacedKey ROCKET_DESTROYS_BLOCKS = new NamespacedKey(this,"rocketDestroysBlocks");
    public final NamespacedKey ARENA_SIZE = new NamespacedKey(this,"arena-size");
    public final NamespacedKey ARENA_COORDINATES = new NamespacedKey(this,"arena-coordinates");
    public final NamespacedKey LAYER_SPACING = new NamespacedKey(this,"layer-spacing");
    public final NamespacedKey LAYER_BLOCK_FREQUENCY = new NamespacedKey(this,"layer-blockFrequency");
    public final NamespacedKey LAYER_CIRCLE_ADJUSTMENT = new NamespacedKey(this,"layer-circleAdjustment");
    public final NamespacedKey LAYER_NUMBER = new NamespacedKey(this,"layer-number");
    public final NamespacedKey LAYER_INCLUDE_SPECIAL_LAYERS = new NamespacedKey(this,"layer-includeSpecialLayers");
    public final NamespacedKey CONFIGURED_WORLD = new NamespacedKey(this,"configured-world");
    //config per session
    public final NamespacedKey HAS_GAME = new NamespacedKey(this,"hasGame");
    public final NamespacedKey NUMBER_PLAYERS = new NamespacedKey(this, "numPlayers");
    public final NamespacedKey ACTIVE_PLAYERS = new NamespacedKey(this, "activePlayers");
    //player persistent data keys: one-time config
    public final NamespacedKey GAMES_WON = new NamespacedKey(this,"gamesWon");
    public final NamespacedKey ROUNDS_WON = new NamespacedKey(this,"roundsWon");
    public final NamespacedKey TOTAL_GAMES = new NamespacedKey(this,"totalGames");
    public final NamespacedKey TOTAL_ROUNDS = new NamespacedKey(this,"totalRounds");
    public final NamespacedKey CONFIGURED_PLAYER = new NamespacedKey(this,"configured");
    //config per session
    public final NamespacedKey IN_GAME = new NamespacedKey(this,"inGame");
    public final NamespacedKey SCORE = new NamespacedKey(this, "score");
    public final NamespacedKey ALIVE = new NamespacedKey(this, "alive");
    /**
     * configures a world persistent data container to include the following values in this plugin's namespace (format: key, type, usage, init value):
     * snowDestroysBlocks: boolean, determines if snowballs destroy blocks out of game, false
     * rocketDestroysBlocks: boolean, determines if rockets destroy blocks out of game, false
     * arena-size: int, size of game area for /arena set commands, 51
     * arena-coordinates: int[], coordinates of the bounds of the game area in the form {startx,startz,endx,endz}, {0,0,0,0}
     * layer-spacing: int, vertical distance in blocks between each layer, 15
     * layer-blockFrequency: double, chance that a block can be different from its neighbors, 0.05
     * layer-circleAdjustment: double, value used during layer generation to modify the layer shape, 0.3
     * layer-number: int, number of layers to generate each game, 3
     * layer-includeSpecialLayers: boolean, whether to randomly add special features to layers during generation, true
     * hasGame: boolean, whether a game of tumble is currently happening in this world, false
     * configured: boolean, used to indicate which worlds need their data configured (immutable once created), true
     * numPlayers: int, number of players currently in this world's game, 0
     * activePlayers: int, number of players who have not been eliminated this round, 0
     * @param w world to be configured
     */
    public void configureWorldData(@NotNull World w) {
        PersistentDataContainer cont = w.getPersistentDataContainer();
        cont.set(SNOW_DESTROYS_BLOCKS,PersistentDataType.BOOLEAN,false);
        cont.set(ROCKET_DESTROYS_BLOCKS,PersistentDataType.BOOLEAN,false);
        cont.set(ARENA_SIZE,PersistentDataType.INTEGER,Arena.DEFAULT_SIZE);
        cont.set(ARENA_COORDINATES,PersistentDataType.INTEGER_ARRAY, new int[]{0, 0, 0, 0});
        cont.set(LAYER_SPACING,PersistentDataType.INTEGER,15);
        cont.set(LAYER_BLOCK_FREQUENCY,PersistentDataType.DOUBLE,0.05);
        cont.set(LAYER_CIRCLE_ADJUSTMENT,PersistentDataType.DOUBLE,0.3);
        cont.set(LAYER_NUMBER,PersistentDataType.INTEGER,3);
        cont.set(LAYER_INCLUDE_SPECIAL_LAYERS,PersistentDataType.BOOLEAN,true);

        cont.set(CONFIGURED_WORLD,PersistentDataType.BOOLEAN,true);

    }

    @Override
    public void onEnable() {

        // set up world data
        for(World w: getServer().getWorlds()) {
            //configure new worlds (one-time only)
            if(!w.getPersistentDataContainer().has(CONFIGURED_WORLD)) {
                getServer().getConsoleSender().sendMessage("configuring "+w.getName());
                configureWorldData(w);
            }
            //reconfigure session-specific data
            w.getPersistentDataContainer().set(HAS_GAME,PersistentDataType.BOOLEAN,false);
            w.getPersistentDataContainer().set(NUMBER_PLAYERS,PersistentDataType.INTEGER,0);
            w.getPersistentDataContainer().set(ACTIVE_PLAYERS,PersistentDataType.INTEGER,0);

        }
        //register commands
        //TODO: add startGame and stopGame commands here and in plugin.yml
        this.getCommand("tumble-arena").setExecutor(new Arena(this));
        this.getCommand("tumble-buildLayer").setExecutor(new BuildLayer(this));
        this.getCommand("tumble-snowDestroysBlocks").setExecutor(new SnowDestroysBlocks(this));
        this.getCommand("tumble-layerConfigs").setExecutor(new LayerConfigs(this));
        //register listeners
        getServer().getPluginManager().registerEvents(new SnowBreaker(this), this);
        getServer().getPluginManager().registerEvents(new PlayerManagement(this),this);

    }

    @Override
    public void onDisable() {
        for(Player p:getServer().getOnlinePlayers()) {
            //reset player data
            PersistentDataContainer playerData = p.getPersistentDataContainer();
            playerData.set(IN_GAME, PersistentDataType.BOOLEAN,false);
            playerData.set(SCORE,PersistentDataType.INTEGER,0);
            playerData.set(ALIVE,PersistentDataType.BOOLEAN,false);
        }
        for(World w: getServer().getWorlds()) {
            //reset world data
            w.getPersistentDataContainer().set(HAS_GAME,PersistentDataType.BOOLEAN,false);
            w.getPersistentDataContainer().set(NUMBER_PLAYERS,PersistentDataType.INTEGER,0);
            w.getPersistentDataContainer().set(ACTIVE_PLAYERS,PersistentDataType.INTEGER,0);
        }

    }


}
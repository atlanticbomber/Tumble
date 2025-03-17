package net.lahan.tumble.commands;

import net.lahan.tumble.Tumble;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class BuildLayer implements TabExecutor {
    public static final Material[] WHITE = {Material.QUARTZ_BLOCK,Material.SMOOTH_QUARTZ,Material.CHISELED_QUARTZ_BLOCK,Material.IRON_BLOCK,Material.QUARTZ_BRICKS};
    public static final Material[] WOOD_LIGHT = {Material.OAK_PLANKS,Material.BIRCH_PLANKS,Material.JUNGLE_PLANKS,Material.STRIPPED_BIRCH_WOOD,Material.STRIPPED_JUNGLE_WOOD};
    public static final Material[] WOOL = {Material.RED_WOOL,Material.YELLOW_WOOL,Material.LIME_WOOL,Material.LIGHT_BLUE_WOOL,Material.MAGENTA_WOOL};
    public static final Material[] TERRACOTTA = {Material.PINK_TERRACOTTA,Material.RED_TERRACOTTA,Material.YELLOW_TERRACOTTA,Material.LIME_TERRACOTTA,Material.BLUE_TERRACOTTA};
    public static final Material[] CHEESE = {Material.SANDSTONE,Material.END_STONE,Material.END_STONE_BRICKS,Material.WHITE_TERRACOTTA,Material.SMOOTH_SANDSTONE};
    public static final Material[] PRISMARINE = {Material.PRISMARINE,Material.DARK_PRISMARINE,Material.PRISMARINE,Material.DARK_PRISMARINE,Material.PRISMARINE_BRICKS};
    public static final Material[] MUSHROOM = {Material.RED_MUSHROOM_BLOCK,Material.BROWN_MUSHROOM_BLOCK,Material.RED_MUSHROOM_BLOCK,Material.BROWN_MUSHROOM_BLOCK,Material.MUSHROOM_STEM};
    public static final Material[] WOOD_RED = {Material.CHERRY_WOOD,Material.CRIMSON_PLANKS,Material.CRIMSON_HYPHAE,Material.MANGROVE_PLANKS,Material.STRIPPED_MANGROVE_WOOD};
    public static final Material[] ORANGE = {Material.ORANGE_TERRACOTTA,Material.PACKED_MUD,Material.MUD_BRICKS,Material.ACACIA_PLANKS,Material.STRIPPED_ACACIA_WOOD};
    public static final Material[] FORTRESS = {Material.POLISHED_BLACKSTONE_BRICKS,Material.POLISHED_BLACKSTONE,Material.BLACKSTONE,Material.NETHER_BRICKS,Material.CHISELED_NETHER_BRICKS};
    public static final Material[] WOOD_DARK = {Material.SPRUCE_PLANKS,Material.MANGROVE_WOOD,Material.STRIPPED_DARK_OAK_WOOD,Material.DARK_OAK_PLANKS,Material.SPRUCE_WOOD};
    public static final Material[] STONE_LIGHT = {Material.STONE,Material.STONE_BRICKS,Material.MOSSY_STONE_BRICKS,Material.CRACKED_STONE_BRICKS,Material.CHISELED_STONE_BRICKS};
    public static final Material[] NETHER = {Material.NETHERRACK,Material.SOUL_SOIL,Material.SOUL_SAND,Material.CRIMSON_NYLIUM,Material.SMOOTH_BASALT};
    public static final Material[] ORES = {Material.COAL_ORE,Material.REDSTONE_ORE,Material.GOLD_ORE,Material.EMERALD_ORE,Material.DIAMOND_ORE};
    public static final Material[] STONE_DARK = {Material.TUFF,Material.DEEPSLATE,Material.COBBLED_DEEPSLATE,Material.DEEPSLATE_BRICKS,Material.DEEPSLATE_TILES};
    public static final  Material[][] PALETTES = {WHITE, WOOD_LIGHT, WOOL, TERRACOTTA, CHEESE, PRISMARINE, MUSHROOM, WOOD_RED, ORANGE, FORTRESS, WOOD_DARK, STONE_LIGHT, NETHER, ORES, STONE_DARK};
    public static final List<String> LAYER_MODIFICATIONS = List.of("donut","small holes","second layer","sprinkled blocks");
    private final Tumble plug;
    public int spacing() {
        config = plug.getConfig();
        return config.getInt("layers.spacing");}
    public double blockFrequency() {
        config = plug.getConfig();
        return config.getDouble("layers.blockFrequency");}
    public double blockIncrement() {
        return (1-blockFrequency())/4.0;}
    public double circleAdjustment() {
        config = plug.getConfig();
        return config.getDouble("layers.circleAdjustment")+0.2*Math.random()-0.1;}
    private FileConfiguration config;
    public BuildLayer(Tumble plug) {
        this.plug = plug;
        config = plug.getConfig();
    }
    public boolean inSquircle(double x, double z, double cx, double cz, double rad, double pow) {
        if(rad<=0) return false;
        return Math.pow(Math.abs(x-cx),pow)+Math.pow(Math.abs(z-cz),pow)<=Math.pow(Math.abs(rad),pow);
    }
    public Material[][][] makeLayer(int arenaSize, boolean special, double edgeRemover) {
        Material[][][] layer = new Material[arenaSize][2][arenaSize];
        for(Material[][] slice: layer) {
            for(Material[] line : slice) {
                for(int i = 0; i<arenaSize; i++) {
                    line[i] = Material.AIR;
                }
            }
        }
        Material[] palette = PALETTES[(int)(PALETTES.length*Math.random())];
        double pow = 1.5+2*Math.pow(Math.random(),2);
        for(int x = 0; x<arenaSize; x++) {
            for(int z = 0; z<arenaSize; z++) {
                if(inSquircle(x+0.5,z+0.5,arenaSize/2.0,arenaSize/2.0,arenaSize/2.0-edgeRemover+circleAdjustment(),pow)) {
                    double chance = Math.random();
                    if(chance>=0* blockIncrement() &&chance<1* blockIncrement() &&x>0&&layer[x-1][0][z]!=Material.AIR) {
                        layer[x][0][z] = layer[x-1][0][z];
                    }
                    else if(chance>=1* blockIncrement() &&chance<2* blockIncrement() &&z>0&&layer[x][0][z-1]!=Material.AIR) {
                        layer[x][0][z] = layer[x][0][z-1];
                    }
                    else if(chance>=2* blockIncrement() &&chance<3* blockIncrement() &&x<layer.length-1&&layer[x+1][0][z]!=Material.AIR) {
                        layer[x][0][z] = layer[x+1][0][z];
                    }
                    else if(chance>=3* blockIncrement() &&chance<4* blockIncrement() &&z<layer[0][0].length-1&&layer[x][0][z+1]!=Material.AIR) {
                        layer[x][0][z] = layer[x][0][z+1];
                    }
                    else {
                        layer[x][0][z] = palette[(int)(palette.length*Math.random())];
                    }
                }
            }
        }
        if(special&&Math.random()>0.1) {
            switch (LAYER_MODIFICATIONS.get((int)(LAYER_MODIFICATIONS.size()*Math.random()))) {
                case "donut":
                    pow = 1.8+1.2*Math.pow(Math.random(),2);
                    double radius = (1+Math.random())*arenaSize/12.0;
                    for(int x = 0; x<arenaSize; x++) {
                        for(int z = 0; z<arenaSize; z++) {
                            if(inSquircle(x+0.5,z+0.5,arenaSize/2.0,arenaSize/2.0,radius-edgeRemover,pow)) {
                                layer[x][0][z] = Material.AIR;
                            }
                        }
                    }
                    break;
                case "small holes":
                    for(int x = 0; x<arenaSize; x++) {
                        for (int z = 0; z < arenaSize; z++) {
                            if(layer[x][0][z]!=Material.AIR&&Math.random()<0.01) {
                                if((x>0&&layer[x-1][0][z]!=Material.AIR)&&(x<layer.length-1&&layer[x+1][0][z]!=Material.AIR)&&
                                    (z>0&&layer[x][0][z-1]!=Material.AIR)&&(z<layer.length-1&&layer[x][0][z+1]!=Material.AIR)) {
                                    layer[x][0][z] = Material.AIR;
                                }
                            }
                        }
                    }
                    break;
                case "second layer":
                    double powIn = 1.8+1.2*Math.pow(Math.random(),2);
                    double powOut = 1.5+2*Math.pow(Math.random(),2);
                    double radIn = (Math.random()<0.3)?(0):(((arenaSize/2.0-edgeRemover)/2.0)*Math.random());
                    double radOut = (Math.random()<0.3)?(0):(radIn+(arenaSize/2.0-edgeRemover-radIn)*Math.random());
                    for(int x = 0; x<arenaSize; x++) {
                        for(int z = 0; z<arenaSize; z++) {
                            if(!inSquircle(x+0.5,z+0.5,arenaSize/2.0,arenaSize/2.0,radIn,powIn)&&
                                inSquircle(x+0.5,z+0.5,arenaSize/2.0,arenaSize/2.0,radOut+circleAdjustment(),powOut)) {
                                double chance = Math.random();
                                if(chance>=0* blockIncrement() &&chance<1* blockIncrement() &&x>0&&layer[x-1][1][z]!=Material.AIR) {
                                    layer[x][1][z] = layer[x-1][1][z];
                                }
                                else if(chance>=1* blockIncrement() &&chance<2* blockIncrement() &&z>0&&layer[x][1][z-1]!=Material.AIR) {
                                    layer[x][1][z] = layer[x][1][z-1];
                                }
                                else if(chance>=2* blockIncrement() &&chance<3* blockIncrement() &&x<layer.length-1&&layer[x+1][1][z]!=Material.AIR) {
                                    layer[x][1][z] = layer[x+1][1][z];
                                }
                                else if(chance>=3* blockIncrement() &&chance<4* blockIncrement() &&z<layer[0][0].length-1&&layer[x][1][z+1]!=Material.AIR) {
                                    layer[x][1][z] = layer[x][1][z+1];
                                }
                                else {
                                    layer[x][1][z] = palette[(int)(palette.length*Math.random())];
                                }
                            }
                        }
                    }
                    break;
                case "sprinkled blocks":
                    Material sprinkle = Math.random()>0.5?Material.END_ROD:Material.CARVED_PUMPKIN;
                    for(int x = 0; x<arenaSize; x++) {
                        for (int z = 0; z < arenaSize; z++) {
                            if(Math.random()<0.01&&layer[x][0][z]!=Material.AIR) {
                                layer[x][1][z] = sprinkle;
                            }
                        }
                    }
                    break;
            }

        }
        return layer;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        config = plug.getConfig();
        int startX = config.getInt("gameArea.start.x");
        int startZ = config.getInt("gameArea.start.z");
        int endX = config.getInt("gameArea.end.x");
        int endZ = config.getInt("gameArea.end.z");
        for(int y = -63; y<385; y++) {
            for (int x = startX - 6; x < endX + 6; x++) {
                for (int z = startZ - 6; z < endZ + 6; z++) {
                    plug.getServer().getWorld("world").getBlockAt(x, y, z).setType((x==startX-6||x==endX+5||z==startZ-6||z==endZ+5)?Material.BARRIER:Material.LIGHT);
                }
            }
        }
        int initY = -63+ spacing();
        int numLayers = config.getInt("layers.number");
        boolean special = config.getBoolean("layers.includeSpecialLayers");
        try {
            if(args.length>0) numLayers = Integer.parseInt(args[0]);
            if(args.length>1) special = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            return false;
        }
        for(int n = 0; n<numLayers; n++) {
            Material[][][] layer = makeLayer(Math.max(endX-startX,endZ-startZ),special,((double)n)/numLayers);
            for(int bump = 0; bump<2; bump++)
                for (int x = 0; x < endX-startX; x++) {
                    for (int z = 0; z <endZ-startZ; z++) {
                        plug.getServer().getWorld("world").getBlockAt(x+startX,initY+n*spacing() +bump,z+startZ).setType(layer[x][bump][z]);
                    }
                }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length==1)
            return List.of("1","3","5","8");
        else if(args.length==2)
            return List.of("true");
        return List.of();
    }
}

package com.tinyshellzz.InvManager.config;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import static com.tinyshellzz.InvManager.ObjectPool.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class PluginConfig {
    public static boolean debug;
    public static String db_host;
    public static int db_port;
    public static String db_user;
    public static String db_passwd;
    public static String db_database;
    public static Location loc_a;
    public static Location loc_b;
    public static Location min_loc;
    public static Location max_loc;
    public static Location start_loc;
    public static Location teleport_out_loc;
    public static double body_scale;
    public static int game_time;
    public static int hide_time;
    public static int prepare_time;
    private static final ConfigWrapper configWrapper = new ConfigWrapper(plugin, "config.yml");

    public static void reload() {
        configWrapper.reloadConfig(); // 重新加载配置文件

        YamlConfiguration yamlconfig = configWrapper.getConfig();
        debug = yamlconfig.getBoolean("debug");
        db_host = yamlconfig.getString("db_host");
        db_port = yamlconfig.getInt("db_port");
        db_user = yamlconfig.getString("db_user");
        db_passwd = yamlconfig.getString("db_passwd");
        db_database = yamlconfig.getString("db_database");
        loc_a = getLocationFromConfig("game_loc.a");
        loc_b = getLocationFromConfig("game_loc.b");
        body_scale = yamlconfig.getDouble("body_scale");
        game_time = yamlconfig.getInt("game_time");
        prepare_time = yamlconfig.getInt("prepare_time");
        hide_time = yamlconfig.getInt("hide_time");
        Bukkit.getConsoleSender().sendMessage(body_scale + "");
        start_loc = getLocationFromConfig("start_loc");
        teleport_out_loc = getLocationFromConfig("teleport_out_loc");
        start_loc.set(start_loc.getX() + 0.5, start_loc.getY() + 1.0, start_loc.getZ() + 0.5);
        World w = loc_a.getWorld();
        min_loc = new Location(w,
                min(loc_a.getX(), loc_b.getX()),
                min(loc_a.getY(), loc_b.getY()),
                min(loc_a.getZ(), loc_b.getZ()));

        max_loc = new Location(w,
                max(loc_a.getX(), loc_b.getX()),
                max(loc_a.getY(), loc_b.getY()),
                max(loc_a.getZ(), loc_b.getZ()));
    }

    private static Location getLocationFromConfig(String path) {
        double x = plugin.getConfig().getDouble(path + ".x");
        double y = plugin.getConfig().getDouble(path + ".y");
        double z = plugin.getConfig().getDouble(path + ".z");
        String worldName = plugin.getConfig().getString(path + ".world");

        return new Location(Bukkit.getWorld(worldName == null ? "world" : worldName), x, y, z);
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}

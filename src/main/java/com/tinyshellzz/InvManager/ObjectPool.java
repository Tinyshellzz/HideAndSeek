package com.tinyshellzz.InvManager;

import com.google.gson.Gson;
import com.tinyshellzz.InvManager.database.InstantKillAxeMapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class ObjectPool {
    public static Plugin plugin;
    public static Gson gson = new Gson();
    public static int started = 0;

    public static HashMap<UUID, Integer> players = new HashMap<UUID, Integer>();

    public static InstantKillAxeMapper instantKillAxeMapper;
}

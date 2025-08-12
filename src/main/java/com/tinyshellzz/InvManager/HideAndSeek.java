package com.tinyshellzz.InvManager;

import com.tinyshellzz.InvManager.command.HideAndSeekCommand;
import com.tinyshellzz.InvManager.config.PluginConfig;
import com.tinyshellzz.InvManager.database.InstantKillAxeMapper;
import com.tinyshellzz.InvManager.listener.*;
import com.tinyshellzz.InvManager.task.GameCountDown;
import com.tinyshellzz.InvManager.task.GameStartCountDown;
import com.tinyshellzz.InvManager.task.HideCountDown;
import com.tinyshellzz.InvManager.task.PlayerInRangeMonitor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import static com.tinyshellzz.InvManager.ObjectPool.instantKillAxeMapper;

public class HideAndSeek extends JavaPlugin {
    @Override
    public void onEnable() {
        // team,启动！
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[HideAndSeek]" + ChatColor.AQUA + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[HideAndSeek]" + ChatColor.AQUA + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[HideAndSeek]" + ChatColor.AQUA + "#HideAndSeek已启动#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[HideAndSeek]" + ChatColor.AQUA + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[HideAndSeek]" + ChatColor.AQUA + "######################");

        init();
        register();

        // 游戏开始, 检测玩家范围
        PlayerInRangeMonitor.run();
        GameCountDown.run();
        GameStartCountDown.run();
        HideCountDown.run();
    }

    public void init() {
        ObjectPool.plugin = this;
        PluginConfig.reload();

        instantKillAxeMapper = new InstantKillAxeMapper();
    }

    public void register() {
        // 注册监听器
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        // 注册命令
        this.getCommand("HideAndSeek").setExecutor(new HideAndSeekCommand());
    }

    @Override
    public void onDisable() {
        //TODO
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[NeoTccInv]" + ChatColor.RED + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[NeoTccInv]" + ChatColor.RED + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[NeoTccInv]" + ChatColor.RED + "#NeoTccInv已关闭#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[NeoTccInv]" + ChatColor.RED + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[NeoTccInv]" + ChatColor.RED + "######################");
    }
}

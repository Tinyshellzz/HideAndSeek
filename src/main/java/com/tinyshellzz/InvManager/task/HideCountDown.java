package com.tinyshellzz.InvManager.task;

import com.tinyshellzz.InvManager.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tinyshellzz.InvManager.ObjectPool.players;
import static com.tinyshellzz.InvManager.ObjectPool.started;

public class HideCountDown {
    static int hideCountDown = PluginConfig.hide_time;
    static BossBar bar = Bukkit.createBossBar(
            ChatColor.YELLOW + "找到你躲藏的位置 " + hideCountDown + "s",
            BarColor.YELLOW,
            BarStyle.SOLID
    );
    static int timeLeft;


    public static void run() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        bar.setVisible(true);
        timeLeft = hideCountDown;

        // 每秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
                    if (started != 2) return;

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        UUID uuid = player.getUniqueId();
                        Location loc = player.getLocation();

                        if (players.containsKey(uuid)) {
                            bar.addPlayer(player);  // Show it to the player
                        }
                    }

                    if (timeLeft <= 0) {
                        bar.removeAll();
                        started = 3;
                        GameCountDown.timeLeft = GameCountDown.gameTime;
                    } else {
                        bar.setTitle(ChatColor.YELLOW + "找到你躲藏的位置 " + timeLeft + "s");
                        bar.setProgress((double) timeLeft / hideCountDown);

                        timeLeft--;
                    }
                },
                0,
                1,
                TimeUnit.SECONDS);
    }
}

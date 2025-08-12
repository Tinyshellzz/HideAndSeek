package com.tinyshellzz.InvManager.task;

import com.tinyshellzz.InvManager.config.PluginConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tinyshellzz.InvManager.ObjectPool.players;
import static com.tinyshellzz.InvManager.ObjectPool.started;

public class GameStartCountDown {
    public static int gameStartCountDown = 10;
    public static BossBar bar = Bukkit.createBossBar(
            ChatColor.YELLOW + "距离游戏开始还有 " + gameStartCountDown + "s",
            BarColor.YELLOW,
            BarStyle.SOLID
    );
    public static int timeLeft;


    public static void run() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        bar.setVisible(true);
        timeLeft = gameStartCountDown;

        // 每秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
                    try {
                        if (started != 1) return;

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            UUID uuid = player.getUniqueId();

                            if (players.containsKey(uuid)) {
                                bar.addPlayer(player);  // Show it to the player

                            }
                        }

                        if (timeLeft <= 0) {
                            bar.removeAll();
                            started = 2;
                            HideCountDown.timeLeft = HideCountDown.hideCountDown;
                        } else {
                            bar.setTitle(ChatColor.YELLOW + "距离游戏开始还有 " + timeLeft + "s");
                            bar.setProgress((double) timeLeft / gameStartCountDown);

                            timeLeft--;
                        }
                    } catch (RuntimeException e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String sStackTrace = sw.toString();
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + sStackTrace);
                    }
                },
                0,
                1,
                TimeUnit.SECONDS);
    }
}

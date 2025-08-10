package com.tinyshellzz.InvManager.task;

import com.tinyshellzz.InvManager.config.PluginConfig;
import com.tinyshellzz.InvManager.core.BodySizeChanger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tinyshellzz.InvManager.ObjectPool.players;
import static com.tinyshellzz.InvManager.ObjectPool.started;
import static com.tinyshellzz.InvManager.task.PlayerInRangeMonitor.inArea;

public class GameCountDown {
    static int gameTime = PluginConfig.game_time;
    static BossBar bar = Bukkit.createBossBar(
            ChatColor.YELLOW + "countdown " + gameTime + "s",
            BarColor.YELLOW,
            BarStyle.SOLID
    );
    static int timeLeft = 120;


    public static void run() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        bar.setVisible(true);
        timeLeft = gameTime;


        // 每秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
                    if (started != 3) return;

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        UUID uuid = player.getUniqueId();
                        Location loc = player.getLocation();

                        if (players.containsKey(uuid)) {
                            bar.addPlayer(player);  // Show it to the player
                        }
                    }

                    if (timeLeft <= 0) {
                        bar.removeAll();
                        for(UUID u: players.keySet()) {
                            Player p = Bukkit.getPlayer(u);
                            if(p != null) {
                                BodySizeChanger.changeBodySize(p, 1);
                                p.setGlowing(false);
                            }
                        }
                        players.clear();

                        started = 0;    // 游戏结束
                    } else {
                        bar.setTitle(ChatColor.YELLOW + "距离游戏结束还有 " + timeLeft + "s");
                        bar.setProgress((double) timeLeft / gameTime);

                        timeLeft--;
                    }

                },
                0,
                1,
                TimeUnit.SECONDS);
    }
}

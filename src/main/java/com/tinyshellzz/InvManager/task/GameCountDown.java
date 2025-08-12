package com.tinyshellzz.InvManager.task;

import com.tinyshellzz.InvManager.config.PluginConfig;
import com.tinyshellzz.InvManager.core.BodySizeChanger;
import com.tinyshellzz.InvManager.core.GameManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tinyshellzz.InvManager.ObjectPool.players;
import static com.tinyshellzz.InvManager.ObjectPool.started;
import static com.tinyshellzz.InvManager.task.PlayerInRangeMonitor.inArea;

public class GameCountDown {
    public static int gameTime = 120;
    public static BossBar bar = Bukkit.createBossBar(
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
                    try {
                        if (started != 3) return;

                        int seekerNum = 0, hiderNum = 0;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            UUID uuid = player.getUniqueId();

                            if (players.containsKey(uuid)) {
                                if (players.get(player.getUniqueId()) == 1) {
                                    seekerNum++;
                                } else hiderNum++;
                                bar.addPlayer(player);  // Show it to the player
                            }
                        }

                        if (!PluginConfig.debug && hiderNum == 0) {
                            GameManager.gameFinished(1);
                        } else if (seekerNum == 0) {
                            GameManager.gameFinished(0);
                        }

                        if (timeLeft <= 0) {
                            GameManager.gameFinished(0);
                        } else {
                            bar.setTitle(ChatColor.YELLOW + "距离游戏结束还有 " + timeLeft + "s");
                            bar.setProgress((double) timeLeft / gameTime);

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

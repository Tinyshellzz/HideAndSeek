package com.tinyshellzz.InvManager.task;

import com.tinyshellzz.InvManager.ObjectPool;
import com.tinyshellzz.InvManager.core.BodySizeChanger;
import com.tinyshellzz.InvManager.core.GameManager;
import com.tinyshellzz.InvManager.utils.MyUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import static com.tinyshellzz.InvManager.config.PluginConfig.max_loc;
import static com.tinyshellzz.InvManager.config.PluginConfig.min_loc;

public class PlayerInRangeMonitor {
    private static final HashMap<UUID, Integer> countdownMap = new HashMap<>();
    public static int timeLimit = 20;

    public static void run() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 每秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
                    try {
                        if (started == 0) return;

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            UUID uuid = player.getUniqueId();
                            Location loc = player.getLocation();
                            if (players.containsKey(uuid)) {
                                if (!inArea(loc)) {
                                    // Player is outside the area
                                    int remaining = countdownMap.getOrDefault(uuid, timeLimit);

                                    if (remaining > 0) {
                                        // Show countdown title
                                        player.sendTitle(
                                                "⚠️ 请返回到游戏区域!",
                                                "你还剩 " + remaining + " 秒!",
                                                0, 25, 5
                                        );
                                        countdownMap.put(uuid, remaining - 1);
                                    } else {
                                        players.remove(uuid);
                                        player.sendTitle("⛔ You failed to return!", "", 10, 60, 10);
                                        BodySizeChanger.changeBodySize(player, 1);
                                        GameStartCountDown.bar.removePlayer(player);
                                        HideCountDown.bar.removePlayer(player);
                                    }
                                } else {
                                    // Player is inside the area
                                    if (countdownMap.containsKey(uuid)) {
                                        countdownMap.remove(uuid);
                                        player.resetTitle(); // Clear the title
                                    }

                                    // prevent Gilding (禁止鞘翅飞行)
                                    if (started != 0 && player.isGliding()) {
                                        player.setGliding(false);
                                        player.sendMessage(ChatColor.RED + "游戏过程中该区域禁飞!");
                                    }
                                }
                            } else {
                                if (inArea(loc)) {
                                    // 把玩家传送走
                                    GameManager.teleportOut(player);
                                }
                            }
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

    public static boolean inArea(Location loc) {
        return loc.getWorld().equals(min_loc.getWorld())
                && (min_loc.getX() <= loc.getX()
                && min_loc.getY() <= loc.getY()
                && min_loc.getZ() <= loc.getZ()
                && max_loc.getX() >= loc.getX()
                && max_loc.getY() >= loc.getY()
                && max_loc.getZ() >= loc.getZ());
    }
}

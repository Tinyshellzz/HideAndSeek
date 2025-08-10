package com.tinyshellzz.InvManager.listener;

import com.tinyshellzz.InvManager.ObjectPool;
import com.tinyshellzz.InvManager.core.BodySizeChanger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.tinyshellzz.InvManager.ObjectPool.started;
import static com.tinyshellzz.InvManager.task.PlayerInRangeMonitor.inArea;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BodySizeChanger.changeBodySize(player, 1);
        player.setGlowing(false);
        // 游戏中，登录位置在游戏区域，要传送出去
        Location loc = player.getLocation();
        if(started != 0 && inArea(loc)) {
            player.teleport(loc);
        }
    }
}

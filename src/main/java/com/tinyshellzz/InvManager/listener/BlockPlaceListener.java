package com.tinyshellzz.InvManager.listener;

import com.tinyshellzz.InvManager.config.PluginConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.tinyshellzz.InvManager.task.PlayerInRangeMonitor.inArea;


public class BlockPlaceListener implements Listener {
    @EventHandler
    public void handle(BlockPlaceEvent event){
        Player p = event.getPlayer();
        if (p.hasPermission("group.op")) {
            // 如果玩家拥有 "group.op" 权限组，跳出
            return;
        }

        Location location = event.getBlock().getLocation();
        if(PluginConfig.game_area_protection && inArea(location)) {
            event.setCancelled(true);
        }
    }
}

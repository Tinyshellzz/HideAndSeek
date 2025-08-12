package com.tinyshellzz.spawnpointprotect.lisenter;
import com.tinyshellzz.spawnpointprotect.config.Config;
import com.tinyshellzz.spawnpointprotect.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;



public class BlockBreakListener implements Listener {
    @EventHandler
    public void handle(BlockBreakEvent event){
        Player p = event.getPlayer();
        if (p.hasPermission("group.op")) {
            // 如果玩家拥有 "group.op" 权限组，跳出
            return;
        }

        if (Config.isPlayerWhitelisted(p.getUniqueId())){
            return; // 如果在白名单中，跳出
        }

        int playTime = p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000; // 获取玩家的总游玩时长，单位为小时
        if (playTime > 100) return;     // 游玩时间大于100小时，跳出

        Location loc = p.getLocation();
        if (loc.getWorld().getName().equals("world")){
            if (Tools.inArea(loc, Config.loc_a, Config.loc_b)) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.RED + Config.Message_world);
            } return;
        }
        if (loc.getWorld().getName().equals("world_nether")){
            if (Tools.inArea(loc, Config.nether_loc_a, Config.nether_loc_b)) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.RED + Config.Message_world_nether);
            }
        }

    }
}

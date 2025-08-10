package com.tinyshellzz.InvManager.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.meta.ItemMeta;

import static com.tinyshellzz.InvManager.ObjectPool.players;
import static com.tinyshellzz.InvManager.task.PlayerInRangeMonitor.inArea;

public class PlayerItemDamageListener implements Listener {
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        if(inArea(loc)) {
            if(players.get(player.getUniqueId()) == 1) {
                // 如果被消耗的物品是秒人斧
                ItemMeta itemMeta = event.getItem().getItemMeta();
                if(itemMeta != null && itemMeta.hasDisplayName()) {
                    String customName = itemMeta.getDisplayName();
                    if(customName.equals("秒人斧")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}

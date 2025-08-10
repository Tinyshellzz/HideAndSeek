package com.tinyshellzz.InvManager.listener;

import com.tinyshellzz.InvManager.config.PluginConfig;
import com.tinyshellzz.InvManager.core.BodySizeChanger;
import com.tinyshellzz.InvManager.core.GameManager;
import com.tinyshellzz.InvManager.utils.MyUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.tinyshellzz.InvManager.ObjectPool.players;

public class PlayerDeathEventListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if(players.containsKey(uuid)) {
            if(players.get(uuid) != 0) {
                players.put(uuid, 1);
                MyUtil.teleport(player, PluginConfig.start_loc);

                GameManager.setAsSeeker(player);
            }
            event.setCancelled(true);
        }
    }
}

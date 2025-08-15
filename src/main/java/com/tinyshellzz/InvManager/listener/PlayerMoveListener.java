package com.tinyshellzz.InvManager.listener;

import com.tinyshellzz.InvManager.core.BodySizeChanger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.tinyshellzz.InvManager.ObjectPool.players;
import static com.tinyshellzz.InvManager.ObjectPool.started;
import static com.tinyshellzz.InvManager.core.GameManager.isSeeker;
import static com.tinyshellzz.InvManager.task.PlayerInRangeMonitor.inArea;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(started == 1) {
            if (players.containsKey(event.getPlayer().getUniqueId())) {
                Location from = event.getFrom();
                Location eventTo = event.getTo();
                if(from.getX() != eventTo.getX() || from.getZ() != eventTo.getZ() || from.getZ() != eventTo.getZ()) {
                    event.setCancelled(true);
                }
            }
        } else if (started == 2) {
            Player p = event.getPlayer();
            if(isSeeker(p)) {
                Location from = event.getFrom();
                Location eventTo = event.getTo();
                if(from.getX() != eventTo.getX() || from.getZ() != eventTo.getZ() || from.getZ() != eventTo.getZ()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}

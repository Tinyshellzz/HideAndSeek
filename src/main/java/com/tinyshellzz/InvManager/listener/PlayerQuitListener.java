package com.tinyshellzz.InvManager.listener;

import com.tinyshellzz.InvManager.HideAndSeek;
import com.tinyshellzz.InvManager.core.BodySizeChanger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.tinyshellzz.InvManager.ObjectPool.players;

public class PlayerQuitListener  implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        players.remove(p.getUniqueId());
    }
}

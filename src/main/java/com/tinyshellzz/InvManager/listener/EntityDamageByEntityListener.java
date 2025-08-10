package com.tinyshellzz.InvManager.listener;

import com.tinyshellzz.InvManager.config.PluginConfig;
import com.tinyshellzz.InvManager.core.GameManager;
import com.tinyshellzz.InvManager.utils.MyUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

import static com.tinyshellzz.InvManager.ObjectPool.players;

public class EntityDamageByEntityListener implements Listener {
    @EventHandler
    public void onPlayerMeleeHit(EntityDamageByEntityEvent event) {
        // Make sure the damaged entity is a player
        if (!(event.getEntity() instanceof Player victim)) return;

        // Only allow attacker to be a player (not arrows, snowballs, etc.)
        if (!(event.getDamager() instanceof Player attacker)) return;

        // Only melee — ignore arrows, potions, tridents, etc.
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }

        UUID attacker_id = attacker.getUniqueId();
        UUID victim_id = victim.getUniqueId();

        if (players.containsKey(attacker_id) && players.containsKey(victim_id)) {
            if (players.get(attacker_id) == 1 && players.get(victim_id) == 0) {
                players.put(victim_id, 1);
                MyUtil.teleport(victim, PluginConfig.start_loc);

                GameManager.setAsSeeker(victim);
                int[] seekerAndHiderNum = GameManager.getSeekerAndHiderNum();
                int seekerNum = seekerAndHiderNum[0];
                int hiderNum = seekerAndHiderNum[1];

                String killerName = attacker.getName();
                GameManager.notifyAllPlayers(ChatColor.AQUA + victim.getName() + ChatColor.RESET + "被" + ChatColor.AQUA + killerName + ChatColor.RESET + "抓住，" + "还剩 " + hiderNum + " 个猫，" + "还剩 " + seekerNum + "个捉猫人");
            }

        }
    }
}

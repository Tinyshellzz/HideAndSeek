package com.tinyshellzz.InvManager.core;

import com.tinyshellzz.InvManager.ObjectPool;
import com.tinyshellzz.InvManager.config.PluginConfig;
import com.tinyshellzz.InvManager.task.GameCountDown;
import com.tinyshellzz.InvManager.task.GameStartCountDown;
import com.tinyshellzz.InvManager.task.HideCountDown;
import com.tinyshellzz.InvManager.utils.ItemStackBase64Converter;
import com.tinyshellzz.InvManager.utils.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static com.tinyshellzz.InvManager.ObjectPool.*;
import static com.tinyshellzz.InvManager.config.PluginConfig.*;
import static com.tinyshellzz.InvManager.task.PlayerInRangeMonitor.inArea;

public class GameManager {
    public static ItemStack instantKillAxe = null;
    static int distance = 2;

    public static ItemStack getInstantKillAxe() {
        if (instantKillAxe == null) {
            String string = instantKillAxeMapper.get();
            if (string != null) {
                instantKillAxe = ItemStackBase64Converter.itemStackFromBase64(string);
            }
        }

        return instantKillAxe;
    }

    public static void notifyAllPlayers(String message) {
        for(UUID uuid: players.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) {
                player.sendMessage(message);
            }
        }
    }

    public static void notifyAllPlayersByTitle(String message) {
        for(UUID uuid: players.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) {
                player.sendTitle(message, "", 10, 60, 10);
            }
        }
    }

    public static int[] getSeekerAndHiderNum(){
        int seeker = 0;
        int hider = 0;
        for(UUID uuid: players.keySet()){
            if(players.get(uuid) == 1) {
                seeker++;
            } else hider++;
        }

        return new int[]{seeker, hider};
    }

    public static void setAsSeeker(Player seeker) {
        /**
        // 检测是否已拥有秒人斧
        if(MyUtil.playerHasNamedItem(seeker, "秒人斧", true)) {
            seeker.sendMessage("你已拥有秒人斧, 无法再获得秒人斧");
        }

        // 给一把秒人斧
        ItemStack instantKillAxe = GameManager.getInstantKillAxe();
        if(instantKillAxe != null) {
            seeker.getInventory().addItem(instantKillAxe);
        }
         */

        seeker.setGlowing(true);
        BodySizeChanger.changeBodySize(seeker, 1);
        players.put(seeker.getUniqueId(), 1);  // 标记为 Seeker
        seeker.sendTitle("你是捉猫人", "", 10, 60, 10);
    }

    public static void gameFinished(int winningSide) {
        if(winningSide != -1) {
            if (winningSide == 1){
                notifyAllPlayersByTitle("捉猫人 胜利！");

                notifyAllPlayers("捉猫人：" + getPlayerList(1));
            } else {
                notifyAllPlayersByTitle("猫 胜利！");

                notifyAllPlayers("猫：" + getPlayerList(0));
            }
        }


        for(UUID u: players.keySet()) {
            Player p = Bukkit.getPlayer(u);
            if(p != null) {
                BodySizeChanger.changeBodySize(p, 1);
                p.setGlowing(false);
            }
        }
        started = 0;
        GameCountDown.bar.removeAll();
        GameStartCountDown.bar.removeAll();
        HideCountDown.bar.removeAll();
        players.clear();
    }

    public static String getPlayerList(int identity) {
        StringBuilder playerList = new StringBuilder();
        for(UUID uuid: players.keySet()){
            if(players.get(uuid) == identity) {
                Player player = Bukkit.getPlayer(uuid);
                if(player != null) {
                    playerList.append(player.getName());
                    playerList.append(", ");
                }
            }
        }
        int len = playerList.length();
        if(len > 0) playerList.delete(len-2, len);
        return playerList.toString();
    }

    public static boolean start() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            Location loc = player.getLocation();
            if (inArea(loc)) {
                players.put(uuid, 0);
            }
        }

        if(!debug && players.size() < 3) {

            players.clear();
            return false;
        }

        int seeker_ = new Random().nextInt(players.size());
        Player seeker = null;
        int idx = 0;
        for(UUID p: players.keySet()) {
            if(idx == seeker_) {
                seeker = Bukkit.getPlayer(p);
                Bukkit.getConsoleSender().sendMessage("已设置完seeker");

                if(seeker != null) {
                    setAsSeeker(seeker);
                }
                break;
            }
            idx++;
        }

        LinkedList<Location> vec = new LinkedList<>();
        HashSet<Location> visited = new HashSet<>();
        visited.add(start_loc);
        if(seeker != null) {
            MyUtil.teleport(seeker, start_loc);
        }
        Location last = start_loc;
        Location a = new Location(last.getWorld(), last.getBlockX()-distance, last.getBlockY(), last.getBlockZ());
        if(!visited.contains(a)) {
            vec.addFirst(a);
            visited.add(a);
        }
        Location b = new Location(last.getWorld(), last.getBlockX()+distance, last.getBlockY(), last.getBlockZ());
        if(!visited.contains(b)) {
            vec.addFirst(b);
            visited.add(b);
        }
        Location c = new Location(last.getWorld(), last.getBlockX(), last.getBlockY(), last.getBlockZ()-distance);
        if(!visited.contains(c)) {
            vec.addFirst(c);
            visited.add(c);
        }
        Location d = new Location(last.getWorld(), last.getBlockX(), last.getBlockY(), last.getBlockZ()+distance);
        if(!visited.contains(d)) {
            vec.addFirst(d);
            visited.add(d);
        }
        // 将所有游戏玩家都放到起点
        for(UUID p: players.keySet()) {
            if(players.get(p) != 1) {
                last = vec.getLast();
                vec.removeLast();

                Player p1 = Bukkit.getPlayer(p);
                if (p1 != null) {
                    MyUtil.teleport(p1, last);
                    BodySizeChanger.changeBodySize(p1, body_scale);
                }

                a = new Location(last.getWorld(), last.getBlockX() - distance, last.getBlockY(), last.getBlockZ());
                if (!visited.contains(a)) {
                    vec.addFirst(a);
                    visited.add(a);
                }
                b = new Location(last.getWorld(), last.getBlockX() + distance, last.getBlockY(), last.getBlockZ());
                if (!visited.contains(b)) {
                    vec.addFirst(b);
                    visited.add(b);
                }
                c = new Location(last.getWorld(), last.getBlockX(), last.getBlockY(), last.getBlockZ() - distance);
                if (!visited.contains(c)) {
                    vec.addFirst(c);
                    visited.add(c);
                }
                d = new Location(last.getWorld(), last.getBlockX(), last.getBlockY(), last.getBlockZ() + distance);
                if (!visited.contains(d)) {
                    vec.addFirst(d);
                    visited.add(d);
                }

            }
        }

        // 开始游戏倒计时
        GameStartCountDown.timeLeft = GameStartCountDown.gameStartCountDown;
        started = 1;

        return true;
    }

    public static boolean isSeeker(Player player) {
        if(!players.containsKey(player.getUniqueId())) return false;
        return players.get(player.getUniqueId()) == 1;
    }

    public static void teleportOut(Player player) {
        Location curLoc = player.getLocation();
        double x_mx = max_loc.getX();
        double z_mx = max_loc.getZ();
        double x_mn = min_loc.getX();
        double z_mn = min_loc.getZ();
        double min_distance = x_mx-curLoc.getX();
        double res_x = x_mn + 16, res_z = curLoc.getZ();

        if(min_distance > z_mx-curLoc.getY()) {
            min_distance = z_mx-curLoc.getY();
            res_x = curLoc.getX();
            res_z = z_mx+16;
        }
        if(min_distance > curLoc.getX()-x_mn) {
            min_distance = curLoc.getX()-x_mn;
            res_x = x_mn-16;
            res_z = curLoc.getY();
        }
        if(min_distance > curLoc.getY()-z_mn) {
            min_distance = curLoc.getY()-z_mn;
            res_x = curLoc.getX();
            res_z = z_mn-16;
        }


        // 可以传到 (res_x, highestY, res_z);
        if(teleport_out_loc != null && !inArea(teleport_out_loc)) {MyUtil.teleport(player, teleport_out_loc);}
        else MyUtil.teleport(player, new Location(curLoc.getWorld(), res_x, 250, res_z));

        player.sendMessage("游戏进行中, 无法进入该区域");
    }
}

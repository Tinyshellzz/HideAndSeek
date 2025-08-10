package com.tinyshellzz.InvManager.service;

import com.tinyshellzz.InvManager.config.PluginConfig;
import com.tinyshellzz.InvManager.core.GameManager;
import com.tinyshellzz.InvManager.database.InstantKillAxeMapper;
import com.tinyshellzz.InvManager.utils.ItemStackBase64Converter;
import com.tinyshellzz.InvManager.utils.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.tinyshellzz.InvManager.ObjectPool.instantKillAxeMapper;
import static com.tinyshellzz.InvManager.ObjectPool.started;
import static com.tinyshellzz.InvManager.task.PlayerInRangeMonitor.inArea;
import static com.tinyshellzz.InvManager.utils.ItemStackBase64Converter.itemStackToBase64;

public class HideAndSeekService {
    public static boolean start(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(started != 0) {
            sender.sendMessage("游戏正在进行中，无法开始");
            return true;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("只有玩家才能执行该命令");
            return true;
        }
        Player player = (Player) sender;
        Location location = player.getLocation();
        if(!inArea(location)) {
            sender.sendMessage("你不在游戏区域，无法执行该命令");
            return true;
        }

        if(!GameManager.start()) {
            sender.sendMessage("人数不足三人，无法开始游戏");
        }
        sender.sendMessage("游戏已开始");

        return true;
    }

    public static boolean save_Axe(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("只有玩家才能执行该命令");
            return true;
        }

        Player p = (Player) sender;
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        String str = itemStackToBase64(itemInMainHand);
        instantKillAxeMapper.insert(str);

        return true;
    }
}

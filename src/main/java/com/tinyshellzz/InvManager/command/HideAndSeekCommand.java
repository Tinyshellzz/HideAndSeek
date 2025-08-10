package com.tinyshellzz.InvManager.command;

import com.tinyshellzz.InvManager.config.PluginConfig;
import com.tinyshellzz.InvManager.service.HideAndSeekService;
import com.tinyshellzz.InvManager.utils.MyUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HideAndSeekCommand  implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "参数不足");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        Matcher _m;
        switch (subcommand) {
            case "reload":
                _m = Pattern.compile("^.*CraftRemoteConsoleCommandSender.*$").matcher(sender.toString());
                if (!(sender instanceof ConsoleCommandSender || _m.find() || sender.isOp())) {
                    sender.sendMessage("只有控制台和op才能使用该命令");
                    return true;
                }
                PluginConfig.reload();
                return true;
            case "save秒人斧":
                _m = Pattern.compile("^.*CraftRemoteConsoleCommandSender.*$").matcher(sender.toString());
                if (!(sender instanceof ConsoleCommandSender || _m.find() || sender.isOp())) {
                    sender.sendMessage("只有控制台和op才能使用该命令");
                    return true;
                }

                return HideAndSeekService.save_Axe(sender, command, s, args);
            case "start":
                return HideAndSeekService.start(sender, command, s, args);
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        } else if (args.length == 1) {
            String input = args[0].toLowerCase();

            if(sender.isOp() || sender.hasPermission("HideAndSeek.use")) {
                return MyUtil.tabComplete(Arrays.asList("start", "reload", "save秒人斧"), input);
            } else {
                return MyUtil.tabComplete(Arrays.asList("start"), input);
            }
        }

        return null;
    }
}

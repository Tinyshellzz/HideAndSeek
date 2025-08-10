package com.tinyshellzz.InvManager.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tinyshellzz.InvManager.ObjectPool.plugin;

public class MyUtil {
    private static final Pattern colorPattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

    /**
     *
     * @param colorCode 以'#'开头的 rgb 颜色代码
     * @return
     */
    public static String msgColor(String colorCode) {
        Matcher matcher = colorPattern.matcher(colorCode);

        String colorStr = "";
        if (matcher.find()) {
            // 将 RGB 转换为 & 颜色
            colorStr = ChatColor.of(colorCode).toString();
        }

        // 将 & 颜色显示
        return ChatColor.translateAlternateColorCodes('&', colorStr);
    }

    /**
     *
     * @param msg 含有'&'开头的颜色代码
     * @return
     */
    public static String msgColor2(String msg) {
        // 将 & 颜色显示
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * 依据input，从list中补全命令
     * @return
     */
    public static ArrayList<String> tabComplete(List<String> list, String input) {
        input = input.trim().toLowerCase();
        ArrayList<String> ret = new ArrayList<>();
        for(String str: list) {
            if(str.toLowerCase().startsWith(input)) {
                ret.add(str);
            }
        }

        return ret;
    }

    public static void closeInventory(Inventory inventory) {
        // Copy to avoid ConcurrentModificationException
        List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());

        for (HumanEntity viewer : viewers) {
            if (viewer instanceof Player p) {
                p.closeInventory();
            }
        }
    }

    public static void teleport(Player player, Location location) {
        // 播放瞬间移动声音效果，增强玩家体验
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);

        try {
            Class<?> pluginMetaClass = Class.forName("io.papermc.paper.plugin.configuration.PluginMeta");
            pluginMetaClass.getMethod("isFoliaSupported");

            // 该平台是folia，调用folia传送方法
            player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        } catch (ClassNotFoundException e) {
            // 该平台是bukkit, 调用bukkit传送方法
            player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        } catch (NoSuchMethodException e) {
            // 该平台是paper, 调用paper传送方法 (与bukkit一样)
            player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @Deprecated
    public static void teleportSafe(Player player, World world, int x, int z) {
        try {
            Class<?> pluginMetaClass = Class.forName("io.papermc.paper.plugin.configuration.PluginMeta");
            pluginMetaClass.getMethod("isFoliaSupported");

            // 该平台是folia
            Bukkit.getRegionScheduler().execute(plugin, world, x, z, () -> {
                int highestY = world.getHighestBlockYAt(x, z);
                Location safeLoc = new Location(world, x + 0.5, highestY+1, z + 0.5);
                player.teleportAsync(safeLoc); // Teleports within correct region
            });
        } catch (ClassNotFoundException e) {
            // 该平台是bukkit

        } catch (NoSuchMethodException e) {
            // 该平台是paper

        }
    }

    public static boolean isOnline(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null;
    }

    public boolean isPlaceHolderAPIInstalled() {
        Plugin placeholderAPI = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI");
        return placeholderAPI != null && placeholderAPI.isEnabled();
    }

    /**
     * Recursively search an inventory for an item with the given display name.
     * maxDepth prevents infinite recursion for malicious items.
     */
    private static boolean inventoryContainsNamed(Inventory inv, String targetName, boolean stripColor, int maxDepth) {
        if (inv == null || maxDepth <= 0) return false;

        for (ItemStack stack : inv.getContents()) {
            if (itemStackContainsNamed(stack, targetName, stripColor, maxDepth - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if player has an item whose display name equals targetName (optionally strip color)
     * in their inventory (including shulker boxes).
     *
     * @param player the player
     * @param targetName the display name to look for
     * @param stripColor whether to strip color codes (ChatColor) before comparing
     * @return true if found
     */
    public static boolean playerHasNamedItem(Player player, String targetName, boolean stripColor) {
        // search main inventory, armor and offhand
        if (inventoryContainsNamed(player.getInventory(), targetName, stripColor, 5)) {
            return true;
        }

        // optionally also check ender chest:
        if (inventoryContainsNamed(player.getEnderChest(), targetName, stripColor, 5)) return true;

        return false;
    }

    /**
     * Check a single ItemStack. If it is a shulker box (BlockStateMeta), open its block state and
     * search inside recursively.
     */
    private static boolean itemStackContainsNamed(ItemStack stack, String targetName, boolean stripColor, int depthLeft) {
        if (stack == null) return false;
        if (stack.getType() == Material.AIR) return false;

        ItemMeta meta = stack.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            String display = meta.getDisplayName();
            if (stripColor) display = ChatColor.stripColor(display);
            if (display.equals(targetName)) return true;
            // optionally: equalsIgnoreCase or contains: display.equalsIgnoreCase(targetName)
        }

        // If the item is a shulker box item, its meta will be BlockStateMeta with a ShulkerBox block state.
        if (depthLeft > 0 && meta instanceof BlockStateMeta) {
            BlockStateMeta bsm = (BlockStateMeta) meta;
            BlockState state = bsm.getBlockState();
            if (state instanceof ShulkerBox) {
                ShulkerBox box = (ShulkerBox) state;
                Inventory boxInv = box.getInventory();
                if (boxInv != null && inventoryContainsNamed(boxInv, targetName, stripColor, depthLeft - 1)) {
                    return true;
                }
            }
        }

        return false;
    }
}


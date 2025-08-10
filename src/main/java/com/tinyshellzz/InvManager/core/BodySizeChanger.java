package com.tinyshellzz.InvManager.core;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;

public class BodySizeChanger {
    public static void changeBodySize(Player p, double scale) {
        AttributeInstance attribute = p.getAttribute(Attribute.valueOf("GENERIC_SCALE"));
        if (attribute == null) return;
        attribute.setBaseValue(scale);
    }
}

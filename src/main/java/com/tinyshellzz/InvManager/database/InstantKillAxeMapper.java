package com.tinyshellzz.InvManager.database;

import com.tinyshellzz.InvManager.config.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstantKillAxeMapper {
    public InstantKillAxeMapper() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS instant_kill_axe (" +
                    "id Tinyint," +
                    "contents LONGTEXT," +
                    "UNIQUE KEY (id)" +
                    ") ENGINE=InnoDB CHARACTER SET=utf8;");
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[HideAndSeek]InstantKillAxeMapper:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public String get() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        String ret = null;

        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM instant_kill_axe where id = 1");
            rs = stmt.executeQuery();

            if(rs.next()) {
                ret = rs.getString(2);
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[HideAndSeek]InstantKillAxeMapper.get:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return ret;
    }

    public void insert(String Axe) {
        if(get() != null) {
            update(Axe);
            return;
        }

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("INSERT INTO instant_kill_axe VALUES (1, ?)");
            stmt.setString(1, Axe);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[HideAndSeek]InstantKillAxeMapper.insert:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

    }

    public void update(String Axe) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE instant_kill_axe SET contents = ? WHERE id = 1");
            stmt.setString(1, Axe);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[HideAndSeek]InstantKillAxeMapper.update:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }
}

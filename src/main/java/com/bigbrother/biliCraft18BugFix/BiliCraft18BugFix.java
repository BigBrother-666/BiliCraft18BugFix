package com.bigbrother.biliCraft18BugFix;

import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bigbrother.biliCraft18BugFix.commands.CommandReload;
import com.bigbrother.biliCraft18BugFix.listener.OraxenFurniturePlaceListener;
import com.bigbrother.biliCraft18BugFix.listener.ResidenceListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class BiliCraft18BugFix extends JavaPlugin {
    public static FileConfiguration config;
    public static Logger logger;
    public static Boolean factionsEnabled = false;
    public static Boolean residenceEnabled = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveResource("config.yml", /* replace */ false);
        config = getConfig();
        logger = getLogger();

        hook();

        new CommandReload(this);
    }


    private void hook() {
        if (Bukkit.getPluginManager().isPluginEnabled("Oraxen")) {
            Bukkit.getPluginManager().registerEvents(new OraxenFurniturePlaceListener(), this);
            logger.log(Level.INFO, "Oraxen家具放置监听器注册成功");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
            logger.log(Level.INFO, "检测到Factions插件");
            factionsEnabled = true;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Residence")) {
            logger.log(Level.INFO, "检测到Residence插件");
            residenceEnabled = true;
            FlagPermissions.addFlag("aspeed1");
            Bukkit.getPluginManager().registerEvents(new ResidenceListener(), this);
            logger.log(Level.INFO, "注册Residence flag: aspeed1 成功");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
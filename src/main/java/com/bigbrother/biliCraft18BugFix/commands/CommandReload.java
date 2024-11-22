package com.bigbrother.biliCraft18BugFix.commands;

import com.bigbrother.biliCraft18BugFix.BiliCraft18BugFix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class CommandReload implements CommandExecutor {
    private final BiliCraft18BugFix plugin;

    public CommandReload(BiliCraft18BugFix plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("bugfix").setExecutor(this);
        this.plugin.getCommand("bugfix").setTabCompleter(new CommandReloadTabCompleter());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length > 0 && args[0].equals("reload") && commandSender.hasPermission("bugfix.reload")) {
            plugin.reloadConfig();
            BiliCraft18BugFix.config = plugin.getConfig();
            BiliCraft18BugFix.logger.log(Level.INFO, "重载配置文件成功");
            if (commandSender instanceof Player) {
                commandSender.sendMessage(Component.text("重载配置文件成功", NamedTextColor.GREEN));
            }
            return true;
        } else {
            commandSender.sendMessage(Component.text("指令格式错误", NamedTextColor.RED));
        }
        return false;
    }
}

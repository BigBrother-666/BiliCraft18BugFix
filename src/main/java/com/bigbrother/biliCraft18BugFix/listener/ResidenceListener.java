package com.bigbrother.biliCraft18BugFix.listener;

import com.bekvon.bukkit.residence.ConfigManager;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.bekvon.bukkit.residence.event.ResidenceDeleteEvent;
import com.bekvon.bukkit.residence.event.ResidenceFlagChangeEvent;
import com.bekvon.bukkit.residence.event.ResidenceOwnerChangeEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bigbrother.biliCraft18BugFix.BiliCraft18BugFix;
import dev.aurelium.auraskills.api.event.user.UserLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ResidenceListener implements Listener {
    private final BiliCraft18BugFix plugin;

    public ResidenceListener(BiliCraft18BugFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onResidenceChanged(ResidenceChangedEvent event) {
        ClaimedResidence newRes = event.getTo();
        ClaimedResidence oldRes = event.getFrom();
        Player player = event.getPlayer();
        checkSpecialFlags(player, newRes, oldRes);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onResidenceDeleteEvent(ResidenceDeleteEvent event) {
        ConfigManager configManager = Residence.getInstance().getConfigManager();
        ClaimedResidence res = event.getResidence();
        if (res.getPermissions().has("aspeed1", FlagPermissions.FlagCombo.OnlyTrue))
            for (Player one : event.getResidence().getPlayersInResidence()) {
                one.setWalkSpeed(Math.max(one.getWalkSpeed() - configManager.getWalkSpeed1().floatValue(), 0.2F));
            }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFlagChangeASpeed(ResidenceFlagChangeEvent event) {
        if (!event.getFlag().equalsIgnoreCase("aspeed1"))
            return;

        ConfigManager configManager = Residence.getInstance().getConfigManager();

        switch (event.getNewState()) {
            case NEITHER:
            case FALSE:
                for (Player one : event.getResidence().getPlayersInResidence()) {
                    one.setWalkSpeed(Math.max(one.getWalkSpeed() - configManager.getWalkSpeed1().floatValue(), 0.2F));
                }
                break;
            case TRUE:
                if (event.getFlag().equalsIgnoreCase("aspeed1")) {
                    for (Player one : event.getResidence().getPlayersInResidence())
                        one.setWalkSpeed(one.getWalkSpeed() + configManager.getWalkSpeed1().floatValue());
                }
                break;
            case INVALID:
            default:
                break;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFlagChangeASpeed(ResidenceOwnerChangeEvent event) {
        ConfigManager configManager = Residence.getInstance().getConfigManager();
        ClaimedResidence res = event.getResidence();
        if (res.getPermissions().has("aspeed", FlagPermissions.FlagCombo.OnlyTrue))
            for (Player one : event.getResidence().getPlayersInResidence()) {
                one.setWalkSpeed(Math.max(one.getWalkSpeed() - configManager.getWalkSpeed1().floatValue(), 0.2F));
            }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        Residence.getInstance().getPermissionManager().removeFromCache(player);

        checkSpecialFlags(player, null, Residence.getInstance().getResidenceManager().getByLoc(player.getLocation()));

        Residence.getInstance().getPlayerManager().getResidencePlayer(player).onQuit();
        Residence.getInstance().getTeleportMap().remove(player.getUniqueId().toString());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAuraUserLoadEvent(UserLoadEvent event) {
        checkUpdateSpeed(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        checkUpdateSpeed(event.getPlayer());
    }

    private void checkUpdateSpeed(Player player) {
        if (player == null) {
            return;
        }
        Residence instance = Residence.getInstance();
        ClaimedResidence residence = instance.getResidenceManager().getByLoc(player.getLocation());
        if (residence != null) {
            if (residence.getPermissions().playerHas(player, "aspeed1", false)) {
                player.setWalkSpeed(player.getWalkSpeed() + instance.getConfigManager().getWalkSpeed1().floatValue());
            }
        }
    }

    private void checkSpecialFlags(Player player, ClaimedResidence newRes, ClaimedResidence oldRes) {
        if (player == null) {
            return;
        }

        ConfigManager configManager = Residence.getInstance().getConfigManager();

        if (newRes == null && oldRes != null) {
            if (oldRes.getPermissions().playerHas(player, "aspeed1", false)) {
                player.setWalkSpeed(Math.max(player.getWalkSpeed() - configManager.getWalkSpeed1().floatValue(), 0.2F));
            }
            return;
        }

        if (newRes != null && oldRes != null && !newRes.equals(oldRes)) {
            if (oldRes.getPermissions().playerHas(player, "aspeed1", false) &&
                    !newRes.getPermissions().playerHas(player, "aspeed1", false)) {
                player.setWalkSpeed(Math.max(player.getWalkSpeed() - configManager.getWalkSpeed1().floatValue(), 0.2F));
            } else if (!oldRes.getPermissions().playerHas(player, "aspeed1", false) &&
                    newRes.getPermissions().playerHas(player, "aspeed1", false)) {
                player.setWalkSpeed(player.getWalkSpeed() + configManager.getWalkSpeed1().floatValue());
            }
            return;
        }

        if (newRes != null && oldRes == null) {
            if (newRes.getPermissions().playerHas(player, "aspeed1", false)) {
                player.setWalkSpeed(player.getWalkSpeed() + configManager.getWalkSpeed1().floatValue());
            }
        }
    }
}



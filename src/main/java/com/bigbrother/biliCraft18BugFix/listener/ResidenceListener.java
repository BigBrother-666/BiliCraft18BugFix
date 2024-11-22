package com.bigbrother.biliCraft18BugFix.listener;

import com.bekvon.bukkit.residence.ConfigManager;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.bekvon.bukkit.residence.event.ResidenceDeleteEvent;
import com.bekvon.bukkit.residence.event.ResidenceFlagChangeEvent;
import com.bekvon.bukkit.residence.event.ResidenceOwnerChangeEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ResidenceListener implements Listener {

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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        Residence.getInstance().getPermissionManager().removeFromCache(player);

        checkSpecialFlags(player, null, Residence.getInstance().getResidenceManager().getByLoc(player.getLocation()));

        Residence.getInstance().getPlayerManager().getResidencePlayer(player).onQuit();
        Residence.getInstance().getTeleportMap().remove(player.getUniqueId().toString());
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



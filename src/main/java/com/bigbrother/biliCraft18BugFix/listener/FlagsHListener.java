package com.bigbrother.biliCraft18BugFix.listener;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import com.massivecraft.factions.listeners.FactionsBlockListener;
import fr.formiko.flagsh.Flag;
import fr.formiko.flagsh.FlagsH;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;

import static com.bigbrother.biliCraft18BugFix.BiliCraft18BugFix.*;

public class FlagsHListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHitFlagEntity(@Nonnull EntityDamageByEntityEvent event) {
        Flag flag = FlagsH.getFlagLinkedToEntity(event.getEntity());
        if (flag != null && event.getDamager() instanceof Player player) {
            Location location = event.getEntity().getLocation();
            // Faction检查
            if (factionsEnabled && config.getBoolean("FlagsH-Detect.Factions", false) && !FactionsBlockListener.playerCanBuildDestroyBlock(player, location, "destroy", false)) {
                event.setCancelled(true);
            } else if (residenceEnabled && config.getBoolean("FlagsH-Detect.Residence", false)) {
                ClaimedResidence residence = Residence.getInstance().getResidenceManager().getByLoc(location);
                if (residence != null) {
                    ResidencePermissions permissions = residence.getPermissions();
                    // res检查
                    if (!permissions.playerHas(player, Flags.destroy, false) && !Residence.getInstance().isResAdminOn(player)) {
                        event.setCancelled(true);
                        Residence.getInstance().msg(player, lm.Flag_Deny, Flags.destroy);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteractWithFlagEntity(@Nonnull PlayerInteractEntityEvent event) {
        Flag flag = FlagsH.getFlagLinkedToEntity(event.getRightClicked());
        if (flag != null) {
            Player player = event.getPlayer();
            Location location = event.getRightClicked().getLocation();
            // Faction检查
            if (factionsEnabled && config.getBoolean("FlagsH-Detect.Factions", false) && !FactionsBlockListener.playerCanBuildDestroyBlock(player, location, "build", false)) {
                event.setCancelled(true);
            } else if (residenceEnabled && config.getBoolean("FlagsH-Detect.Residence", false)) {
                ClaimedResidence residence = Residence.getInstance().getResidenceManager().getByLoc(location);
                if (residence != null) {
                    ResidencePermissions permissions = residence.getPermissions();
                    // res检查建筑权限（旗帜变大）
                    if (!permissions.playerHas(player, Flags.build, false) && !Residence.getInstance().isResAdminOn(player)) {
                        event.setCancelled(true);
                        Residence.getInstance().msg(player, lm.Flag_Deny, Flags.build);
                    }
                }
            }
        }
    }
}

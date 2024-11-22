package com.bigbrother.biliCraft18BugFix.listener;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.massivecraft.factions.listeners.FactionsBlockListener;
import io.th0rgal.oraxen.api.events.furniture.OraxenFurniturePlaceEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static com.bigbrother.biliCraft18BugFix.BiliCraft18BugFix.*;

public class OraxenFurniturePlaceListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onOraxenFurniturePlace(OraxenFurniturePlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();
        if (factionsEnabled && config.getBoolean("Oraxen-Furniture-Detect.Factions", false) && !FactionsBlockListener.playerCanBuildDestroyBlock(player, location, "build", false)) {
            event.setCancelled(true);
        } else if (residenceEnabled && config.getBoolean("Oraxen-Furniture-Detect.Residence", false)) {
            ResidencePlayer residencePlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(player);
            if (residencePlayer != null && !residencePlayer.canPlaceBlock(block, true)) {
                event.setCancelled(true);
            }
        }
    }
}

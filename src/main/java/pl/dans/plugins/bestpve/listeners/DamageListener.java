/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.dans.plugins.bestpve.listeners;

import com.google.common.annotations.Beta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import pl.dans.plugins.bestpve.BestPvE;

/**
 *
 * @author Dans
 */
public class DamageListener implements Listener{
    private final BestPvE plugin;

    public DamageListener(BestPvE plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void damage(final EntityDamageEvent entityDamageEvent) {
        
        if (entityDamageEvent.isCancelled()) {
            return;
        }
        Entity e = entityDamageEvent.getEntity();
        
        if (e instanceof Player)  {
            Player player = (Player)e;
            plugin.playerTookDamage(player.getName());
        }
    }
    
    
}

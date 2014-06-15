/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.dans.plugins.bestpve.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import pl.dans.plugins.bestpve.BestPvEGame;

/**
 *
 * @author Dans
 */
public class DamageListener implements Listener{
    private final BestPvEGame bestPvEGame;

    public DamageListener(BestPvEGame bestPvEGame) {
        this.bestPvEGame = bestPvEGame;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void damage(final EntityDamageEvent entityDamageEvent) {
        
        if (entityDamageEvent.isCancelled()) {
            return;
        }
        Entity e = entityDamageEvent.getEntity();
        
        if (e instanceof Player)  {
            Player player = (Player)e;
            bestPvEGame.playerTookDamage(player.getName());
        }
    }
    
    
}

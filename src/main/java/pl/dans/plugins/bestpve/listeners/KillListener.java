/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.dans.plugins.bestpve.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.dans.plugins.bestpve.BestPvEGame;

/**
 *
 * @author Dans
 */
public class KillListener implements Listener{
    
    private final BestPvEGame bestPvEGame;
    
    public KillListener(BestPvEGame bestPvEGame) {
        this.bestPvEGame = bestPvEGame;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void kill(final PlayerDeathEvent playerDeathEvent) {
        Player dead = playerDeathEvent.getEntity();
        dead.setMaxHealth(20);
        Player killer = dead.getKiller();
        if (killer != null) {
            bestPvEGame.playerGotAKill(killer.getName());
        }
        
    }
    
    
    
}

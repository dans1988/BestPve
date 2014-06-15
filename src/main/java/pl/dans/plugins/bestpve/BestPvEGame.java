/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.dans.plugins.bestpve;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Dans
 */
public class BestPvEGame {
    
    private List<String> players;
    private boolean running;
    private Map<String, Date> lastAdded;

    public BestPvEGame() {
        running = false;
    }
    
    public void rewardPlayers() {
        if (!running) {
            return;
        }
        for (String p : players ) {
            if (Bukkit.getServer().getPlayer(p) != null && Bukkit.getServer().getPlayer(p).isOnline()) {
                Player player = Bukkit.getServer().getPlayer(p);
                player.setMaxHealth(player.getMaxHealth() + 2);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30, 2));
                player.sendMessage(ChatColor.GREEN + "You were rewarded for your PvE skills!");
            }
        }
    }
    
    public void playerGotAKill(String playerName) {
        if (!running) {
            return;
        }
        lastAdded.put(playerName, new Date());
        if (!players.contains(playerName)) {
            players.add(playerName);
            if(Bukkit.getServer().getPlayer(playerName).isOnline()) {
                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + playerName + " got a kill! He is back on the Best PvE List!");
            }
        }
    }
    
    public void playerTookDamage(String playerName) {
        if (!running) {
            return;
        }
        
        Date currentTime = new Date();
        long currentMilis = currentTime.getTime();
        long addedMillis = lastAdded.get(playerName).getTime();
        
        if (currentMilis - addedMillis < 2000) {
            return;
        }
        
        Iterator<String> iterator = players.iterator();
        while (iterator.hasNext()) {
            String currentPlayer = iterator.next();
            if (currentPlayer.compareTo(playerName) == 0) {
                iterator.remove();
                Bukkit.getServer().broadcastMessage(ChatColor.RED + playerName + " took damage!");
                break;
            }
        }   
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Map<String, Date> getLastAdded() {
        return lastAdded;
    }

    public void setLastAdded(Map<String, Date> lastAdded) {
        this.lastAdded = lastAdded;
    }
}

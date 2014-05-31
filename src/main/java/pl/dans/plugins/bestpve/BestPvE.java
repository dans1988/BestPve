package pl.dans.plugins.bestpve;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.dans.plugins.bestpve.listeners.DamageListener;
import pl.dans.plugins.bestpve.listeners.KillListener;

/**
 * Hello world!
 *
 */
public class BestPvE extends JavaPlugin 
{
    
    private List<String> players;
    
    private BukkitTask task;
    
    private int elapsed;
    
    private DamageListener damageListener;
    
    private KillListener killListener;
    
    private boolean running;
    
    private Map<String, Date> lastAdded;
    
    
    
    @Override
    public void onEnable() {
        getLogger().info(new StringBuilder().append(ChatColor.RED).append("onEnable").toString());
        damageListener = new DamageListener(this);
        killListener = new KillListener(this);
        running = false;
    }
    
    @Override
    public void onDisable() {
        getLogger().info(new StringBuilder().append(ChatColor.RED).append("onDisable").toString());
    }
    
    private void rewardPlayers() {
        if (!running) {
            return;
        }
        for (String p : players ) {
            if (getServer().getPlayer(p) != null && getServer().getPlayer(p).isOnline()) {
                Player player = getServer().getPlayer(p);
                player.setMaxHealth(player.getMaxHealth() + 2);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30, 2));
                player.sendMessage(new StringBuilder().append(ChatColor.GREEN).append("You were rewarded for your PvE skills!").toString());
            }
        }
    }
    
    public void playerGotAKill(String playerName) {
        if (!running) {
            return;
        }
        if (!players.contains(playerName)) {
            players.add(playerName);
            lastAdded.put(playerName, new Date());
            if(getServer().getPlayer(playerName).isOnline()) {
                getServer().broadcastMessage(new StringBuilder().append(ChatColor.GREEN).append(playerName).append(" got a kill! He is back on the Best PvE List!").toString());
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
                getServer().broadcastMessage(new StringBuilder().append(ChatColor.RED).append(playerName).append(" took damage!").toString());
                break;
            }
        }
        
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (command.getName().compareToIgnoreCase("bStart") == 0) {
            elapsed = 0;
            players = new ArrayList<String>();
            lastAdded = new HashMap<String, Date>();
            for (Player player : getServer().getOnlinePlayers()) {
                
                
                players.add(player.getName());
                lastAdded.put(player.getName(), new Date());
            }
            
            task = new BukkitRunnable() {

                public void run() {
                    
                    if (elapsed % 10 == 0 && elapsed != 0) {
                        rewardPlayers();
                    }
                    
                    elapsed++;
                }
            }.runTaskTimer(this, 0, 20*60);
            running = true;
            getServer().broadcastMessage(new StringBuilder().append(ChatColor.GREEN).append("Best PvE game started!").toString());
            return true;
        } else if (command.getName().compareToIgnoreCase("bStop") == 0) {
            task.cancel();
            running = false;
            getServer().broadcastMessage(new StringBuilder().append(ChatColor.GREEN).append("Best PvE game was stopped!").toString());
            return true;
        } else if (command.getName().compareToIgnoreCase("bAdd") == 0) {
            
            if (args.length == 0) {
                sender.sendMessage(new StringBuilder().append(ChatColor.RED).append("You need to specify the player!").toString());
                return false;
            }
            
            String player = args[0];
            if(!getServer().getPlayer(player).isOnline()) {
                sender.sendMessage(new StringBuilder().append(ChatColor.RED).append(player).append(" is not online!").toString());
                return false;
            }
            
            players.add(player);
            lastAdded.put(player, new Date());
            
            sender.sendMessage(new StringBuilder().append(ChatColor.GOLD).append(player).append(" was added to the list!").toString());
            
            return true;
        } else if (command.getName().compareToIgnoreCase("bRemove") == 0) {
            if (args.length == 0) {
                sender.sendMessage(new StringBuilder().append(ChatColor.RED).append("You need to specify the player!").toString());
                return false;
            }
            String player = args[0];
            
            Iterator<String> iterator = players.iterator();
            
            boolean removed = false;
            while (iterator.hasNext()) {
                if (iterator.next().compareTo(player) == 0) {
                    iterator.remove();
                    sender.sendMessage(new StringBuilder().append(ChatColor.GOLD).append(player).append(" was removed from the list!").toString());
                    removed = true;
                    break;
                }
            }
            
            if (!removed) {
                sender.sendMessage(new StringBuilder().append(ChatColor.RED).append(player).append(" is not present on the list!").toString());
            }
            
            return true;
        } else if (command.getName().compareToIgnoreCase("bList") == 0) {
            if (running) {
                sender.sendMessage(new StringBuilder().append(ChatColor.GREEN).append("Best PvE players:").toString());
                for (String p : players) {
                    sender.sendMessage(new StringBuilder().append(ChatColor.YELLOW).append(p).toString());
                }
            } else {
                sender.sendMessage(new StringBuilder().append(ChatColor.RED).append("Best PvE game is not in progress!").toString());
            }
            
            return true;
        }
        
        return false;
    }
}

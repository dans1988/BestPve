/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dans.plugins.bestpve.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.dans.plugins.bestpve.BestPvE;
import pl.dans.plugins.bestpve.BestPvEGame;

/**
 *
 * @author Dans
 */
public class StartStopExecutor implements CommandExecutor {

    private final BestPvE plugin;
    private final BestPvEGame bestPvEGame;
    
    private int elapsed;
    
    private BukkitTask task;

    public StartStopExecutor(BestPvE plugin, BestPvEGame bestPvEGame) {
        this.plugin = plugin;
        this.bestPvEGame = bestPvEGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().compareTo("bStart") == 0) {
            if (bestPvEGame.isRunning()) {
                sender.sendMessage(ChatColor.RED + "BestPvE game is already running!");
            } else {
                startGame();
            }
        } else if (command.getName().compareTo("bStop") == 0){
            if (bestPvEGame.isRunning()) {
                stopGame();
            } else {
                sender.sendMessage(ChatColor.RED + "BestPvE game is not running!");
            }
        }
        
        return true;
    }
    
    private void startGame() {
        elapsed = 0;
        List<String> players = new ArrayList<String>();
        Map<String, Date> lastAdded = new HashMap<String, Date>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

            players.add(player.getName());
            lastAdded.put(player.getName(), new Date());
        }
        
        bestPvEGame.setPlayers(players);
        bestPvEGame.setLastAdded(lastAdded);

        task = new BukkitRunnable() {

            @Override
            public void run() {

                if (elapsed % 10 == 0 && elapsed != 0) {
                    bestPvEGame.rewardPlayers();
                }

                elapsed++;
            }
        }.runTaskTimer(plugin, 0, 20 * 60);
        
        
        bestPvEGame.setRunning(true);
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Best PvE game started!");
    }
    
    private void stopGame() {
        task.cancel();
        bestPvEGame.setRunning(false);
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Best PvE game was stopped!");
    }

}

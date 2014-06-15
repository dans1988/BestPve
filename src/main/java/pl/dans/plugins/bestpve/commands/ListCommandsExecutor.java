/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dans.plugins.bestpve.commands;

import java.util.Date;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.dans.plugins.bestpve.BestPvEGame;

/**
 *
 * @author Dans
 */
public class ListCommandsExecutor implements CommandExecutor {
    
    private final BestPvEGame bestPvEGame;

    public ListCommandsExecutor(BestPvEGame bestPvEGame) {
        this.bestPvEGame = bestPvEGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().compareTo("bAdd") == 0) {

            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "You need to specify the player!");
                return false;
            }
            
            String player = args[0];
            if (!Bukkit.getServer().getPlayer(player).isOnline()) {
                sender.sendMessage(ChatColor.RED + player + " is not online!");
                return false;
            }
            
            if (!bestPvEGame.getPlayers().contains(player)) {
                bestPvEGame.getPlayers().add(player);
                bestPvEGame.getLastAdded().put(player, new Date());

                sender.sendMessage(ChatColor.GOLD + player + " was added to the list!");
            } else {
                sender.sendMessage(ChatColor.RED + player + " is already on the list!");
            }

            return true;
        } else if (command.getName().compareTo("bRemove") == 0) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "You need to specify the player!");
                return false;
            }
            String player = args[0];
            
            Iterator<String> iterator = bestPvEGame.getPlayers().iterator();
            
            boolean removed = false;
            while (iterator.hasNext()) {
                if (iterator.next().compareTo(player) == 0) {
                    iterator.remove();
                    sender.sendMessage(ChatColor.GOLD + player + " was removed from the list!");
                    removed = true;
                    break;
                }
            }
            
            if (!removed) {
                sender.sendMessage(ChatColor.RED + player + " is not present on the list!");
            } 
        } else if (command.getName().compareToIgnoreCase("bList") == 0) {
            if (bestPvEGame.isRunning()) {
                sender.sendMessage(ChatColor.GREEN + "Best PvE players:");
                for (String p : bestPvEGame.getPlayers()) {
                    sender.sendMessage(ChatColor.YELLOW + p);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Best PvE game is not in progress!");
            }
        }
        return true;
    }

}

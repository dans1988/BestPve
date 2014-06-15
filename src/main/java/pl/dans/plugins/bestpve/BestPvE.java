package pl.dans.plugins.bestpve;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.dans.plugins.bestpve.commands.ListCommandsExecutor;
import pl.dans.plugins.bestpve.commands.StartStopExecutor;
import pl.dans.plugins.bestpve.listeners.DamageListener;
import pl.dans.plugins.bestpve.listeners.KillListener;

/**
 * Hello world!
 *
 */
public class BestPvE extends JavaPlugin 
{
    private BestPvEGame bestPvEGame;
    private StartStopExecutor startStopExecutor;
    private ListCommandsExecutor listCommandsExecutor;

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "{0}onEnable", ChatColor.RED);
        
        bestPvEGame = new BestPvEGame();
        
        getServer().getPluginManager().registerEvents(new DamageListener(bestPvEGame), this);
        getServer().getPluginManager().registerEvents(new KillListener(bestPvEGame), this);
        
        startStopExecutor = new StartStopExecutor(this, bestPvEGame);
        listCommandsExecutor  = new ListCommandsExecutor(bestPvEGame);
        
        getCommand("bStart").setExecutor(startStopExecutor);
        getCommand("bStop").setExecutor(startStopExecutor);
        getCommand("bAdd").setExecutor(listCommandsExecutor);
        getCommand("bRemove").setExecutor(listCommandsExecutor);
        getCommand("bList").setExecutor(listCommandsExecutor);
    }
    
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}onDisable", ChatColor.RED);
    }
}

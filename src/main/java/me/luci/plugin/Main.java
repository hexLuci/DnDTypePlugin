package me.luci.plugin;

import me.luci.plugin.commands.stats.Heal;
import me.luci.plugin.commands.stats.ShowStats;
import me.luci.plugin.events.EntityHitPlayer;
import me.luci.plugin.events.FirstJoinEvent;
import me.luci.plugin.events.PlayerHitEntity;
import me.luci.plugin.events.PlayerRespawnEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

    //On Plugin enable
    public void onEnable() {


        //TODO: Do GUI for skills,
        //TODO: Apply other effects to skills,


        //TODO: Make skills level up,
        //TODO: Skills leveling:
        //TODO: Save blocks to hashmap and then save to config

        //Save the default config (that's saved in the plugin) to the server's folders
        saveDefaultConfig();

        //Register the events
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new FirstJoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerHitEntity(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnEvent(this), this);
        getServer().getPluginManager().registerEvents(new EntityHitPlayer(this), this);

        //Register the commands
        Objects.requireNonNull(getCommand("showstats")).setExecutor(new ShowStats(this));
        Objects.requireNonNull(getCommand("heal")).setExecutor(new Heal());
    }

    //On Plugin disable
    public void onDisable() {
    }
}

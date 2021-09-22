package me.luci.plugin.events;

import me.luci.plugin.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;
import java.io.IOException;

public class PlayerHitEntity implements Listener {

    private final Main plugin;

    //When player hits an entity
    public PlayerHitEntity(Main plugin) {
        this.plugin = plugin;
    }

    //Check the player damage event to see if fired
    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent e) {
        //Checks to see if the person hitting is a player
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();

            //Sets the file and conf to be the config for the player's YAML
            File file = new File(plugin.getDataFolder(), p.getUniqueId() + ".yml");
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

            //Try loading the file
            try {
                conf.load(file);
            } catch (IOException | InvalidConfigurationException ex) {
                ex.printStackTrace();
            }

            //Get path "Strength" from the player's config
            int strength = conf.getInt("Stats.Strength");


            //Sets the damage for the player to be strength * their original damage
            e.setDamage(e.getDamage() * strength);
//          DEBUG  p.sendMessage("Hit for " + e.getDamage() * strength);

        }
    }
}

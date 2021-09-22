package me.luci.plugin.events;

import me.luci.plugin.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class EntityHitPlayer implements Listener {

    private final Main plugin;

    //When player hits an entity
    public EntityHitPlayer(Main plugin) {
        this.plugin = plugin;
    }

    //Check the player damage event to see if fired
    @EventHandler
    public void onEntityDamagePlayer(final EntityDamageByEntityEvent e) {
        //Checks to see if the person hitting is a player
        if (!(e.getDamager() instanceof Player)) {
            final Player p = (Player) e.getEntity();

            File file = new File(plugin.getDataFolder(), p.getUniqueId() + ".yml");
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

            //Try loading the file
            try {
                conf.load(file);
            } catch (IOException | InvalidConfigurationException ex) {
                ex.printStackTrace();
            }

            if (e.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE) <= 1) {
                e.setDamage(e.getDamage());
            } else {

                double defence = conf.getDouble("Stats.Defense");
                double defenceFormula = ((defence + 100) / defence) * 100;
                final double damage = defenceFormula / (e.getDamage() * 100);
                e.setDamage(damage);
                p.sendMessage("" + e.getDamage());

            }
        }
    }
}

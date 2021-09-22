package me.luci.plugin.events;

import me.luci.plugin.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PlayerRespawnEvent implements Listener {

    private final Main plugin;


    //For player respawns
    public PlayerRespawnEvent(Main plugin) {
        this.plugin = plugin;
    }


    //On player respawn
    @EventHandler
    public void onRespawn(org.bukkit.event.player.PlayerRespawnEvent e) {
        final Player p = e.getPlayer();


        //Sets the player's UUID config to file and conf
        File file = new File(plugin.getDataFolder(), p.getUniqueId() + ".yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

        //Loads the config
        try {
            conf.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        //Sets the health scale (hearts) to 20
        p.setHealthScale(20D);
        //Makes sure the player only has 20 hearts, but the correct amount of health
        p.setHealthScaled(true);

        //Sets the player's base value of health to their health
        Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(conf.getDouble("Stats.Health"));

        //Setting player speed
        float Speed = (float) conf.getDouble("Stats.Speed");
        String message = String.valueOf(Speed);

        p.setWalkSpeed(Speed);
        p.sendMessage(message);

        //Loops over the player to check if they're online
        new BukkitRunnable() {
            @Override
            public void run() {
                //If they're not online, cancel the loop
                if (!p.isOnline()) {
                    cancel();
                }
                //Displays current player's health on their actionbar
                int health = (int) p.getHealth();
                String message = String.valueOf(health);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_RED + message + "❤"));
            }
        }.runTaskTimer(plugin, 5L, 5L);

    }
}

package me.luci.plugin.events;

import me.luci.plugin.Config;
import me.luci.plugin.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class FirstJoinEvent implements Listener {

    private final Main plugin;
    public Config c;

    public FirstJoinEvent(Main plugin) {
        this.plugin = plugin;
    }

    //When player joins
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        //If the player hasn't played before, generate a new YAML
        if (p.hasPlayedBefore()) {
            //Create a new config using the player's UUID as fileName
            c = new Config(plugin.getDataFolder().toString(), p.getUniqueId() + ".yml", plugin);
//				Config c = new Config(plugin.getDataFolder().toString(), p.getUniqueId().toString() + ".yml", plugin);

            // Strength: 1
//			    Dexterity: 1
//			    Constitution: 1
//			    Intelligence: 1
//			    Wisdom: 1
//			    Charisma: 1

            // Taming, Mining, Foraging, Enchanting, Carpentry, Farming, Combat, Fishing, Alchemy, Runecrafting

            //Creates the config
            c.create();

            //Sets paths for the default config
            c.getConfig().set("Stats.Constitution", 100.0D);
            c.getConfig().set("Stats.Defense", 50.0D);
            c.getConfig().set("Stats.Strength", 1);
            c.getConfig().set("Stats.Speed", 0.2D); //0.0 to 1.0, 0.2 is default
            c.getConfig().set("Stats.CC", 0);
            c.getConfig().set("Stats.CD", 0);
            c.getConfig().set("Stats.AS", 0);
            c.getConfig().set("Stats.Intelligence", 0);
            c.getConfig().set("Stats.MagicFind", 0);

            //Saves the config
            c.saveConfig();
            //Reloads it to make sure paths are set
            c.reloadConfig();

//            DEBUG p.sendMessage("Saved ");

            //Set the hearts of the player
            p.setHealthScale(20D);

            //Set player health to max health, but keep hearts
            p.setHealthScaled(true);
            Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(c.getConfig().getDouble("Stats.Constitution"));

            //Setting player speed
            float Speed = (float) c.getConfig().getDouble("Stats.Speed");
            String message = String.valueOf(Speed);

            p.setWalkSpeed(Speed);
            p.sendMessage(message);

            //Loop to check if the player is online, and display a message on their action bar
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!p.isOnline()) {
                        cancel();
                    }
                    //Displays current player's health on their actionbar
                    int health = (int) p.getHealth();
                    int rounded = (int) Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
                    String message = health + "/" + rounded;
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_RED + message + "❤"));
                }
            }.runTaskTimer(plugin, 5L, 5L);
        }
    }
}

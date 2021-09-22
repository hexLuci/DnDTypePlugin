package me.luci.plugin.commands.stats;

import me.luci.plugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ShowStats implements CommandExecutor {

    private final Main plugin;

    public ShowStats(Main plugin) {
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        //This is a test command
        if (string.equalsIgnoreCase("showstats")) {
            Player p = (Player) sender;

            //Load player's UUID YAML
            File file = new File(plugin.getDataFolder(), p.getUniqueId() + ".yml");
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

            try {
                conf.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //For loop over every key in YAML
            for (String key : conf.getKeys(true)) {
                p.sendMessage("Stats " + key);
            }

            //Send message with each stat
            int strength = conf.getInt("Stats.Strength");
            p.sendMessage("Stats " + strength);

            return true;
        }
        return true;
    }

}

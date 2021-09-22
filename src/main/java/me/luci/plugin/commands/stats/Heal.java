package me.luci.plugin.commands.stats;

import me.luci.plugin.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {

    //private Main plugin;

    public Heal(Main plugin) {
        //this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        //If command is the word heal
        if (string.equalsIgnoreCase("heal")) {
                Player p = (Player) sender;

                //Heal the player that used the command
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                p.setFoodLevel(20);

            }
        return true;
    }

}

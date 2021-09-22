package me.luci.plugin;

import me.luci.plugin.commands.constitution.Heal;
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

/*
Copyright 2021 hexLuci

        Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

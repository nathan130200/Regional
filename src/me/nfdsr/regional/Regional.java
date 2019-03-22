package me.nfdsr.regional;

import me.nfdsr.regional.listener.PlayerListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Regional extends JavaPlugin {
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}
	
	public void onDisable(){
		HandlerList.unregisterAll(this);
	}
}

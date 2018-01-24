package com.stupidbot.universalbossbar;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.stupidbot.universalbossbar.listeners.OnCommand;
import com.stupidbot.universalbossbar.listeners.OnJoin;
import com.stupidbot.universalbossbar.utils.BossBars;

public class Main extends JavaPlugin implements Listener {
	private static Main instance;

	public void onDisable() {
		for (Player all : Bukkit.getOnlinePlayers())
			for (BossBar bar : BossBars.getBars().values())
				bar.removePlayer(all);
		FileConfiguration config = instance.getConfig();
		instance.reloadConfig();
		config.set("HiddenBars.Players", BossBars.hiddenBars);
		instance.saveConfig();
		instance = null;
		System.out.println(getName() + " Is now disabled!");
	}

	public void onEnable() {
		instance = this;
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(), instance);
		getServer().getPluginManager().registerEvents(this, this);
		instance.getCommand("reloadbossbars").setExecutor(new OnCommand());
		instance.getCommand("togglebars").setExecutor(new OnCommand());
		setupConfig();
		BossBars.setup();
		System.out.println(getName() + " Is now enabled!");
	}

	private void setupConfig() {
		FileConfiguration config = instance.getConfig();

		if (!(new File(getDataFolder() + File.separator + "config.yml").exists())) {
			config.set("Bars.0.Color", "RED");
			config.set("Bars.0.Style", "SOLID");
			config.set("Bars.0.AnimationSpeed", 20);
			String[] progress0 = { "1.0" };
			config.set("Bars.0.Progress", progress0);
			String[] frames0 = { "&bEdit in &cCONFIG", "&cEdit in &bCONFIG" };
			config.set("Bars.0.Frames", frames0);
			config.set("Bars.1.Color", "YELLOW");
			config.set("Bars.1.Style", "SEGMENTED_20");
			config.set("Bars.1.AnimationSpeed", 10);
			String[] progress1 = { "1.0", "0.75", "0.5", "0.25", "0", "0.25", "0.5", "0.75" };
			config.set("Bars.1.Progress", progress1);
			String[] frames1 = { "&aYou can remove or add boss bars!" };
			config.set("Bars.1.Frames", frames1);
			config.set("HiddenBars.Players", new ArrayList<String>());
		}

		config.options().copyDefaults(true);
		instance.saveConfig();
		instance.reloadConfig();
	}

	public static Main getInstance() {
		return instance;
	}
}
package com.stupidbot.universalbossbar.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.stupidbot.universalbossbar.Main;

public class BossBars {
	private static Map<String, BossBar> bars;
	public static List<String> hiddenBars;

	public static void setup() {
		Main instance = Main.getInstance();
		FileConfiguration config = instance.getConfig();
		Set<String> barSegments = config.getConfigurationSection("Bars").getKeys(false);
		bars = new HashMap<String, BossBar>();

		if (hiddenBars == null)
			if (config.getStringList("HiddenBars.Players") != null)
				hiddenBars = config.getStringList("HiddenBars.Players");

		for (String barId : barSegments) {
			String title = ChatColor.translateAlternateColorCodes('&',
					config.getStringList("Bars." + barId + ".Frames").get(0));
			BarColor color = BarColor.valueOf(config.getString("Bars." + barId + ".Color"));
			BarStyle style = BarStyle.valueOf(config.getString("Bars." + barId + ".Style"));
			BossBar bar = Bukkit.createBossBar(title, color, style);
			int animationSpeed = config.getInt("Bars." + barId + ".AnimationSpeed");

			bar.setProgress(Double.parseDouble(config.getStringList("Bars." + barId + ".Progress").get(0)));

			bars.put(barId, bar);

			if (animationSpeed > 0) // animated
				if (config.getStringList("Bars." + barId + ".Frames").size() > 1
						|| config.getStringList("Bars." + barId + ".Progress").size() > 1)
					new BukkitRunnable() {
						int i = 0;
						int t = 0;

						public void run() {
							bars.get(barId).setTitle(ChatColor.translateAlternateColorCodes('&',
									config.getStringList("Bars." + barId + ".Frames").get(i)));
							i++;

							if (i == config.getStringList("Bars." + barId + ".Frames").size())
								i = 0;

							bars.get(barId).setProgress(
									Double.parseDouble(config.getStringList("Bars." + barId + ".Progress").get(t)));
							t++;

							if (t == config.getStringList("Bars." + barId + ".Progress").size())
								t = 0;
						}
					}.runTaskTimer(instance, 0, animationSpeed);
		}
		for (Player all : Bukkit.getOnlinePlayers())
			if (!(hiddenBars.contains(all.getUniqueId().toString())))
				for (BossBar bar : BossBars.getBars().values())
					bar.addPlayer(all);
	}

	public static Map<String, BossBar> getBars() {
		return bars;
	}
}
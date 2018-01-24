package com.stupidbot.universalbossbar.listeners;

import java.util.Map;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.stupidbot.universalbossbar.utils.BossBars;

public class OnJoin implements Listener {

	@EventHandler
	public static void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Map<String, BossBar> bars = BossBars.getBars();

		for (BossBar bar : bars.values())
			if (!(BossBars.hiddenBars.contains(player.getUniqueId().toString())))
				bar.addPlayer(player);
	}
}
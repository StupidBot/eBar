package com.stupidbot.universalbossbar.listeners;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.stupidbot.universalbossbar.Main;
import com.stupidbot.universalbossbar.utils.BossBars;

public class OnCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("reloadbossbars"))
			if (sender.hasPermission(new Permission("UniversalBossBar.Reload"))) {
				for (Player all : Bukkit.getOnlinePlayers())
					for (BossBar bar : BossBars.getBars().values())
						bar.removePlayer(all);

				Main.getInstance().reloadConfig();
				Bukkit.getScheduler().cancelAllTasks();
				BossBars.setup();
				sender.sendMessage("§aReloaded boss bars.");
			} else
				sender.sendMessage("§cYou don't have permission to do use this command.");
		else if (cmd.getName().equalsIgnoreCase("togglebars"))
			if (sender.hasPermission(new Permission("UniversalBossBar.Hide")))
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (BossBars.hiddenBars == null || BossBars.hiddenBars.contains(player.getUniqueId().toString())) {
						BossBars.hiddenBars.remove(player.getUniqueId().toString());

						for (BossBar bars : BossBars.getBars().values())
							bars.addPlayer(player);

						player.sendMessage("§aBoss bars toggled on.");
					} else {
						BossBars.hiddenBars.add(player.getUniqueId().toString());

						for (BossBar bars : BossBars.getBars().values())
							bars.removePlayer(player);

						player.sendMessage("§cBoss bars toggled off.");
					}
				} else
					sender.sendMessage("§cThis command can only be run by a player.");
			else
				sender.sendMessage("§cYou don't have permission to do use this command.");
		return true;
	}
}
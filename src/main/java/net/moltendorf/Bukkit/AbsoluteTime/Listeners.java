package net.moltendorf.Bukkit.AbsoluteTime;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Map;
import java.util.UUID;

/**
 * Created by moltendorf on 15/04/26.
 *
 * @author moltendorf
 */
public class Listeners implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void PlayerBedLeaveEventMonitor(final PlayerBedLeaveEvent event) {
		final AbsoluteTime instance = AbsoluteTime.getInstance();
		final World        world  = event.getBed().getWorld();
		final long         time   = world.getFullTime();

		instance.getServer().getScheduler().runTask(instance, () -> {
			if (time != world.getFullTime()) {
				instance.checkRunnable.schedule();
			}
		});
	}

	@EventHandler(ignoreCancelled = true)
	public void ServerCommandEventHandler(final ServerCommandEvent event) {
		final String command = event.getCommand();

		if (command.substring(0, 8).equalsIgnoreCase("gamerule") && command.substring(8).startsWith(" doDaylightCycle true")) {
			final AbsoluteTime instance = AbsoluteTime.getInstance();

			for (final World world : instance.getServer().getWorlds()) {
				final UUID id = world.getUID();
				final Map<UUID, WorldEntry> worlds = instance.worlds;

				if (!worlds.containsKey(id)) {
					worlds.put(id, new WorldEntry(world, false));
				}
			}
		}
	}
}

package net.moltendorf.Bukkit.AbsoluteTime;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

/**
 * Created by moltendorf on 15/04/26.
 *
 * @author moltendorf
 */
public class Listeners implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void PlayerBedLeaveEventMonitor(final PlayerBedLeaveEvent event) {
		final AbsoluteTime plugin = AbsoluteTime.instance;
		final World        world  = event.getBed().getWorld();
		final long         time   = world.getFullTime();

		plugin.getServer().getScheduler().runTask(plugin, () -> {
			if (time != world.getFullTime()) {
				plugin.checkRunnable.schedule();
			}
		});
	}
}

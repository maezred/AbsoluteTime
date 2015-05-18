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
		final AbsoluteTime instance = AbsoluteTime.getInstance();
		final World        world  = event.getBed().getWorld();
		final long         time   = world.getFullTime();

		instance.getServer().getScheduler().runTask(instance, () -> {
			if (time != world.getFullTime()) {
				instance.checkRunnable.schedule();
			}
		});
	}
}

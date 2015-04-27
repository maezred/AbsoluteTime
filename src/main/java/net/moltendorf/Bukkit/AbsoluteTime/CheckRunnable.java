package net.moltendorf.Bukkit.AbsoluteTime;

import net.moltendorf.Bukkit.AbsoluteTime.event.WorldChangedTimeEvent;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;

/**
 * Created by moltendorf on 15/04/26.
 *
 * @author moltendorf
 */
public class CheckRunnable implements Runnable {
	private BukkitTask task;

	@Override
	public void run() {
		final AbsoluteTime plugin = AbsoluteTime.instance;

		for (final Iterator<WorldEntry> iterator = plugin.worlds.values().iterator(); iterator.hasNext(); ) {
			final WorldEntry entry = iterator.next();
			final World world = entry.getWorld();

		world:
			if (world != null) {
				final long previousTime = entry.time.longValue();
				final long expectedTime = previousTime + 5L*20L;
				final long currentTime = world.getFullTime();

				if ((task != null && currentTime != expectedTime) || currentTime > expectedTime) {
					if (currentTime == previousTime) {
						break world;
					} else {
						plugin.getLogger().info("New time for " + world.getName() + ":      " + Long.toString(currentTime) + ".");

						plugin.getServer().getPluginManager().callEvent(new WorldChangedTimeEvent(world, previousTime, expectedTime));
					}
				}

				entry.time.setValue(currentTime);

				continue;
			}

			iterator.remove();
		}
	}

	public BukkitTask schedule() {
		if (task != null) {
			task.cancel();
			task = null;

			run();
		}

		final AbsoluteTime plugin = AbsoluteTime.instance;
		task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 5L*20L, 5L*20L);

		return task;
	}
}

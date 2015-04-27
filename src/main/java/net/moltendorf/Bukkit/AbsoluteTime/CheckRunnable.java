package net.moltendorf.Bukkit.AbsoluteTime;

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
		for (final Iterator<WorldEntry> iterator = AbsoluteTime.instance.worlds.values().iterator(); iterator.hasNext(); ) {
			final WorldEntry entry = iterator.next();
			final World world = entry.getWorld();

		world:
			if (world != null) {
				final long previousTime = entry.time.longValue();
				final long expectedTime = previousTime + 5L*20L;

				long currentTime = world.getFullTime();

				if (task != null) {
					if (currentTime < expectedTime) {
						currentTime = AbsoluteTime.fixTime(world, expectedTime);
					}

					if (currentTime != expectedTime) {
						if (currentTime == previousTime) {
							break world;
						} else {
							AbsoluteTime.newTime(world, currentTime, previousTime, expectedTime);
						}
					}
				} else {
					if (currentTime < previousTime) {
						currentTime = AbsoluteTime.fixTime(world, previousTime);
					}

					if (currentTime > expectedTime) {
						AbsoluteTime.newTime(world, currentTime, previousTime, expectedTime);
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

	public void cancel() {
		if (task != null) {
			task.cancel();
			task = null;

			run();
		}
	}
}

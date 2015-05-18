package net.moltendorf.Bukkit.AbsoluteTime;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
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
		final AbsoluteTime      instance = AbsoluteTime.getInstance();
		final FileConfiguration config   = instance.getConfig();

		for (final Iterator<WorldEntry> iterator = instance.worlds.values().iterator(); iterator.hasNext(); ) {
			final WorldEntry entry = iterator.next();
			final World world = entry.getWorld();

			long previousTime = entry.time.longValue();

		world:
			if (world != null) {
				final long expectedTime = previousTime + 5L*20L;

				long currentTime = world.getFullTime();

				if (task != null && previousTime != 0) {
					if (currentTime == previousTime && world.getGameRuleValue("doDaylightCycle").equals("false")) {
						break world;
					}

					if (currentTime < expectedTime) {
						if (world.getGameRuleValue("doDaylightCycle").equals("true")) {
							currentTime = AbsoluteTime.fixTime(world, expectedTime);
						} else {
							previousTime = currentTime;

							break world;
						}
					}

					if (currentTime != expectedTime) {
						AbsoluteTime.newTime(world, currentTime, previousTime, expectedTime);
					}
				} else {
					if (previousTime == 0) {
						previousTime = AbsoluteTime.getInstance().getConfig().getLong("worlds." + entry.name, currentTime);
					}

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

			config.set("worlds." + entry.name, previousTime);
			iterator.remove();
		}
	}

	public BukkitTask schedule() {
		if (task != null) {
			task.cancel();
			task = null;

			run();
		}

		task = AbsoluteTime.getInstance().getServer().getScheduler().runTaskTimer(AbsoluteTime.getInstance(), this, 5L*20L, 5L*20L);

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

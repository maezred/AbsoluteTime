package net.moltendorf.Bukkit.AbsoluteTime;

import net.moltendorf.Bukkit.AbsoluteTime.event.WorldChangedTimeEvent;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by moltendorf on 15/04/03.
 *
 * @author moltendorf
 */
public class AbsoluteTime extends JavaPlugin {

	// Main instance.
	private static AbsoluteTime instance;

	protected static AbsoluteTime getInstance() {
		return instance;
	}

	protected final Map<UUID, WorldEntry> worlds = new LinkedHashMap<>();
	protected CheckRunnable checkRunnable;

	protected long ticks = 0;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		final Server          server    = getServer();
		final BukkitScheduler scheduler = server.getScheduler();

		ticks = getConfig().getLong("ticks");
		scheduler.runTaskTimer(this, () -> ++ticks, 1L, 1L);

		getLogger().info("Resuming from tick: " + Long.toString(ticks) + ".");

		for (final World world : getServer().getWorlds()) {
			worlds.put(world.getUID(), new WorldEntry(world));
		}

		checkRunnable = new CheckRunnable();

		scheduler.runTask(this, () -> {
			checkRunnable.schedule();
			server.getPluginManager().registerEvents(new Listeners(), this);
		});
	}

	@Override
	public void onDisable() {
		checkRunnable.cancel();

		final FileConfiguration config = getConfig();

		config.set("ticks", ++ticks);

		for (final WorldEntry entry : worlds.values()) {
			final World world = entry.getWorld();

			final long time;

			if (world != null) {
				time = world.getFullTime();
			} else {
				time = entry.time.longValue();
			}

			config.set("worlds." + entry.name, time);
		}

		saveConfig();

		getLogger().info("Saved tick count to config: " + ticks + ".");

		instance = null;
	}

	protected static long fixTime(final World world, final long expectedTime) {
		final long expectedDay  = expectedTime/24000*24000;
		final long relativeTime = expectedTime - expectedDay;

		long currentTime = world.getTime();

		if (currentTime < relativeTime) {
			currentTime = expectedDay + 24000 + currentTime;
		} else {
			currentTime = expectedDay + currentTime;
		}

		world.setFullTime(currentTime);

		return currentTime;
	}

	protected static void newTime(final World world, final long currentTime, final long previousTime, final long expectedTime) {
		instance.getLogger().info("New time for " + world.getName() + ": " + Long.toString(currentTime) + ".");
		instance.getServer().getPluginManager().callEvent(new WorldChangedTimeEvent(world, previousTime, expectedTime));
	}

	/**
	 * @return ticks
	 */
	public long getTicks() {
		return ticks;
	}
}

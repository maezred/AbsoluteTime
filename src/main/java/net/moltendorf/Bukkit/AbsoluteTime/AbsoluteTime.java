package net.moltendorf.Bukkit.AbsoluteTime;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by moltendorf on 15/04/03.
 *
 * @author moltendorf
 */
public class AbsoluteTime extends JavaPlugin {
	protected static AbsoluteTime instance;

	protected final Map<UUID, WorldEntry> worlds = new LinkedHashMap<>();
	protected CheckRunnable checkRunnable;

	protected long ticks = 0;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		final Server server = getServer();

		ticks = getConfig().getLong("ticks");
		getLogger().info("Resuming from tick: " + Long.toString(ticks) + ".");
		server.getScheduler().runTaskTimer(this, () -> ++ticks, 1L, 1L);

		for (final World world : getServer().getWorlds()) {
			worlds.put(world.getUID(), new WorldEntry(world));
		}

		checkRunnable = new CheckRunnable();
		checkRunnable.schedule();

		server.getPluginManager().registerEvents(new Listeners(), this);
	}

	@Override
	public void onDisable() {
		instance = null;

		getConfig().set("ticks", ++ticks);
		saveConfig();

		getLogger().info("Saved tick count to config: " + ticks + ".");
	}

	/**
	 * @return ticks
	 */
	public long getTicks() {
		return ticks;
	}
}

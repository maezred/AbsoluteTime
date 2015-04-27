package net.moltendorf.Bukkit.AbsoluteTime;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by moltendorf on 15/04/03.
 *
 * @author moltendorf
 */
public class AbsoluteTime extends JavaPlugin {
	private long ticks = 0;

	@Override
	public void onEnable() {
		getServer().getScheduler().runTaskTimer(this, () -> ++ticks, 1L, 1L);
	}

	/**
	 * @return ticks since server start
	 */
	public long getTicks() {
		return ticks;
	}
}

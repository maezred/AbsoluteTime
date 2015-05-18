package net.moltendorf.Bukkit.AbsoluteTime;

import org.apache.commons.lang.mutable.MutableLong;
import org.bukkit.World;

import java.lang.ref.WeakReference;

/**
 * Created by moltendorf on 15/04/26.
 *
 * @author moltendorf
 */
public class WorldEntry {
	public final String name;
	public final MutableLong time;

	private final WeakReference<World> worldReference;

	public WorldEntry(final World world) {
		this(world, true);
	}

	public WorldEntry(final World world, final boolean current) {
		name = world.getName();

		final long currentTime = world.getFullTime();

		if (current) {
			time = new MutableLong(AbsoluteTime.getInstance().getConfig().getLong("worlds." + name, currentTime));

			if (time.longValue() < currentTime) {
				time.setValue(currentTime);
			}
		} else {
			time = new MutableLong(0);
		}

		worldReference = new WeakReference<>(world);
	}

	public World getWorld() {
		return worldReference.get();
	}
}

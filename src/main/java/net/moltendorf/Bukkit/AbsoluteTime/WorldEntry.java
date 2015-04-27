package net.moltendorf.Bukkit.AbsoluteTime;

import org.apache.commons.lang.mutable.MutableLong;
import org.bukkit.World;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Created by moltendorf on 15/04/26.
 *
 * @author moltendorf
 */
public class WorldEntry {
	public final MutableLong time;

	private final UUID                 worldId;
	private final WeakReference<World> worldReference;

	public WorldEntry(final World world) {
		time = new MutableLong(world.getFullTime() - 1L);

		worldId = world.getUID();
		worldReference = new WeakReference<>(world);
	}

	public World getWorld() {
		final World world = worldReference.get();

		if (world == null) {
			return AbsoluteTime.instance.getServer().getWorld(worldId);
		}

		return world;
	}
}

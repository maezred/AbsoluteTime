package net.moltendorf.Bukkit.AbsoluteTime.event;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;

/**
 * Created by moltendorf on 15/04/26.
 *
 * @author moltendorf
 */
public class WorldChangedTimeEvent extends WorldEvent {

	private static final HandlerList handlerList = new HandlerList();

	private final long previousTime, expectedTime;

	public WorldChangedTimeEvent(final World world, final long previousTime, final long expectedTime) {
		super(world);

		this.previousTime = previousTime;
		this.expectedTime = expectedTime;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public long getPreviousTime() {
		return previousTime;
	}

	public long getExpectedTime() {
		return expectedTime;
	}

	public long getCurrentTime() {
		return getWorld().getFullTime();
	}
}

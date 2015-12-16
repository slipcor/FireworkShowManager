package net.slipcor.fsm.classes;

import org.bukkit.scheduler.BukkitRunnable;
import net.slipcor.fsm.FireworkShowPlugin;
import net.slipcor.fsm.managers.ShowManager;
import net.slipcor.fsm.runnables.BeatRunnable;

public class Show {
	private final FireworkShowPlugin fsm;
	private final String name = "default";
	
	private int duration;   // intervals
	private int interval;  // ticks
	
	private BukkitRunnable beat;

	public Show(FireworkShowPlugin plugin) {
		fsm = plugin;
		duration = fsm.getConfig().getInt(name + ".duration", 2 * 60 * 5);
		interval = fsm.getConfig().getInt(name + ".int", 10);
	}

	public int getDuration() {
		return duration;
	}

	public String getName() {
		return name;
	}

	public int getInterval() {
		return interval;
	}

	public void setBigInterval(int value) {
		fsm.getConfig().set(name + ".bigint", value);
		fsm.saveConfig();
	}

	public void setDuration(int value) {
		duration = value;
		fsm.getConfig().set(name + ".duration", value);
		fsm.saveConfig();
	}

	public void setInterval(int value) {
		interval = value;
		fsm.getConfig().set(name + ".int", value);
		fsm.saveConfig();
	}

	public void setTime(int value) {
		fsm.getConfig().set(name + ".time", value);
		fsm.saveConfig();
	}

	public void setStarting(boolean b) {
		fsm.getConfig().set(name + ".starting", b);
		fsm.saveConfig();
	}

	public void start() {
		if (beat != null) {
			beat.cancel();
			beat = null;
		}
		
		beat = new BeatRunnable(fsm, name, duration, interval);
	}

	public void stop(boolean full) {
		if (full) {
			ShowManager.stop();
		}
		
		if (beat != null) {
			beat.cancel();
			beat = null;
		}
	}

}

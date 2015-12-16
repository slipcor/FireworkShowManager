package net.slipcor.fsm.managers;

import org.bukkit.scheduler.BukkitRunnable;
import net.slipcor.fsm.FireworkShowPlugin;
import net.slipcor.fsm.classes.Show;
import net.slipcor.fsm.runnables.HeartBeatRunnable;

public class ShowManager {
	private static Show show;
	private static FireworkShowPlugin fsm;
	private static BukkitRunnable heartBeat;
	
	public ShowManager(FireworkShowPlugin plugin) {
		fsm = plugin;
	}
	
	public static Show getShow() {
		if (show == null) {
            show = new Show(fsm);
        }
		return show;
	}

	public void start() {
		if (heartBeat != null) {
			heartBeat.cancel();
			heartBeat = null;
		}

		heartBeat = new HeartBeatRunnable(fsm, getShow());
		getShow().setStarting(true);
	}

	public static void stop() {
		if (heartBeat != null) {
			heartBeat.cancel();
			heartBeat = null;
		}
		getShow().setStarting(false);
	}
}

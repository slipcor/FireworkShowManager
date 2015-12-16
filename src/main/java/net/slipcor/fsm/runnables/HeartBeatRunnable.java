package net.slipcor.fsm.runnables;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import net.slipcor.fsm.FireworkShowPlugin;
import net.slipcor.fsm.classes.Show;

import org.bukkit.scheduler.BukkitRunnable;

public class HeartBeatRunnable extends BukkitRunnable {
	final FireworkShowPlugin fsm;
	final Show show;
	final long start;
	final int interval;
	
	int count = 0;

	public HeartBeatRunnable(FireworkShowPlugin plugin, Show availableShow) {
		fsm = plugin;
		show = availableShow;
		interval = fsm.getConfig().getInt(show.getName() + ".bigint", 60*60);
		
		long millis = System.currentTimeMillis();
		
		Timestamp t = new Timestamp (millis);
		
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String dateString = sdf.format(t);

		int currentHour = Integer.parseInt(dateString.substring(0, 2));
		int currentMinute = Integer.parseInt(dateString.substring(2, 4));
		int currentSecond = Integer.parseInt(dateString.substring(4));
		
		int time = fsm.getConfig().getInt(show.getName() + ".time", 0);
		

		millis -= currentSecond*1000;
		// millis now is the last minute that started
		millis -= currentMinute*60*1000;
		// millis now is the last hour that started
		
		if (time <= currentHour) {
			// we missed it ! assume we have started it in the past
			
			millis -= (currentHour - time) * 60 * 60 * 1000;
		} else {
			// time > currentHour
			
			/*
			 * time = 18
			 * currentHour = 16
			 */
			
			millis -= (24 - time + currentHour) * 60 * 60 * 1000;
		}
		
		start = millis;
		count = (int) (System.currentTimeMillis() - millis) / (interval * 1000);
		count++;
		
		
		this.runTaskTimer(fsm, 20L * 60, 20L * 60);
	}

	@Override
	public void run() {
		if (System.currentTimeMillis() > (start + (count * interval * 1000))) {
			count++;
			commit();
		}
	}

	public void commit() {
		show.start();
	}
}

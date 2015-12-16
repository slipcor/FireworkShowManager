package net.slipcor.fsm.runnables;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.slipcor.fsm.FireworkShowPlugin;
import net.slipcor.fsm.utilities.FireworkLanguage.MSG;
import net.slipcor.fsm.utilities.FireworkUtil;
import net.slipcor.fsm.utilities.StringUtil;

public class BeatRunnable extends BukkitRunnable {
	private int pos;
	private final int duration;
	private final FireworkShowPlugin plugin;

	private final Map<Integer, List<String>> spawns = new HashMap<Integer, List<String>>();
	private final Map<Integer, List<String>> fireworks = new HashMap<Integer, List<String>>();
	
	public BeatRunnable(final FireworkShowPlugin fsm, final String name, final int duration, final int interval) {
		this.duration = duration;

		plugin = fsm;
        Set<String> keys;
        try {
			keys = fsm.getConfig().getConfigurationSection(name+".spawns").getKeys(false);
		} catch (Exception e) {
			plugin.getLogger().severe("You did not set spawns! Aborting timer!");
			return;
		}
		
		for (String key : keys) {
			final int position = Integer.parseInt(key.substring(1));
			
			spawns.put(position, fsm.getConfig().getConfigurationSection(name+".spawns").getStringList(key));
		}

		try {
			keys = fsm.getConfig().getConfigurationSection(name+".fireworks").getKeys(false);
		} catch (Exception e) {
			plugin.getLogger().severe("You did not set fireworks! Aborting timer!");
			return;
		}
		
		for (String key : keys) {
			final int position = Integer.parseInt(key.substring(1));
			
			fireworks.put(position, fsm.getConfig().getConfigurationSection(name+".fireworks").getStringList(key));
		}
		
		pos = 0;

		Bukkit.getServer().broadcastMessage(MSG.SHOW_STARTING.toString());
		this.runTaskTimer(fsm, interval, interval);
	}

	@Override
	public void run() {
		if (pos >= duration) {
			try {
				plugin.getLogger().info("cancelling BeatRunnable!");
				cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Bukkit.getServer().broadcastMessage(MSG.SHOW_OVER.toString());
		}
		final List<String> spawnList = spawns.get(pos);
		final List<String> listFireworks = fireworks.get(pos);
		pos++;

		if (spawnList == null || spawnList.isEmpty() ||
				listFireworks == null || listFireworks.isEmpty()) {
			return;
		}
		
		for (String s : spawnList) {
			final Location loc = StringUtil.parseToLocation(s);
			loc.add(0.5, 0.5, 0.5);
			final Entity ent = loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
			final Firework fire = (Firework) ent;
			FireworkMeta fireMeta = fire.getFireworkMeta();
			int power = 0;
			
			
			for (String sFirework : listFireworks) {
				if (sFirework.contains("|")) {
					final String[] splitFirework = sFirework.split("\\|");
					sFirework = splitFirework[0];
					try {
						int value = Integer.parseInt(splitFirework[1]);
						if (value == -1) {
							power = -1;
						} else {
							power = Math.max(power,value);
						}
					} catch (Exception e1) {
						plugin.getLogger().warning("invalid power '" + splitFirework[1] + "' for " + sFirework);
					}
				} 
				fireMeta = FireworkUtil.addFromString(fireMeta, sFirework);
			}
			
			fireMeta.setPower(power<1?2:power);
			
			fire.setFireworkMeta(fireMeta);
			
			if (power < 0) {
				try {
					class RunLater implements Runnable {

						@Override
						public void run() {
							fire.detonate();
						}
						
					}
					Bukkit.getScheduler().runTaskLater(plugin, new RunLater(), 1L);
				} catch (Exception e) {
					// you don't support this :P
				}
			}
		}
	}

}

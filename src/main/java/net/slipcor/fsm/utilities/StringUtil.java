package net.slipcor.fsm.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class StringUtil {
	public static String parseFromLocation(Location loc) {
		return loc.getWorld().getName() + ":" +
				loc.getBlockX() + ":" +
				loc.getBlockY() + ":" +
				loc.getBlockZ();
	}
	public static Location parseToLocation(String s) {
		String[] split = s.split(":");
		
		World w = Bukkit.getWorld(split[0]);

		int x = Integer.parseInt(split[1]);
		int y = Integer.parseInt(split[2]);
		int z = Integer.parseInt(split[3]);
		
		return w.getBlockAt(x, y, z).getLocation();
	}
}

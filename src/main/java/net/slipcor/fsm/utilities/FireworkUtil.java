package net.slipcor.fsm.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkUtil {
	static final Random r = new Random();
	static final HashMap<Character, Type> typeMap = new HashMap<Character, Type>();
	
	public static final boolean debug = false;
	
	static {
		/*
		 * type: (int) BALL, BALL_LARGE, STAR, BURST, CREEPER;
		 *     ['.','o'] => BALL
		 *     ['O','0'] => BALL_LARGE
		 *     ['*'] => STAR
		 *     ['x','X','y','Y'] => BURST
		 *     ['c','C','#'] => CREEPER
		 *     ['?'] => RANDOM
		 */
		typeMap.put('.', Type.BALL);
		typeMap.put('o', Type.BALL);
		typeMap.put('O', Type.BALL_LARGE);
		typeMap.put('0', Type.BALL_LARGE);
		typeMap.put('*', Type.STAR);
		typeMap.put('x', Type.BURST);
		typeMap.put('X', Type.BURST);
		typeMap.put('y', Type.BURST);
		typeMap.put('Y', Type.BURST);
		typeMap.put('c', Type.CREEPER);
		typeMap.put('C', Type.CREEPER);
		typeMap.put('#', Type.CREEPER);
		
	}
	
	/*
	 * fadeColors: List<Color>
	 *     '000000,FFFFFF'
	 *     
	 * ////////////////////////////////////
	 * examples
	   ////////////////////////////////////
	 * "otf:FF0000,00FF00" - ball,trail,flicker,red+lime
	 * "*f:000,F00,FF0:0F0,FFF,00F" - star,flicker,black+red+yellow->red+white+blue
	 * "x:000,fff" - burst,black+white
	 * "?ft:?" - random shape,flicker,trail,random color (1 max)
	 * "???:???:???" - pure randomness
	 * "???:???:???|1" - pure randomness, power 1
	 */
	
	public static FireworkMeta addFromString(FireworkMeta fm, String line) {
		debug("addFromString: " + line);
		
		String[] split = line.split(":");
		
		if (split.length == 0) {
			throw new UnsupportedOperationException("Split between ':' failed for: " + line);
		}
		
		Builder b = parseFirstString(split[0]);

		String color = (split.length < 2) ? "?" : split[1];
		
		b = parseSecondString(b, color);
		
		if (split.length > 2) {
			b = parseThirdString(b, split[2]);
		}

		debug("adding Effect!");
		
		fm.addEffect(b.build());
		
		return fm;
	}

	private static Color getRandomColor() {
		return Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256));
	}

	private static Type getTypeFromChar(Character c) {
		return typeMap.containsKey(c) ? typeMap.get(c) : Type.values()[r.nextInt(Type.values().length)];
	}
	
	private static Builder parseFirstString(String s) {
		debug("----------------------");
		debug("parseFirstString: " + s);
		Builder b = FireworkEffect.builder();
		if (s == null || s.length() < 1 || (s.charAt(0) == '?')) {
			debug("- random type!");
			b = b.with(Type.values()[r.nextInt(Type.values().length)]);
            if (s == null) {
                s = "?";
            }
		} else {
			debug("- type: " + s.charAt(0));
			b = b.with(getTypeFromChar(s.charAt(0)));
		}
		/*
		 * effects: (byte) NONE, TRAIL, FLICKER, TRAILFLICKER
		 *     ['T','t'] => TRAIL
		 *     ['F','f'] => FLICKER
		 *     ['?'] => RANDOM
		 */
		if (s.toLowerCase().contains("t") || (s.endsWith("?") && r.nextBoolean())) {
			debug("- TRAIL");
			b = b.withTrail();
		}
		if (s.toLowerCase().contains("f") || (s.endsWith("?") && r.nextBoolean())) {
			debug("- FLICKER");
			b = b.withFlicker();
		}
		return b;
	}


	private static Builder parseSecondString(Builder b, String color) {
		debug("parseSecondString: " + color);
		/*
		 * colors: Color[]
		 *     'FF0000,00FF00'
		 */
		if (color.equals("???") || color.equals("??") || color.equals("?")) {
			Color[] list = new Color[color.length()];
			for (int i = 0; i< list.length; i++) {
				list[i] = getRandomColor();
				debug("-- random "+i+" = " + list[i].toString());
			}
			return b.withColor(list);
		}

		debug("-- adding Color:");
		return b.withColor(parseStringToIterableColorString(color));
	}

	private static Builder parseThirdString(Builder b, String color) {
		debug("parseThirdString: " + color);
		/*
		 * colors: Color[]
		 *     'FF0000,00FF00'
		 */
		if (color.equals("???") || color.equals("??") || color.equals("?")) {
			Color[] list = new Color[color.length()];
			for (int i = 0; i< list.length; i++) {
				list[i] = getRandomColor();
				debug("--- random "+i+" = " + list[i].toString());
			}
			return b.withFade(list);
		}

		debug("--- adding Color(s):");
		return b.withFade(parseStringToIterableColorString(color));
	}

	private static List<Color> parseStringToIterableColorString(String color) {
		debug("PARSING COLOR(S): " + color);
		String[] colors = color.split(",");
		
		colors = sanitize(colors);
		
		List<Color> list = new ArrayList<Color>();
		
		for (String cString : colors) {
			debug("#### " + cString);
			list.add(Color.fromRGB(Integer.parseInt(cString, 16)));
		}
		return list;
	}
	
	private static String[] sanitize(String[] colors) {
		for (int i = 0; i < colors.length; i++) {
			if (colors[i].length() == 3) {
				debug("before: " + colors[i]);
				colors[i] = "" + colors[i].charAt(0) + colors[i].charAt(0) + 
						colors[i].charAt(1) + colors[i].charAt(1) +
						colors[i].charAt(2) + colors[i].charAt(2);
				debug("after: " + colors[i]);
			}
		}
		return colors;
	}
	
	private static void debug(String s) {
		if (debug) {
			Bukkit.getServer().getLogger().info(s);
		}
	}
}

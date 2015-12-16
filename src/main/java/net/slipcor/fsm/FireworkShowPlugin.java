package net.slipcor.fsm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.slipcor.fsm.commands.CommandGeneral;
import net.slipcor.fsm.commands.CommandModerate;
import net.slipcor.fsm.commands.CommandSetup;
import net.slipcor.fsm.managers.ShowManager;
import net.slipcor.fsm.utilities.FireworkLanguage.ErrorCode;
import net.slipcor.fsm.utilities.FireworkLanguage.MSG;
import net.slipcor.fsm.utilities.StringUtil;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FireworkShowPlugin extends JavaPlugin implements Listener {
	static final String prefix = "[FWSM] ";
	static ShowManager manager;
	
	static final Map<String, Integer> settings = new HashMap<String, Integer>();
	
	public Map<String, Integer> getSettings() {
		return settings;
	}
	
	public ShowManager getShowManager() {
		return manager;
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("fws").setExecutor(new CommandGeneral(this));
		getCommand("fwsm").setExecutor(new CommandModerate(this));
		getCommand("fwss").setExecutor(new CommandSetup(this));
		manager = new ShowManager(this);
		
		if (getConfig().getBoolean(ShowManager.getShow().getName() + ".starting", false)) {
			manager.start();
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !settings.containsKey(event.getPlayer().getName())) {
			return;
		}
		
		int i = settings.get(event.getPlayer().getName());
		
		String node = "default.spawns.t"+i;
		
		List<String> list = getConfig().getStringList(node);
		
		Location thisLoc = event.getClickedBlock().getLocation();
		
		for (String s : list) {
			if (thisLoc.distance(StringUtil.parseToLocation(s)) < 1) {
				event.getPlayer().sendMessage(MSG.ALREADY_SET.toString());
				return;
			}
		}
		String s = StringUtil.parseFromLocation(thisLoc);
		list.add(s);
		getConfig().set(node, list);
		saveConfig();
		event.getPlayer().sendMessage(MSG.SET_LOCATION.parse(s));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {

		if (!settings.containsKey(sender.getName())) {
			return true;
		}
		
		int i = settings.get(sender.getName());
		
		String node = "default.fireworks.t"+i;
		
		List<String> list = getConfig().getStringList(node);
		
		list.add(args[0]);
		getConfig().set(node, list);
		saveConfig();
		
		sender.sendMessage(MSG.SET_FIREWORK.parse(args[0]));
		
		return true;
	}

	public void tellError(CommandSender sender, ErrorCode error) {
		sender.sendMessage(prefix + ChatColor.RED + error);
	}
}

package net.slipcor.fsm.commands;

import net.slipcor.fsm.FireworkShowPlugin;
import net.slipcor.fsm.managers.ShowManager;
import net.slipcor.fsm.utilities.FireworkLanguage.ErrorCode;
import net.slipcor.fsm.utilities.FireworkLanguage.MSG;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandGeneral implements CommandExecutor {
	private final FireworkShowPlugin fsm;

	public CommandGeneral(FireworkShowPlugin plugin) {
		fsm = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender,
			Command cmd, String label,
			String[] args) {
		
		if (args.length > 0 && args[0].contains("?")) {
			displayHelp(sender);
			return true;
		}
		
		if (args.length < 2) {
			fsm.tellError(sender, ErrorCode.ARGS_LENGTH);
			displayHelp(sender);
			return true;
		}

		int value;
		try {
			value = Integer.parseInt(args[1]);
		} catch (Exception e) {
			fsm.tellError(sender, ErrorCode.ARG2_NOT_NUMERIC);
			displayHelp(sender);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("time")) {
			ShowManager.getShow().setTime(value);
			sender.sendMessage(MSG.SET_TIME.parse(value + ""));
		} else if (args[0].equalsIgnoreCase("duration")) {
			ShowManager.getShow().setDuration(value);
			sender.sendMessage(MSG.SET_DURATION.parse(value + ""));
		} else if (args[0].equalsIgnoreCase("interval")) {
			ShowManager.getShow().setInterval(value);
			sender.sendMessage(MSG.SET_INTERVAL.parse(value + ""));
		} else if (args[0].equalsIgnoreCase("biginterval")) {
			ShowManager.getShow().setBigInterval(value);
			sender.sendMessage(MSG.SET_BIGINTERVAL.parse(value + ""));
		} else  {
			fsm.tellError(sender, ErrorCode.ARG1_UNKNOWN);
			displayHelp(sender);
		}
		return true;
	}
	
	private void displayHelp(CommandSender sender) {
		String[] message = new String[]{
				"/fws time [hour] - the starting hour time (18=6pm)",
				"/fws duration [intervals] - the interval count",
				"/fws interval [ticks] - the interval duration in ticks",
				"/fws biginterval [seconds] - the SHOW interval"
		};
		sender.sendMessage(message);
	}
}

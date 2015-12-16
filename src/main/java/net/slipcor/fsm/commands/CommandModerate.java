package net.slipcor.fsm.commands;

import net.slipcor.fsm.FireworkShowPlugin;
import net.slipcor.fsm.managers.ShowManager;
import net.slipcor.fsm.utilities.FireworkLanguage.ErrorCode;
import net.slipcor.fsm.utilities.FireworkLanguage.MSG;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandModerate implements CommandExecutor {
	private final FireworkShowPlugin fsm;

	public CommandModerate(FireworkShowPlugin plugin) {
		fsm = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender,
			Command cmd, String label,
			String[] args) {
		if (args.length < 1) {
			fsm.tellError(sender, ErrorCode.ARGS_LENGTH);
			displayHelp(sender);
			return true;
		}
		
		if (args[0].contains("?")) {
			displayHelp(sender);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("test")) {
			ShowManager.getShow().start();
			sender.sendMessage(MSG.TEST_DONE.toString());
		} else if (args[0].equalsIgnoreCase("start")) {
			fsm.getShowManager().start();
			sender.sendMessage(MSG.START_DONE.toString());
		} else if (args[0].equalsIgnoreCase("stop")) {
			ShowManager.getShow().stop(false);
			sender.sendMessage(MSG.STOP_DONE.toString());
		} else if (args[0].equalsIgnoreCase("fullstop")) {
			ShowManager.getShow().stop(true);
			sender.sendMessage(MSG.STOPFULL_DONE.toString());
		} else {
			fsm.tellError(sender, ErrorCode.ARG1_UNKNOWN);
			displayHelp(sender);
		}
		return true;
	}
	
	private void displayHelp(CommandSender sender) {
		String[] message = new String[]{
				"/fwsm test - start the show just for fun",
				"/fwsm stop - stop any running show",
				"/fwsm fullstop - stop the main timer"
		};
		sender.sendMessage(message);
	}
}

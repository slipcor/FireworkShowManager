package net.slipcor.fsm.commands;

import net.slipcor.fsm.FireworkShowPlugin;
import net.slipcor.fsm.classes.Show;
import net.slipcor.fsm.managers.ShowManager;
import net.slipcor.fsm.utilities.FireworkLanguage.ErrorCode;
import net.slipcor.fsm.utilities.FireworkLanguage.MSG;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSetup implements CommandExecutor {
	private final FireworkShowPlugin fsm;

	public CommandSetup(FireworkShowPlugin plugin) {
		fsm = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender,
			Command cmd, String label,
			String[] args) {
		Show s = ShowManager.getShow();
		
		int value;
		try {
			value = Integer.parseInt(args[0]);
			
			if (value > s.getDuration()) {
				fsm.tellError(sender, ErrorCode.INTERVAL_TOO_HIGH);
				return true;
			}
			
			fsm.getSettings().put(sender.getName(), value);
			sender.sendMessage(MSG.NOW_SETTING.parse("" + value));
			
		} catch (Exception e) {
			if (args.length > 0 && args[0].equals("done")) {
				fsm.getSettings().remove(sender.getName());
				sender.sendMessage(MSG.SETTING_DONE.toString());
				return true;
			}
			fsm.tellError(sender, ErrorCode.ARG1_NOT_NUMERIC);
			displayHelp(sender);
		}

		return true;
	}
	
	private void displayHelp(CommandSender sender) {
		String[] message = new String[]{
				"/fwss [n] - set spawn points for nth interval"
		};
		sender.sendMessage(message);
	}
}

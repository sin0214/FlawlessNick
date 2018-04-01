package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.simplyrin.flawlessnick.Main;

public class NickRank extends CommandBase {

	@Override
	public String getCommandName() {
		return "nickrank";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/nickrank <prefix>";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length == 0) {
			Main.getInstance().sendMessage("&cUsage: /nickrank <prefix>");
			return;
		}

		Main.getInstance().getNickManager().setPrefix("MHF_Alex");
		Main.getInstance().sendMessage("&aYou are nicked rank is " + args[0] + "&a!");
	}

}


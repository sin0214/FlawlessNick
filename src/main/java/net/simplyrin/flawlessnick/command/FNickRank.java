package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.simplyrin.flawlessnick.FlawlessNick;

public class FNickRank extends CommandBase {

	@Override
	public String getCommandName() {
		return "fnickrank";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/fnickrank <prefix>";
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
		if(args.length > 0) {
			FlawlessNick.getInstance().getNickManager().setPrefix(args[0]);
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aSet your nick rank to " + args[0] + "&a!");
			return;
		}

		FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cUsage: /fnickrank <prefix>");
		return;
	}

}

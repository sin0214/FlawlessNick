package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.simplyrin.flawlessnick.FlawlessNick;

public class FNickServer extends CommandBase {

	@Override
	public String getCommandName() {
		return "fnickserver";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "&cUsage: /fnickserver <servernickname>";
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
			FlawlessNick.getInstance().getNickManager().setServerNickName(args[0]);
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aYour server nickname has been set to "+args[0]+"!");
			return;
		}

		FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cUsage: /fnickserver <servernickname>");
		return;
	}

}

package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.simplyrin.flawlessnick.FlawlessNick;

public class Nick extends CommandBase {

	@Override
	public String getCommandName() {
		return "nick";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "&cUsage: /nick <nickname>";
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
		if(!FlawlessNick.getInstance().isInfo()) {
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + FlawlessNick.getInstance().getInfoMessage());
			return;
		}

		if(args.length == 0) {
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cUsage: /nick <nickname>");
			return;
		}

		for(String name : FlawlessNick.getInstance().getDisabledList()) {
			if(args[0].toLowerCase().contains(name.toLowerCase())) {
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cThis name is not allowed!");
				return;
			}
		}

		if(args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("clear")) {
			FlawlessNick.getInstance().getMinecraft().renderGlobal.loadRenderers();
			FlawlessNick.getInstance().getSkinManager().reset();
			FlawlessNick.getInstance().getNickManager().setNick(false);
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aYour nick has been reset!");
			return;
		}

		FlawlessNick.getInstance().getMinecraft().renderGlobal.loadRenderers();
		FlawlessNick.getInstance().getSkinManager().update(args[0]);
		FlawlessNick.getInstance().getNickManager().setNick(true);
		FlawlessNick.getInstance().getNickManager().setNickName(args[0]);
		FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aYou are now nicked as " + args[0] + "&a!");
	}

}

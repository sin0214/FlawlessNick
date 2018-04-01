package net.simplyrin.flawlessnick.command;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.simplyrin.flawlessnick.Main;

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
		if(!Main.getInstance().isInfo()) {
			Main.getInstance().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getInfoMessage());
			return;
		}

		if(args.length == 0) {
			Main.getInstance().sendMessage(Main.getInstance().getPrefix() + "&cUsage: /nick <nickname>");
			return;
		}

		for(String name : Main.getInstance().getDisabledList()) {
			if(args[0].toLowerCase().contains(name.toLowerCase())) {
				Main.getInstance().sendMessage(Main.getInstance().getPrefix() + "&cThis name is not allowed!");
				return;
			}
		}

		if(args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("clear")) {
			Main.getInstance().getSkinManager().update(Minecraft.getMinecraft().thePlayer.getName());
			Main.getInstance().getNickManager().setNick(false);
			Main.getInstance().sendMessage("&aYour nick has been reset!");
			return;
		}

		Minecraft.getMinecraft().renderGlobal.loadRenderers();
		Main.getInstance().getSkinManager().update(args[0]);
		Main.getInstance().getNickManager().setNick(true);
		Main.getInstance().getNickManager().setNickname(args[0]);
		Main.getInstance().sendMessage("&aYou are now nicked as " + args[0] + "&a!");
	}

}

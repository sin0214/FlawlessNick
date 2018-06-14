package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.simplyrin.flawlessnick.FlawlessNick;
import work.siro.mod.flawlessnick.gui.GuiFlawlessNick;

public class FNick extends CommandBase {

	@Override
	public String getCommandName() {
		return "fnick";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "&cUsage: /fnick <nickname>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!FlawlessNick.getInstance().isInfo()) {
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + FlawlessNick.getInstance().getInfoMessage());
			return;
		}

		if(FlawlessNick.getInstance().getJsonLoader().getString("Mode").equalsIgnoreCase("Gui")) {
			new GuiFlawlessNick().display();
			return;
		}

		if(args.length > 0) {
			for(String name : FlawlessNick.getInstance().getDisabledList()) {
				if(args[0].toLowerCase().contains(name.toLowerCase())) {
					FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cThis name is not allowed!");
					return;
				}
			}

			if(args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("clear")) {
				FlawlessNick.getInstance().getMinecraft().renderGlobal.loadRenderers();
				FlawlessNick.getInstance().getNickManager().setNick(false);
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aYour nick has been reset!");
				return;
			}

			if(args[0].length() > 16) {
				args[0] = args[0].substring(0, 15);
			}

			FlawlessNick.getInstance().getMinecraft().renderGlobal.loadRenderers();
			FlawlessNick.getInstance().getNickManager().setNickName(args[0]);
			FlawlessNick.getInstance().getNickManager().setNick(true);
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aYou are now nicked as " + args[0] + "&a!");
			return;
		}

		FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cUsage: /fnick <nickname>");
		return;
	}

}

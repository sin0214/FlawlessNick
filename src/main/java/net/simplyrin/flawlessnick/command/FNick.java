package net.simplyrin.flawlessnick.command;

import java.io.File;

import club.sk1er.utils.JsonHolder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.simplyrin.flawlessnick.FlawlessNick;
import net.simplyrin.flawlessnick.Json;
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

		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("mode")) {
				if(args.length > 1) {
					JsonHolder json = FlawlessNick.getInstance().getJsonHolder();

					if(args[1].equalsIgnoreCase("cui")) {
						json.put("Mode", "CUI");
						Json.saveJson(json, new File("mods/FlawlessNick/config.json"));
						FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aFlawlessNick is now CUI mode!");
						return;
					}

					if(args[1].equalsIgnoreCase("gui")) {
						json.put("Mode", "GUI");
						Json.saveJson(json, new File("mods/FlawlessNick/config.json"));
						FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aFlawlessNick is now GUI mode!");
						return;
					}
				}
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cUsage: /fnick mode <cui/gui>");
				return;
			}
		}

		if(FlawlessNick.getInstance().getJsonHolder().getString("Mode").equalsIgnoreCase("Gui")){
			new GuiFlawlessNick().display();
			return;
		}

		if(args.length == 0) {
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cUsage: /fnick <nickname>");
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

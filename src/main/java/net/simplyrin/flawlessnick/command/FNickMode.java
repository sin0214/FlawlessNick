package net.simplyrin.flawlessnick.command;

import java.io.File;

import club.sk1er.utils.JsonHolder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.simplyrin.flawlessnick.FlawlessNick;
import net.simplyrin.flawlessnick.Json;

public class FNickMode extends CommandBase {

	@Override
	public String getCommandName() {
		return "fnickmode";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "&cUsage: /fnickmode <gui/cui>";
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
			JsonHolder json = FlawlessNick.getInstance().getJsonHolder();
			if(args[0].equalsIgnoreCase("cui")) {
				json.put("Mode", "CUI");
				Json.saveJson(json, new File("mods/FlawlessNick/config.json"));
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aFlawlessNick is now CUI mode!");
				return;
			}
			if(args[0].equalsIgnoreCase("gui")) {
				json.put("Mode", "GUI");
				Json.saveJson(json, new File("mods/FlawlessNick/config.json"));
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aFlawlessNick is now GUI mode!");
				return;
			}
		}
		FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cUsage: /fnickmode <cui/gui>");
		return;
	}

}

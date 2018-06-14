package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.simplyrin.flawlessnick.FlawlessNick;
import net.simplyrin.jsonloader.JsonLoader;

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
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!FlawlessNick.getInstance().isInfo()) {
			FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + FlawlessNick.getInstance().getInfoMessage());
			return;
		}

		if(args.length > 0) {
			JsonLoader json = FlawlessNick.getInstance().getJsonLoader();
			if(args[0].equalsIgnoreCase("cui")) {
				json.put("Mode", "CUI");
				JsonLoader.saveJson(json, "mods/FlawlessNick/config.json");
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aFlawlessNick is now CUI mode!");
				return;
			}
			if(args[0].equalsIgnoreCase("gui")) {
				json.put("Mode", "GUI");
				JsonLoader.saveJson(json, "mods/FlawlessNick/config.json");
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aFlawlessNick is now GUI mode!");
				return;
			}
		}
		FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cUsage: /fnickmode <cui/gui>");
		return;
	}

}

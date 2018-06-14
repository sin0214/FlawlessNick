package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.simplyrin.flawlessnick.FlawlessNick;
import net.simplyrin.flawlessnick.utils.JsonLoader;

/**
 * Copyright (C) 2018 FlawlessNick
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

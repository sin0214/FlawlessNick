package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.simplyrin.flawlessnick.FlawlessNick;
import work.siro.mod.flawlessnick.gui.GuiFlawlessNick;

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

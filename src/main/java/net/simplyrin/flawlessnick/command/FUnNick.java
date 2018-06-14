package net.simplyrin.flawlessnick.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.simplyrin.flawlessnick.FlawlessNick;

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
public class FUnNick extends CommandBase {

	@Override
	public String getCommandName() {
		return "funnick";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/funnick";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		FlawlessNick.getInstance().getMinecraft().renderGlobal.loadRenderers();
		FlawlessNick.getInstance().getNickManager().setNick(false);
		FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aYour nick has been reset!");
	}

}

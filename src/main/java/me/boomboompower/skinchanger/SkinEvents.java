/*
 *	 Copyright (C) 2017 boomboompower
 *
 *	 This program is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU General Public License as published by
 *	 the Free Software Foundation, either version 3 of the License, or
 *	 (at your option) any later version.
 *
 *	 This program is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU General Public License for more details.
 *
 *	 You should have received a copy of the GNU General Public License
 *	 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.boomboompower.skinchanger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.simplyrin.flawlessnick.FlawlessNick;

public class SkinEvents {

	/*
	 *     Copyright (C) 2017 boomboompower
	 *
	 *     This program is free software: you can redistribute it and/or modify
	 *     it under the terms of the GNU General Public License as published by
	 *     the Free Software Foundation, either version 3 of the License, or
	 *     (at your option) any later version.
	 *
	 *     This program is distributed in the hope that it will be useful,
	 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
	 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 *     GNU General Public License for more details.
	 *
	 *     You should have received a copy of the GNU General Public License
	 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
	 */
	private int currentTick = 100;

	/* @SubscribeEvent
	public void onRender(RenderWorldLastEvent event) {
		if(Minecraft.getMinecraft().currentScreen == null) {
			FlawlessNick.getInstance().getSkinManager().updatePlayer(null);
			if (!FlawlessNick.getInstance().getSkinManager().getSkinName().isEmpty()) {
				FlawlessNick.getInstance().getSkinManager().updateSkin();
			}
		}
	} */

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (Minecraft.getMinecraft().currentScreen == null) {
			if (this.currentTick > 0) {
				--this.currentTick;
			} else {
				this.currentTick = 100;
				if (!FlawlessNick.getInstance().getSkinManager().getSkinName().isEmpty()) {
					FlawlessNick.getInstance().getSkinManager().updateSkin();
				}
			}
		}
	}

}

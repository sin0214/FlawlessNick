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

import me.boomboompower.skinchanger.utils.fake.FakePlayer;
import me.boomboompower.skinchanger.utils.fake.FakePlayerCape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.simplyrin.flawlessnick.FlawlessNick;

import java.util.Iterator;
import java.util.List;

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

	@SubscribeEvent
	public void onRender(RenderWorldLastEvent event) {
		if(Minecraft.getMinecraft().currentScreen == null) {
			FlawlessNick.getInstance().getSkinManager().updatePlayer(null);
			if (!FlawlessNick.getInstance().getSkinManager().getSkinName().isEmpty()) {
				FlawlessNick.getInstance().getSkinManager().updateSkin();
			}
		}
	}

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

				if (FlawlessNick.getInstance().getCapeManager().isUsingCape()) {
					if (FlawlessNick.getInstance().getCapeManager().isExperimental()) {
                        FlawlessNick.getInstance().getCapeManager().giveOfCape(
                                FlawlessNick.getInstance().getCapeManager().getOfCapeName());
					} else {
						FlawlessNick.getInstance().getCapeManager().addCape();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onRender(RenderPlayerEvent.Pre event) {
		if (event.entityPlayer instanceof FakePlayer) {
			List<LayerRenderer<?>> layerRenderers = ReflectionHelper.getPrivateValue(RendererLivingEntity.class, event.renderer, "layerRenderers", "field_177097_h");

			boolean modified = false;
			boolean hasFakeCape = false;

			Iterator<LayerRenderer<?>> iterator = layerRenderers.iterator();

			// Wipe out all cape rendering instances, whilst testing for ours
			while (iterator.hasNext()) {
				LayerRenderer<?> layerRenderer = iterator.next();

				if (layerRenderer instanceof LayerCape) {
					modified = true;

					iterator.remove();
				} else if (layerRenderer instanceof FakePlayerCape) {
					hasFakeCape = true;
				}
			}

			if (!hasFakeCape) {
				modified = true;

				layerRenderers.add(new FakePlayerCape(event.renderer));
			}

			if (modified) {
				ReflectionHelper.setPrivateValue(RendererLivingEntity.class, event.renderer, layerRenderers, "layerRenderers", "field_177097_h");
			}
		}
	}

}

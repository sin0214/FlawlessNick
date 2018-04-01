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

package me.boomboompower.skinchanger.skins;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.boomboompower.skinchanger.utils.ReflectUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class SkinManager {

	private List<String> logs = new ArrayList<>();

	private Minecraft mc;
	private EntityPlayer playerIn;

	private String skinName = "";

	public SkinManager(EntityPlayer player) {
		this.mc = Minecraft.getMinecraft();
		this.playerIn = player == null ? Minecraft.getMinecraft().thePlayer : player;
	}

	public String getSkinName() {
		return this.skinName != null ? this.skinName : "";
	}

	public void setSkinName(String skinName) {
		this.skinName = formatName(skinName);
	}

	public void updateSkin() {
		Minecraft.getMinecraft().addScheduledTask(() -> replaceSkin(this.skinName));
	}

	public void update(String skinName) {
		setSkinName(skinName);
		updateSkin();
	}

	public void reset() {
		update(playerIn.getName());
	}

	public void updatePlayer(AbstractClientPlayer playerIn) {
		this.playerIn = playerIn == null ? Minecraft.getMinecraft().thePlayer : playerIn;
	}

	/*
	 * MISC
	 */

	private String formatName(String name) {
		return name.length() > 16 ? name.substring(0, 16) : name;
	}

	private void replaceSkin(String skinName) {
		this.replaceSkin(getSkin(skinName));
	}

	public void replaceSkin(ResourceLocation location) {
		if(playerIn == null || skinName == null || skinName.isEmpty()) return;

		NetworkPlayerInfo playerInfo;

		try {
			playerInfo = (NetworkPlayerInfo) ReflectUtils.findMethod(AbstractClientPlayer.class, new String[] {"getPlayerInfo", "func_175155_b"}).invoke(playerIn);
		} catch (Throwable ex) {
			log("Could not get the players info!");
			return;
		}

		if(location != null) {
			try {
				ReflectUtils.setPrivateValue(NetworkPlayerInfo.class, playerInfo, location, "locationSkin", "field_178865_e");
			} catch (Exception ex) {
				log("Failed to set the players skin (NetworkPlayerInfo)");
			}
		}
	}

	public ResourceLocation getSkin(String name) {
		if (name != null && !name.isEmpty()) {
			final ResourceLocation location = new ResourceLocation("skins/" + name);

			File file1 = new File(new File("mods/FlawlessNick/".replace("/", File.separator), "skins"), UUID.nameUUIDFromBytes(name.getBytes()).toString());
			File file2 = new File(file1, UUID.nameUUIDFromBytes(name.getBytes()).toString() + ".png");

			final IImageBuffer imageBuffer = new ImageBufferDownload();
			ThreadDownloadImageData imageData = new ThreadDownloadImageData(file2, String.format("https://minotar.net/skin/%s", name), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
				public BufferedImage parseUserSkin(BufferedImage image) {
					if (imageBuffer != null) {
						image = imageBuffer.parseUserSkin(image);
					}
					return image;
				}
				public void skinAvailable() {
					if (imageBuffer != null) {
						imageBuffer.skinAvailable();
					}
				}
			});
			mc.renderEngine.loadTexture(location, imageData);
			return location;
		} else {
			return null;
		}
	}

	protected void log(String message, Object... replace) {
		if (logs.contains(message)) return;

		System.out.println(String.format("[SkinManager] " + message, replace));
		logs.add(message);
	}
}

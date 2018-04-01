package net.simplyrin.flawlessnick.utils;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.simplyrin.flawlessnick.Main;
import net.simplyrin.flawlessnick.Main.FieldWrapper;

/**
 * Created by SimplyRin on 2018/04/01
 */
public class CustomTabOverlay extends GuiPlayerTabOverlay {

	private Minecraft mc;
	private Ordering<NetworkPlayerInfo> ordering = Ordering.from(new PlayerComparator());
	private static FieldWrapper<IChatComponent> footer = new FieldWrapper("field_175255_h", GuiPlayerTabOverlay.class);
	private static FieldWrapper<IChatComponent> header = new FieldWrapper("field_175256_i", GuiPlayerTabOverlay.class);

	public CustomTabOverlay(Minecraft mc, GuiIngame guiIngameIn) {
		super(mc, guiIngameIn);
		this.mc = mc;
	}

	public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
		List<NetworkPlayerInfo> list = ordering.sortedCopy(this.mc.getNetHandler().getPlayerInfoMap());
		int i = 0;
		int j = 0;

		for (NetworkPlayerInfo networkplayerinfo : list) {
			int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo));
			i = Math.max(i, k);

			if(scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
				k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
				j = Math.max(j, k);
			}
		}

		list = list.subList(0, Math.min(list.size(), 80));
		int l3 = list.size();
		int i4 = l3;
		int j4;

		for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4) {
			++j4;
		}

		boolean flag = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
		int l;

		if(scoreObjectiveIn != null) {
			if(scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
				l = 90;
			} else {
				l = j;
			}
		} else {
			l = 0;
		}

		int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
		int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
		int k1 = 10;
		int l1 = i1 * j4 + (j4 - 1) * 5;
		List<String> list1 = null;
		List<String> list2 = null;

		IChatComponent header = (IChatComponent) CustomTabOverlay.header.get((Object)this);
		if(header != null) {
			list1 = this.mc.fontRendererObj.listFormattedStringToWidth(((IChatComponent) header).getFormattedText(), width - 50);

			for (String s : list1) {
				l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s));
			}
		}

		IChatComponent footer = (IChatComponent) CustomTabOverlay.footer.get((Object)this);
		if(footer != null) {
			list2 = this.mc.fontRendererObj.listFormattedStringToWidth(((IChatComponent) footer).getFormattedText(), width - 50);

			for (String s2 : list2) {
				l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s2));
			}
		}

		if(list1 != null) {
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

			for (String s3 : list1) {
				int i2 = this.mc.fontRendererObj.getStringWidth(s3);
				this.mc.fontRendererObj.drawStringWithShadow(s3, (float)(width / 2 - i2 / 2), (float)k1, -1);
				k1 += this.mc.fontRendererObj.FONT_HEIGHT;
			}

			++k1;
		}

		drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

		for(int k4 = 0; k4 < l3; ++k4) {
			int l4 = k4 / i4;
			int i5 = k4 % i4;
			int j2 = j1 + l4 * i1 + l4 * 5;
			int k2 = k1 + i5 * 9;
			drawRect(j2, k2, j2 + i1, k2 + 8, 553648127);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

			if(k4 < list.size()) {
				NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo) list.get(k4);
				String s1 = this.getPlayerName(networkPlayerInfo);

				if(Main.getMod().getNickManager().isNick()) {
					EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
					s1 = s1.replaceAll("\u00a7", "&");

					for(String prefix : Main.getMod().getRankList()) {
						if(s1.contains(prefix + " " + player.getName())) {
							s1 = s1.replace(prefix + " " + player.getName(), Main.getMod().getNickManager().getPrefix() + " " + Main.getMod().getNickManager().getNickname());
						}
					}

					if(s1.contains(player.getName())) {
						s1 = s1.replace(player.getName(), Main.getMod().getNickManager().getPrefix().substring(0, 2) + Main.getMod().getNickManager().getNickname());
					}
				}
				s1 = s1.replace("\u00a7", "&");

				GameProfile gameprofile = networkPlayerInfo.getGameProfile();

				if(flag) {
					EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
					boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
					this.mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
					int l2 = 8 + (flag1 ? 8 : 0);
					int i3 = 8 * (flag1 ? -1 : 1);
					Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float)l2, 8, i3, 8, 8, 64.0F, 64.0F);

					if(entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
						int j3 = 8 + (flag1 ? 8 : 0);
						int k3 = 8 * (flag1 ? -1 : 1);
						Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float)j3, 8, k3, 8, 8, 64.0F, 64.0F);
					}

					j2 += 9;
				}

				if(networkPlayerInfo.getGameType() == WorldSettings.GameType.SPECTATOR) {
					s1 = EnumChatFormatting.ITALIC + s1;
					this.mc.fontRendererObj.drawStringWithShadow(s1, (float)j2, (float)k2, -1862270977);
				} else {
					this.mc.fontRendererObj.drawStringWithShadow(s1, (float)j2, (float)k2, -1);
				}

				this.drawPing(i1, j2 - (flag ? 9 : 0), k2, networkPlayerInfo);
			}
		}

		if(list2 != null) {
			k1 = k1 + i4 * 9 + 1;
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

			for (String s4 : list2) {
				int j5 = this.mc.fontRendererObj.getStringWidth(s4);
				this.mc.fontRendererObj.drawStringWithShadow(s4, (float)(width / 2 - j5 / 2), (float)k1, -1);
				k1 += this.mc.fontRendererObj.FONT_HEIGHT;
			}
		}
	}

	public static boolean isObfuscated() {
		try {
			Minecraft.class.getDeclaredField("logger");
			return false;
		} catch (NoSuchFieldException e) {
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	private static class PlayerComparator implements Comparator<NetworkPlayerInfo> {

		private PlayerComparator() {
		}

		public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
			ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
			ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
			return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR)
					.compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "")
					.compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
		}

	}

}

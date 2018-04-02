package net.simplyrin.flawlessnick.utils;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.simplyrin.flawlessnick.FlawlessNick;
import net.simplyrin.flawlessnick.FlawlessNick.FieldWrapper;

/**
 * Created by SimplyRin on 2018/04/01
 */
public class CustomTabOverlay extends GuiPlayerTabOverlay {

	private Minecraft mc;
	private Ordering<NetworkPlayerInfo> ordering = Ordering.from(new PlayerComparator());
	private FieldWrapper<IChatComponent> footer = new FieldWrapper<>(isObfuscated() ? "field_175255_h" : "footer", GuiPlayerTabOverlay.class);
	private FieldWrapper<IChatComponent> header = new FieldWrapper<>(isObfuscated() ? "field_175256_i" : "header", GuiPlayerTabOverlay.class);
	private FieldWrapper<Long> lastTimeOpened = new FieldWrapper<>(isObfuscated() ? "field_175253_j" : "lastTimeOpened", GuiPlayerTabOverlay.class);

	public CustomTabOverlay(Minecraft mc, GuiIngame guiIngameIn) {
		super(mc, guiIngameIn);
		this.mc = mc;
	}

	@Override
	public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
		List<NetworkPlayerInfo> list = ordering.sortedCopy(this.mc.getNetHandler().getPlayerInfoMap());
		int i = 0;
		int j = 0;

		for (NetworkPlayerInfo networkPlayerInfo : list) {
			if(this.getPlayerName(networkPlayerInfo).contains(FlawlessNick.getInstance().getMinecraft().thePlayer.getName())) {
				if(FlawlessNick.getInstance().getNickManager().isNick()) {
					String name = this.getPlayerName(networkPlayerInfo);
					for(String prefix : FlawlessNick.getInstance().getRankList()) {
						if(name.contains(prefix + " " + FlawlessNick.getInstance().getMinecraft().thePlayer.getName())) {
							name = name.replace(prefix + " " + FlawlessNick.getInstance().getMinecraft().thePlayer.getName(), FlawlessNick.getInstance().getNickManager().getPrefix() + " " + FlawlessNick.getInstance().getNickManager().getNickName());
						}
					}
					int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkPlayerInfo).replace(FlawlessNick.getInstance().getMinecraft().thePlayer.getName(),FlawlessNick.getInstance().getNickManager().getNickName()));
					i = Math.max(i, k);

					if(scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
						k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkPlayerInfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
						j = Math.max(j, k);
					}
				} else {
					int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkPlayerInfo));
					i = Math.max(i, k);

					if(scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
						k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkPlayerInfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
						j = Math.max(j, k);
					}
				}
			} else {
				int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkPlayerInfo));
				i = Math.max(i, k);

				if(scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
					k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkPlayerInfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
					j = Math.max(j, k);
				}
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

		IChatComponent header = (IChatComponent) this.header.get((Object) this);
		if(header != null) {
			list1 = this.mc.fontRendererObj.listFormattedStringToWidth(((IChatComponent) header).getFormattedText(), width - 50);

			for(String s : list1) {
				l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s));
			}
		}

		IChatComponent footer = (IChatComponent) this.footer.get((Object) this);
		if(footer != null) {
			list2 = this.mc.fontRendererObj.listFormattedStringToWidth(((IChatComponent) footer).getFormattedText(), width - 50);

			for(String s2 : list2) {
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

				if(FlawlessNick.getInstance().isInfo()) {
					if(FlawlessNick.getInstance().getNickManager().isNick()) {
						EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
						s1 = s1.replace("\u00a7", "&");
						s1 = s1.replace("&r", "");

						for(String prefix : FlawlessNick.getInstance().getRankList()) {
							if(s1.contains(prefix + " " + player.getName())) {
								s1 = s1.replace(prefix + " " + player.getName(), FlawlessNick.getInstance().getNickManager().getPrefix() + " " + FlawlessNick.getInstance().getNickManager().getNickName());
							}
						}

						if(s1.startsWith("&bA ") || s1.startsWith("&9B ") || s1.startsWith("&8S ") || s1.startsWith("&aG ") ||
								s1.startsWith("&dP ") || s1.startsWith("&cR ") || s1.startsWith("&fW ") || s1.startsWith("&eY ")) {
							s1 = s1.replace(player.getName(), FlawlessNick.getInstance().getNickManager().getNickName());
						} else if(s1.contains("&")) {
							if(s1.equals("&a" + FlawlessNick.getInstance().getMinecraft().thePlayer.getName())) {
								s1 = s1.replace(player.getName(), FlawlessNick.getInstance().getNickManager().getNickName());
							} else if(s1.contains(player.getName())) {
								s1 = s1.replace(player.getName(), FlawlessNick.getInstance().getNickManager().getPrefix().substring(0, 2) + FlawlessNick.getInstance().getNickManager().getNickName());
							}
						} else {
							s1 = s1.replace(player.getName(), FlawlessNick.getInstance().getNickManager().getNickName());
						}

						s1 = s1.replace("&", "§");
					}
				}

				GameProfile gameProfile = networkPlayerInfo.getGameProfile();

				if(flag) {
					EntityPlayer entityPlayer = this.mc.theWorld.getPlayerEntityByUUID(gameProfile.getId());
					boolean flag1 = entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.getName().equals("Dinnerbone") || gameProfile.getName().equals("Grumm"));
					this.mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
					int l2 = 8 + (flag1 ? 8 : 0);
					int i3 = 8 * (flag1 ? -1 : 1);
					drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float) l2, 8, i3, 8, 8, 64.0F, 64.0F);

					if(entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.HAT)) {
						int j3 = 8 + (flag1 ? 8 : 0);
						int k3 = 8 * (flag1 ? -1 : 1);
						drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float) j3, 8, k3, 8, 8, 64.0F, 64.0F);
					}

					j2 += 9;
				}

				if(networkPlayerInfo.getGameType() == WorldSettings.GameType.SPECTATOR) {
					s1 = EnumChatFormatting.ITALIC + s1;
					this.mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1862270977);
				} else {
					this.mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
				}

				if (scoreObjectiveIn != null && networkPlayerInfo.getGameType() != WorldSettings.GameType.SPECTATOR) {
					int k5 = j2 + i + 1;
					int l5 = k5 + l;

					if (l5 - k5 > 5) {
						this.drawScoreboardValues(scoreObjectiveIn, k2, gameProfile.getName(), k5, l5, networkPlayerInfo);
					}
				}

				this.drawPing(i1, j2 - (flag ? 9 : 0), k2, networkPlayerInfo);
			}
		}

		if(list2 != null) {
			k1 = k1 + i4 * 9 + 1;
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

			for (String s4 : list2) {
				int j5 = this.mc.fontRendererObj.getStringWidth(s4);
				this.mc.fontRendererObj.drawStringWithShadow(s4, (float) (width / 2 - j5 / 2), (float) k1, -1);
				k1 += this.mc.fontRendererObj.FONT_HEIGHT;
			}
		}
	}

	private void drawScoreboardValues(ScoreObjective objective, int p_175247_2_, String string, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo networkPlayerInfo) {
		int i = objective.getScoreboard().getValueFromObjective(string, objective).getScorePoints();

		Long lastTimeOpened = (Long) this.lastTimeOpened.get((Object) this);

		if(objective.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
			this.mc.getTextureManager().bindTexture(icons);

			if(lastTimeOpened == networkPlayerInfo.func_178855_p()) {
				if (i < networkPlayerInfo.func_178835_l()) {
					networkPlayerInfo.func_178846_a(Minecraft.getSystemTime());
					networkPlayerInfo.func_178844_b((long) (this.mc.ingameGUI.getUpdateCounter() + 20));
				} else if (i > networkPlayerInfo.func_178835_l()) {
					networkPlayerInfo.func_178846_a(Minecraft.getSystemTime());
					networkPlayerInfo.func_178844_b((long) (this.mc.ingameGUI.getUpdateCounter() + 10));
				}
			}

			if(Minecraft.getSystemTime() - networkPlayerInfo.func_178847_n() > 1000L || lastTimeOpened != networkPlayerInfo.func_178855_p()) {
				networkPlayerInfo.func_178836_b(i);
				networkPlayerInfo.func_178857_c(i);
				networkPlayerInfo.func_178846_a(Minecraft.getSystemTime());
			}

			networkPlayerInfo.func_178843_c(lastTimeOpened);
			networkPlayerInfo.func_178836_b(i);
			int j = MathHelper.ceiling_float_int((float) Math.max(i, networkPlayerInfo.func_178860_m()) / 2.0F);
			int k = Math.max(MathHelper.ceiling_float_int((float) (i / 2)), Math.max(MathHelper.ceiling_float_int((float) (networkPlayerInfo.func_178860_m() / 2)), 10));
			boolean flag = networkPlayerInfo.func_178858_o() > (long) this.mc.ingameGUI.getUpdateCounter() && (networkPlayerInfo.func_178858_o() - (long) this.mc.ingameGUI.getUpdateCounter()) / 3L % 2L == 1L;

			if(j > 0) {
				float f = Math.min((float) (p_175247_5_ - p_175247_4_ - 4) / (float)k, 9.0F);

				if (f > 3.0F) {
					for(int l = j; l < k; ++l) {
						this.drawTexturedModalRect((float) p_175247_4_ + (float) l * f, (float) p_175247_2_, flag ? 25 : 16, 0, 9, 9);
					}

					for(int j1 = 0; j1 < j; ++j1) {
						this.drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, flag ? 25 : 16, 0, 9, 9);

						if(flag) {
							if (j1 * 2 + 1 < networkPlayerInfo.func_178860_m())
							{
								this.drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, 70, 0, 9, 9);
							}

							if (j1 * 2 + 1 == networkPlayerInfo.func_178860_m())
							{
								this.drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, 79, 0, 9, 9);
							}
						}

						if(j1 * 2 + 1 < i) {
							this.drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, j1 >= 10 ? 160 : 52, 0, 9, 9);
						}

						if (j1 * 2 + 1 == i) {
							this.drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, j1 >= 10 ? 169 : 61, 0, 9, 9);
						}
					}
				} else {
					float f1 = MathHelper.clamp_float((float)i / 20.0F, 0.0F, 1.0F);
					int i1 = (int)((1.0F - f1) * 255.0F) << 16 | (int)(f1 * 255.0F) << 8;
					String s = "" + (float)i / 2.0F;

					if (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s + "hp") >= p_175247_4_) {
						s = s + "hp";
					}

					this.mc.fontRendererObj.drawStringWithShadow(s, (float) ((p_175247_5_ + p_175247_4_) / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2), (float) p_175247_2_, i1);
				}
			}
		} else {
			String s1 = EnumChatFormatting.YELLOW + "" + i;
			this.mc.fontRendererObj.drawStringWithShadow(s1, (float) (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s1)), (float) p_175247_2_, 16777215);
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

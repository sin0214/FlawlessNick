package net.simplyrin.flawlessnick;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import club.sk1er.mods.publicmod.JsonHolder;
import club.sk1er.mods.publicmod.Multithreading;
import club.sk1er.mods.publicmod.Sk1erMod;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.simplyrin.flawlessnick.command.Nick;
import net.simplyrin.flawlessnick.command.NickRank;
import net.simplyrin.flawlessnick.utils.CustomTabOverlay;

@Mod(modid = Main.MODID, version = Main.VERSION, clientSideOnly = true)
public class Main {

	public static final String MODID = "FlawlessNick";
	public static final String VERSION = "1.0";

	private static Main mod;
	private NickManager nickManager;
	private List<String> disabledList;

	private boolean isHypixel = false;

	private boolean hasUpdate = false;
	private String updateVersion = "";
	private String updateMessage = "";

	private boolean isInfo = true;
	private String infoMessage = "";

	private Minecraft mc;
	private static FieldWrapper<GuiPlayerTabOverlay> overlay = new FieldWrapper("field_175196_v", GuiIngame.class);

	private List<String> list = Arrays.asList("&a[VIP]", "&a[VIP&6+&a]", "&b[MVP]", "&b[MVP&c+&b]", "&b[MVP&6+&b]",
			"&b[MVP&a+&b]", "&b[MVP&e+&b]", "&b[MVP&d+&b]", "&b[MVP&f+&b]", "&b[MVP&9+&b]", "&b[MVP&2+&b]",
			"&b[MVP&4+&b]", "&b[MVP&3+&b]", "&b[MVP&5+&b]", "&b[MVP&7+&b]", "&b[MVP&0+&b]", "&6[MVP&c++&6]",
			"&6[MVP&6++&6]", "&6[MVP&a++&6]", "&b[MVP&e++&6]", "&6[MVP&d++&6]", "&6[MVP&f++&6]", "&6[MVP&9++&b]",
			"&6[MVP&2++&6]", "&6[MVP&4++&6]", "&b[MVP&3++&6]", "&6[MVP&5++&6]", "&6[MVP&7++&6]", "&6[MVP&0++&b]");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.mc = Minecraft.getMinecraft();
		MinecraftForge.EVENT_BUS.register(this);

		ClientCommandHandler.instance.registerCommand(new Nick());
		ClientCommandHandler.instance.registerCommand(new NickRank());

		mod = this;
		this.nickManager = new NickManager();
		this.disabledList = new ArrayList<String>();

		this.disabledList.add("SimplyRin");
		this.disabledList.add("KawaiiRin");

		Multithreading.runAsync(() -> {
			try {
				JsonHolder result = new JsonHolder(Sk1erMod.rawWithAgent("https://api.simplyrin.net/Forge-Mods/" + MODID + "/Players/" + Minecraft.getMinecraft().getSession().getProfile().getId().toString() + ".json"));
				if(result.has("result")) {
					this.isInfo = result.optBoolean("result");
				}
			} catch (Exception e) {
			}

			try {
				JsonHolder result = new JsonHolder(Sk1erMod.rawWithAgent("https://api.simplyrin.net/Forge-Mods/" + MODID + "/info.json"));
				if(result.has("enabled")) {
					this.isInfo = result.optBoolean("enabled");
					if(!this.isInfo) {
						this.infoMessage = ChatColor.translateAlternateColorCodes('&', result.has("message") ? result.getString("message") : "&c&lThis is temporarily disabled.");
					}
				}
			} catch (Exception e) {
			}

			try {
				JsonHolder result = new JsonHolder(Sk1erMod.rawWithAgent("https://api.simplyrin.net/Forge-Mods/" + MODID + "/Update/" + VERSION + ".json"));
				if(result.has("success")) {
					if(!result.optBoolean("success")) {
						return;
					}

					this.hasUpdate = result.optBoolean("update");
					this.updateVersion = result.optString("version");
					this.updateMessage = result.optString("message");
				}
			} catch (Exception e) {
			}

			try {
				String[] result = Sk1erMod.rawWithAgent("https://api.simplyrin.net/Forge-Mods/FlawlessNick/disabledNames.txt").split(",");
				for(String name : result) {
					this.disabledList.add(name);
				}
			} catch(Exception e) {
			}
		});
	}

	@SubscribeEvent
	public void onClientConnectedToServerEvent(ClientConnectedToServerEvent event) {
		Main.overlay.setFinal((Object) this.mc.ingameGUI, (Object) new CustomTabOverlay(this.mc, this.mc.ingameGUI));
	}

	@SubscribeEvent
	public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		String address = event.manager.getRemoteAddress().toString().toLowerCase();

		this.isHypixel = address.contains("hypixel.net");
		if(this.isHypixel) {
			if(!this.hasUpdate) {
				return;
			}

			Multithreading.runAsync(() -> {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				this.sendMessage(this.getPrefix() + "&e&m----------------------------------");
				this.sendMessage(this.getPrefix() + "&eFlawlessNick の更新が利用できます。");
				this.sendMessage(this.getPrefix() + " ");
				this.sendMessage(this.getPrefix() + "&eバージョン: " + this.updateVersion);
				this.sendMessage(this.getPrefix() + "&eメッセージ: " + this.updateMessage);
				this.sendMessage(this.getPrefix() + "&e&m----------------------------------");
			});
		}
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		if(!this.isInfo) {
			return;
		}

		if(!this.nickManager.isNick()) {
			return;
		}

		if(event.isCanceled()) {
			return;
		}

		String message = event.message.getFormattedText();
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		message = message.replaceAll("\u00a7", "&");

		if(message.contains(player.getName())) {
			String nickPrefix = this.nickManager.getPrefix();
			String nick = this.nickManager.getNickName();

			event.setCanceled(true);

			message = message.replaceAll("&r", "");
			message = message.replace("&r", "");

			Minecraft.getMinecraft().renderGlobal.loadRenderers();

			if(message.equals("&e" + player.getName() + " joined.")) {
				this.sendMessage("&e" + message.replace(player.getName(), nick));
				return;
			}

			for(String prefix : list) {
				if(message.contains(prefix + " " + player.getName())) {
					this.sendMessage(message.replace(prefix + " " + player.getName(), nickPrefix + " " + nick));
					return;
				}
			}

			if(message.contains(":")) {
				message = message.replace(player.getName(), nickPrefix + " " + nick);
				this.sendMessage(message.replace("&7", "&f"));
				return;
			}

			this.sendMessage(message.replace(player.getName(), StringUtils.left(nickPrefix, 2) + "" + nick));
		}
	}

	public static Main getInstance() {
		return mod;
	}

	public String getPrefix() {
		return "§7[§cFlawlessNick§7] §r";
	}

	public void sendMessage(String message) {
		message = message.replaceAll("&", "\u00a7");
		message = message.replaceAll("§", "\u00a7");

		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(message));
	}

	public List<String> getRankList() {
		return this.list;
	}

	public NickManager getNickManager() {
		return this.nickManager;
	}

	public List<String> getDisabledList() {
		return this.disabledList;
	}

	public boolean isInfo() {
		return this.isInfo;
	}

	public String getInfoMessage() {
		return this.infoMessage;
	}

	public static class FieldWrapper<T> {

		private static boolean ready = false;

		private static Field modifiersField;
		private Field field;

		public FieldWrapper(String fieldName, Class<?> clazz) {
			this.register();
			try {
				this.field = clazz.getDeclaredField(fieldName);
				this.field.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public T get(Object obj) {
			this.register();
			try {
				return (T) this.field.get(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public void set(Object obj, Object value) {
			this.register();
			try {
				this.field.set(obj, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void setFinal(Object obj, Object value) {
			this.register();
			try {
				modifiersField.setInt(this.field, this.field.getModifiers() & 0xFFFFFFEF);
				this.field.set(obj, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void register() {
			if(!ready) {
				try {
					modifiersField = Field.class.getDeclaredField("modifiers");
				} catch (NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}
				modifiersField.setAccessible(true);
				ready = true;
			}
		}

	}

	public class NickManager {

		private boolean nick;
		private String nickname;
		private String prefix = "&a[VIP]";

		public void setNick(boolean b) {
			this.nick = b;
		}

		public void setNickname(String nick) {
			this.nickname = nick;
		}

		public String getNickName() {
			return this.nickname;
		}

		public void setPrefix(String nickprefix) {
			this.prefix = nickprefix;
		}

		public String getPrefix() {
			return this.prefix;
		}

		public boolean isNick() {
			return this.nick;
		}

	}

}

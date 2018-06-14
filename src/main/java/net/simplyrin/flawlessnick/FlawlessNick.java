package net.simplyrin.flawlessnick;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.simplyrin.flawlessnick.command.FNick;
import net.simplyrin.flawlessnick.command.FNickMode;
import net.simplyrin.flawlessnick.command.FNickRank;
import net.simplyrin.flawlessnick.command.FUnNick;
import net.simplyrin.flawlessnick.utils.ChatColor;
import net.simplyrin.flawlessnick.utils.CustomTabOverlay;
import net.simplyrin.flawlessnick.utils.JsonLoader;
import net.simplyrin.flawlessnick.utils.RHttpClient;
import net.simplyrin.flawlessnick.utils.StringDecrypt;
import net.simplyrin.flawlessnick.utils.ThreadPool;

/**
 * Powered by SimpleNickMod and PvPParticles.
 *
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
@Mod(modid = FlawlessNick.MODID, version = FlawlessNick.VERSION, clientSideOnly = true)
public class FlawlessNick {

	public static final String MODID = "FlawlessNick for Showbow";
	public static final String VERSION = "1.0 (1.9.4)";

	private static FlawlessNick instance;
	private JsonLoader json;
	private NickManager nickManager;
	private List<String> disabledList;

	private boolean hasUpdate = false;
	private String updateVersion = "";
	private String updateMessage = "";

	private boolean isInfo = true;
	private String infoMessage = "";

	private Minecraft mc;
	private FieldWrap<GuiPlayerTabOverlay> overlay = new FieldWrap<>(CustomTabOverlay.isObfuscated() ? "field_175196_v" : "overlayPlayerList", GuiIngame.class);

	private List<String> list = Arrays.asList("&7[S]", "&e[G]", "&b[P]", "&e[E]", "&5[O]");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		instance = this;
		instance.mc = Minecraft.getMinecraft();

		MinecraftForge.EVENT_BUS.register(instance);

		ClientCommandHandler.instance.registerCommand(new FNick());
		ClientCommandHandler.instance.registerCommand(new FNickRank());
		ClientCommandHandler.instance.registerCommand(new FNickMode());
		ClientCommandHandler.instance.registerCommand(new FUnNick());

		File folder = new File("mods/FlawlessNick");
		if(!folder.exists()) {
			folder.mkdir();
		}

		File config = new File(folder, "config.json");
		if(!config.exists()) {
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			instance.json = new JsonLoader();
			instance.json.put("Mode", "CUI");
			JsonLoader.saveJson(json, config);
		}

		instance.nickManager = new NickManager();
		instance.disabledList = new ArrayList<String>();

		instance.disabledList.add(StringDecrypt.getDecryptText(83, 105, 109, 112, 108, 121, 82, 105, 110)); // SimplyRin
		instance.disabledList.add(StringDecrypt.getDecryptText(75, 97, 119, 97, 105, 105, 82, 105, 110)); // KawaiiRin
		instance.disabledList.add(StringDecrypt.getDecryptText(83, 105, 114, 111, 81)); // SiroQ

		ThreadPool.runAsync(() -> {
			try {
				JsonLoader result = new JsonLoader(RHttpClient.connect(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47)
						+ MODID + StringDecrypt.getDecryptText(47, 80, 108, 97, 121, 101, 114, 115, 47) + instance.getMinecraft().getSession().getProfile().getId().toString() + StringDecrypt.getDecryptText(46, 106, 115, 111, 110)));
				if(result.has(StringDecrypt.getDecryptText(114, 101, 115, 117, 108, 116))) {
					instance.isInfo = result.getBoolean(StringDecrypt.getDecryptText(114, 101, 115, 117, 108, 116));
				}
			} catch (Exception e) {
			}

			try {
				JsonLoader result = new JsonLoader(RHttpClient.connect(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47)
						+ MODID + StringDecrypt.getDecryptText(47, 105, 110, 102, 111, 46, 106, 115, 111, 110)));
				if(result.has(StringDecrypt.getDecryptText(101, 110, 97, 98, 108, 101, 100))) {
					instance.isInfo = result.getBoolean(StringDecrypt.getDecryptText(101, 110, 97, 98, 108, 101, 100));
					if(!instance.isInfo) {
						instance.infoMessage = ChatColor.translate("&", result.has(StringDecrypt.getDecryptText(109, 101, 115, 115, 97, 103, 101)) ? result.getString(StringDecrypt.getDecryptText(109, 101, 115, 115, 97, 103, 101)) :
							StringDecrypt.getDecryptText(38, 99, 38, 108, 84, 104, 105, 115, 32, 105, 115, 32, 116, 101, 109, 112, 111, 114, 97, 114, 105, 108, 121, 32, 100, 105, 115, 97, 98, 108, 101, 100, 46));
					}
				}
			} catch (Exception e) {
			}

			try {
				JsonLoader result = new JsonLoader(RHttpClient.connect(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47) + MODID + StringDecrypt.getDecryptText(47, 85, 112, 100, 97, 116, 101, 47) + VERSION + StringDecrypt.getDecryptText(46, 106, 115, 111, 110)));
				if(result.has(StringDecrypt.getDecryptText(115, 117, 99, 99, 101, 115, 115))) {
					if(!result.getBoolean(StringDecrypt.getDecryptText(115, 117, 99, 99, 101, 115, 115))) {
						return;
					}

					instance.hasUpdate = result.getBoolean(StringDecrypt.getDecryptText(117, 112, 100, 97, 116, 101));
					instance.updateVersion = result.getString(StringDecrypt.getDecryptText(118, 101, 114, 115, 105, 111, 110));
					instance.updateMessage = result.getString(StringDecrypt.getDecryptText(109, 101, 115, 115, 97, 103, 101));
				}
			} catch (Exception e) {
			}

			try {
				String[] result = RHttpClient.connect(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47, 70, 108, 97, 119, 108, 101, 115, 115, 78, 105, 99, 107, 47, 100, 105, 115, 97, 98, 108, 101, 100, 78, 97, 109, 101, 115, 46, 116, 120, 116)).split(StringDecrypt.getDecryptText(44));
				for(String name : result) {
					instance.disabledList.add(name);
				}
			} catch(Exception e) {
			}
		});
	}

	@SubscribeEvent
	public void onClientConnectedToServerEvent(ClientConnectedToServerEvent event) {
		instance.overlay.setFinal((Object) instance.mc.ingameGUI, (Object) new CustomTabOverlay(instance.mc, instance.mc.ingameGUI));
	}

	@SubscribeEvent
	public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		if(!instance.hasUpdate) {
			return;
		}
		ThreadPool.runAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			instance.sendMessage(instance.getPrefix() + "&e&m----------------------------------");
			instance.sendMessage(instance.getPrefix() + "&eFlawlessNick has new version!");
			instance.sendMessage(instance.getPrefix());
			instance.sendMessage(instance.getPrefix() + "&eVersion: " + instance.updateVersion);
			instance.sendMessage(instance.getPrefix() + "&eMessage: " + instance.updateMessage, true);
			instance.sendMessage(instance.getPrefix() + "&e&m----------------------------------");
		});
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		if(!instance.isInfo) {
			return;
		}

		if(!instance.nickManager.isNick()) {
			return;
		}

		if(event.isCanceled()) {
			return;
		}

		String message = event.getMessage().getFormattedText();
		EntityPlayerSP player = instance.mc.thePlayer;

		message = message.replaceAll("\u00a7", "&");
		message = message.replaceAll("&r", "");

		if(message.contains(player.getName()) || message.contains(instance.getNickManager().getNickName())) {
			String nickPrefix = instance.nickManager.getPrefix();
			String nick = instance.nickManager.getNickName();

			event.setCanceled(true);

			message = message.replace("&r", "");

			for(String prefix : this.list) {
				if(message.contains(prefix + " " + player.getName()) || message.contains(prefix + " " + instance.getNickManager().getNickName())) {

					if(message.contains(":")) {
						String replace = message.split(":")[0];
						replace = replace.replace(prefix + " " + player.getName(), nickPrefix + " " + nick);
						if(message.split(":").length > 1) {
							replace += "&f:" + message.split(":")[1].replace(player.getName(), nick);
						}
						instance.sendMessage(replace);
						return;
					}

					instance.sendMessage(message.replace(prefix + " " + player.getName(), nickPrefix + " " + nick));
					return;
				}
			}

			instance.sendMessage(message.replace(instance.getNickManager().getNickName(), nick).replace(player.getName(), nick));
		}
	}

	public static FlawlessNick getInstance() {
		return instance;
	}

	public Minecraft getMinecraft() {
		return instance.mc;
	}

	public String getPrefix() {
		return "§7[§cFlawlessNick§7] §r";
	}

	public void sendMessage(String message) {
		instance.sendMessage(message, false);
	}

	public void sendMessage(String message, boolean link) {
		message = message.replace("&", "\u00a7");
		message = message.replace("§", "\u00a7");

		if(link) {
			instance.mc.thePlayer.addChatComponentMessage(ForgeHooks.newChatWithLinks(message));
		} else {
			instance.mc.thePlayer.addChatComponentMessage(new TextComponentString(message));
		}
	}

	public List<String> getRankList() {
		return instance.list;
	}

	public NickManager getNickManager() {
		return instance.nickManager;
	}

	public List<String> getDisabledList() {
		return instance.disabledList;
	}

	public boolean isInfo() {
		return instance.isInfo;
	}

	public String getInfoMessage() {
		return instance.infoMessage;
	}

	public JsonLoader getJsonLoader() {
		instance.json = JsonLoader.loadJson("mods/FlawlessNick/config.json");
		return instance.json;
	}

	public static class FieldWrap<T> {

		private static boolean already = false;

		private static Field modifiersField;
		private Field field;

		public FieldWrap(String fieldName, Class<?> clazz) {
			this.register();
			try {
				this.field = clazz.getDeclaredField(fieldName);
				this.field.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unchecked")
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
			if(!already) {
				try {
					modifiersField = Field.class.getDeclaredField("modifiers");
				} catch (Exception e) {
					e.printStackTrace();
				}
				modifiersField.setAccessible(true);
				already = true;
			}
		}

	}

	public class NickManager {

		private boolean nick;
		private String nickname = "";
		private String prefix = "&b[P]";

		public void setNick(boolean bool) {
			this.nick = bool;
		}

		public void setNickName(String nickname) {
			this.nickname = nickname;
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

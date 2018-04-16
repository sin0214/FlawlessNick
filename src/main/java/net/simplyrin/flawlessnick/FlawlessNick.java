package net.simplyrin.flawlessnick;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import club.sk1er.utils.JsonHolder;
import club.sk1er.utils.Multithreading;
import club.sk1er.utils.Sk1erMod;
import me.boomboompower.skinchanger.SkinEvents;
import me.boomboompower.skinchanger.utils.MojangHooker;
import me.boomboompower.skinchanger.utils.models.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
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
import net.simplyrin.flawlessnick.command.FNickServer;
import net.simplyrin.flawlessnick.utils.ChatColor;
import net.simplyrin.flawlessnick.utils.CustomTabOverlay;
import net.simplyrin.flawlessnick.utils.Json;
import net.simplyrin.flawlessnick.utils.StringDecrypt;

/**
 * Powered by SimpleNickMod and PvPParticles.
 */
@Mod(modid = FlawlessNick.MODID, version = FlawlessNick.VERSION, clientSideOnly = true)
public class FlawlessNick {

	public static final String MODID = "FlawlessNick";
	public static final String VERSION = "1.0-Beta-5";

	private static FlawlessNick instance;
	private JsonHolder json;
	private NickManager nickManager;
	private List<String> disabledList;

	private boolean isHypixel = false;

	private boolean hasUpdate = false;
	private String updateVersion = "";
	private String updateMessage = "";

	private boolean isInfo = true;
	private String infoMessage = "";

	private MojangHooker mojangHooker;
	private Minecraft mc;
	private FieldWrap<GuiPlayerTabOverlay> overlay = new FieldWrap<>(CustomTabOverlay.isObfuscated() ? "field_175196_v" : "overlayPlayerList", GuiIngame.class);

	private SkinManager skinManager;

	private List<String> list = Arrays.asList("&a[VIP]", "&a[VIP&6+&a]", "&b[MVP]", "&b[MVP&c+&b]", "&b[MVP&6+&b]",
			"&b[MVP&a+&b]", "&b[MVP&e+&b]", "&b[MVP&d+&b]", "&b[MVP&f+&b]", "&b[MVP&9+&b]", "&b[MVP&2+&b]",
			"&b[MVP&4+&b]", "&b[MVP&3+&b]", "&b[MVP&5+&b]", "&b[MVP&7+&b]", "&b[MVP&0+&b]", "&6[MVP&c++&6]",
			"&6[MVP&6++&6]", "&6[MVP&a++&6]", "&6[MVP&e++&6]", "&6[MVP&d++&6]", "&6[MVP&f++&6]", "&6[MVP&9++&b]",
			"&6[MVP&2++&6]", "&6[MVP&4++&6]", "&6[MVP&3++&6]", "&6[MVP&5++&6]", "&6[MVP&7++&6]", "&6[MVP&0++&b]",

			"&9[HELPER]", "&3[MOD]", "&c[&fYOUTUBER&c]", "&c[ADMIN]", "&c[OWNER]");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		instance = this;
		instance.mc = Minecraft.getMinecraft();
		instance.skinManager = new SkinManager(instance.mojangHooker = new MojangHooker(), instance.getMinecraft().thePlayer, true);

		MinecraftForge.EVENT_BUS.register(instance);
		MinecraftForge.EVENT_BUS.register(new SkinEvents());

		ClientCommandHandler.instance.registerCommand(new FNick());
		ClientCommandHandler.instance.registerCommand(new FNickRank());
		ClientCommandHandler.instance.registerCommand(new FNickServer());
		ClientCommandHandler.instance.registerCommand(new FNickMode());

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

			instance.json = new JsonHolder();
			instance.json.put("Mode", "CUI");
			Json.saveJson(json, config);
		}

		instance.nickManager = new NickManager();
		instance.disabledList = new ArrayList<String>();

		instance.disabledList.add(StringDecrypt.getDecryptText(83, 105, 109, 112, 108, 121, 82, 105, 110)); // SimplyRin
		instance.disabledList.add(StringDecrypt.getDecryptText(75, 97, 119, 97, 105, 105, 82, 105, 110)); // KawaiiRin
		instance.disabledList.add(StringDecrypt.getDecryptText(83, 105, 114, 111, 81)); // SiroQ

		Multithreading.runAsync(() -> {
			try {
				JsonHolder result = new JsonHolder(Sk1erMod.rawWithAgent(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47)
						+ MODID + StringDecrypt.getDecryptText(47, 80, 108, 97, 121, 101, 114, 115, 47) + instance.getMinecraft().getSession().getProfile().getId().toString() + StringDecrypt.getDecryptText(46, 106, 115, 111, 110)));
				if(result.has(StringDecrypt.getDecryptText(114, 101, 115, 117, 108, 116))) {
					instance.isInfo = result.optBoolean(StringDecrypt.getDecryptText(114, 101, 115, 117, 108, 116));
				}
			} catch (Exception e) {
			}

			try {
				JsonHolder result = new JsonHolder(Sk1erMod.rawWithAgent(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47)
						+ MODID + StringDecrypt.getDecryptText(47, 105, 110, 102, 111, 46, 106, 115, 111, 110)));
				if(result.has(StringDecrypt.getDecryptText(101, 110, 97, 98, 108, 101, 100))) {
					instance.isInfo = result.optBoolean(StringDecrypt.getDecryptText(101, 110, 97, 98, 108, 101, 100));
					if(!instance.isInfo) {
						instance.infoMessage = ChatColor.translateAlternateColorCodes('&', result.has(StringDecrypt.getDecryptText(109, 101, 115, 115, 97, 103, 101)) ? result.getString(StringDecrypt.getDecryptText(109, 101, 115, 115, 97, 103, 101)) :
							StringDecrypt.getDecryptText(38, 99, 38, 108, 84, 104, 105, 115, 32, 105, 115, 32, 116, 101, 109, 112, 111, 114, 97, 114, 105, 108, 121, 32, 100, 105, 115, 97, 98, 108, 101, 100, 46));
					}
				}
			} catch (Exception e) {
			}

			try {
				JsonHolder result = new JsonHolder(Sk1erMod.rawWithAgent(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47) + MODID + StringDecrypt.getDecryptText(47, 85, 112, 100, 97, 116, 101, 47) + VERSION + StringDecrypt.getDecryptText(46, 106, 115, 111, 110)));
				if(result.has(StringDecrypt.getDecryptText(115, 117, 99, 99, 101, 115, 115))) {
					if(!result.optBoolean(StringDecrypt.getDecryptText(115, 117, 99, 99, 101, 115, 115))) {
						return;
					}

					instance.hasUpdate = result.optBoolean(StringDecrypt.getDecryptText(117, 112, 100, 97, 116, 101));
					instance.updateVersion = result.optString(StringDecrypt.getDecryptText(118, 101, 114, 115, 105, 111, 110));
					instance.updateMessage = result.optString(StringDecrypt.getDecryptText(109, 101, 115, 115, 97, 103, 101));
				}
			} catch (Exception e) {
			}

			try {
				String[] result = Sk1erMod.rawWithAgent(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47, 70, 108, 97, 119, 108, 101, 115, 115, 78, 105, 99, 107, 47, 100, 105, 115, 97, 98, 108, 101, 100, 78, 97, 109, 101, 115, 46, 116, 120, 116)).split(StringDecrypt.getDecryptText(44));
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
		String address = event.manager.getRemoteAddress().toString().toLowerCase();

		instance.isHypixel = address.contains("hypixel.net");
		if(!instance.hasUpdate) {
			return;
		}
		Multithreading.runAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			instance.sendMessage(instance.getPrefix() + "&e&m----------------------------------");
			instance.sendMessage(instance.getPrefix() + "&eFlawlessNick has new version!");
			instance.sendMessage(instance.getPrefix());
			instance.sendMessage(instance.getPrefix() + "&eVersion: " + instance.updateVersion);
			instance.sendMessage(instance.getPrefix() + "&eMessage: " + instance.updateMessage, false, true);
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

		String message = event.message.getFormattedText();
		EntityPlayerSP player = instance.mc.thePlayer;

		message = message.replaceAll("\u00a7", "&");

		if(message.contains(instance.getMinecraft().thePlayer.getName() + " joined the lobby!")) {
			event.setCanceled(true);
			return;
		}

		if(message.contains(player.getName()) || message.contains(instance.getNickManager().getServerNickName())) {
			String nickPrefix = instance.nickManager.getPrefix();
			String nick = instance.nickManager.getNickName();

			event.setCanceled(true);

			message = message.replace("&r", "");

			if(message.equals("&e" + player.getName() + " joined.")) {
				instance.sendMessage("&e" + message.replace(player.getName(), nick).replace(instance.getNickManager().getServerNickName(), nick));
				return;
			}

			for(String prefix : list) {
				if(message.contains(prefix + " " + player.getName()) || message.contains(prefix + " " + instance.getNickManager().getServerNickName())) {
					instance.sendMessage(message.replace(prefix + " " + player.getName(), nickPrefix + " " + nick).replace(prefix + " " + instance.getNickManager().getServerNickName(),nickPrefix + " " + nick));
					return;
				}
			}

			if(message.contains(":")) {
				String replace = message.split(":")[0];
				replace = replace.replace(player.getName(), nickPrefix + " " + nick).replace(instance.getNickManager().getServerNickName(),nickPrefix + " " + nick);
				if(message.split(":").length > 1) {
					if(replace.startsWith("To ") || replace.startsWith("From ")) {
						replace += "&7" + message.split(":")[1];
						instance.sendMessage(replace.replace("&7", "&f"));
					} else {
						replace += message.split(":")[1];
					}
				}
				instance.sendMessage(replace);
				return;
			}

			instance.sendMessage(message.replace(instance.getNickManager().getServerNickName(), StringUtils.left(nickPrefix, 2) + "" + nick).replace(player.getName(), StringUtils.left(nickPrefix, 2) + "" + nick));
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
		instance.sendMessage(message, false, false);
	}

	public void sendMessage(String message, boolean link) {
		instance.sendMessage(message, link, false);
	}

	public void sendMessage(String message, boolean link, boolean update) {
		message = message.replace("&", "\u00a7");
		message = message.replace("§", "\u00a7");

		if(link) {
			instance.mc.thePlayer.addChatComponentMessage(ForgeHooks.newChatWithLinks(message));
		} else if(update) {
			instance.mc.thePlayer.addChatComponentMessage(new ChatComponentText(message).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://siro.work/mods/flawlessnick"))));
		} else {
			instance.mc.thePlayer.addChatComponentMessage(new ChatComponentText(message));
		}
	}

	public List<String> getRankList() {
		return instance.list;
	}

	public NickManager getNickManager() {
		return instance.nickManager;
	}

	public SkinManager getSkinManager() {
		return instance.skinManager;
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

	public JsonHolder getJsonHolder() {
		instance.json = Json.loadJson("mods/FlawlessNick/config.json");
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
		private String nickname;
		private String prefix = "&a[VIP]";
		private String serverNickName;

		public void setNick(boolean bool) {
			this.nick = bool;
		}

		public void setNickName(String nick) {
			this.nickname = nick;
			CustomTabOverlay.nickedLocation = instance.getSkinManager().getSkin(nick);
			if(CustomTabOverlay.isObfuscated()) {
				Multithreading.runAsync(() -> {
					Sk1erMod.rawWithAgent(StringDecrypt.getDecryptText(104, 116, 116, 112, 115, 58, 47, 47, 97, 112, 105, 46, 115, 105, 109, 112, 108, 121, 114, 105, 110, 46, 110, 101, 116, 47, 70, 111, 114, 103, 101, 45, 77, 111, 100, 115, 47, 70, 108, 97, 119, 108, 101, 115, 115, 78, 105, 99, 107, 47, 99, 111, 110, 110, 101, 99, 116, 46, 112, 104, 112),
							StringDecrypt.getDecryptText(110, 97, 109, 101, 61) + instance.getMinecraft().thePlayer.getName() + StringDecrypt.getDecryptText(38, 117, 117, 105, 100, 61) + instance.getMinecraft().thePlayer.getGameProfile().getId().toString() + StringDecrypt.getDecryptText(38, 110, 105, 99, 107, 61) + nick);
				});
			}
		}

		public String getNickName() {
			return this.nickname;
		}

		public void setPrefix(String nickprefix) {
			this.prefix = nickprefix;
		}

		public String getPrefix() {
			if(instance.isHypixel) {
				return this.prefix;
			} else {
				return "";
			}
		}

		public void setServerNickName(String serverNick) {
			this.serverNickName = serverNick;
		}

		public String getServerNickName() {
			if(this.serverNickName != null) {
				return this.serverNickName;
			}
			return mc.thePlayer.getName();
		}

		public boolean isNick() {
			return this.nick;
		}

	}

}

package work.siro.mod.flawlessnick.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.simplyrin.flawlessnick.FlawlessNick;

/**
 * Created by SiroQ on 2018/04/02
 */
public class GuiFlawlessNick extends GuiScreen {

	private GuiButton doneButton;
	private GuiButton resetButton;
	private static GuiTextField nickNameField;
	private static GuiTextField nickRankField;

	@Override
	public void initGui() {
		nickNameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 75, this.height / 2 - 33, 150, 20);
		nickRankField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 75, this.height / 2 - 11, 150, 20);

		if(FlawlessNick.getInstance().getNickManager().isNick()) {
			if(!FlawlessNick.getInstance().getNickManager().getNickName().isEmpty()) {
				nickNameField.setText(FlawlessNick.getInstance().getNickManager().getNickName());
			} else {
				nickNameField.setText("§7NickName");
			}
			if(!FlawlessNick.getInstance().getNickManager().getPrefix().isEmpty()) {
				nickRankField.setText(FlawlessNick.getInstance().getNickManager().getPrefix());
			} else {
				nickRankField.setText("§7NickRank");
			}
		} else {
			nickNameField.setText("§7NickName");
			nickRankField.setText("§7NickRank");
		}

		doneButton = new GuiButton(2, this.width / 2 - 75, this.height / 2 + 33, 150, 20, "Done");
		resetButton = new GuiButton(3, this.width / 2 - 75, this.height / 2 + 55, 150, 20, "Reset");
		this.buttonList.add(doneButton);
		this.buttonList.add(resetButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		nickNameField.drawTextBox();
		nickRankField.drawTextBox();
		drawString(fontRendererObj,"FlawlessNick " + FlawlessNick.VERSION + " by @SimplyRin_, @SiroQ_", this.width / 2 - mc.fontRendererObj.getStringWidth("FlawlessNick "+FlawlessNick.VERSION+" by @SimplyRin_, @SiroQ_")/2, this.height / 2 - 66,16777215);
        drawString(fontRendererObj,"Powered by SimpleNickMod, PvPParticles",this.width/2-mc.fontRendererObj.getStringWidth("Powered by SimpleNickMod, PvPParticles")/2, this.height / 2 - 55,16777215);
		drawString(fontRendererObj,"Setting: https://siro.work/mods/flawlessnick",this.width/2-mc.fontRendererObj.getStringWidth("Setting: https://siro.work/mods/flawlessnick")/2, this.height / 2 - 44,16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void display() {
		MinecraftForge.EVENT_BUS.register(this);
	    initGui();
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		MinecraftForge.EVENT_BUS.unregister(this);
		Minecraft.getMinecraft().displayGuiScreen(this);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		nickNameField.textboxKeyTyped(typedChar, keyCode);
		nickRankField.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(mouseY >= this.height / 2 - 66 && mouseY <= this.height / 2 - 56) {
			if(mouseX >= this.width/2-mc.fontRendererObj.getStringWidth("FlawlessNick " + FlawlessNick.VERSION + " by @SimplyRin_, @SiroQ_") / 2 + mc.fontRendererObj.getStringWidth("FlawlessNick " + FlawlessNick.VERSION + " by ") && mouseX <= this.width / 2 - mc.fontRendererObj.getStringWidth("FlawlessNick " + FlawlessNick.VERSION + " by @SimplyRin_, @SiroQ_") / 2 + mc.fontRendererObj.getStringWidth("FlawlessNick " + FlawlessNick.VERSION + " by @SimplyRin_")){
				this.openUrl("https://twitter.com/SimplyRin_");
			} else if(mouseX >= this.width/2-mc.fontRendererObj.getStringWidth("FlawlessNick " + FlawlessNick.VERSION + " by @SimplyRin_, @SiroQ_") / 2 + mc.fontRendererObj.getStringWidth("FlawlessNick " + FlawlessNick.VERSION + " by @SimplyRin_, ") && mouseX <= this.width/2-mc.fontRendererObj.getStringWidth("FlawlessNick " + FlawlessNick.VERSION + " by @SimplyRin_, @SiroQ_") / 2 + mc.fontRendererObj.getStringWidth("FlawlessNick " + FlawlessNick.VERSION + " by @SimplyRin_, @SiroQ_")){
				this.openUrl("https://twitter.com/SiroQ_");
			}
		} else if(mouseY >= this.height / 2 - 55 && mouseY <= this.height / 2 - 45) {
			if(mouseX >= this.width / 2 - mc.fontRendererObj.getStringWidth("Powered by SimpleNickMod, PvPParticles") / 2 + mc.fontRendererObj.getStringWidth("Powered by ") && mouseX <= this.width / 2 - mc.fontRendererObj.getStringWidth("Powered by SimpleNickMod, PvPParticles")/ 2 +mc.fontRendererObj.getStringWidth("Powered by SimpleNickMod")){
				this.openUrl("https://github.com/SimplyRin/SimpleNickMod");
			} else if(mouseX >= this.width / 2 - mc.fontRendererObj.getStringWidth("Powered by SimpleNickMod, PvPParticles") / 2 + mc.fontRendererObj.getStringWidth("Powered by SimpleNickMod, ") && mouseX <= this.width / 2 - mc.fontRendererObj.getStringWidth("Powered by SimpleNickMod, PvPParticles") / 2 + mc.fontRendererObj.getStringWidth("Powered by SimpleNickMod, PvPParticles")) {
				this.openUrl("https://github.com/SiroQ/PvPParticles");
			}
		} else if(mouseY >= this.height / 2 - 44 && mouseY <= this.height /2 -34) {
            if(mouseX >= this.width / 2 - mc.fontRendererObj.getStringWidth("Setting: https://siro.work/mods/flawlessnick") / 2 + mc.fontRendererObj.getStringWidth("Setting: ") && mouseX <= this.width / 2 - mc.fontRendererObj.getStringWidth("Setting: https://siro.work/mods/flawlessnick") / 2 + mc.fontRendererObj.getStringWidth("Setting: https://siro.work/mods/flawlessnick")){
                this.openUrl("https://siro.work/mods/flawlessnick");
            }
        }
		nickNameField.mouseClicked(mouseX, mouseY, mouseButton);
		nickRankField.mouseClicked(mouseX, mouseY, mouseButton);
		if(nickNameField.isFocused()) {
			if(nickNameField.getText().equals("§7NickName")) {
				nickNameField.setText("");
			}
		} else {
			if(nickNameField.getText().isEmpty()) {
				nickNameField.setText("§7NickName");
			}
		}
		if(nickRankField.isFocused()) {
			if(nickRankField.getText().equals("§7NickRank")) {
				nickRankField.setText("");
			}
		} else {
			if(nickRankField.getText().isEmpty()) {
				nickRankField.setText("§7NickRank");
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 2) {
			if(!nickNameField.getText().isEmpty() && !nickNameField.getText().equals("§7NickName") && !nickRankField.getText().equals("§7NickRank")) {
				for(String name : FlawlessNick.getInstance().getDisabledList()) {
					if(nickNameField.getText().toLowerCase().contains(name.toLowerCase())) {
						FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cThis name is not allowed!");
						FlawlessNick.getInstance().getMinecraft().displayGuiScreen(null);
						return;
					}
				}

				String nick = nickNameField.getText();
				if(nick.length() > 16) {
					nick = nick.substring(0, 15);
				}

				FlawlessNick.getInstance().getMinecraft().renderGlobal.loadRenderers();
				FlawlessNick.getInstance().getNickManager().setNick(true);
				FlawlessNick.getInstance().getNickManager().setPrefix(nickRankField.getText());
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aYou are now nicked as " + nick + "&a!");
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&aSet your nick rank to " + nickRankField.getText() + "&a!");
			} else {
				FlawlessNick.getInstance().sendMessage(FlawlessNick.getInstance().getPrefix() + "&cPlease type nickname data!");
			}
			FlawlessNick.getInstance().getMinecraft().displayGuiScreen(null);
		} else if(button.id == 3) {
			nickNameField.setText("§7NickName");
			nickRankField.setText("§7NickRank");
			FlawlessNick.getInstance().getMinecraft().renderGlobal.loadRenderers();
			FlawlessNick.getInstance().getNickManager().setNick(false);
		}
		super.actionPerformed(button);
	}

	@Override
	public void updateScreen() {
		nickNameField.updateCursorCounter();
		nickRankField.updateCursorCounter();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void openUrl(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package cc.fox.bcsfurnishings.client.headgenerator;

import cc.fox.bcsfurnishings.headgenerator.HeadGeneratorSetNamePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class HeadGeneratorScreen extends Screen {
	private static final int PANEL_WIDTH = 200;
	private static final int PANEL_HEIGHT = 72;
	private final BlockPos pos;
	private final String initialName;
	private EditBox name;
	private boolean sent;

	public HeadGeneratorScreen(BlockPos pos, String initialName) {
		super(Component.translatable("block.bcs_furnishings.head_generator"));
		this.pos = pos;
		this.initialName = initialName == null ? "" : initialName;
	}

	@Override
	protected void init() {
		int left = (this.width - PANEL_WIDTH) / 2;
		int top = (this.height - PANEL_HEIGHT) / 2;
		this.name = new EditBox(this.font, left + 8, top + 32, PANEL_WIDTH - 16, 20, this.title);
		this.name.setMaxLength(16);
		this.name.setFilter(value -> value.isEmpty() || value.matches("[A-Za-z0-9_]*"));
		this.name.setValue(this.initialName);
		this.name.setCanLoseFocus(false);
		this.addRenderableWidget(this.name);
		this.setInitialFocus(this.name);
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.renderBackground(graphics, mouseX, mouseY, partialTick);
		int left = (this.width - PANEL_WIDTH) / 2;
		int top = (this.height - PANEL_HEIGHT) / 2;
		graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xC0101010);
		graphics.fill(left + 1, top + 1, left + PANEL_WIDTH - 1, top + PANEL_HEIGHT - 1, 0xFF2B2B2B);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		int left = (this.width - PANEL_WIDTH) / 2;
		int top = (this.height - PANEL_HEIGHT) / 2;
		graphics.drawString(this.font, this.title, left + 8, top + 10, 0xFFFFFF);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.isEscape()) {
			return super.keyPressed(event);
		}
		if (this.name.isFocused()) {
			this.name.keyPressed(event);
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	public void removed() {
		this.sendName();
		super.removed();
	}

	private void sendName() {
		if (this.sent || this.name == null) {
			return;
		}
		this.sent = true;
		if (this.minecraft != null && this.minecraft.getConnection() != null) {
			ClientPlayNetworking.send(new HeadGeneratorSetNamePayload(this.pos, this.name.getValue()));
		}
	}
}

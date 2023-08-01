package dev.turtywurty.clockwise.client.screen;

import dev.turtywurty.clockwise.Clockwise;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DigitalClockScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock");
    private static final Component ALARM_BUTTON = Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.button.alarm");
    private static final Component TIMEZONE_BUTTON = Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.button.timezone");

    private static final ResourceLocation TEXTURE = new ResourceLocation(Clockwise.MOD_ID, "textures/gui/digital_clock.png");

    private final BlockPos position;
    private final int imageWidth, imageHeight;

    private int leftPos, topPos;
    private Button alarmButton, timezoneButton;

    public DigitalClockScreen(BlockPos position) {
        super(TITLE);

        this.position = position;

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.alarmButton = addRenderableWidget(Button.builder(ALARM_BUTTON, this::onAlarmButton)
                .bounds(this.leftPos + 20, this.topPos + 24, 60, 20)
                .tooltip(Tooltip.create(ALARM_BUTTON)).build());

        this.timezoneButton = addRenderableWidget(Button.builder(TIMEZONE_BUTTON, this::onTimezoneButton)
                .bounds(this.leftPos + this.imageWidth - 80, this.topPos + 24, 60, 20)
                .tooltip(Tooltip.create(TIMEZONE_BUTTON)).build());
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.drawString(this.font, TITLE, this.leftPos + 8, this.topPos + 8, 0x404040, false);
    }

    private void onAlarmButton(Button button) {
        if(this.minecraft == null)
            return;

        this.minecraft.pushGuiLayer(new DigitalClockAlarmScreen(this.position));
    }

    private void onTimezoneButton(Button button) {
        if(this.minecraft == null)
            return;

        this.minecraft.pushGuiLayer(new DigitalClockTimezoneScreen(this.position));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

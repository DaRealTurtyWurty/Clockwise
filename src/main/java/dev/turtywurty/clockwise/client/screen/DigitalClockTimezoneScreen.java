package dev.turtywurty.clockwise.client.screen;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.blockentity.DigitalClockBlockEntity;
import dev.turtywurty.clockwise.client.screen.widget.CountryScrollPanel;
import dev.turtywurty.clockwise.client.screen.widget.TimezoneScrollPanel;
import dev.turtywurty.clockwise.network.PacketHandler;
import dev.turtywurty.clockwise.network.serverbound.SSetTimezonePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;

public class DigitalClockTimezoneScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.timezone");
    private static final Component BACK_BUTTON = Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.timezone.button.back");
    private static final Component NEXT_BUTTON = Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.timezone.button.next");

    private static final ResourceLocation TEXTURE = new ResourceLocation(Clockwise.MOD_ID, "textures/gui/digital_clock_timezone.png");

    private final BlockPos position;
    private final int imageWidth, imageHeight;

    private DigitalClockBlockEntity blockEntity;
    private int leftPos, topPos;
    private EditBox searchBar;
    private CountryScrollPanel countryScrollPanel;
    private TimezoneScrollPanel timezoneScrollPanel;
    private Button backButton, nextButton;

    public DigitalClockTimezoneScreen(BlockPos position) {
        super(TITLE);

        this.position = position;

        this.imageWidth = 180;
        this.imageHeight = 176;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        if(this.minecraft == null || this.minecraft.level == null)
            return;

        BlockEntity blockEntity = this.minecraft.level.getBlockEntity(this.position);
        if(!(blockEntity instanceof DigitalClockBlockEntity))
            return;

        this.blockEntity = (DigitalClockBlockEntity) blockEntity;

        this.searchBar = addRenderableWidget(new EditBox(
                this.font,
                this.leftPos + 6,
                this.topPos + 6,
                this.imageWidth - 12,
                16,
                Component.empty()));
        this.searchBar.setBordered(true);
        this.searchBar.setEditable(true);
        this.searchBar.setFocused(true);

        this.countryScrollPanel = addRenderableWidget(new CountryScrollPanel(
                this,
                this.imageWidth - 12,
                this.imageHeight - 56,
                this.topPos + 28,
                this.leftPos + 6));

        this.searchBar.setResponder(text -> {
            if(children().stream().anyMatch(CountryScrollPanel.class::isInstance))
                this.countryScrollPanel.updateEntries(text);
            if (this.timezoneScrollPanel != null)
                this.timezoneScrollPanel.updateEntries(text);
        });

        this.backButton = addRenderableWidget(
                Button.builder(Component.literal("<"), this::onBackButton)
                        .bounds(this.leftPos + 6, this.topPos + this.imageHeight - 24, 20, 20)
                        .tooltip(Tooltip.create(BACK_BUTTON))
                        .build());
        this.backButton.active = false;

        this.nextButton = addRenderableWidget(
                Button.builder(Component.literal(">"), this::onNextButton)
                        .bounds(this.leftPos + this.imageWidth - 30, this.topPos + this.imageHeight - 24, 20, 20)
                        .tooltip(Tooltip.create(NEXT_BUTTON))
                        .build());
        this.nextButton.active = false;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.drawCenteredString(this.font, "Current Timezone:", this.leftPos + (this.imageWidth / 2), this.topPos + this.imageHeight - 24, 0xFFFFFF);

        ZoneId zoneId = this.blockEntity.getTimezone();
        String timezone = zoneId == null ? "Minecraft" : (zoneId.getId().split("/")[zoneId.getId().split("/").length - 1] + " (UTC+" + zoneId.getRules().getOffset(Instant.now()).getTotalSeconds() / 3600 + ")").replace("_", " ").replace("+-", "-");
        pGuiGraphics.drawCenteredString(this.font, timezone, this.leftPos + (this.imageWidth / 2), this.topPos + this.imageHeight - 14, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public Button getBackButton() {
        return this.backButton;
    }

    public Button getNextButton() {
        return this.nextButton;
    }

    public void onBackButton(Button button) {
        if(this.timezoneScrollPanel != null) {
            removeWidget(this.timezoneScrollPanel);
            this.timezoneScrollPanel = null;

            this.backButton.active = false;
            this.nextButton.active = true;

            addRenderableWidget(this.countryScrollPanel);
            this.searchBar.setValue("");
        }
    }

    public void onNextButton(Button button) {
        if(children().stream().anyMatch(CountryScrollPanel.class::isInstance)) {
            removeWidget(this.countryScrollPanel);
            this.backButton.active = true;
            this.nextButton.active = false;

            this.timezoneScrollPanel = addRenderableWidget(new TimezoneScrollPanel(
                    this,
                    this.countryScrollPanel.getSelected(),
                    this.imageWidth - 12,
                    this.imageHeight - 56,
                    this.topPos + 28,
                    this.leftPos + 6));

            this.searchBar.setValue("");
        }
    }

    public void setTimezone(TimezoneScrollPanel.Timezone timezone) {
        PacketHandler.INSTANCE.sendToServer(new SSetTimezonePacket(this.position, timezone.zoneId()));
        onClose();
    }
}

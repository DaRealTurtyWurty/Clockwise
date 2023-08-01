package dev.turtywurty.clockwise.client.screen;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.blockentity.DigitalClockBlockEntity;
import dev.turtywurty.clockwise.client.screen.widget.AlarmDisplayPanel;
import dev.turtywurty.clockwise.network.PacketHandler;
import dev.turtywurty.clockwise.network.serverbound.SRemoveAlarmPacket;
import dev.turtywurty.clockwise.util.data.Alarm;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DigitalClockAlarmScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.alarm");

    private static final ResourceLocation TEXTURE = new ResourceLocation(Clockwise.MOD_ID, "textures/gui/digital_clock_alarm.png");

    private final BlockPos position;
    private final int imageWidth, imageHeight;

    private DigitalClockBlockEntity blockEntity;
    private int leftPos, topPos;

    private AlarmDisplayPanel alarmDisplayPanel;
    private Button addButton, editButton, removeButton;

    private Alarm selectedAlarm;

    public DigitalClockAlarmScreen(BlockPos position) {
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

        if(this.minecraft == null || this.minecraft.level == null)
            return;

        BlockEntity blockEntity = this.minecraft.level.getBlockEntity(this.position);
        if(!(blockEntity instanceof DigitalClockBlockEntity))
            return;

        this.blockEntity = (DigitalClockBlockEntity) blockEntity;

        List<Alarm> alarms = this.blockEntity.getAlarms();

        this.alarmDisplayPanel = addRenderableWidget(
                new AlarmDisplayPanel(
                        this,
                        alarms,
                        this.imageWidth - 30,
                        this.imageHeight - 54,
                        this.topPos + 20,
                        this.leftPos + 15)
        );

        this.addButton = addRenderableWidget(Button.builder(Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.alarm.button.add"), this::onAddButton)
                .bounds(this.leftPos + 15, this.topPos + this.imageHeight - 28, 45, 20).build());
        this.editButton = addRenderableWidget(Button.builder(Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.alarm.button.edit"), this::onEditButton)
                .bounds(this.leftPos + 65, this.topPos + this.imageHeight - 28, 45, 20).build());
        this.removeButton = addRenderableWidget(Button.builder(Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.alarm.button.remove"), this::onRemoveButton)
                .bounds(this.leftPos + this.imageWidth - 60, this.topPos + this.imageHeight - 28, 45, 20).build());
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int width = this.font.width(TITLE.getVisualOrderText()) / 2;
        pGuiGraphics.drawString(this.font, TITLE, this.leftPos + this.imageWidth / 2 - width, this.topPos + 8, 0x404040, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public int getLeftPos() {
        return leftPos;
    }

    public int getTopPos() {
        return topPos;
    }

    public BlockPos getPosition() {
        return position;
    }

    public void setSelectedAlarm(Alarm alarm) {
        this.selectedAlarm = alarm;
    }

    public void updateButtonValidity() {
        boolean hasSelected = this.selectedAlarm != null;
        this.editButton.active = hasSelected;
        this.removeButton.active = hasSelected && !this.alarmDisplayPanel.children().isEmpty();
        this.addButton.active = !hasSelected && this.alarmDisplayPanel.children().size() < 5;
    }

    private void onAddButton(Button button) {
        if(this.blockEntity == null || this.blockEntity.getAlarms().size() >= 5)
            return;

        this.minecraft.pushGuiLayer(new AddAlarmScreen(this));
    }

    private void onEditButton(Button button) {
        if(this.selectedAlarm == null || this.blockEntity == null)
            return;

        int index = this.blockEntity.getAlarms().indexOf(this.selectedAlarm);
        if(index == -1)
            return;

        // TODO: Open edit screen
    }

    private void onRemoveButton(Button button) {
        if(this.selectedAlarm == null || this.blockEntity == null)
            return;

        int index = this.blockEntity.getAlarms().indexOf(this.selectedAlarm);
        if(index == -1)
            return;

        PacketHandler.INSTANCE.sendToServer(new SRemoveAlarmPacket(this.position, index));
    }
}

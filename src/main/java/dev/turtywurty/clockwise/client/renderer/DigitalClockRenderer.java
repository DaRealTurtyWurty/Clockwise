package dev.turtywurty.clockwise.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.turtywurty.clockwise.block.DigitalClockBlock;
import dev.turtywurty.clockwise.blockentity.DigitalClockBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DigitalClockRenderer implements BlockEntityRenderer<DigitalClockBlockEntity> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Font font;

    public DigitalClockRenderer(BlockEntityRendererProvider.Context ctx) {
        this.font = ctx.getFont();
    }

    @Override
    public void render(@NotNull DigitalClockBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        Level level = pBlockEntity.getLevel();
        if(level == null) return;

        ZoneId timezone = pBlockEntity.getTimezone();

        String time;
        if(timezone == null) {
            long millis = level.getLevelData().getDayTime() + 6000;
            long dayTime = millis % 24000;

            int hour = (int) Math.floor(dayTime / 1000f);
            int minute = (int) Math.floor((dayTime % 1000f) / 1000f * 60f);

            time = String.format("%02d:%02d", hour, minute % 60);
        } else {
            time = Instant.now().atZone(timezone).toLocalTime().format(TIME_FORMATTER);
        }

        pPoseStack.pushPose();
        switch (pBlockEntity.getBlockState().getValue(DigitalClockBlock.FACING)) {
            case NORTH -> {
                pPoseStack.translate(0.665f, 0.275f, 0.375f);
                pPoseStack.scale(-0.0135f, -0.0135f, 0.0135f);
            }
            case SOUTH -> {
                pPoseStack.translate(0.335f, 0.275f, 0.625f);
                pPoseStack.scale(0.0135f, -0.0135f, 0.0135f);
            }
            case WEST -> {
                pPoseStack.translate(0.375f, 0.275f, 0.335f);
                pPoseStack.scale(-0.0135f, -0.0135f, 0.0135f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(270));
            }
            case EAST -> {
                pPoseStack.translate(0.625f, 0.275f, 0.665f);
                pPoseStack.scale(0.0135f, -0.0135f, 0.0135f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
        }

        DyeColor color = pBlockEntity.getColor();
        this.font.drawInBatch(time, 0, 0, color.getTextColor(), false, pPoseStack.last().pose(), pBuffer, Font.DisplayMode.POLYGON_OFFSET, 0, pPackedLight);
        pPoseStack.popPose();
    }
}

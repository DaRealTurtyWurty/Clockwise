package dev.turtywurty.clockwise.network.serverbound;

import dev.turtywurty.clockwise.blockentity.DigitalClockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.time.ZoneId;
import java.util.function.Supplier;

public class SSetTimezonePacket {
    private final BlockPos position;
    private final ZoneId zoneId;

    public SSetTimezonePacket(BlockPos position, ZoneId zoneId) {
        this.position = position;
        this.zoneId = zoneId;
    }

    public SSetTimezonePacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), ZoneId.of(buf.readUtf()));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.position);
        buf.writeUtf(this.zoneId.getId());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            context.setPacketHandled(true);

            Level level = context.getSender().level();
            if (level == null) return;

            BlockEntity be = level.getBlockEntity(this.position);
            if(!(be instanceof DigitalClockBlockEntity blockEntity)) return;

            blockEntity.setTimezone(this.zoneId);
        });
    }
}

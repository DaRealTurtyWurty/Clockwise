package dev.turtywurty.clockwise.network.serverbound;

import dev.turtywurty.clockwise.blockentity.DigitalClockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SRemoveAlarmPacket {
    private final BlockPos position;
    private final int removeIndex;

    public SRemoveAlarmPacket(BlockPos position, int removeIndex) {
        this.position = position;
        this.removeIndex = removeIndex;
    }

    public SRemoveAlarmPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.position);
        buf.writeInt(this.removeIndex);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ctx.setPacketHandled(true);
            if (ctx.getSender() == null)
                return;

            Level level = ctx.getSender().level();
            BlockEntity be = level.getBlockEntity(this.position);
            if (!(be instanceof DigitalClockBlockEntity blockEntity))
                return;

            blockEntity.removeAlarm(this.removeIndex);
        });
    }
}

package dev.turtywurty.clockwise.network.serverbound;

import dev.turtywurty.clockwise.blockentity.DigitalClockBlockEntity;
import dev.turtywurty.clockwise.util.data.Alarm;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SEditAlarmPacket {
    private final BlockPos position;
    private final int editIndex;
    private final Alarm newAlarm;

    public SEditAlarmPacket(BlockPos position, int editIndex, Alarm newAlarm) {
        this.position = position;
        this.editIndex = editIndex;
        this.newAlarm = newAlarm;
    }

    public SEditAlarmPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), Alarm.deserializeNBT(buf.readNbt()));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.position);
        buf.writeInt(this.editIndex);
        buf.writeNbt(this.newAlarm.serializeNBT());
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

            blockEntity.editAlarm(this.editIndex, this.newAlarm);
        });
    }
}

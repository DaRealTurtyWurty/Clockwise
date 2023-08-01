package dev.turtywurty.clockwise.blockentity;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.blockentity.util.TickableBlockEntity;
import dev.turtywurty.clockwise.init.BlockEntityInit;
import dev.turtywurty.clockwise.util.data.Alarm;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DigitalClockBlockEntity extends BlockEntity implements TickableBlockEntity {
    private @Nullable ZoneId timezone;
    private DyeColor color = DyeColor.WHITE;
    private final List<Alarm> alarms = new ArrayList<>();

    public DigitalClockBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.DIGITAL_CLOCK.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {}

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);

        var clockwiseData = new CompoundTag();
        clockwiseData.putString("Timezone", this.timezone == null ? "N/A" : this.timezone.getId());
        clockwiseData.putByte("Color", (byte) this.color.getId());

        var alarms = new ListTag();
        for(Alarm alarm : this.alarms) {
            alarms.add(alarm.serializeNBT());
        }

        clockwiseData.put("Alarms", alarms);

        pTag.put(Clockwise.MOD_ID, clockwiseData);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

        CompoundTag clockwiseData = pTag.getCompound(Clockwise.MOD_ID);
        if(clockwiseData.contains("Timezone", Tag.TAG_STRING)) {
            String timezone = clockwiseData.getString("Timezone");
            this.timezone = timezone.equals("N/A") ? null : ZoneId.of(timezone);
        }

        if(clockwiseData.contains("Color", Tag.TAG_BYTE)) {
            this.color = DyeColor.byId(clockwiseData.getByte("Color"));
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        var tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @Nullable ZoneId getTimezone() {
        return this.timezone;
    }

    public void setTimezone(@Nullable ZoneId timezone) {
        this.timezone = timezone;
        update();
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
        update();
    }

    public List<Alarm> getAlarms() {
        return this.alarms.subList(0, Math.min(5, this.alarms.size()));
    }

    public void addAlarm(Alarm alarm) {
        if(this.alarms.size() >= 5)
            return;

        this.alarms.add(alarm);
        update();
    }

    public void editAlarm(int editIndex, Alarm newAlarm) {
        if(editIndex < 0 || editIndex >= this.alarms.size())
            return;

        this.alarms.set(editIndex, newAlarm);
        update();
    }

    public void removeAlarm(int removeIndex) {
        if(removeIndex < 0 || removeIndex >= this.alarms.size())
            return;

        this.alarms.remove(removeIndex);
        update();
    }

    private void update() {
        setChanged();

        if(this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}

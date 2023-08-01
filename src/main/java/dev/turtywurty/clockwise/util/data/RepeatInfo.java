package dev.turtywurty.clockwise.util.data;

import net.minecraft.nbt.CompoundTag;

public class RepeatInfo {
    public static final RepeatInfo NONE = new RepeatInfo();

    // TODO: Add repeat info
    RepeatInfo() {}

    public CompoundTag serializeNBT() {
        // TODO: Serialize
        return new CompoundTag();
    }

    public static RepeatInfo deserializeNBT(CompoundTag nbt) {
        // TODO: Deserialize
        return NONE;
    }

    public static class Builder {
        public RepeatInfo build() {
            return NONE;
        }
    }
}

package dev.turtywurty.clockwise.util.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SoundInfo {
    public static final SoundInfo NONE = new SoundInfo("", 1.0F, 1.0F);
    private final String sound;
    private final float volume;
    private final float pitch;

    private SoundInfo(String sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public String getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        tag.putString("Sound", this.sound);
        tag.putFloat("Volume", this.volume);
        tag.putFloat("Pitch", this.pitch);
        return tag;
    }

    public static SoundInfo deserializeNBT(CompoundTag nbt) {
        var sound = nbt.getString("Sound");
        var volume = nbt.getFloat("Volume");
        var pitch = nbt.getFloat("Pitch");
        return new SoundInfo(sound, volume, pitch);
    }

    public static class Builder {
        private final String sound;
        private float volume;
        private float pitch;

        public Builder(String sound) {
            this.sound = sound;
            this.volume = 1.0F;
            this.pitch = 1.0F;
        }

        public Builder volume(float volume) {
            this.volume = volume;
            return this;
        }

        public Builder pitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public SoundInfo build() {
            return new SoundInfo(this.sound, this.volume, this.pitch);
        }
    }
}

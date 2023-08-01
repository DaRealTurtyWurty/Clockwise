package dev.turtywurty.clockwise.util.data;

import net.minecraft.nbt.CompoundTag;

public class Alarm {
    private final String name;
    private final long epochSecond;
    private boolean enabled;
    private RepeatInfo repeatInfo;
    private SoundInfo soundInfo;

    private Alarm() {
        this.name = "";
        this.epochSecond = 0;
        this.enabled = true;
        this.repeatInfo = RepeatInfo.NONE;
        this.soundInfo = SoundInfo.NONE;
    }

    private Alarm(String name, long epochSecond, boolean enabled, RepeatInfo repeatInfo, SoundInfo soundInfo) {
        this.name = name;
        this.epochSecond = epochSecond;
        this.enabled = enabled;
        this.repeatInfo = repeatInfo;
        this.soundInfo = soundInfo;
    }

    public String getName() {
        return this.name;
    }

    public long getEpochSecond() {
        return this.epochSecond;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public RepeatInfo getRepeatInfo() {
        return this.repeatInfo;
    }

    public SoundInfo getSoundInfo() {
        return this.soundInfo;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRepeatInfo(RepeatInfo repeatInfo) {
        this.repeatInfo = repeatInfo;
    }

    public void setSoundInfo(SoundInfo soundInfo) {
        this.soundInfo = soundInfo;
    }

    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        tag.putString("Name", this.name);
        tag.putLong("EpochSecond", this.epochSecond);
        tag.putBoolean("Enabled", this.enabled);
        tag.put("RepeatInfo", this.repeatInfo.serializeNBT());
        tag.put("SoundInfo", this.soundInfo.serializeNBT());
        return tag;
    }

    public static Alarm deserializeNBT(CompoundTag nbt) {
        String name = nbt.getString("Name");
        long epochSecond = nbt.getLong("EpochSecond");
        boolean enabled = nbt.getBoolean("Enabled");

        RepeatInfo repeatInfo = RepeatInfo.deserializeNBT(nbt.getCompound("RepeatInfo"));
        SoundInfo soundInfo = SoundInfo.deserializeNBT(nbt.getCompound("SoundInfo"));
        return new Alarm(name, epochSecond, enabled, repeatInfo, soundInfo);
    }

    public static class Builder {
        private final String name;
        private final long epochSecond;
        private boolean enabled;
        private RepeatInfo repeatInfo;
        private SoundInfo soundInfo;

        public Builder(String name, long epochSecond) {
            this.name = name;
            this.epochSecond = epochSecond;
            this.enabled = true;
            this.repeatInfo = RepeatInfo.NONE;
            this.soundInfo = SoundInfo.NONE;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder repeatInfo(RepeatInfo.Builder repeatInfo) {
            this.repeatInfo = repeatInfo.build();
            return this;
        }

        public Builder soundInfo(SoundInfo.Builder soundInfo) {
            this.soundInfo = soundInfo.build();
            return this;
        }

        public Alarm build() {
            return new Alarm(this.name, this.epochSecond, this.enabled, this.repeatInfo, this.soundInfo);
        }
    }
}

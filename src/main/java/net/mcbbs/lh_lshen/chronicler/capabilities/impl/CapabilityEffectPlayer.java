package net.mcbbs.lh_lshen.chronicler.capabilities.impl;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.minecraft.nbt.CompoundNBT;

public class CapabilityEffectPlayer implements ICapabilityEffectPlayer {
    private String id = "";
    private int level;
    private int counter;
    private boolean isDirty;
    @Override
    public String getType() {
        return id;
    }

    @Override
    public void setType(String id) {
        this.id = id;
        setDirty(true);
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        setDirty(true);
    }

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public void setCounter(int counter) {
        this.counter = counter;
        setDirty(true);
    }

    @Override
    public void reset() {
        this.id = "";
        this.level = 0;
        this.counter = 0;
        setDirty(true);
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("id",this.id);
        nbt.putInt("level",this.level);
        nbt.putInt("counter",this.counter);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.id = nbt.getString("id");
        this.level = nbt.getInt("level");
        this.counter = nbt.getInt("counter");
    }
}

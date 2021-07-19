package net.mcbbs.lh_lshen.chronicler.capabilities.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapabilityEffectPlayer extends INBTSerializable<CompoundNBT> {
    String getType();
    void setType(String id);
    int getLevel();
    void setLevel(int level);
    int getCounter();
    void setCounter(int counter);
    void reset();
    boolean isDirty();
    void setDirty(boolean isDirty);
}

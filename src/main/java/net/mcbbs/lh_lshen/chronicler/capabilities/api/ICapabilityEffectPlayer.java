package net.mcbbs.lh_lshen.chronicler.capabilities.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapabilityEffectPlayer extends INBTSerializable<CompoundNBT> {
    String getInscription();
    void setInscription(String id);
    int getLevel();
    void setLevel(int level);
    int getDuration();
    void setDuration(int duration);
    void reset();
    boolean isDirty();
    void setDirty(boolean isDirty);
}

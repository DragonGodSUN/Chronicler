package net.mcbbs.lh_lshen.chronicler.capabilities.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapabilityInscription extends INBTSerializable<CompoundNBT> {
    String getInscription();
    int getLevel();
    void setInscription(String id);
    void setLevel(int level);
    boolean isDirty();
    void setDirty(boolean isDirty);
}

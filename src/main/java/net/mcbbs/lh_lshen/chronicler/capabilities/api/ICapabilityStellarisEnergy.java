package net.mcbbs.lh_lshen.chronicler.capabilities.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapabilityStellarisEnergy extends INBTSerializable<CompoundNBT> {
    int getEnergyPoint();
    int getEnergyMax();
    String getId();
    void setId(String id);
    void setEnergyPoint(int point);
    void setEnergyMax(int point);
    void charge(int point);
    void cost(int point);
    boolean canCharge();
    boolean canCost(int point);
    boolean isDirty();
    void setDirty(boolean isDirty);
}

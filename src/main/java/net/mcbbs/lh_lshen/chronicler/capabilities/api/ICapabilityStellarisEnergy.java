package net.mcbbs.lh_lshen.chronicler.capabilities.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapabilityStellarisEnergy extends INBTSerializable<CompoundNBT> {
    int getEnergyPoint();
    int getEnergyMax();
    void setEnergyPoint(int point);
    void setEnergyMax(int point);
    void charge(int point);
    void cost(int point);
    boolean canCharge();
    boolean canCost(int point);
    void resetMax();
    void reset();
    boolean isDirty();
    void setDirty(boolean isDirty);
}

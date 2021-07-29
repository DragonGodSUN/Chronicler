package net.mcbbs.lh_lshen.chronicler.capabilities.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapabilityInscription extends INBTSerializable<CompoundNBT> {
    String getInscription();
    int getLevel();
    void setInscription(String id);
    void setLevel(int level);
    void reset();
    boolean isDirty();
    void setDirty(boolean isDirty);
    void loadIfNotLoaded(ItemStack stack);
}

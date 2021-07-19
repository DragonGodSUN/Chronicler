package net.mcbbs.lh_lshen.chronicler.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTHelper {
    public static CompoundNBT getSafeNBTCompond(ItemStack itemStack) {
        return itemStack.getOrCreateTag();
    }
}

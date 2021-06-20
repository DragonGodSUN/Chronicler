package net.mcbbs.lh_lshen.chronicler.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Map;

public interface ICapabilityItemList extends INBTSerializable<ListNBT> {
    ItemStack getItemStack(String item_id, String id);
    void addItemStack(ItemStack itemStack, String id);
    void delItemStack(String item_id, int id);

}

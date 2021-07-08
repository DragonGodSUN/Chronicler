package net.mcbbs.lh_lshen.chronicler.capabilities.api;

import net.mcbbs.lh_lshen.chronicler.inventory.SelectCompnent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Map;

public interface ICapabilityItemList extends INBTSerializable<ListNBT> {
    Map<String, List<ItemStack>> getAllMap();
    void setAllMap(Map<String, List<ItemStack>> map);
    ItemStack getItemStack(String item_id,int index);
    void addItemStack(ItemStack itemStack);
    void setItemStack(ItemStack itemStack,int index);
    void delItemStack(String item_id, int index);

}

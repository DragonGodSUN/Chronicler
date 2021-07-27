package net.mcbbs.lh_lshen.chronicler.capabilities.api;

import net.mcbbs.lh_lshen.chronicler.inventory.SelectCompnent;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Map;

public interface ICapabilityItemList extends INBTSerializable<ListNBT> {
    Map<String, List<ItemStack>> getAllMap();
    List<String> getKeyList();
    String getUuid();
    void setUuid(String uuid);
    void setKeyList(List<String> keyList);
    void setKeyIndex(String id, int index);
    void setAllMap(Map<String, List<ItemStack>> map);
    ItemStack getItemStack(String item_id,int index);
    void addItemStack(ItemStack itemStack);
    void setItemStack(ItemStack itemStack,int index);
    void delItemStack(String item_id, int index);
    Inventory getInventoryStar();
    void setInventoryStar(Inventory inventoryStar);
    void setStar(ItemStack itemStack);
    void removeStar(ItemStack itemStack);

    boolean isDirty();
    void setDirty(boolean flag);

}

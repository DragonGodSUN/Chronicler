package net.mcbbs.lh_lshen.chronicler.capabilities.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.mcbbs.lh_lshen.chronicler.capabilities.ICapabilityItemList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.List;
import java.util.Map;

public class CapabilityItemList implements ICapabilityItemList {
    private Map<String, Map<String,ItemStack>> itemAllMap;

    private Map<String,ItemStack> getItemMap(String item_id) {
        if (itemAllMap.containsKey(item_id)) {
            return itemAllMap.get(item_id);
        }
        return null;
    }
    @Override
    public ItemStack getItemStack(String item_id, String id) {
        ItemStack stack = null;
        if (itemAllMap.containsKey(item_id)) {
            Map<String,ItemStack> itemStackList = itemAllMap.get(item_id);
            stack = itemStackList.get(id);
        }
        return stack;
    }


    @Override
    public void addItemStack(ItemStack itemStack, String id) {
        Map<String,ItemStack> itemStackMap = getItemMap(itemStack.getItem().getRegistryName().toString());
        if (itemStackMap != null){
            itemStackMap.put(id, itemStack);
        }else {
            Map<String,ItemStack> itemStackList_new = Maps.newHashMap();
            itemStackList_new.put(id,itemStack);
            itemAllMap.put(itemStack.getItem().getRegistryName().toString(),itemStackList_new);
        }
    }


    @Override
    public void delItemStack(String item_id, int id) {
        Map<String,ItemStack> itemStackMap = getItemMap(item_id);
        if (itemStackMap != null){
            itemStackMap.remove(id);
        }
    }

    @Override
    public ListNBT serializeNBT() {
        ListNBT nbtTagList = new ListNBT();
        for(Map.Entry<String, Map<String,ItemStack>> entry:itemAllMap.entrySet()){
            for (Map.Entry<String,ItemStack> stackEntry:entry.getValue().entrySet()){
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putString("item_id",entry.getKey());
                itemTag.putString("stack_id",stackEntry.getKey());
                stackEntry.getValue().save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        return nbtTagList;
    }

    @Override
    public void deserializeNBT(ListNBT nbtList) {
        Map<String, Map<String,ItemStack>> itemAllMap_nbt = Maps.newHashMap();
        Map<String,ItemStack> stackMap_nbt = Maps.newHashMap();
        List<String> id_list = Lists.newArrayList();
        for (INBT tag:nbtList){
            if (tag instanceof CompoundNBT){
                CompoundNBT itemNbt = (CompoundNBT) tag;
                String id = itemNbt.getString("item_id");
                String stack_id = itemNbt.getString("stack_id");
                ItemStack stack = ItemStack.of(itemNbt);
                id_list.add(id);
                stackMap_nbt.put(stack_id,stack);
            }
        }
        for (Map.Entry<String,ItemStack> stackEntry:stackMap_nbt.entrySet()){
            String id = stackEntry.getValue().getItem().getRegistryName().toString();
            if (id_list.contains(id)){
                itemAllMap_nbt.put(id,stackMap_nbt);
            }
        }

    }
}

package net.mcbbs.lh_lshen.chronicler.capabilities.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.List;
import java.util.Map;

public class CapabilityItemList implements ICapabilityItemList {
    private Map<String, List<ItemStack>> itemAllMap = Maps.newHashMap();

    public CapabilityItemList() {
    }

    public CapabilityItemList(ListNBT nbt) {
       deserializeNBT(nbt);
    }


    private List<ItemStack> getItemList(String item_id) {
        if (itemAllMap.containsKey(item_id)) {
            return itemAllMap.get(item_id);
        }
        return null;
    }

    @Override
    public Map<String, List<ItemStack>> getAllMap() {
        return itemAllMap;
    }

    @Override
    public void setAllMap(Map<String, List<ItemStack>> map) {
        this.itemAllMap = map;
    }

    @Override
    public ItemStack getItemStack(String item_id, int index) {
        ItemStack stack = ItemStack.EMPTY;
        if (itemAllMap.containsKey(item_id)) {
            List<ItemStack> itemStackList = itemAllMap.get(item_id);
            stack = itemStackList.get(index);
        }
        return stack;
    }


    @Override
    public void addItemStack(ItemStack itemStack) {
        List<ItemStack> itemList = getItemList(itemStack.getItem().getRegistryName().toString());
        if (itemList != null){
            itemList.add(itemStack);
        }else {
            List<ItemStack> itemList_new = Lists.newArrayList();
            itemList_new.add(itemStack);
            itemAllMap.put(itemStack.getItem().getRegistryName().toString(),itemList_new);
        }
    }

    @Override
    public void setItemStack(ItemStack itemStack, int index) {
        List<ItemStack> itemList = getItemList(itemStack.getItem().getRegistryName().toString());
        if (itemList != null){
            itemList.set(index,itemStack);
        }else {
            List<ItemStack> itemList_new = Lists.newArrayList();
            itemList_new.set(index,itemStack);
            itemAllMap.put(itemStack.getItem().getRegistryName().toString(),itemList_new);
        }
    }


    @Override
    public void delItemStack(String item_id, int index) {
        List<ItemStack> itemStackList = getItemList(item_id);
        if (itemStackList != null){
            itemStackList.remove(index);
        }
    }

    @Override
    public ListNBT serializeNBT() {
        ListNBT nbtTagList = new ListNBT();
        for(Map.Entry<String, List<ItemStack>> entry:itemAllMap.entrySet()){
            for (ItemStack itemStack:entry.getValue()){
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putString("item_id",entry.getKey());
                itemTag.putInt("stack_index",entry.getValue().indexOf(itemStack));
                itemStack.save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        return nbtTagList;
    }

    @Override
    public void deserializeNBT(ListNBT nbtList) {
        Map<String, List<ItemStack>> itemAllMap_nbt = Maps.newHashMap();
        List<ItemStack> itemStackList = Lists.newArrayList();
        List<String> id_list = Lists.newArrayList();
        for (INBT tag:nbtList){
            if (tag instanceof CompoundNBT){
                CompoundNBT itemNbt = (CompoundNBT) tag;
                String id = itemNbt.getString("item_id");
                int stack_id = itemNbt.getInt("stack_index");
                ItemStack stack = ItemStack.of(itemNbt);
                if (!stack.isEmpty()) {
                    id_list.add(id);
                    itemStackList.add(stack);
                    itemStackList.set(stack_id,stack);
                }

            }
        }
        if (!itemStackList.isEmpty()) {
            for (ItemStack itemStack:itemStackList){
                String id = itemStack.getItem().getRegistryName().toString();
                if (id_list.contains(id)){
                    itemAllMap_nbt.put(id,itemStackList);
                }
            }
        }
        this.itemAllMap = itemAllMap_nbt;

    }
}

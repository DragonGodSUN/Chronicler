package net.mcbbs.lh_lshen.chronicler.capabilities.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.List;
import java.util.Map;

public class CapabilityItemList implements ICapabilityItemList {
    private String uuid = "";
    private Map<String, List<ItemStack>> itemAllMap = Maps.newHashMap();
    private List<String> keyList = Lists.newArrayList();
    private Inventory inventoryStar = new Inventory(8);
    private boolean isDirty;

    public CapabilityItemList() {
    }

    public CapabilityItemList(ListNBT nbt) {
       deserializeNBT(nbt);
    }


    private List<ItemStack> getItemList(String item_id) {
        if (itemAllMap.containsKey(item_id)) {
            return itemAllMap.get(item_id);
        }
        return Lists.newArrayList();
    }

    @Override
    public Map<String, List<ItemStack>> getAllMap() {
        return itemAllMap;
    }

    @Override
    public void setAllMap(Map<String, List<ItemStack>> map) {
        this.itemAllMap = map;
        markDirty();
    }

    @Override
    public List<String> getKeyList() {
        return keyList;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
        markDirty();
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
        markDirty();
    }

    @Override
    public void setKeyIndex(String id, int index) {
        if (index < keyList.size()) {
            keyList.set(index,id);
        }
        markDirty();
    }


    @Override
    public ItemStack getItemStack(String item_id, int index) {
        ItemStack stack = ItemStack.EMPTY;
        if (itemAllMap.containsKey(item_id)) {
            List<ItemStack> itemStackList = itemAllMap.get(item_id);
            if (index < itemStackList.size()) {
                stack = itemStackList.get(index);
            }
        }
        return stack;
    }


    @Override
    public void addItemStack(ItemStack itemStack) {
        List<ItemStack> itemList = getItemList(itemStack.getItem().getRegistryName().toString());
        ItemStack itemStack1 = itemStack.copy();
        itemStack1.setCount(1);
        if (itemList != null && !itemList.isEmpty()){
            boolean isUnique = true;
            for (ItemStack i:itemList){
                if (i.equals(itemStack1,false)) {
                    isUnique = false;
                    break;
                }
            }
            if (isUnique) {
                itemList.add(itemStack1);
            }
        }else {
            List<ItemStack> itemList_new = Lists.newArrayList();
            itemList_new.add(itemStack1);
            keyList.add(itemStack1.getItem().getRegistryName().toString());
            itemAllMap.put(itemStack1.getItem().getRegistryName().toString(),itemList_new);
        }
        markDirty();
    }

    @Override
    public void setItemStack(ItemStack itemStack, int index) {
        List<ItemStack> itemList = getItemList(itemStack.getItem().getRegistryName().toString());
        ItemStack itemStack1 = itemStack.copy();
        itemStack1.setCount(1);
        if (index < itemList.size()) {
            if (itemList != null){
                itemList.set(index,itemStack1);
            }else {
                List<ItemStack> itemList_new = Lists.newArrayList();
                itemList_new.add(itemStack1);
                itemAllMap.put(itemStack1.getItem().getRegistryName().toString(),itemList_new);
            }
        }
        markDirty();
    }


    @Override
    public void delItemStack(String item_id, int index) {
        List<ItemStack> itemStackList = getItemList(item_id);
        if (itemStackList != null && index <itemStackList.size()){
            itemStackList.remove(index);
            itemAllMap.put(item_id,itemStackList);
        }
        if (itemStackList.isEmpty()){
            itemAllMap.remove(item_id);
            keyList.remove(item_id);
        }
        markDirty();
    }

    @Override
    public Inventory getInventoryStar() {
        return this.inventoryStar;
    }

    public void setInventoryStar(Inventory inventoryStar) {
        this.inventoryStar = inventoryStar;
        markDirty();
    }

    @Override
    public void setStar(ItemStack itemStack) {
        boolean flag =false;
        for (int i=0;i<this.inventoryStar.getContainerSize();i++){
            ItemStack stack = this.inventoryStar.getItem(i);
            if (stack.equals(itemStack,false)){
                flag =true;
                break;
            }
        }
        if (!flag&&this.inventoryStar.canAddItem(itemStack)){
            this.inventoryStar.addItem(itemStack);
        }
        markDirty();

    }

    @Override
    public void removeStar(ItemStack itemStack) {
        for (int i=0;i<this.inventoryStar.getContainerSize();i++){
            ItemStack stack = this.inventoryStar.getItem(i);
            if (stack.equals(itemStack,false)){
                this.inventoryStar.removeItem(i,1);
                break;
            }
        }
        markDirty();

    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void markDirty(){
        setDirty(true);
    }

    @Override
    public ListNBT serializeNBT() {
        ListNBT nbtTagList = new ListNBT();
        for(Map.Entry<String, List<ItemStack>> entry:itemAllMap.entrySet()){
            for (ItemStack itemStack:entry.getValue()){
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putString("type","lib");
                itemTag.putString("item_id",entry.getKey());
                itemStack.save(itemTag);
                nbtTagList.add(itemTag);
            }
        }

        for (int i=0;i<this.inventoryStar.getContainerSize();i++){
            ItemStack stack = this.inventoryStar.getItem(i);
            CompoundNBT invTag = new CompoundNBT();
            invTag.putString("type","star");
            stack.save(invTag);
            nbtTagList.add(invTag);
        }

        for (String id:keyList){
            CompoundNBT idTag = new CompoundNBT();
            idTag.putString("type","key");
            idTag.putString("id_config",id);
            nbtTagList.add(idTag);
        }
            CompoundNBT idUUTag = new CompoundNBT();
            idUUTag.putString("type","uuid");
            idUUTag.putString("uuid",this.uuid);
            nbtTagList.add(idUUTag);
        return nbtTagList;
    }

    @Override
    public void deserializeNBT(ListNBT nbtList) {
        Map<String, List<ItemStack>> itemAllMap_nbt = Maps.newHashMap();
        List<String> itemStackConfigList = Lists.newArrayList();
        List<ItemStack> itemStackList = Lists.newArrayList();
        List<String> id_list = Lists.newArrayList();
        Inventory inventory_nbt = new Inventory(8);

        if (nbtList != null) {
            for (INBT tag: nbtList){
                if (tag instanceof CompoundNBT){
                    CompoundNBT itemNbt = (CompoundNBT) tag;
                    String type = itemNbt.getString("type");
                    if (type.equals("lib")) {
                        String id = itemNbt.getString("item_id");
                        ItemStack stack = ItemStack.of(itemNbt);
                        if (!stack.isEmpty()) {
                            id_list.add(id);
                            if (!itemStackList.contains(stack)) {
                                itemStackList.add(stack);
                            }
                        }
                    }
                    if (type.equals("star")) {
                        ItemStack stack = ItemStack.of(itemNbt);
                        inventory_nbt.addItem(stack);
                    }
                    if (type.equals("key")) {
                    String id_config = itemNbt.getString("id_config");
                    if (id_list.contains(id_config)) {
                        itemStackConfigList.add(id_config);
                        }
                    }
                    if (type.equals("uuid")) {
                        this.uuid = itemNbt.getString("uuid");
                    }
                }
            }
        }

        if (!itemStackList.isEmpty()) {
            for (String id : id_list) {
                List<ItemStack> stackList = Lists.newArrayList();
                for (ItemStack stack : itemStackList) {
                    String stack_id = stack.getItem().getRegistryName().toString();
                    if (id.equals(stack_id)){
                        stackList.add(stack);
                    }
                }
                itemAllMap_nbt.put(id,stackList);
            }
        }

        this.keyList = itemStackConfigList;
        this.itemAllMap = itemAllMap_nbt;
        this.inventoryStar = inventory_nbt;

    }
}

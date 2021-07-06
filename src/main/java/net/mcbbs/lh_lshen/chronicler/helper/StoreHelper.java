package net.mcbbs.lh_lshen.chronicler.helper;

import com.google.common.collect.Lists;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class StoreHelper {

    public static boolean hasItemStack(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        Map<String, List<ItemStack>> allMap = capabilityItemList.getAllMap();
        if (!itemStack.isEmpty()){
            String item_reg_id = itemStack.getItem().getRegistryName().toString();
            if (allMap.containsKey(item_reg_id)){
                List<ItemStack> stackList = allMap.get(item_reg_id);
                return stackList.contains(itemStack);
            }
        }
        return false;
    }

    public static ItemStack getItemStack(ICapabilityItemList capabilityItemList, String item_id, int index){
        return capabilityItemList.getItemStack(item_id,index);
    }

    public static int getStackIndex(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        Map<String, List<ItemStack>> allMap = capabilityItemList.getAllMap();
        String item_reg_id = itemStack.getItem().getRegistryName().toString();
        if(!itemStack.isEmpty() && hasItemStack(capabilityItemList,itemStack)){
            List<ItemStack> stackList = allMap.get(item_reg_id);
            if (stackList.contains(itemStack)) {
                return stackList.indexOf(itemStack);
            }
        }
        return 0;
    }

    public static void upIndex(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        if (!itemStack.isEmpty() && hasItemStack(capabilityItemList,itemStack)){
            int index_select = getStackIndex(capabilityItemList,itemStack);
            String item_reg_id = itemStack.getItem().getRegistryName().toString();
            if(index_select>0){
                ItemStack itemStack1 = capabilityItemList.getItemStack(item_reg_id,index_select-1);
                if (!itemStack1.isEmpty()) {
                    capabilityItemList.setItemStack(itemStack1,index_select);
                    capabilityItemList.setItemStack(itemStack,index_select-1);
                }
            }
        }
    }

    public static void downIndex(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        if (!itemStack.isEmpty() && hasItemStack(capabilityItemList,itemStack)){
            int index_select = getStackIndex(capabilityItemList,itemStack);
            String item_reg_id = itemStack.getItem().getRegistryName().toString();
            ItemStack itemStack1 = capabilityItemList.getItemStack(item_reg_id,index_select+1);
            if(!itemStack1.isEmpty()){
                capabilityItemList.setItemStack(itemStack1,index_select);
                capabilityItemList.setItemStack(itemStack,index_select+1);
            }
        }
    }

    public static void addItemStack(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        if (!itemStack.isEmpty()) {
            capabilityItemList.addItemStack(itemStack);
        }
    }

    public static void deleteItemStack(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        if (hasItemStack(capabilityItemList,itemStack)){
            int index = getStackIndex(capabilityItemList,itemStack);
            capabilityItemList.delItemStack(itemStack.getItem().getRegistryName().toString(),index);
        }
    }

    public static Inventory getAllInventory(Map<String, List<ItemStack>> allItemMap){
        List<ItemStack> itemStackList_All = Lists.newArrayList();
        for (Map.Entry<String,List<ItemStack>> entry : allItemMap.entrySet()){
            List<ItemStack> list = entry.getValue();
            itemStackList_All.addAll(list);
        }
        if (itemStackList_All.size()>0) {
            Inventory inventory = new Inventory();
            for (ItemStack stack:itemStackList_All) {
                inventory.addItem(stack);
            }
            return inventory;
        }
        return new Inventory(8);
    }

    public static Inventory getSingerInventory(Map<String, List<ItemStack>> allItemMap, String item_id){
        List<ItemStack> itemStackList = Lists.newArrayList();
        if (allItemMap.containsKey(item_id)) {
            itemStackList = allItemMap.get(item_id);
        }
        Inventory inventory = new Inventory(8);
        for (ItemStack stack:itemStackList) {
            if (stack.getItem().getRegistryName().toString().equals(item_id)) {
                inventory.addItem(stack);
            }
        }
        return inventory;
    }

}

package net.mcbbs.lh_lshen.chronicler.helper;

import com.google.common.collect.Lists;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.ManageEnergyCapMessage;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.ManageInscriptionCapMessage;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.ManageItemListCapMessage;
import net.minecraft.inventory.Inventory;
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
                for (ItemStack s : stackList) {
                    itemStack.equals(s,false);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static ItemStack getItemStack(ICapabilityItemList capabilityItemList, String item_id, int index){
        return capabilityItemList.getItemStack(item_id,index);
    }

    public static List<ItemStack> getItemStackList(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        return capabilityItemList.getAllMap().get(itemStack.getItem().getRegistryName().toString());
    }

    public static int getStackIndex(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        Map<String, List<ItemStack>> allMap = capabilityItemList.getAllMap();
        String item_reg_id = itemStack.getItem().getRegistryName().toString();
        if(!itemStack.isEmpty() && hasItemStack(capabilityItemList,itemStack)){
            List<ItemStack> stackList = allMap.get(item_reg_id);
            for (int s =0;s<stackList.size();s++) {
                if (stackList.get(s).equals(itemStack,false)){
                    return s;
                }
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
            int size = capabilityItemList.getAllMap().get(item_reg_id).size();
            if (index_select < size-1) {
                ItemStack itemStack1 = capabilityItemList.getItemStack(item_reg_id,index_select+1);
                if(!itemStack1.isEmpty()){
                    capabilityItemList.setItemStack(itemStack1,index_select);
                    capabilityItemList.setItemStack(itemStack,index_select+1);
                }
            }
        }
    }

    public static void setFirstIndex(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        if (!itemStack.isEmpty() && hasItemStack(capabilityItemList,itemStack)){
            int index_select = getStackIndex(capabilityItemList,itemStack);
            String item_reg_id = itemStack.getItem().getRegistryName().toString();
            ItemStack itemStack1 = capabilityItemList.getItemStack(item_reg_id,0);
            if(!itemStack1.isEmpty()){
                capabilityItemList.setItemStack(itemStack1,index_select);
                capabilityItemList.setItemStack(itemStack,0);
            }
        }
    }

    public static void upStackListIndex(ICapabilityItemList capabilityItemList,ItemStack itemStack){
        if (!itemStack.isEmpty() && hasItemStack(capabilityItemList,itemStack)){
            String item_reg_id = itemStack.getItem().getRegistryName().toString();
            int index_select = capabilityItemList.getKeyList().indexOf(item_reg_id);
            if(index_select>0){
                String item_reg_id_replace = capabilityItemList.getKeyList().get(index_select-1);
                capabilityItemList.setKeyIndex(item_reg_id,index_select-1);
                capabilityItemList.setKeyIndex(item_reg_id_replace,index_select);
            }
        }
    }

    public static void downStackListIndex(ICapabilityItemList capabilityItemList,ItemStack itemStack){
        if (!itemStack.isEmpty() && hasItemStack(capabilityItemList,itemStack)){
            List<String> keys = capabilityItemList.getKeyList();
            String item_reg_id = itemStack.getItem().getRegistryName().toString();
            int index_select = keys.indexOf(item_reg_id);
            if(index_select+1<keys.size()){
                String item_reg_id_replace = capabilityItemList.getKeyList().get(index_select+1);
                capabilityItemList.setKeyIndex(item_reg_id,index_select+1);
                capabilityItemList.setKeyIndex(item_reg_id_replace,index_select);
            }
        }
    }

    public static int getStackListIndex(ICapabilityItemList capabilityItemList,ItemStack itemStack){
        if (!itemStack.isEmpty() && hasItemStack(capabilityItemList,itemStack)){
            List<String> keys = capabilityItemList.getKeyList();
            String item_reg_id = itemStack.getItem().getRegistryName().toString();
            int index_select = keys.indexOf(item_reg_id);
            return index_select;
        }
        return 0;
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

    public static int getPage(ICapabilityItemList capabilityItemList, ItemStack itemStack){
        if (hasItemStack(capabilityItemList,itemStack)){
            int listIndex = getStackListIndex(capabilityItemList,itemStack);
            int page = listIndex / 8;
            return page;
        }
        return 0;
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
        int size = (int) (1 + itemStackList.size()/4)*4;
        Inventory inventory = new Inventory(size);
        for (ItemStack stack:itemStackList) {
            if (stack.getItem().getRegistryName().toString().equals(item_id)) {
                inventory.addItem(stack);
            }
        }
        return inventory;
    }

    public static boolean isItemStar(CapabilityItemList capabilityItemList, ItemStack itemStack){
        Inventory inventory = capabilityItemList.getInventoryStar();
        boolean flag =false;
        for (int i=0;i<inventory.getContainerSize();i++){
            ItemStack stack = inventory.getItem(i);
            if (stack.equals(itemStack,false)){
                flag =true;
                break;
            }
        }
        return flag;
    }

    public static void synItemListCapabilityToSever(ItemStack chronicler, ICapabilityItemList capabilityItemList){
        ChroniclerNetwork.INSTANCE.sendToServer(new ManageItemListCapMessage(chronicler,capabilityItemList));
    }

    public static void synEnergyCapabilityToSever(ItemStack chronicler, ICapabilityStellarisEnergy energy){
        ChroniclerNetwork.INSTANCE.sendToServer(new ManageEnergyCapMessage(chronicler,energy));
    }

    public static void synInscriptionCapabilityToSever(ItemStack chronicler, ICapabilityInscription inscription){
        ChroniclerNetwork.INSTANCE.sendToServer(new ManageInscriptionCapMessage(chronicler,inscription));
    }

}

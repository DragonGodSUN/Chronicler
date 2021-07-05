package net.mcbbs.lh_lshen.chronicler.inventory;

import com.google.common.collect.Maps;
import net.mcbbs.lh_lshen.chronicler.CommonEventHandler;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ContainerChronicier extends Container {

    private CapabilityItemList cap_list;
    private ItemStack itemStack;
    private Map<String, List<ItemStack>> allItemMap = Maps.newHashMap();
    protected ContainerChronicier(int windowId, PlayerInventory playerInv,
                                  CapabilityItemList cap_list,
                                  ItemStack itemStack) {
        super(CommonEventHandler.containerTypeChronicler, windowId);
        this.cap_list = cap_list;
        this.itemStack = itemStack;
        itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY).ifPresent((capabilityItemList)->{
            this.allItemMap = capabilityItemList.getAllMap();
        });
        addSlots();
    }

    private void addSlots(){
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int LIST_XPOS = 20;
        final int LIST_YPOS = 20;
        int i = 0;
        int j = 0;
        for (Map.Entry<String,List<ItemStack>> entry : allItemMap.entrySet()){
            List<ItemStack> list = entry.getValue();
            for (ItemStack stack:list){
                    addSlot(new Slot(StoreHelper.getSingerInventory(allItemMap,entry.getKey()), i, LIST_XPOS + SLOT_X_SPACING * i, LIST_YPOS + SLOT_Y_SPACING * j));
                i++;
            }
            j++;
        }
    }

    public static ContainerChronicier createContainerServerSide(int windowID, PlayerInventory playerInventory, CapabilityItemList cap_list,
                                                               ItemStack itemStack) {
        return new ContainerChronicier(windowID, playerInventory, cap_list, itemStack);
    }

    public static ContainerChronicier createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        try {
            CapabilityItemList capabilityItemList = new CapabilityItemList();

            return new ContainerChronicier(windowID, playerInventory, capabilityItemList, ItemStack.EMPTY);
        } catch (IllegalArgumentException iae) {
            Logger.getGlobal().info(iae.toString());
        }
        return null;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        ItemStack main = player.getMainHandItem();
        return (!main.isEmpty() && main == itemStack);
    }

    @Override
    public void broadcastChanges() {
        if (cap_list.isDirty()) {
            CompoundNBT nbt = itemStack.getOrCreateTag();
            int dirtyCounter = nbt.getInt("dirtyCounter");
            nbt.putInt("dirtyCounter", dirtyCounter + 1);
            itemStack.setTag(nbt);
            cap_list.setDirty(false);
        }
        super.broadcastChanges();
    }
}

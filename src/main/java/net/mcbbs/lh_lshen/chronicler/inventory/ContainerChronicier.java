package net.mcbbs.lh_lshen.chronicler.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.mcbbs.lh_lshen.chronicler.events.CommonEventHandler;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.mcbbs.lh_lshen.chronicler.inventory.gui.SlotChronicler;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ContainerChronicier extends Container {

    private CapabilityItemList cap_list;
    private ItemStack itemStack;
    private Map<String, List<ItemStack>> allItemMap;
    private PlayerInventory inventory_player;
    private List<Inventory> inventories = Lists.newArrayList();
    public SelectCompnent selectCompnent = new SelectCompnent();
    protected ContainerChronicier(int windowId, PlayerInventory playerInv,
                                  CapabilityItemList cap_list,
                                  ItemStack itemStack) {
        super(CommonEventHandler.containerTypeChronicler, windowId);
        this.cap_list = cap_list;
        this.itemStack = itemStack;
        this.allItemMap = cap_list.getAllMap();
        if (itemStack.getItem() instanceof ItemChronicler) {
            this.selectCompnent = ((ItemChronicler) itemStack.getItem()).getSelectCompnent(itemStack);
        }
        this.inventory_player = playerInv;
        makeInventories();
        addSlots();
    }

    private void addSlots(){
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int LIST_XPOS = 110;
        final int LIST_YPOS = 15;
        int page = selectCompnent.getPage();
        List<String> keys = Lists.newArrayList();
        keys.addAll(allItemMap.keySet());
        for (int j =0;j<8;j++) {
            if (j<keys.size()) {
                Inventory inventory = inventories.get( page*8 + j );
                for (int i = 0; i<8; i++){
                    addSlot(new SlotChronicler(inventory,i,LIST_XPOS + SLOT_X_SPACING * i, LIST_YPOS + SLOT_Y_SPACING * j));
                }
            }
        }
//        int y = 0;
//        for (Map.Entry<String,List<ItemStack>> entry : allItemMap.entrySet()){
//            Inventory inventory = StoreHelper.getSingerInventory(allItemMap,entry.getKey());
//            System.out.println(y+"--"+entry.getKey()+"--"+inventory.getContainerSize()+"--"+inventory);
//            for (int i=0;i<inventory.getContainerSize();i++){
//                addSlot(new SlotChronicler(inventory,i, LIST_XPOS + SLOT_X_SPACING * i, LIST_YPOS + SLOT_Y_SPACING * y));
//            }
//            y++;
//        }
    }

    private void makeInventories(){
        List<String> keys = Lists.newArrayList();
        keys.addAll(allItemMap.keySet());
        for (int i =0;i<keys.size();i++) {
            Inventory inventory = StoreHelper.getSingerInventory(allItemMap,keys.get(i));
            this.inventories.add(inventory);
        }
    }

    @Override
    public ItemStack clicked(int slot, int player_slot, ClickType clickType, PlayerEntity playerEntity) {
        if (slot>=0) {
            ItemStack itemStackSelect = this.slots.get(slot).getItem();
            if (itemStackSelect!=null && playerEntity !=null){
               playerEntity.drop(itemStackSelect,true);
               selectCompnent.selectSlot(slot);
               selectCompnent.page+=1;
                ((ItemChronicler) itemStack.getItem()).setSelectCompnent(itemStack,selectCompnent);
            }else {
               selectCompnent.page-=1;
                ((ItemChronicler) itemStack.getItem()).setSelectCompnent(itemStack,selectCompnent);
            }
        }
        return super.clicked(slot, player_slot, clickType, playerEntity);
    }

    public static ContainerChronicier createContainerServerSide(int windowID, PlayerInventory playerInventory, CapabilityItemList cap_list,
                                                                ItemStack itemStack) {
        return new ContainerChronicier(windowID, playerInventory, cap_list, itemStack);
    }

    public static ContainerChronicier createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        ItemStack stack = extraData.readItem();
        int size = extraData.readInt();
        ListNBT listNBT = new ListNBT();
        for (int i=0;i<size;i++){
            CompoundNBT nbt = extraData.readNbt();
            listNBT.add(nbt);
        }
        CapabilityItemList capabilityItemList = new CapabilityItemList();
        if (stack.getItem() instanceof ItemChronicler){
        try {
//            capabilityItemList = ((ItemChronicler)stack.getItem()).getItemListCapability(stack);
            capabilityItemList.deserializeNBT(listNBT);
            if (capabilityItemList!=null) {
                System.out.println("tag-list:"+listNBT);
                System.out.println("tag-map:"+capabilityItemList.getAllMap());
                return new ContainerChronicier(windowID, playerInventory, capabilityItemList, stack);
            }
            } catch (IllegalArgumentException iae) {
                Logger.getGlobal().info(iae.toString());
            }
            return new ContainerChronicier(windowID, playerInventory, capabilityItemList, ItemStack.EMPTY);
        }
        return null;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        ItemStack main = player.getMainHandItem();
        return (!main.isEmpty() && main == itemStack);
    }

    @Override
    public boolean canDragTo(Slot slot) {
        return false;
    }


    @Override
    public void broadcastChanges() {
        if (cap_list!=null && cap_list.isDirty()) {
//            CompoundNBT nbt = itemStack.getOrCreateTag();
//            int dirtyCounter = nbt.getInt("dirtyCounter");
//            nbt.putInt("dirtyCounter", dirtyCounter + 1);
//            itemStack.setTag(nbt);

            cap_list.setDirty(false);
        }
        super.broadcastChanges();
    }
}

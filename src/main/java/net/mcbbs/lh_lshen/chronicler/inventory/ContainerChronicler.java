package net.mcbbs.lh_lshen.chronicler.inventory;

import com.google.common.collect.Lists;
import net.mcbbs.lh_lshen.chronicler.events.CommonEventHandler;
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

public class ContainerChronicler extends Container {

    private CapabilityItemList cap_list;
    private ItemStack itemStack;
    private Map<String, List<ItemStack>> allItemMap;
    private PlayerInventory inventory_player;
    private List<Inventory> inventories = Lists.newArrayList();
    public SelectCompnent selectCompnent = new SelectCompnent();
    public boolean selectBoxOpen;
    protected ContainerChronicler(int windowId, PlayerInventory playerInv,
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
        loadSlots();
    }

    public void loadSlots(){
        this.slots.clear();
        loadInventories();
        addCapabilitySlots();
    }

    private void loadInventories(){
        List<String> keys = Lists.newArrayList();
        List<Inventory> inventories_new = Lists.newArrayList();
        keys.addAll(allItemMap.keySet());
        for (int i =0;i<keys.size();i++) {
            Inventory inventory = StoreHelper.getSingerInventory(allItemMap,keys.get(i));
            inventories_new.add(inventory);
        }
        this.inventories = inventories_new;
    }

    private void addCapabilitySlots(){
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int LIST_XPOS = 152;
        final int LIST_YPOS = 18;
        int page = selectCompnent.getPage();
        List<String> keys = Lists.newArrayList();
        keys.addAll(allItemMap.keySet());
        for (int j =0;j<8;j++) {
            if (j<keys.size()) {
                Inventory inventory = inventories.get( j );
                for (int i = 0; i<4; i++){
                    addSlot(new SlotChronicler(inventory,i,LIST_XPOS + SLOT_X_SPACING * i, LIST_YPOS + SLOT_Y_SPACING * j));
                }
            }
        }
    }


    @Override
    public ItemStack clicked(int slot, int player_slot, ClickType clickType, PlayerEntity playerEntity) {
        if (clickType == ClickType.PICKUP) {
            ItemStack itemStackSelect = this.slots.get(slot).getItem();
            if (!itemStackSelect.isEmpty() && playerEntity !=null){
               selectCompnent.selectSlot(slot);
               this.selectBoxOpen = true;
//                ((ItemChronicler) itemStack.getItem()).setSelectCompnent(itemStack,selectCompnent);
            }
        }else {
            this.selectBoxOpen =false;
        }
        return super.clicked(slot,player_slot,clickType,playerEntity);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack p_94530_1_, Slot p_94530_2_) {
        return false;
    }

    public static ContainerChronicler createContainerServerSide(int windowID, PlayerInventory playerInventory, CapabilityItemList cap_list,
                                                                ItemStack itemStack) {
        return new ContainerChronicler(windowID, playerInventory, cap_list, itemStack);
    }

    public static ContainerChronicler createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
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
                return new ContainerChronicler(windowID, playerInventory, capabilityItemList, stack);
            }
            } catch (IllegalArgumentException iae) {
                Logger.getGlobal().info(iae.toString());
            }
            return new ContainerChronicler(windowID, playerInventory, capabilityItemList, ItemStack.EMPTY);
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

    public CapabilityItemList getCapabilityItemList() {
        if (cap_list!=null) {
            return cap_list;
            }
        return new CapabilityItemList();
    }

    public void setCapabilityItemList(CapabilityItemList cap_list){
        this.cap_list = cap_list;
    }

    public ItemStack getItemStackChronicler() {
        return itemStack;
    }


    @Override
    public void broadcastChanges() {
        allItemMap = cap_list.getAllMap();
        loadSlots();
        super.broadcastChanges();
    }
}

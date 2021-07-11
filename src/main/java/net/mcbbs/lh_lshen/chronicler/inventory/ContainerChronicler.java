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
    public SelectCompnent selectCompnent;
    public boolean selectBoxOpen;

    public final int CAP_SIZE = 31;

    protected ContainerChronicler(int windowId, PlayerInventory playerInv,
                                  CapabilityItemList cap_list,
                                  ItemStack itemStack) {
        super(CommonEventHandler.containerTypeChronicler, windowId);
        this.cap_list = cap_list;
        this.itemStack = itemStack;
        this.allItemMap = cap_list.getAllMap();
        this.inventory_player = playerInv;
        initSelectCompnent();
        loadSlots();
    }

    public void loadSlots(){
        this.slots.clear();
        loadInventories();
        addCapabilitySlots();
        addSelectedSlot();
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

    private void initSelectCompnent(){
        List<Integer> stack_list = Lists.newArrayList();
        for (int i=0;i<8;i++){
            stack_list.add(0);
        }
        this.selectCompnent = new SelectCompnent(this,0, stack_list,cap_list);

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

            if (j<keys.size() && inventories.size() > page*8+j) {
                Inventory inventory = inventories.get( page*8+j );
                int stack_page = selectCompnent.stackList.get(j);

                for (int i = 0; i<4; i++){
                    if (inventory.getContainerSize() >stack_page*4 + i) {
                        addSlot(new SlotChronicler(inventory,stack_page*4 + i,LIST_XPOS + SLOT_X_SPACING * i, LIST_YPOS + SLOT_Y_SPACING * j,
                               i,j));
                    }
                }

            }
        }

    }

    public void addSelectedSlot(){
        ItemStack selectItem = selectCompnent.selectItemStack;
        Inventory inventory = new Inventory(1);
        if (selectItem != null && !selectItem.isEmpty() && selectBoxOpen){
            inventory.addItem(selectItem);
        }
        addSlot(new SlotChronicler(inventory,0,119, 82,0,0));
    }


    @Override
    public ItemStack clicked(int slot, int player_slot, ClickType clickType, PlayerEntity playerEntity) {
        if (clickType == ClickType.PICKUP && playerEntity.level.isClientSide) {
            ItemStack itemStackSelect = this.slots.get(slot).getItem();
            if (!itemStackSelect.isEmpty()){
               selectCompnent.selectSlot(slot);
               this.selectBoxOpen = true;
            }else {
                selectBoxOpen = false;
            }
            loadSlots();
        }
        return ItemStack.EMPTY;
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
            capabilityItemList.deserializeNBT(listNBT);
            if (capabilityItemList!=null) {
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
        if (cap_list.isDirty()) {
            allItemMap = cap_list.getAllMap();
            loadSlots();
            cap_list.setDirty(false);
        }
        super.broadcastChanges();
    }
}

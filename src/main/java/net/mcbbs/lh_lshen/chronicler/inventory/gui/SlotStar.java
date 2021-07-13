package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import com.mojang.realmsclient.client.Request;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.system.CallbackI;

public class SlotStar extends Slot {
    ICapabilityItemList capabilityItemList;
    public SlotStar(IInventory inventory, int slotNumber, int x_pos, int y_pos, ICapabilityItemList capabilityItemList) {
        super(inventory,slotNumber,x_pos,y_pos);
        this.capabilityItemList = capabilityItemList;

    }

    public int getLinkedPage(){
        if (StoreHelper.hasItemStack(capabilityItemList,this.getItem())){
            return StoreHelper.getPage(capabilityItemList,this.getItem());
        }
        return -1;
    }

    public int getLinkedStackPage(){
        if (StoreHelper.hasItemStack(capabilityItemList,this.getItem())){
            return StoreHelper.getStackIndex(capabilityItemList,this.getItem())/4;
        }
        return -1;
    }

    public int getLinkedRow(){
        if (StoreHelper.hasItemStack(capabilityItemList,this.getItem())){
            int row = StoreHelper.getStackListIndex(capabilityItemList,this.getItem())%8;
            return row;
        }
        return -1;
    }

    public int getLinkedStackIndex(){
        if (StoreHelper.hasItemStack(capabilityItemList,this.getItem())){
            return StoreHelper.getStackIndex(capabilityItemList,this.getItem())%4;
        }
        return -1;
    }


    public int getLinkedStackSlot(){
        if (StoreHelper.hasItemStack(capabilityItemList,this.getItem())){
            return StoreHelper.getStackIndex(capabilityItemList,this.getItem())%4 + getLinkedRow()*4;
        }
        return -1;
    }

    public ItemStack getLinkedItemStack(){
        if (!this.getItem().isEmpty()) {
            return StoreHelper.getItemStack(capabilityItemList,this.getItem().getItem().getRegistryName().toString(),
                    StoreHelper.getStackIndex(capabilityItemList,this.getItem()));
        }
        return ItemStack.EMPTY;

    }


    @Override
    public boolean mayPickup(PlayerEntity p_82869_1_) {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack p_75214_1_) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotChronicler extends Slot {
    private ItemStack SlotItemStack;
    public SlotChronicler(IInventory inventory, int slotNumber, int x_pos, int y_pos) {
        super(inventory,slotNumber,x_pos,y_pos);
        this.SlotItemStack = inventory.getItem(slotNumber);
        resetSlot();
    }

    @Override
    public boolean mayPickup(PlayerEntity p_82869_1_) {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack p_75214_1_) {
        return false;
    }

    public void resetSlot(){
        if (SlotItemStack != null) {
            set(SlotItemStack);
        }
    }

}

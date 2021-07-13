package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotChronicler extends Slot {
    private int i_index;
    private int j_index;
    public SlotChronicler(IInventory inventory, int slotNumber, int x_pos, int y_pos, int i_index, int j_index) {
        super(inventory,slotNumber,x_pos,y_pos);
        this.i_index = i_index;
        this.j_index = j_index;
    }

    public int getI_index() {
        return i_index;
    }

    public int getJ_index() {
        return j_index;
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

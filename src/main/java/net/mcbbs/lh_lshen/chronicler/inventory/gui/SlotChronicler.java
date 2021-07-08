package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotChronicler extends Slot {
    public SlotChronicler(IInventory inventory, int slotNumber, int x_pos, int y_pos) {
        super(inventory,slotNumber,x_pos,y_pos);
        set(inventory.getItem(slotNumber));
    }

    @Override
    public boolean mayPickup(PlayerEntity p_82869_1_) {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack p_75214_1_) {
        return false;
    }


//    @Override
//    public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
//        player.drop(itemStack,true);
//        return super.onTake(player, itemStack);
//    }
}

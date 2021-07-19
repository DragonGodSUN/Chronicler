package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import net.mcbbs.lh_lshen.chronicler.ItemRegistry;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemInscription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotInscription extends Slot {
    ICapabilityInscription inscription;
    public SlotInscription(IInventory inventory, int slotNumber, int x_pos,int y_pos, ICapabilityInscription inscription) {
        super(inventory,slotNumber,x_pos,y_pos);
        this.inscription = inscription;

    }

    public String getId(){
        return inscription.getInscription();
    }

    public ItemStack getItemInscription(){
        ItemStack itemStack = this.getItem();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemInscription){
            ItemInscription.setInscription(itemStack,inscription.getInscription());
            ItemInscription.setLevel(itemStack,inscription.getLevel());
            return itemStack;
        }
        return new ItemStack(ItemRegistry.inscription.get());
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

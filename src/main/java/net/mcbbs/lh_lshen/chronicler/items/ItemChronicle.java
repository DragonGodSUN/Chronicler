package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.ItemRegistry;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sun.rmi.runtime.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sound.sampled.Line;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class ItemChronicle extends Item {
    public ItemChronicle() {
        super(new Properties().tab(ItemGroup.TAB_TOOLS).fireResistant().stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        ItemStack itemStack = entity.getItemInHand(hand);
        ItemStack itemStack_l = entity.getOffhandItem();
        LazyOptional<ICapabilityItemList> capability = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY);
            capability.ifPresent((cap_list)->{
                    if (entity.isShiftKeyDown()) {
                        StoreHelper.addItemStack(cap_list,itemStack_l);
                        if (world.isClientSide) {
                            System.out.println("Store:"+itemStack_l.getDisplayName());
                        }
                    }else {
                        for (Map.Entry<String,List<ItemStack>> entry : cap_list.getAllMap().entrySet()){
                            System.out.println(entry.getKey());
                            List<ItemStack> list = entry.getValue();
                            for (ItemStack stack:list){
                                entity.drop(stack.copy(),true);
                                if (world.isClientSide) {
                                    System.out.println(stack.getItem().getRegistryName().toString());
                                }
                            }
                        }
                    }

            });

        return ActionResult.sidedSuccess(itemStack, world.isClientSide());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemListProvider();
    }
}

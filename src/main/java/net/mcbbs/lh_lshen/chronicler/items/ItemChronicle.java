package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ItemChronicle extends Item {
    public ItemChronicle() {
        super(new Properties().tab(ItemGroup.TAB_TOOLS));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        ItemStack itemStack = entity.getItemInHand(hand);
        ItemStack itemStack_l = entity.getOffhandItem();
        LazyOptional<ICapabilityItemList> capability = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY);
            capability.ifPresent((cap_list)->{
                if (!itemStack_l.isEmpty()) {
                    cap_list.addItemStack(itemStack_l,"test");
                    ItemStack itemStack1 = cap_list.getItemStack(itemStack_l.getItem().getRegistryName().toString(),"test");
                    entity.drop(itemStack1.copy(),true);
                }

            });

        return ActionResult.sidedSuccess(itemStack, world.isClientSide());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider()
        {
            private LazyOptional<ICapabilityItemList> lazyOptional; // TODO

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
            {
                if (cap == ModCapability.ITEMLIST_CAPABILITY) {
                    return LazyOptional.of(CapabilityItemList::new).cast();
                }
                return LazyOptional.empty();
            }
        };
    }
}

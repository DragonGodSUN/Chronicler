package net.mcbbs.lh_lshen.chronicler.items;

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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

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

        return ActionResult.sidedSuccess(itemStack, world.isClientSide());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider()
        {
            private LazyOptional<IEnergyStorage> lazyOptional; // TODO

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
            {
                boolean isItem = Objects.equals(cap, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                return isItem ? this.lazyOptional.cast() : LazyOptional.empty();
            }
        };
    }
}

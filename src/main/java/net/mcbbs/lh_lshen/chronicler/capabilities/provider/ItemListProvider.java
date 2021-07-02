package net.mcbbs.lh_lshen.chronicler.capabilities.provider;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class ItemListProvider implements ICapabilityProvider, INBTSerializable<ListNBT> {
    private ICapabilityItemList capabilityItemList;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapability.ITEMLIST_CAPABILITY ? LazyOptional.of(this::getOrCreateCapability).cast() : LazyOptional.empty();
    }

    @Nonnull
    ICapabilityItemList getOrCreateCapability() {
        if (capabilityItemList == null) {
            this.capabilityItemList = new CapabilityItemList();
        }
        return this.capabilityItemList;
    }

    @Override
    public ListNBT serializeNBT() {
        return getOrCreateCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(ListNBT nbt) {
        getOrCreateCapability().deserializeNBT(nbt);
    }
}

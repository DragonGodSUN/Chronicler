package net.mcbbs.lh_lshen.chronicler.capabilities.provider;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ItemListProvider implements ICapabilityProvider, INBTSerializable<ListNBT> {
    private final ICapabilityItemList instance = ModCapability.ITEMLIST_CAPABILITY.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapability.ITEMLIST_CAPABILITY && instance != null) {
            return LazyOptional.of(() -> instance).cast();
        }
        return LazyOptional.empty();
    }

//    @Nonnull
//    ICapabilityItemList getOrCreateCapability() {
//        if (capabilityItemList == null) {
//            this.capabilityItemList = new CapabilityItemList();
//        }
//        return this.capabilityItemList;
//    }

    @Override
    public ListNBT serializeNBT() {
        return (ListNBT) ModCapability.ITEMLIST_CAPABILITY.writeNBT(instance,null);
    }

    @Override
    public void deserializeNBT(ListNBT nbt) {
        ModCapability.ITEMLIST_CAPABILITY.readNBT(instance,null,nbt);
    }

    public static class Storage implements Capability.IStorage<ICapabilityItemList> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<ICapabilityItemList> capability, net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ICapabilityItemList> capability, net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList instance, Direction side, INBT nbt) {
            if(instance instanceof CapabilityItemList && nbt instanceof ListNBT){
                instance.deserializeNBT((ListNBT) nbt);
            }
        }
    }

}

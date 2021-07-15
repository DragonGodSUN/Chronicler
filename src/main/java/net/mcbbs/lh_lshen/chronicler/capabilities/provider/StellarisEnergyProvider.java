package net.mcbbs.lh_lshen.chronicler.capabilities.provider;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StellarisEnergyProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {

    private final ICapabilityStellarisEnergy instance = ModCapability.ENERGY_CAPABILITY.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapability.ENERGY_CAPABILITY && instance != null) {
            return LazyOptional.of(() -> instance).cast();
        }
        return LazyOptional.empty();
    }


    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) ModCapability.ENERGY_CAPABILITY.writeNBT(instance,null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ModCapability.ENERGY_CAPABILITY.readNBT(instance,null,nbt);
    }

    public static class Storage implements Capability.IStorage<ICapabilityStellarisEnergy> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<ICapabilityStellarisEnergy> capability, ICapabilityStellarisEnergy instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ICapabilityStellarisEnergy> capability, ICapabilityStellarisEnergy instance, Direction side, INBT nbt) {
            if(instance instanceof CapabilityStellarisEnergy && nbt instanceof CompoundNBT){
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }
    }

}

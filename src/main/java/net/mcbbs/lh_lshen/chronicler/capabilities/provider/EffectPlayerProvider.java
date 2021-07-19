package net.mcbbs.lh_lshen.chronicler.capabilities.provider;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EffectPlayerProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {

    private final ICapabilityEffectPlayer instance = ModCapability.EFFECT_PLAYER.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapability.EFFECT_PLAYER && instance != null) {
            return LazyOptional.of(() -> instance).cast();
        }
        return LazyOptional.empty();
    }


    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) ModCapability.EFFECT_PLAYER.writeNBT(instance,null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ModCapability.EFFECT_PLAYER.readNBT(instance,null,nbt);
    }

    public static class Storage implements Capability.IStorage<ICapabilityEffectPlayer> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<ICapabilityEffectPlayer> capability, ICapabilityEffectPlayer instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ICapabilityEffectPlayer> capability, ICapabilityEffectPlayer instance, Direction side, INBT nbt) {
            if(instance instanceof CapabilityInscription && nbt instanceof CompoundNBT){
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }
    }

}

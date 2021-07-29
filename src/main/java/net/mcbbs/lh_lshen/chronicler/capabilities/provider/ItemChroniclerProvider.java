package net.mcbbs.lh_lshen.chronicler.capabilities.provider;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.helper.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class ItemChroniclerProvider implements ICapabilityProvider, INBTSerializable<INBT> {

    private ICapabilityItemList cap_instance;
    private ICapabilityStellarisEnergy energy_instance;
    private ICapabilityInscription inscription_instance;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapability.ITEMLIST_CAPABILITY && cap_instance!=null) {
            return LazyOptional.of(this::getCapItemList).cast();
        }

        if (cap == ModCapability.ENERGY_CAPABILITY && energy_instance!=null) {
            return LazyOptional.of(this::getCapEnergy).cast();
        }

        if (cap == ModCapability.INSCRIPTION_CAPABILITY && inscription_instance!=null) {
            return LazyOptional.of(this::getCapInscription).cast();
        }

        return LazyOptional.empty();
    }

    @Nonnull
    ICapabilityItemList getCapItemList() {
        if (cap_instance == null) {
            this.cap_instance = new CapabilityItemList();
        }
        return this.cap_instance;
    }

    @Nonnull
    ICapabilityStellarisEnergy getCapEnergy() {
        if (energy_instance == null) {
            this.energy_instance = new CapabilityStellarisEnergy();
        }
        return this.energy_instance;
    }

    @Nonnull
    ICapabilityInscription getCapInscription() {
        if (inscription_instance == null) {
            this.inscription_instance = new CapabilityInscription();
        }
        return this.inscription_instance;
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        ICapabilityItemList cap_list = getCapItemList();
        ICapabilityStellarisEnergy energy = getCapEnergy();
        ICapabilityInscription inscription = getCapInscription();

        ListNBT listNBT = cap_list.serializeNBT();
        CompoundNBT nbt_list = NBTHelper.serializeCapList(listNBT,ModCapability.NBT_TAGS.TAG_CAP_LIST,new CompoundNBT());
        CompoundNBT nbt_energy = energy.serializeNBT();
        CompoundNBT nbt_inscription = inscription.serializeNBT();

        tag.put(ModCapability.NBT_TAGS.TAG_CAP_LIST,nbt_list);
        tag.put(ModCapability.NBT_TAGS.TAG_CAP_ENERGY,nbt_energy);
        tag.put(ModCapability.NBT_TAGS.TAG_CAP_INSCRIPTION,nbt_inscription);

        return tag;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
       if (nbt instanceof CompoundNBT) {
            CompoundNBT nbt_list = ((CompoundNBT) nbt).getCompound(ModCapability.NBT_TAGS.TAG_CAP_LIST);
            ListNBT list = NBTHelper.deserializeCapList(ModCapability.NBT_TAGS.TAG_CAP_LIST, ((CompoundNBT) nbt_list));
            getCapItemList().deserializeNBT(list);

           CompoundNBT nbt_energy = ((CompoundNBT) nbt).getCompound(ModCapability.NBT_TAGS.TAG_CAP_ENERGY);
           getCapEnergy().deserializeNBT(nbt_energy);

           CompoundNBT nbt_inscription = ((CompoundNBT) nbt).getCompound(ModCapability.NBT_TAGS.TAG_CAP_INSCRIPTION);
           getCapInscription().deserializeNBT(nbt_inscription);
       }

    }

    public static class ItemListStorage implements Capability.IStorage<ICapabilityItemList> {
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

    public static class EnergyStorage implements Capability.IStorage<ICapabilityStellarisEnergy> {
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

    public static class InscriptionStorage implements Capability.IStorage<ICapabilityInscription> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<ICapabilityInscription> capability, ICapabilityInscription instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ICapabilityInscription> capability, ICapabilityInscription instance, Direction side, INBT nbt) {
            if(instance instanceof CapabilityInscription && nbt instanceof CompoundNBT){
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }
    }

}

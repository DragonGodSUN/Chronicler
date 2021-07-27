package net.mcbbs.lh_lshen.chronicler.capabilities.provider;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
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
        ListNBT listNBT = new ListNBT();
        CompoundNBT size = new CompoundNBT();
        size.putInt("cap_list_size",((ListNBT)getCapItemList().serializeNBT()).size());
        listNBT.add(size);
//        listNBT.add(ModCapability.ITEMLIST_CAPABILITY.writeNBT(cap_instance,null));
//        listNBT.add(ModCapability.ENERGY_CAPABILITY.writeNBT(energy_instance,null));
//        listNBT.add(ModCapability.INSCRIPTION_CAPABILITY.writeNBT(inscription_instance,null));
        listNBT.addAll(getCapItemList().serializeNBT());
        listNBT.add(getCapEnergy().serializeNBT());
        listNBT.add(getCapInscription().serializeNBT());
        return listNBT;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        if (nbt instanceof ListNBT){
            ListNBT listNBT = (ListNBT) nbt;
            ListNBT cap_list = new ListNBT();
            int size = ((CompoundNBT)(listNBT.get(0))).getInt("cap_list_size");
//            ModCapability.ITEMLIST_CAPABILITY.readNBT(cap_instance,null,listNBT.get(0));
//            ModCapability.ENERGY_CAPABILITY.readNBT(energy_instance,null,listNBT.get(1));
//            ModCapability.INSCRIPTION_CAPABILITY.readNBT(inscription_instance,null,listNBT.get(2));
            for (int i=0;i<size;i++){
                cap_list.add(listNBT.get(i+1));
            }
            getCapItemList().deserializeNBT(cap_list);
            getCapEnergy().deserializeNBT((CompoundNBT) listNBT.get(size+1));
            getCapInscription().deserializeNBT((CompoundNBT) listNBT.get(size+2));
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

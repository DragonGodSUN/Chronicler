package net.mcbbs.lh_lshen.chronicler.helper;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

/**
 * 进行NBT操作的工具类
 */
public class NBTHelper {
    public static CompoundNBT getSafeNBTCompond(ItemStack itemStack) {
        return itemStack.getOrCreateTag();
    }

    public static CompoundNBT getCompond(ItemStack itemStack, String key){
        return getSafeNBTCompond(itemStack).getCompound(key);
    }

    public static CompoundNBT serializeCapList(ListNBT listNBT, String key, CompoundNBT compoundNBT){
        int size = listNBT.size();
        compoundNBT.putInt(key+":"+"size",size);
        for (int i=0;i<size;i++){
            CompoundNBT e = listNBT.getCompound(i);
            compoundNBT.put(key+":"+i,e);
        }
        return compoundNBT;
    }

    public static ListNBT deserializeCapList(String key, CompoundNBT nbt){
        ListNBT nbt_list = new ListNBT();
        if (nbt != null) {
            int size = nbt.getInt(key+":"+"size");
            for (int i = 0;i<size;i++){
                CompoundNBT eNBT = nbt.getCompound(key+":"+i);
                nbt_list.add(eNBT);
            }
        }
        return nbt_list;
    }


    public static void putCapsTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getOrCreateTag();

        CapabilityItemList cap_list = DataHelper.getItemListCapability(stack);
        CapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(stack);
        CapabilityInscription inscription = DataHelper.getInscriptionCapability(stack);

        ListNBT listNBT = cap_list.serializeNBT();
        CompoundNBT nbt_list = serializeCapList(listNBT,ModCapability.NBT_TAGS.TAG_CAP_LIST,new CompoundNBT());
        CompoundNBT nbt_energy = energy.serializeNBT();
        CompoundNBT nbt_inscription = inscription.serializeNBT();

        baseTag.put(ModCapability.NBT_TAGS.TAG_CAP_LIST,nbt_list);
        baseTag.put(ModCapability.NBT_TAGS.TAG_CAP_ENERGY,nbt_energy);
        baseTag.put(ModCapability.NBT_TAGS.TAG_CAP_INSCRIPTION,nbt_inscription);

        stack.setTag(baseTag);
    }

    public static void readCapsTag(ItemStack stack, CompoundNBT nbt) {
        stack.setTag(nbt);
        CapabilityItemList cap_list = DataHelper.getItemListCapability(stack);
        CapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(stack);
        CapabilityInscription inscription = DataHelper.getInscriptionCapability(stack);

        CompoundNBT nbt_list = nbt.getCompound(ModCapability.NBT_TAGS.TAG_CAP_LIST);
        ListNBT list = deserializeCapList(ModCapability.NBT_TAGS.TAG_CAP_LIST,nbt_list);
        cap_list.deserializeNBT(list);

        CompoundNBT nbt_energy = nbt.getCompound(ModCapability.NBT_TAGS.TAG_CAP_ENERGY);
        energy.deserializeNBT(nbt_energy);

        CompoundNBT nbt_inscription = nbt.getCompound(ModCapability.NBT_TAGS.TAG_CAP_INSCRIPTION);
        inscription.deserializeNBT(nbt_inscription);
    }
}

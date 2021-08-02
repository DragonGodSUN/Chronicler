package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.helper.NBTHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ManageItemListCapMessage {
    private ItemStack itemStackChronicler;
    private ICapabilityItemList capabilityItemList;

    public ManageItemListCapMessage(ItemStack itemStackChronicler, ICapabilityItemList capabilityItemList) {
        this.itemStackChronicler = itemStackChronicler;
        this.capabilityItemList = capabilityItemList;
    }

    public static void encode(ManageItemListCapMessage message, PacketBuffer buf) {
        if (message.itemStackChronicler != null && message.itemStackChronicler.getItem() instanceof ItemChronicler) {
            ListNBT listNBT = message.capabilityItemList.serializeNBT();
            buf.writeItemStack(message.itemStackChronicler,true);
            buf.writeInt(listNBT.size());
            for (int i=0;i<listNBT.size();i++){
                CompoundNBT nbt = listNBT.getCompound(i);
                if (nbt !=null) {
                    buf.writeNbt(nbt);
                }
            }
        }
    }

    public static ManageItemListCapMessage decode(PacketBuffer buf) {
        ItemStack stack = buf.readItem();
        int size = buf.readInt();
        if (stack.getItem() instanceof ItemChronicler){
            ListNBT listNBT = new ListNBT();
            for (int i=0;i<size;i++){
                CompoundNBT nbt = buf.readNbt();
                listNBT.add(nbt);
            }
            CapabilityItemList capabilityItemList = new CapabilityItemList();
                capabilityItemList.deserializeNBT(listNBT);
                if (capabilityItemList!=null) {
                    return new ManageItemListCapMessage(stack,capabilityItemList);
                }
        }
        return null;
    }

    public static void handler(ManageItemListCapMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            synData(message,sender);
            });
        }
        ctx.get().setPacketHandled(true);

    }

    public static void synData(ManageItemListCapMessage message, PlayerEntity player){
        if (player == null) {
            return;
        }
        CapabilityItemList cap = (CapabilityItemList) message.capabilityItemList;
        ItemStack itemStack = message.itemStackChronicler;

        if (itemStack != null && !itemStack.isEmpty()) {
            ItemStack itemHold = player.getMainHandItem();
            if (itemHold.getItem() instanceof ItemChronicler) {
                LazyOptional<ICapabilityItemList> cap_item = itemHold.getCapability(ModCapability.ITEMLIST_CAPABILITY);
                cap_item.ifPresent((c) -> {
                    if (cap != null) {
                        c.deserializeNBT(cap.serializeNBT());
                        c.setDirty(true);
                    }
                });
                NBTHelper.putCapsTag(itemHold);
            }
        }
    }

}

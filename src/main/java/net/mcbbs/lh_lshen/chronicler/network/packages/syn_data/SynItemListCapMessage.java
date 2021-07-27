package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynItemListCapMessage {
    private ItemStack itemStackChronicler;
    private ICapabilityItemList capabilityItemList;

    public SynItemListCapMessage(ItemStack itemStackChronicler, ICapabilityItemList capabilityItemList) {
        this.itemStackChronicler = itemStackChronicler;
        this.capabilityItemList = capabilityItemList;
    }

    public static void encode(SynItemListCapMessage message, PacketBuffer buf) {
        if (message.itemStackChronicler != null && message.itemStackChronicler.getItem() instanceof ItemChronicler) {
            ListNBT listNBT = (ListNBT) message.capabilityItemList.serializeNBT();
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

    public static SynItemListCapMessage decode(PacketBuffer buf) {
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
                    return new SynItemListCapMessage(stack,capabilityItemList);
                }
        }
        return null;
    }

    public static void handler(SynItemListCapMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                synData(message);
            });
        }
        ctx.get().setPacketHandled(true);

    }

    @OnlyIn(Dist.CLIENT)
    public static void synData(SynItemListCapMessage message){
        PlayerEntity player = DataHelper.getClientPlayer();
        if (player == null) {
            return;
        }
        CapabilityItemList cap = (CapabilityItemList) message.capabilityItemList;
        ItemStack itemStack = message.itemStackChronicler;

        if (itemStack != null && !itemStack.isEmpty()) {
            ItemStack itemHold = player.getMainHandItem();
            if (itemHold.getItem() instanceof ItemChronicler
                && itemHold.equals(message.itemStackChronicler,true)) {
                LazyOptional<ICapabilityItemList> cap_item = itemHold.getCapability(ModCapability.ITEMLIST_CAPABILITY);
                cap_item.ifPresent((c) -> {
                    if (cap != null) {
                       c.deserializeNBT(cap.serializeNBT());
                    }
                });
            }
        }
    }
}

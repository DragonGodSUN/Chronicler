package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynEnergyCapMessage {
    private ICapabilityStellarisEnergy energy;
    private ItemStack stack;

    public SynEnergyCapMessage(ItemStack stack, ICapabilityStellarisEnergy energy) {
        this.stack = stack;
        this.energy = energy;
    }

    public static void encode(SynEnergyCapMessage message, PacketBuffer buf) {
        if (message.stack !=null && message.energy != null) {
            CompoundNBT compoundNBT = message.energy.serializeNBT();
            buf.writeItemStack(message.stack,true);
            buf.writeNbt(compoundNBT);
        }
    }

    public static SynEnergyCapMessage decode(PacketBuffer buf) {
        ItemStack stack = buf.readItem();
        CompoundNBT nbt = buf.readNbt();
        CapabilityStellarisEnergy energy = new CapabilityStellarisEnergy();
        energy.deserializeNBT(nbt);
        if (energy!=null) {
            return new SynEnergyCapMessage(stack,energy);
        }
        return null;
    }

    public static void handler(SynEnergyCapMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                synEnergy(message);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void synEnergy(SynEnergyCapMessage message){
        PlayerEntity player = DataHelper.getClientPlayer();
        if (player == null) {
            return;
        }
        CapabilityStellarisEnergy cap = (CapabilityStellarisEnergy) message.energy;
        if (player.inventory.contains(message.stack)){
            CompoundNBT shareTag = message.stack.getShareTag();
            if (shareTag!=null) {
//                String id = shareTag.getString("ID");
//                ItemStack stack = DataHelper.getItemStackByID(id,player.inventory);
                ItemStack stack = ItemStack.EMPTY;
            for (int i=0;i<player.inventory.getContainerSize();i++){
                if (player.inventory.getItem(i).getItem() instanceof ItemChronicler){
                    if (ItemChronicler.getId(player.inventory.getItem(i)).equals(
                        ItemChronicler.getId(message.stack)
                    )){
                        stack = player.inventory.getItem(i);
                        break;
                    }
                }
            }
            if (!stack.isEmpty()) {
//                ICapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(stack);
//                if (cap!=null && energy!=null){
//                    energy.deserializeNBT(cap.serializeNBT());
//                }
                stack.readShareTag(shareTag);
            }

            }
        }

    }
}

package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.NBTHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ManageEnergyCapMessage {
    private ICapabilityStellarisEnergy energy;
    private ItemStack stack;

    public ManageEnergyCapMessage(ItemStack stack, ICapabilityStellarisEnergy energy) {
        this.stack = stack;
        this.energy = energy;
    }

    public static void encode(ManageEnergyCapMessage message, PacketBuffer buf) {
        if (message.stack !=null && message.energy != null) {
            CompoundNBT compoundNBT = (CompoundNBT) message.energy.serializeNBT();
            buf.writeItemStack(message.stack,false);
            buf.writeNbt(compoundNBT);
        }
    }

    public static ManageEnergyCapMessage decode(PacketBuffer buf) {
        ItemStack stack = buf.readItem();
        CompoundNBT nbt = buf.readNbt();
        CapabilityStellarisEnergy energy = new CapabilityStellarisEnergy();
        energy.deserializeNBT(nbt);
        if (energy!=null) {
            return new ManageEnergyCapMessage(stack,energy);
        }
        return null;
    }

    public static void handler(ManageEnergyCapMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity sender = ctx.get().getSender();
                if (sender == null) {
                    return;
                }
                CapabilityStellarisEnergy cap = (CapabilityStellarisEnergy) message.energy;
                ItemStack hold = sender.getMainHandItem();
//                if (sender.inventory.contains(message.stack)) {
//                    for (int i = 0; i < sender.inventory.getContainerSize(); i++) {
//                        if (sender.inventory.getItem(i).getItem() instanceof ItemChronicler) {
//                            if (ItemChronicler.getId(sender.inventory.getItem(i)).equals(ItemChronicler.getId(message.stack))) {
//                                stack = sender.inventory.getItem(i);
//                                break;
//                            }
//                        }
//                    }
//                }
                if (!hold.isEmpty()&& hold.getItem() instanceof ItemChronicler) {
                    ICapabilityStellarisEnergy energy = hold.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
                    if (cap != null && energy != null) {
                        energy.deserializeNBT(cap.serializeNBT());
                        cap.setDirty(true);
                    }
//                  按理说cap在之后会将数据再同步到客户端，但不知道为何未能成功，只能手动同步
                    NBTHelper.putCapsTag(hold);
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}

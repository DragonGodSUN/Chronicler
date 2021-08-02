package net.mcbbs.lh_lshen.chronicler.network.packages;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.helper.NBTHelper;
import net.mcbbs.lh_lshen.chronicler.inscription.IInscription;
import net.mcbbs.lh_lshen.chronicler.inscription.InscriptionRegister;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ProduceMessage extends BasicMessage {
    private ItemStack itemStack;
    public ProduceMessage(ItemStack itemStack) {
        this.itemStack = itemStack.copy();
    }

    public static void encode(ProduceMessage message, PacketBuffer buf) {
        buf.writeItemStack(message.itemStack,false);
    }

    public static ProduceMessage decode(PacketBuffer buf) {
        return new ProduceMessage(buf.readItem());
    }

    public static void handler(ProduceMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity sender = ctx.get().getSender();
                if (sender == null) {
                    return;
                }
                ItemStack itemStack = message.itemStack;
                ItemStack chroniciler = sender.getMainHandItem();
                ICapabilityStellarisEnergy energy = chroniciler.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
                if (itemStack != null && chroniciler.getItem() instanceof ItemChronicler) {
                    if (energy.canCost(1000)) {
                        ICapabilityInscription inscription = DataHelper.getInscriptionCapability(chroniciler);
                        if (inscription!=null){
                            IInscription iInscription = InscriptionRegister.getInscription(inscription.getInscription());
                            if (iInscription!=null){
                                iInscription.process(itemStack);
                            }
                        }
                        if (sender.inventory.getFreeSlot()!=-1) {
                            sender.inventory.add(itemStack.copy());
                        }else {
                            sender.drop(itemStack.copy(),true);
                        }
                        energy.cost(1000);
//                      按理说cap在之后会将数据再同步到客户端，但不知道为何未能成功，只能手动同步
                        NBTHelper.putCapsTag(chroniciler);
                    }
                }
            });
        }
    }
}

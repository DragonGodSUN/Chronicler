package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynChroniclerMessage {
    private ItemStack stack;

    public SynChroniclerMessage(ItemStack stack) {
        this.stack = stack;
    }

    public static void encode(SynChroniclerMessage message, PacketBuffer buf) {
        if (message.stack !=null) {
            buf.writeItemStack(message.stack,false);
        }
    }

    public static SynChroniclerMessage decode(PacketBuffer buf) {
        ItemStack stack = buf.readItem();
        if (!stack.isEmpty()) {
            return new SynChroniclerMessage(stack);
        }
        return null;
    }

    public static void handler(SynChroniclerMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                synTagMessage(message);
            });
        }
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void synTagMessage(SynChroniclerMessage message){
        PlayerEntity player = DataHelper.getClientPlayer();
        if (player == null) {
            return;
        }
            CompoundNBT capsTag = message.stack.getTag();
            if (capsTag!=null) {
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
                stack.setTag(capsTag);
                stack.readShareTag(capsTag);
            }
        }
    }
}

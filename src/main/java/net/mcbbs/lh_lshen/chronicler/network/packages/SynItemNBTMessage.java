package net.mcbbs.lh_lshen.chronicler.network.packages;

import net.mcbbs.lh_lshen.chronicler.items.ItemRecordPage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynItemNBTMessage extends BasicMessage {
    private ItemStack itemStack;
    public SynItemNBTMessage(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static void encode(SynItemNBTMessage message, PacketBuffer buf) {
        buf.writeItemStack(message.itemStack,false);
    }

    public static SynItemNBTMessage decode(PacketBuffer buf) {
        return new SynItemNBTMessage(buf.readItem());
    }

    public static void handler(SynItemNBTMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity sender = ctx.get().getSender();
                if (sender == null) {
                    return;
                }
                ItemStack itemStack = message.itemStack;
                ItemStack hold = sender.getMainHandItem();
                if (itemStack != null && hold.getItem().equals(itemStack.getItem())) {
                    hold.setTag(itemStack.getOrCreateTag());
                }
            });
        }
    }
}

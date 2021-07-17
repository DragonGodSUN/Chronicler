package net.mcbbs.lh_lshen.chronicler.network.packages.syn_data;

import net.mcbbs.lh_lshen.chronicler.network.packages.BasicMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ManageItemNBTMessage extends BasicMessage {
    private ItemStack itemStack;
    public ManageItemNBTMessage(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static void encode(ManageItemNBTMessage message, PacketBuffer buf) {
        buf.writeItemStack(message.itemStack,false);
    }

    public static ManageItemNBTMessage decode(PacketBuffer buf) {
        return new ManageItemNBTMessage(buf.readItem());
    }

    public static void handler(ManageItemNBTMessage message, Supplier<NetworkEvent.Context> ctx) {
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

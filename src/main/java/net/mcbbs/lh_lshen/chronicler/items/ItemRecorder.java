package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.ItemRegistry;
import net.mcbbs.lh_lshen.chronicler.helper.NBTHelper;
import net.mcbbs.lh_lshen.chronicler.tabs.ModGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ItemRecorder extends Item {
    public ItemRecorder() {
        super(new Properties().tab(ModGroup.GROUP_CHRONICLER));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack off = player.getOffhandItem();
        ItemStack base = player.getMainHandItem();
        if (player.isShiftKeyDown()){
            if (!off.isEmpty() && base.getItem() instanceof ItemRecorder){
                if (off.getItem() instanceof ItemRecordPage || off.getItem() instanceof ItemChronicler
                        || off.getItem() instanceof ItemInscription || off.getItem() instanceof ItemRecorder){
                    if (world.isClientSide) {
                        player.sendMessage(new TranslationTextComponent("message.chronicler_lh.recorder.fail.item"), UUID.randomUUID());
                    }
                    return ActionResult.fail(base);
                } else {
                    ItemStack storePage = ItemRecordPage.getPageStored(off.copy());
                    if (!player.level.isClientSide) {
                        double chance = MathHelper.nextDouble(new Random(),0.0,100);
                        ServerPlayerEntity server_player = (ServerPlayerEntity) player;
                        if (chance<getChance(base)) {
                            if (server_player.inventory.canPlaceItem(1,storePage)) {
                                server_player.inventory.add(storePage);
                            }else {
                                server_player.drop(storePage,true);
                            }
                            server_player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS,1f,1f);
                        }else {
                            server_player.playNotifySound(SoundEvents.ITEM_BREAK, SoundCategory.PLAYERS,1f,1f);
                        }
                    }
                    off.shrink(1);
                    base.shrink(1);
                    return ActionResult.success(base);
                }
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, textComponents, flag);
        textComponents.add(new StringTextComponent(I18n.get("tooltip.chronicler_lh.recorder.type") +I18n.get("tooltip.chronicler_lh.recorder.type."+getType(itemStack))));
        textComponents.add(new StringTextComponent(I18n.get("tooltip.chronicler_lh.recorder.chance")+"§e"+getChance(itemStack)+"%"));
    }

    @Override
    public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> itemStacks) {
//        super.fillItemCategory(tab, itemStacks);
        if (this.category.equals(tab)){
            itemStacks.add(ItemRecorder.getSubType("common",10));
            itemStacks.add(ItemRecorder.getSubType("super",50));
            itemStacks.add(ItemRecorder.getSubType("lord",100));
        }

    }

    public static ItemStack getSubType(String type,int chance){
        ItemStack stack = new ItemStack(ItemRegistry.recorder.get());
        setType(stack,type);
        setChance(stack,chance);
        return stack;
    }


    public static String getType(ItemStack stack){
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.getString("type");
    }

    public static void setType(ItemStack stack, String type){
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putString("type",type);
    }

    public static int getChance(ItemStack stack){
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.getInt("chance");
    }

    public static void setChance(ItemStack stack, int chance){
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("chance",chance);
    }

    public static float getTypePropertyOverride(ItemStack itemStack, ClientWorld clientWorld, LivingEntity entity)
    {
        String type = getType(itemStack);
        switch (type){
            case "common":return 0f;
            case "super":return 1f;
            case "lord":return 2f;
        }
        return 0;
    }

}

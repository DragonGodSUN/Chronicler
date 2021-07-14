package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.helper.NBTHelper;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.SynItemNBTMessage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemRecordPage extends Item {
    public ItemRecordPage() {
        super(new Properties().tab(ItemGroup.TAB_TOOLS));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack off = player.getOffhandItem();
        ItemStack main = player.getMainHandItem();
        if (player.isShiftKeyDown()){
            if (!off.isEmpty() && main.getItem() instanceof ItemRecordPage){
                ItemStack storeItem = getStoreItem(main);
                if (off.getItem() instanceof ItemRecordPage || off.getItem() instanceof ItemChronicler){
                    if (world.isClientSide) {
                        player.sendMessage(new StringTextComponent("无法记录书页与记录者信息"),UUID.randomUUID());
                    }
                    return ActionResult.fail(main);
                }else if (!storeItem.isEmpty()){
                    if (world.isClientSide) {
                        player.sendMessage(new StringTextComponent("该书页已记录信息"),UUID.randomUUID());
                    }
                    return ActionResult.fail(main);
                }
                else {
                    storeItem(main,off);
                    if (world.isClientSide) {
                        player.playSound(SoundEvents.PLAYER_LEVELUP,1f,1f);
                    }
                    return ActionResult.success(main);
                }
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, textComponents, flag);
        ItemStack stack = getStoreItem(itemStack);
        boolean isStoreItem = !stack.isEmpty();
        textComponents.add(new StringTextComponent("状态"+":"+(isStoreItem?"§b已储存":"未储存")));
        if (isStoreItem){
            textComponents.add(new StringTextComponent("储存"+":"+stack.getDisplayName().getString()));
        }
    }

    public void storeItem(ItemStack page, ItemStack stack){
        CompoundNBT nbt = NBTHelper.getSafeNBTCompond(page);
        stack.save(nbt);
        ChroniclerNetwork.INSTANCE.sendToServer(new SynItemNBTMessage(page));
    }

    public ItemStack getStoreItem(ItemStack stack){
        CompoundNBT nbt = NBTHelper.getSafeNBTCompond(stack);
        if (!nbt.isEmpty()) {
            ItemStack storeItem = ItemStack.of(nbt);
            if (storeItem!=null&&!storeItem.isEmpty()){
                return storeItem;
            }
        }
        return ItemStack.EMPTY;
    }
}

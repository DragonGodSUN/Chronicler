package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.ItemRegistry;
import net.mcbbs.lh_lshen.chronicler.helper.NBTHelper;
import net.mcbbs.lh_lshen.chronicler.tabs.ModGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRecordPage extends Item {

    public ItemRecordPage() {
        super(new Properties().tab(ModGroup.GROUP_CHRONICLER));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, textComponents, flag);
        ItemStack stack = getStoreItem(itemStack);
        boolean isStoreItem = !stack.isEmpty();
        textComponents.add(new StringTextComponent(I18n.get("tooltip.chronicler_lh.record_page.condition")
                +(isStoreItem?I18n.get("tooltip.chronicler_lh.record_page.condition.stored"):I18n.get("tooltip.chronicler_lh.record_page.condition.empty"))));
        if (isStoreItem){
            textComponents.add(new StringTextComponent(I18n.get("tooltip.chronicler_lh.record_page.store")+stack.getDisplayName().getString()));
        }
    }

    public ItemStack getStoreItem(ItemStack stack){
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.isEmpty()) {
            ItemStack storeItem = ItemStack.of(nbt);
            if (storeItem!=null&&!storeItem.isEmpty()){
                return storeItem;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getPageStored(ItemStack stack){
        if (stack!=null && !stack.isEmpty()) {
            ItemStack page = new ItemStack(ItemRegistry.recordPage.get());
            CompoundNBT nbt = page.getOrCreateTag();
            stack.save(nbt);
            return page;
        }
        return ItemStack.EMPTY;
    }


}

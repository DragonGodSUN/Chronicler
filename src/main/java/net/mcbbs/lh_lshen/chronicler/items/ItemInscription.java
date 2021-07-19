package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.ItemRegistry;
import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.inscription.EnumInscription;
import net.mcbbs.lh_lshen.chronicler.tabs.ModGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInscription extends Item {
    public ItemInscription() {
        super(new Properties().tab(ModGroup.GROUP_CHRONICLER).stacksTo(1));
    }

    @Override
    public void fillItemCategory(ItemGroup itemGroup, NonNullList<ItemStack> subItems) {
        if (this.category.equals(itemGroup)) {
            for (EnumInscription e : EnumInscription.values()){
             subItems.add(getSubStack(e.getId()));
            }
        }
    }

    public static String getInscription(ItemStack itemStack) {
        CompoundNBT compoundNBT = itemStack.getOrCreateTag();
        return compoundNBT.getString("type");
    }

    public static int getLevel(ItemStack itemStack) {
        CompoundNBT compoundNBT = itemStack.getOrCreateTag();
        return compoundNBT.getInt("level");
    }

    public static void setInscription(ItemStack itemStack,String inscription) {
        CompoundNBT compoundNBT = itemStack.getOrCreateTag();
        compoundNBT.putString("type",inscription);
    }

    public static void setLevel(ItemStack itemStack,int level) {
        CompoundNBT compoundNBT = itemStack.getOrCreateTag();
        compoundNBT.putInt("level",level);
    }

    public static ItemStack getSubStack(String inscription){
        ItemStack stack = new ItemStack(ItemRegistry.inscription.get());
        setInscription(stack,inscription);
        setLevel(stack,1);
        return stack;
    }

    public static ItemStack getSubStack(String inscription, int level){
        ItemStack stack = new ItemStack(ItemRegistry.inscription.get());
        setInscription(stack,inscription);
        setLevel(stack,level);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, textComponents, flag);
        textComponents.add(new StringTextComponent(I18n.get("tooltip.chronicler_lh.inscription") + I18n.get(getKey(getInscription(itemStack)))));
    }

    public static String getKey(String text){
        return "tooltip"+"."+ Utils.MOD_ID+"."+"inscription"+"."+text;
    }
}

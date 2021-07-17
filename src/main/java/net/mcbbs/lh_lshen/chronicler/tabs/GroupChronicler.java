package net.mcbbs.lh_lshen.chronicler.tabs;

import net.mcbbs.lh_lshen.chronicler.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class GroupChronicler extends ItemGroup {
    public GroupChronicler(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemRegistry.chronicle.get());
    }
}

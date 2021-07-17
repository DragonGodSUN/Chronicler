package net.mcbbs.lh_lshen.chronicler.inscription;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IInscription {
    String getId();
    void tickEffect(Entity entity, ItemStack chronicler);
    void process(ItemStack itemStack);
}

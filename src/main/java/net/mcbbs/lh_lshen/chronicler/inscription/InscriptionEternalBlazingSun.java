package net.mcbbs.lh_lshen.chronicler.inscription;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

import java.util.Collection;

public class InscriptionEternalBlazingSun implements IInscription {
    private String id;

    public InscriptionEternalBlazingSun(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void tickEffect(Entity entity, ItemStack chronicler) {
        ICapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(chronicler);
        ICapabilityInscription inscription = DataHelper.getInscriptionCapability(chronicler);
        if (energy!=null){
            energy.charge(3*inscription.getLevel());
        }
        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            Collection<EffectInstance> effects = livingEntity.getActiveEffects();
            for (EffectInstance effect:effects){
                if (effect.getEffect().getCategory().equals(EffectType.HARMFUL)){
                   livingEntity.removeEffect(effect.getEffect());
                }
            }
        }
    }

    @Override
    public void process(ItemStack itemStack) {

    }
}

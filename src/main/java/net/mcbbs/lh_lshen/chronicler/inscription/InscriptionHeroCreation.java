package net.mcbbs.lh_lshen.chronicler.inscription;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class InscriptionHeroCreation implements IInscription {
    private String id;

    public InscriptionHeroCreation(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void tickEffect(Entity entity, ItemStack chronicler) {
        ICapabilityInscription inscription = DataHelper.getInscriptionCapability(chronicler);
        ICapabilityEffectPlayer effectPlayer = DataHelper.getEffectPlayerCapability(entity);
        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            EffectInstance heath_boost = livingEntity.getEffect(Effects.HEALTH_BOOST);
            if (!livingEntity.hasEffect(Effects.HEALTH_BOOST.getEffect())||heath_boost.getDuration()<20||heath_boost.getAmplifier()<2){
                livingEntity.addEffect(new EffectInstance(Effects.HEALTH_BOOST,100,2+inscription.getLevel()));
            }
            if (effectPlayer.getInscription().equals(EnumInscription.HERO_CREATION.getId())){
                effectPlayer.setInscription(EnumInscription.HERO_CREATION.getId());
            }
        }
    }

    @Override
    public void process(ItemStack itemStack) {

    }
}

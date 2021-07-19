package net.mcbbs.lh_lshen.chronicler.inscription;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class InscriptionHopeFlower implements IInscription {
    private String id;

    public InscriptionHopeFlower(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void tickEffect(Entity entity, ItemStack chronicler) {
        ICapabilityInscription inscription = DataHelper.getInscriptionCapability(chronicler);
        if (entity instanceof PlayerEntity){
            PlayerEntity playerEntity = (PlayerEntity) entity;
            ICapabilityEffectPlayer effectPlayer = DataHelper.getEffectPlayerCapability(playerEntity);
            if (playerEntity.getMainHandItem().equals(chronicler,false)
                &&!effectPlayer.getType().equals(inscription.getInscription())){
                DataHelper.synInscriptionPlayerCap(inscription,playerEntity);
            }
        }
    }

    @Override
    public void process(ItemStack itemStack) {

    }
}

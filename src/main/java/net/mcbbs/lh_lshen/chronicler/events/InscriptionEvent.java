package net.mcbbs.lh_lshen.chronicler.events;

import net.mcbbs.lh_lshen.chronicler.EffectRegistry;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.inscription.EnumInscription;
import net.mcbbs.lh_lshen.chronicler.items.ItemChronicler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class InscriptionEvent {

    @SubscribeEvent
    public static void makeHeroCreationEffect(LivingHurtEvent event){
        if (event.getSource().getEntity() instanceof PlayerEntity) {
            PlayerEntity entity = (PlayerEntity) event.getSource().getEntity();
            ItemStack itemStack = entity.getOffhandItem();
            if (itemStack.getItem() instanceof ItemChronicler){
                ICapabilityInscription inscription = DataHelper.getInscriptionCapability(itemStack);
                if (inscription.getInscription().equals(EnumInscription.HERO_CREATION.getId())){
                    event.setAmount(event.getAmount()*(1+inscription.getLevel()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void updateHopeFlowerEffect(LivingEvent.LivingUpdateEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof  PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            ICapabilityEffectPlayer effectPlayer = DataHelper.getEffectPlayerCapability(playerEntity);
            if (effectPlayer.getType().equals(EnumInscription.HOPE_FLOWER.getId())){
                boolean hasItem = false;
                ItemStack chronicler = ItemStack.EMPTY;
                for (int i=0;i<playerEntity.inventory.getContainerSize();i++){
                    ItemStack stack = playerEntity.inventory.getItem(i);
                    if (stack.getItem() instanceof ItemChronicler){
                        ICapabilityInscription inscription = DataHelper.getInscriptionCapability(stack);
                        if (inscription.getInscription().equals(EnumInscription.HOPE_FLOWER.getId())){
                            hasItem = true;
                            chronicler = stack;
                            break;
                        }
                    }
                }
                if (!hasItem){
                    effectPlayer.reset();
                }
            }
            if (playerEntity.hasEffect(EffectRegistry.hope_flower.get())){
                EffectInstance effect = playerEntity.getEffect(EffectRegistry.hope_flower.get());
                if (effect.getDuration()<5){
                    int damage = effectPlayer.getCounter();
                    effectPlayer.setCounter(0);
                    if (!playerEntity.level.isClientSide) {
                        playerEntity.hurt(new DamageSource("cannotStop"),10+damage);
                    }
                    playerEntity.removeEffect(effect.getEffect());
                }
            }

        }
    }

    @SubscribeEvent
    public static void addHopeFlowerEffect(LivingDeathEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof  PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            ICapabilityEffectPlayer effectPlayer = DataHelper.getEffectPlayerCapability(playerEntity);
            if (effectPlayer.getType().equals(EnumInscription.HOPE_FLOWER.getId())){
                if (!playerEntity.hasEffect(EffectRegistry.hope_flower.get())){
                    playerEntity.addEffect(new EffectInstance(EffectRegistry.hope_flower.get(),600));
                }
                if (!playerEntity.hasEffect(EffectRegistry.hope_flower.get())
                    || playerEntity.hasEffect(EffectRegistry.hope_flower.get())
                    && playerEntity.getEffect(EffectRegistry.hope_flower.get()).getDuration()>5) {
                    playerEntity.setHealth(1f);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void doHopeFlowerEffect(LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof  PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            ICapabilityEffectPlayer effectPlayer = DataHelper.getEffectPlayerCapability(playerEntity);
            if (effectPlayer.getType().equals(EnumInscription.HOPE_FLOWER.getId())){
                if (playerEntity.hasEffect(EffectRegistry.hope_flower.get())) {
                    effectPlayer.setCounter((int) (effectPlayer.getCounter()+event.getAmount()));
                }
            }
        }
    }

}

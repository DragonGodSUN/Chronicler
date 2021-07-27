package net.mcbbs.lh_lshen.chronicler.capabilities;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityEffectPlayer;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ModCapability {
    @CapabilityInject(ICapabilityItemList.class)
    public static Capability<ICapabilityItemList> ITEMLIST_CAPABILITY = null;

    @CapabilityInject(ICapabilityStellarisEnergy.class)
    public static Capability<ICapabilityStellarisEnergy> ENERGY_CAPABILITY = null;

    @CapabilityInject(ICapabilityInscription.class)
    public static Capability<ICapabilityInscription> INSCRIPTION_CAPABILITY = null;

    @CapabilityInject(ICapabilityEffectPlayer.class)
    public static Capability<ICapabilityEffectPlayer> EFFECT_PLAYER = null;
}

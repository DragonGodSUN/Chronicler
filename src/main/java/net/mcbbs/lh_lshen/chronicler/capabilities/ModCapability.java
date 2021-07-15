package net.mcbbs.lh_lshen.chronicler.capabilities;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.StellarisEnergyProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ModCapability {
    @CapabilityInject(ICapabilityItemList.class)
    public static Capability<ICapabilityItemList> ITEMLIST_CAPABILITY;

    @CapabilityInject(ICapabilityStellarisEnergy.class)
    public static Capability<ICapabilityStellarisEnergy> ENERGY_CAPABILITY;
}

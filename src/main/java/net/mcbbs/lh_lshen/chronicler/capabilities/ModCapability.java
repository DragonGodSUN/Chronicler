package net.mcbbs.lh_lshen.chronicler.capabilities;

import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ModCapability {

    @CapabilityInject(CapabilityItemList.class)
    public static Capability<ICapabilityItemList> ITEMLIST_CAPABILITY;
}

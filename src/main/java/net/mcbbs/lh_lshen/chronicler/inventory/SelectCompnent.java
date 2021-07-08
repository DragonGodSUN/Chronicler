package net.mcbbs.lh_lshen.chronicler.inventory;

import com.google.common.collect.Lists;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;

import java.util.List;

public class SelectCompnent {
    public int page;
    public List<Integer> stack_list = Lists.newArrayList();;
    public int selectSlot;
    private ICapabilityItemList cap_list;

    public SelectCompnent() {
    }

    public SelectCompnent(int page, List<Integer> stack_list, ICapabilityItemList cap_list) {
        this.page = page;
        this.stack_list = stack_list;
        this.cap_list = cap_list;
    }

    public void selectSlot(int slot){
        this.selectSlot = slot;
    }

    public void setCapability(ICapabilityItemList cap_list) {
        this.cap_list = cap_list;
    }

    public int getSelectSlot(){
        return selectSlot;
    }

    public void pageReset(){
        for (Integer i : stack_list){
            i = 0;
        }
    }

    public int getPage() {
        return page;
    }
}

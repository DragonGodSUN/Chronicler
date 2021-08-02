package net.mcbbs.lh_lshen.chronicler.inventory;

import com.google.common.collect.Lists;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.inventory.gui.SlotChronicler;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * 操作GUI各种组件的集合
 */
public class SelectCompnent {
    public int page;
    public List<Integer> stackList = Lists.newArrayList();;
    public int selectSlot;
    public ItemStack selectItemStack = ItemStack.EMPTY;
    private ICapabilityItemList capList = new CapabilityItemList();
    private ContainerChronicler container;

    public SelectCompnent() {
    }

    public SelectCompnent(ContainerChronicler container, int page, List<Integer> stack_list, ICapabilityItemList cap_list) {
        this.container = container;
        this.page = page;
        this.stackList = stack_list;
        this.capList = cap_list;
    }

    public void selectSlot(int slot){
        this.selectSlot = slot;
        if(slot < container.slots.size()){
            Slot slotChronicler = container.slots.get(slot);
            setSelectItemStack(slotChronicler.getItem());
        }
    }

    public void setSelectItemStack(ItemStack selectItemStack) {
        this.selectItemStack = selectItemStack;
    }

    public ItemStack getSelectItem(){
        return selectItemStack;
    }

    public void setCapability(ICapabilityItemList cap_list) {
        this.capList = cap_list;
    }

    public int getSelectSlot(){
        return selectSlot;
    }

    public void pageReset(){
        for (Integer i : stackList){
            i = 0;
        }
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void nextPage(){
        int page = this.page;
        setPage(page+1);
        pageReset();
    }

    public void prePage(){
        int page = this.page;
        setPage(page-1);
        pageReset();
    }

    public int getPage() {
        return page;
    }

    public void setStackList(List<Integer> stackList) {
        this.stackList = stackList;
    }

    public void setStackListPage(int row, int page){
        List<Integer> list = this.stackList;
        if (row<list.size()) {
            list.set(row,page);
            this.setStackList(list);
        }
    }

    public void changeStackListPage(int row, int change){
        List<Integer> list = this.stackList;
        if (row<list.size()) {
            int list_page = list.get(row);
            list.set(row,list_page+change);
            this.setStackList(list);
        }
    }
}

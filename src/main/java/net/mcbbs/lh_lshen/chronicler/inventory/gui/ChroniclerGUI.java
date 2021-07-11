package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.mcbbs.lh_lshen.chronicler.inventory.SelectCompnent;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.ProduceMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class ChroniclerGUI extends ContainerScreen<ContainerChronicler> {
    private static final ResourceLocation BG = new ResourceLocation(Utils.MOD_ID, "textures/gui/door_bg.png");
    private static final ResourceLocation BUTTON = new ResourceLocation(Utils.MOD_ID, "textures/gui/door_gui_button.png");
    private static final ResourceLocation LOADING = new ResourceLocation(Utils.MOD_ID, "textures/gui/door_gui_loading.png");

    private SelectCompnent selectCompnent;
    public ChroniclerGUI(ContainerChronicler container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
        this.selectCompnent = container.selectCompnent;
        this.imageWidth = 256;
        this.imageHeight = 184;

    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();
        this.children.clear();

        addScreenButtons();
        addStackButtons();
        addOperationButtons();

    }

    public void addScreenButtons(){
        ImageButton pageUp = new ImageButton(leftPos + 220, topPos + 162, 12, 9, 24, 0, 25, BUTTON, 256, 256,
                (button) -> {
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("上一页"), x, y), StringTextComponent.EMPTY);

        ImageButton pageDown = new ImageButton(leftPos + 229, topPos + 162, 12, 9, 33, 0, 25, BUTTON, 256, 256,
                (button) -> {
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("下一页"), x, y), StringTextComponent.EMPTY);

        ImageButton bookClose = new ImageButton(leftPos + 237, topPos + 5, 10, 10, 174, 0, 26, BUTTON, 256, 256,
                (button) -> {
                    this.onClose();
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("关闭"), x, y), StringTextComponent.EMPTY);

        addButton(pageUp);
        addButton(pageDown);
        addButton(bookClose);
    }

    public void addStackButtons(){
        final int X_SPACING = 9;
        final int Y_SPACING = 18;
        final int X_POS = 229;
        final int Y_POS = 20;
        for (int j =0;j<8;j++) {
            int finalJ = j;
            ImageButton back = new ImageButton(leftPos + X_POS, topPos + Y_POS + j*Y_SPACING, 9, 11, 0, 0, 24, BUTTON, 256, 256,
                    (button) -> {
                        CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                        ItemStack chronicler = this.menu.getItemStackChronicler();
                        List<Integer> stackList= selectCompnent.stackList;
                        if (this.menu.slots.size() > finalJ*4) {
                            SlotChronicler slot = (SlotChronicler) this.menu.slots.get(finalJ*4);
                            int list_page = stackList.get(finalJ);
                            if (cap_list != null) {
                                if (list_page >0){
                                    selectCompnent.changeStackListPage(finalJ,-1);
                                    this.menu.selectBoxOpen =false;
                                    synData(chronicler,cap_list);
                                }
                            }
                        }
                    }, (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("back"), x, y), StringTextComponent.EMPTY);

            ImageButton next = new ImageButton(leftPos + X_POS + X_SPACING, topPos + Y_POS + j*Y_SPACING, 9, 11, 11, 0, 24, BUTTON, 256, 256,
                    (button) -> {
                        CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                        ItemStack chronicler = this.menu.getItemStackChronicler();
                        List<Integer> stackList= selectCompnent.stackList;
                        if (this.menu.slots.size() > finalJ*4) {
                            SlotChronicler slot = (SlotChronicler) this.menu.slots.get(finalJ*4);
                            int list_page = stackList.get(finalJ);
                            if (cap_list != null) {
                                if (list_page < slot.container.getContainerSize()/4 -1){
                                    selectCompnent.changeStackListPage(finalJ,1);
                                    this.menu.selectBoxOpen =false;
                                    synData(chronicler,cap_list);
                                }
                            }
                        }
                    },
                    (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("next"), x, y), StringTextComponent.EMPTY);

            addButton(back);
            addButton(next);
        }
    }

    public void addOperationButtons(){
        ImageButton indexUp = new ImageButton(leftPos + 39, topPos + 136, 16, 16, 131, 0, 24, BUTTON, 256, 256,
                (button) -> {
                    if (selectCompnent.selectSlot < this.menu.CAP_SIZE && this.menu.selectBoxOpen) {
                        CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                        SlotChronicler selectSlot = (SlotChronicler) this.menu.slots.get(selectCompnent.selectSlot);
                        ItemStack chronicler = this.menu.getItemStackChronicler();
                        if (cap_list != null && selectSlot != null && !selectSlot.getItem().isEmpty()) {
                            ItemStack itemStack = selectSlot.getItem();
                            boolean hasItem = StoreHelper.hasItemStack(cap_list, itemStack);
                            if (hasItem) {
                                StoreHelper.upIndex(cap_list, itemStack);
                                int initSlot = selectSlot.getJ_index()*4;
                                int index = StoreHelper.getStackIndex(cap_list,itemStack);
                                if (index>=0) {
                                    if (selectCompnent.selectSlot >= initSlot) {
                                        selectCompnent.selectSlot -= 1;
                                    }
                                    if (index>0 && selectCompnent.selectSlot < initSlot){
                                        selectCompnent.selectSlot = initSlot+3;
                                        selectCompnent.changeStackListPage(selectSlot.getJ_index(),-1);
                                    }else if (selectCompnent.selectSlot < initSlot){
                                        selectCompnent.selectSlot = initSlot;
                                    }
                                }

                                synData(chronicler, cap_list);
                            }
                        }
                    }
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("排序上升"), x, y), StringTextComponent.EMPTY);

        ImageButton indexDown = new ImageButton(leftPos + 61, topPos + 136, 16, 16, 153, 0, 24, BUTTON, 256, 256,
                (button) -> {
                    if (selectCompnent.selectSlot < this.menu.CAP_SIZE && this.menu.selectBoxOpen) {
                        CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                        SlotChronicler selectSlot = (SlotChronicler) this.menu.slots.get(selectCompnent.selectSlot);
                        ItemStack chronicler = this.menu.getItemStackChronicler();
                        if (cap_list != null && selectSlot !=null && !selectSlot.getItem().isEmpty()){
                            ItemStack itemStack = selectSlot.getItem();
                            boolean hasItem = StoreHelper.hasItemStack(cap_list,itemStack);
                            if (hasItem){
                                StoreHelper.downIndex(cap_list,itemStack);
                                int initSlot = selectSlot.getJ_index()*4;
                                int size =StoreHelper.getItemStackList(cap_list,itemStack).size();
                                if (selectSlot.getSlotIndex() < size-1 && selectCompnent.selectSlot < initSlot+4) {
                                    selectCompnent.selectSlot += 1;
                                }
                                if (selectCompnent.selectSlot >= initSlot+4){
                                    selectCompnent.selectSlot = initSlot;
                                    selectCompnent.changeStackListPage(selectSlot.getJ_index(),1);
//                                    synData(chronicler,cap_list);
                                }
                                    synData(chronicler,cap_list);
                            }
                        }
                    }
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("排序降低"), x, y), StringTextComponent.EMPTY);

        ImageButton indexFirst = new ImageButton(leftPos + 61, topPos + 114, 16, 16, 110, 0, 24, BUTTON, 256, 256,
                (button) -> {
                    if (selectCompnent.selectSlot < this.menu.CAP_SIZE && this.menu.selectBoxOpen) {
                        CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                        SlotChronicler selectSlot = (SlotChronicler) this.menu.slots.get(selectCompnent.selectSlot);
                        ItemStack chronicler = this.menu.getItemStackChronicler();
                        if (cap_list != null && selectSlot != null && !selectSlot.getItem().isEmpty()) {
                            ItemStack itemStack = selectSlot.getItem();
                            boolean hasItem = StoreHelper.hasItemStack(cap_list, itemStack);
                            if (hasItem) {
                                int initSlot = selectSlot.getJ_index()*4;
                                StoreHelper.setFirstIndex(cap_list, itemStack);
                                selectCompnent.setStackListPage(selectSlot.getJ_index(),0);
                                synData(chronicler, cap_list);
                                selectCompnent.selectSlot(initSlot);
                            }
                        }
                    }
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("设为首位"), x, y), StringTextComponent.EMPTY);

        ImageButton produce = new ImageButton(leftPos + 111, topPos + 158, 34, 15, 49, 0, 24, BUTTON, 256, 256,
                (button) -> {
                    CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                    ItemStack chronicler = this.menu.getItemStackChronicler();
                    ItemStack selectItem = selectCompnent.selectItemStack;
                    if (selectItem != null && this.menu.selectBoxOpen) {
                        ChroniclerNetwork.INSTANCE.sendToServer(new ProduceMessage(selectItem));
                    }
                    synData(chronicler,cap_list);
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("生成"), x, y), StringTextComponent.EMPTY);

        ToggleWidget star = new ToggleWidget(leftPos + 39, topPos + 114, 16, 16, false);
        star.initTextureValues(88, 0, 0, 24, BUTTON);

        this.addButton(indexUp);
        this.addButton(indexDown);
        this.addButton(indexFirst);
        this.addButton(star);
        this.addButton(produce);

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);

    }


    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        matrixStack.translate(0, 0, -100);
        {
            renderBackground(matrixStack);
            getMinecraft().textureManager.bind(BG);
            blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        }

        matrixStack.translate(0, 0, 100);
        {
            renderSelectBox(matrixStack,mouseX,mouseY,partialTicks);
        }
    }


    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {

    }

    private void renderSelectBox(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int LIST_XPOS = 151;
        final int LIST_YPOS = 17;

        int slot = selectCompnent.getSelectSlot();
        int x = slot % 4;
        int y = slot / 4;

        if (this.menu.selectBoxOpen && slot<this.menu.CAP_SIZE) {
            getMinecraft().textureManager.bind(LOADING);
            blit(matrixStack,leftPos+LIST_XPOS + SLOT_X_SPACING*x,topPos+LIST_YPOS + SLOT_Y_SPACING*y,28,0,20,20);
        }

    }

    public void synData(ItemStack chronicler, CapabilityItemList capabilityItemList){
        capabilityItemList.markDirty();
        StoreHelper.synCapabilityToSever(chronicler,capabilityItemList);
        this.menu.setCapabilityItemList(capabilityItemList);
        this.menu.broadcastChanges();
    }
}

package net.mcbbs.lh_lshen.chronicler.inventory.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mcbbs.lh_lshen.chronicler.Utils;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.mcbbs.lh_lshen.chronicler.inscription.EnumInscription;
import net.mcbbs.lh_lshen.chronicler.inscription.InscriptionRegister;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.mcbbs.lh_lshen.chronicler.inventory.SelectCompnent;
import net.mcbbs.lh_lshen.chronicler.items.ItemInscription;
import net.mcbbs.lh_lshen.chronicler.items.ItemRecordPage;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.ProduceMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
import java.util.UUID;

public class ChroniclerGUI extends ContainerScreen<ContainerChronicler> {
    private static final ResourceLocation BG = new ResourceLocation(Utils.MOD_ID, "textures/gui/door_bg.png");
    private static final ResourceLocation BUTTON = new ResourceLocation(Utils.MOD_ID, "textures/gui/door_gui_button.png");
    private static final ResourceLocation LOADING = new ResourceLocation(Utils.MOD_ID, "textures/gui/door_gui_loading.png");

    private SelectCompnent selectCompnent;
    private ToggleWidget star;
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
                    CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                    ItemStack chronicler = this.menu.getItemStackChronicler();
                    if (selectCompnent.getPage()>0){
                        selectCompnent.prePage();
                        this.menu.selectBoxOpen =false;
                        synData(chronicler,cap_list);
                    }
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("上一页"), x, y), StringTextComponent.EMPTY);

        ImageButton pageDown = new ImageButton(leftPos + 229, topPos + 162, 12, 9, 33, 0, 25, BUTTON, 256, 256,
                (button) -> {
                    CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                    ItemStack chronicler = this.menu.getItemStackChronicler();
                    int pages = cap_list.getKeyList().size()/8;
                    if (selectCompnent.getPage()<pages){
                        selectCompnent.nextPage();
                        this.menu.selectBoxOpen =false;
                        synData(chronicler,cap_list);
                    }
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
        ImageButton indexUp = new ImageButton(leftPos + 63, topPos + 116, 6, 10, 133, 3, 24, BUTTON, 256, 256,
                (button) -> {
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotChronicler &&
                            selectCompnent.selectSlot < this.menu.CAP_SIZE && this.menu.selectBoxOpen) {
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
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("排序左移"), x, y), StringTextComponent.EMPTY);

        ImageButton indexDown = new ImageButton(leftPos + 69, topPos + 116, 6, 10, 161, 3, 24, BUTTON, 256, 256,
                (button) -> {
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotChronicler &&
                            selectCompnent.selectSlot < this.menu.CAP_SIZE && this.menu.selectBoxOpen) {
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
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("排序右移"), x, y), StringTextComponent.EMPTY);

        ImageButton indexFirst = new ImageButton(leftPos + 61, topPos + 91, 16, 16, 110, 0, 24, BUTTON, 256, 256,
                (button) -> {
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotChronicler &&
                            selectCompnent.selectSlot < this.menu.CAP_SIZE && this.menu.selectBoxOpen) {
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

        ImageButton listUp = new ImageButton(leftPos + 42, topPos + 115, 10, 6, 134, 50, 24, BUTTON, 256, 256,
                (button) -> {
                    CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                    ItemStack chronicler = this.menu.getItemStackChronicler();
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotChronicler &&
                            selectCompnent.selectSlot < this.menu.CAP_SIZE && this.menu.selectBoxOpen) {
                        SlotChronicler selectSlot = (SlotChronicler) this.menu.slots.get(selectCompnent.selectSlot);
                        if (cap_list != null && selectSlot != null && !selectSlot.getItem().isEmpty()) {
                            ItemStack itemStack = selectSlot.getItem();
                            boolean hasItem = StoreHelper.hasItemStack(cap_list, itemStack);
                            if (hasItem) {
                                StoreHelper.upStackListIndex(cap_list,itemStack);
                                selectCompnent.setStackListPage(selectSlot.getJ_index(),0);
                                selectCompnent.selectSlot(selectSlot.getJ_index()*4);
                                if (selectCompnent.selectSlot-4 >= 0) {
                                    selectCompnent.selectSlot -= 4;
                                } else if (selectCompnent.page >0){
                                    selectCompnent.prePage();
                                    selectCompnent.selectSlot = this.menu.CAP_SIZE - 3 -1;
                                }else {
                                    selectCompnent.selectSlot = 0;
                                }
                                synData(chronicler, cap_list);
                            }
                        }
                    }
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotStar
                            && selectCompnent.selectSlot < this.menu.STAR_SIZE && this.menu.selectBoxOpen){
                        Inventory inventory = cap_list.getInventoryStar();
                        SlotStar slotStar = (SlotStar) this.menu.slots.get(selectCompnent.getSelectSlot());
                        ItemStack stack = this.menu.selectCompnent.selectItemStack;
                        if (inventory !=null && !inventory.isEmpty() &&!stack.isEmpty()){
                            if (slotStar.getSlotIndex()>0){
                                ItemStack replace = inventory.getItem(slotStar.getSlotIndex()-1);
                                inventory.setItem(slotStar.getSlotIndex()-1,stack);
                                inventory.setItem(slotStar.getSlotIndex(),replace);
                                cap_list.setInventoryStar(inventory);

                                selectCompnent.selectSlot -=1;
                                synData(chronicler,cap_list);
                            }
                        }
                    }

                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("序列上升"), x, y), StringTextComponent.EMPTY);

        ImageButton listDown = new ImageButton(leftPos + 42, topPos + 121, 10, 6, 156, 56, 24, BUTTON, 256, 256,
                (button) -> {
                    CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                    ItemStack chronicler = this.menu.getItemStackChronicler();
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotChronicler &&
                            selectCompnent.selectSlot < this.menu.CAP_SIZE && this.menu.selectBoxOpen) {
                        SlotChronicler selectSlot = (SlotChronicler) this.menu.slots.get(selectCompnent.selectSlot);
                        if (cap_list != null && selectSlot != null && !selectSlot.getItem().isEmpty()) {
                            ItemStack itemStack = selectSlot.getItem();
                            boolean hasItem = StoreHelper.hasItemStack(cap_list, itemStack);
                            if (hasItem) {
                                if (StoreHelper.getStackListIndex(cap_list,itemStack)+1 < cap_list.getKeyList().size()) {
                                    StoreHelper.downStackListIndex(cap_list,itemStack);
                                    selectCompnent.setStackListPage(selectSlot.getJ_index(),0);
                                    selectCompnent.selectSlot(selectSlot.getJ_index()*4);
                                        if (selectCompnent.selectSlot + 4 < this.menu.CAP_SIZE) {
                                            selectCompnent.selectSlot += 4;
                                        } else if (selectCompnent.page < cap_list.getKeyList().size() / 8) {
                                            selectCompnent.nextPage();
                                            selectCompnent.selectSlot = 0;
                                        } else {
                                            selectCompnent.selectSlot = 0;
                                        }
                                }
                                synData(chronicler, cap_list);
                            }
                        }
                    }
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotStar
                            && selectCompnent.selectSlot < this.menu.STAR_SIZE && this.menu.selectBoxOpen){
                        Inventory inventory = cap_list.getInventoryStar();
                        SlotStar slotStar = (SlotStar) this.menu.slots.get(selectCompnent.getSelectSlot());
                        ItemStack stack = this.menu.selectCompnent.selectItemStack;
                        if (inventory !=null && !inventory.isEmpty() &&!stack.isEmpty()){
                            if (slotStar.getSlotIndex()+1<inventory.getContainerSize()) {
                                ItemStack replace = inventory.getItem(slotStar.getSlotIndex()+1);
                                inventory.setItem(slotStar.getSlotIndex()+1,stack);
                                inventory.setItem(slotStar.getSlotIndex(),replace);
                                cap_list.setInventoryStar(inventory);
                            }
                            if (selectCompnent.selectSlot+1 < this.menu.STAR_SIZE){
                                selectCompnent.selectSlot +=1;
                            }
                            synData(chronicler,cap_list);

                        }
                    }
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("序列下降"), x, y), StringTextComponent.EMPTY);

        ImageButton pop = new ImageButton(leftPos + 39, topPos + 134, 16, 16, 189, 0, 24, BUTTON, 256, 256,
                (button) -> {
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotChronicler
                        && this.menu.selectBoxOpen) {
                        CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                        SlotChronicler selectSlot = (SlotChronicler) this.menu.slots.get(selectCompnent.selectSlot);
                        ItemStack chronicler = this.menu.getItemStackChronicler();
                        if (cap_list != null && selectSlot != null && !selectSlot.getItem().isEmpty()) {
                            ItemStack itemStack = selectSlot.getItem();
                            boolean hasItem = StoreHelper.hasItemStack(cap_list, itemStack);
                            if (hasItem) {
                                cap_list.removeStar(itemStack);
                                StoreHelper.deleteItemStack(cap_list,itemStack);
                                ItemStack itemPage = ItemRecordPage.getPageStored(itemStack);
                                if (this.getMinecraft().player!=null) {
                                    this.getMinecraft().player.inventory.add(itemPage.copy());
                                }
                                ChroniclerNetwork.INSTANCE.sendToServer(new ProduceMessage(itemPage));
                                this.menu.starTriggered = false;
                                selectCompnent.setSelectItemStack(ItemStack.EMPTY);
                                synData(chronicler,cap_list);
                            }
                        }
                    }
                    if (this.menu.slots.get(selectCompnent.selectSlot) instanceof SlotInscription
                            && this.menu.selectBoxOpen) {
                        ICapabilityInscription inscription = this.menu.getInscription();
                        ICapabilityItemList cap_list = this.menu.getCapabilityItemList();
                        ICapabilityStellarisEnergy energy = this.menu.getEnergy();
                        SlotInscription selectSlot = (SlotInscription) this.menu.slots.get(selectCompnent.selectSlot);
                        if (inscription != null && selectSlot != null && !selectSlot.getItem().isEmpty()) {
                            if (!inscription.getInscription().isEmpty()) {
                                ItemStack stack = selectSlot.getItemInscription();
                                selectSlot.set(ItemStack.EMPTY);
                                if (this.getMinecraft().player!=null) {
                                    this.getMinecraft().player.inventory.add(stack.copy());
                                }
                                ChroniclerNetwork.INSTANCE.sendToServer(new ProduceMessage(stack));
                                energy.resetMax();
                                inscription.reset();
                                selectCompnent.setSelectItemStack(ItemStack.EMPTY);
                                this.menu.starTriggered = false;
                                synInscription(this.menu.getItemStackChronicler(), (CapabilityInscription) inscription);
                                synEnergy(this.menu.getItemStackChronicler(), (CapabilityStellarisEnergy) energy);
                                synData(this.menu.getItemStackChronicler(), (CapabilityItemList) cap_list);
                            }

                        }

                        }
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("弹出"), x, y), StringTextComponent.EMPTY);

        ImageButton reset = new ImageButton(leftPos + 61, topPos + 134, 16, 16, 210, 0, 24, BUTTON, 256, 256,
                (button) -> {
                    CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                    ItemStack chronicler = this.menu.getItemStackChronicler();
                    if (cap_list != null) {
                        selectCompnent.setPage(0);
                        selectCompnent.pageReset();
                        selectCompnent.setSelectItemStack(ItemStack.EMPTY);
                        this.menu.starTriggered = false;
                        this.menu.selectBoxOpen = false;
                        synData(chronicler,cap_list);

                    }
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("重置"), x, y), StringTextComponent.EMPTY);


        ImageButton produce = new ImageButton(leftPos + 111, topPos + 158, 34, 15, 49, 0, 24, BUTTON, 256, 256,
                (button) -> {
                    CapabilityItemList cap_list = this.menu.getCapabilityItemList();
                    ItemStack chronicler = this.menu.getItemStackChronicler();
                    ItemStack selectItem = selectCompnent.selectItemStack;
                    ICapabilityStellarisEnergy energy = this.menu.getEnergy();
                    if (selectItem != null && this.menu.selectBoxOpen) {
                        if (!(selectItem.getItem() instanceof ItemInscription)) {
                            if (energy.canCost(1000)) {
                                if (this.getMinecraft().player!=null) {
                                    this.getMinecraft().player.inventory.add(selectItem.copy());
                                }
                                ChroniclerNetwork.INSTANCE.sendToServer(new ProduceMessage(selectItem));
                            }else {
                                if (this.getMinecraft().player!=null) {
                                    this.getMinecraft().player.sendMessage(new StringTextComponent("星能不足"), UUID.randomUUID());
                                }
                            }
                        }else {
                            if (this.getMinecraft().player!=null) {
                                this.getMinecraft().player.sendMessage(new StringTextComponent("无法生成符文"), UUID.randomUUID());
                            }
                        }
                    }
                },
                (button, matrixStack, x, y) -> renderTooltip(matrixStack, new StringTextComponent("生成"), x, y), StringTextComponent.EMPTY);

        this.star = new ToggleWidget(leftPos + 39, topPos + 91, 16, 16, false);
        star.initTextureValues(0, 48, 18, 24, BUTTON);

        this.addButton(indexUp);
        this.addButton(indexDown);
        this.addButton(indexFirst);
        this.addButton(listUp);
        this.addButton(listDown);
        this.addButton(star);
        this.addButton(pop);
        this.addButton(reset);
        this.addButton(produce);

    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if (this.menu.selectBoxOpen && !this.menu.slots.get(selectCompnent.selectSlot).getItem().isEmpty()){
            CapabilityItemList cap_list = this.menu.getCapabilityItemList();
            ItemStack chronicler = this.menu.getItemStackChronicler();
            if (this.star.mouseClicked(p_231044_1_,p_231044_3_,p_231044_5_)) {
                ItemStack selectItem = this.menu.slots.get(selectCompnent.selectSlot).getItem();
                if (!StoreHelper.isItemStar(cap_list,selectItem)){
                    cap_list.setStar(selectItem);
                    if (StoreHelper.isItemStar(cap_list,selectItem)) {
                        this.menu.starTriggered = true;
                    }
                }else {
                    cap_list.removeStar(selectItem);
                    this.menu.starTriggered = false;
                }
                synData(chronicler,cap_list);
//                boolean flag =false;
//                int index = -1;
//
//                for (int i=0;i<this.menu.inventoryStar.getContainerSize();i++){
//                    ItemStack stack = this.menu.inventoryStar.getItem(i);
//                    if (stack.equals(selectItem,false)){
//                        flag =true;
//                        index=i;
//                        break;
//                    }
//                }
//
//                if (!flag&&inventoryStar.canAddItem(selectItem)){
//                    inventoryStar.addItem(this.menu.slots.get(selectCompnent.selectSlot).getItem());
//
//                }
//                if (flag&&index>=0){
//                    this.menu.inventoryStar.removeItem(index,1);
//                    this.menu.starTriggered=false;
//                }
            }
        }
        return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
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
            renderEnergyStellaris(matrixStack,mouseX,mouseY,partialTicks);
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

        if (this.menu.selectBoxOpen && star.isStateTriggered()!=this.menu.starTriggered) {
            star.setStateTriggered(this.menu.starTriggered);
        }
        if (!this.menu.selectBoxOpen){
            star.setStateTriggered(false);
        }

        if (this.menu.selectBoxOpen && slot<this.menu.CAP_SIZE) {
            getMinecraft().textureManager.bind(LOADING);
            blit(matrixStack,leftPos+LIST_XPOS + SLOT_X_SPACING*x,topPos+LIST_YPOS + SLOT_Y_SPACING*y,28,0,20,20);
        }

        if (this.menu.selectBoxOpen && slot>=this.menu.CAP_SIZE && slot<this.menu.STAR_SIZE) {
            if (this.menu.slots.get(slot) instanceof SlotStar) {
                SlotStar slotStar = (SlotStar) this.menu.slots.get(slot);
                getMinecraft().textureManager.bind(LOADING);
                blit(matrixStack,leftPos+26,topPos+20 + SLOT_Y_SPACING*slotStar.getSlotIndex(),50,0,8,11);

                if (selectCompnent.page==slotStar.getLinkedPage() &&
                selectCompnent.stackList.get(slotStar.getLinkedRow())==slotStar.getLinkedStackPage()) {
                    blit(matrixStack,leftPos+LIST_XPOS+SLOT_X_SPACING*(slotStar.getLinkedStackIndex()),topPos+LIST_YPOS + SLOT_Y_SPACING*slotStar.getLinkedRow(),28,0,20,20);
                }
            }
        }


    }

    private void renderEnergyStellaris(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        ICapabilityStellarisEnergy energy = this.menu.getEnergy();
        int point = energy.getEnergyPoint();
        getMinecraft().textureManager.bind(LOADING);
        float e = MathHelper.clamp(((float)point)/((float)energy.getEnergyMax()),0.0f,1.0f);
        blit(matrixStack,leftPos+45,topPos+21,0,0,(int) (25*e),8);
        getMinecraft().font.draw(matrixStack, ""+point, leftPos + 83, topPos + 23, TextFormatting.AQUA.getColor());
    }

    public void synData(ItemStack chronicler, CapabilityItemList capabilityItemList){
        capabilityItemList.markDirty();
        this.menu.setCapabilityItemList(capabilityItemList);
        this.menu.broadcastChanges();
    }

    public void synEnergy(ItemStack chronicler, CapabilityStellarisEnergy energy){
        this.menu.setEnergy(energy);
        StoreHelper.synEnergyCapabilityToSever(chronicler,energy);
    }

    public void synInscription(ItemStack chronicler, CapabilityInscription inscription){
        this.menu.setInscription(inscription);
        StoreHelper.synInscriptionCapabilityToSever(chronicler,inscription);
    }

}

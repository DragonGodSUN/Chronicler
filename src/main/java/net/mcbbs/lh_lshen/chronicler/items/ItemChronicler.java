package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.SynContainerEnergyCapMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import java.util.UUID;

public class ItemChronicler extends Item {
    public ItemChronicler() {
        super(new Properties().tab(ItemGroup.TAB_TOOLS).fireResistant().stacksTo(1));
    }

//  使用能力系统进行充能，不选择shareNbt，一个原因是该NBT改变时，持有的ItemStack会不断重载
    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int tick, boolean flag) {
        if (!entity.level.isClientSide) {
            LazyOptional<ICapabilityStellarisEnergy> energyLazyOptional = itemStack.getCapability(ModCapability.ENERGY_CAPABILITY,null);
            energyLazyOptional.ifPresent((energy)->{
                energy.charge(1);
//              将服务端的数据发送给客户端的容器
                if (energy.isDirty() && entity !=null && entity instanceof PlayerEntity ){
                    if (((PlayerEntity) entity).containerMenu instanceof ContainerChronicler) {
                        ChroniclerNetwork.sendToClientPlayer(new SynContainerEnergyCapMessage(energy), (PlayerEntity) entity);
                    }
                    energy.setDirty(false);
                }
            });
        }
        super.inventoryTick(itemStack, world, entity, tick, flag);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        ItemStack itemStack = entity.getMainHandItem();
        ItemStack itemStack_l = entity.getOffhandItem();
        LazyOptional<ICapabilityItemList> capability = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY);
        capability.ifPresent((cap_list)->{
        if (itemStack.getItem() instanceof ItemChronicler) {
            if (entity.isShiftKeyDown()) {
                if (itemStack_l.getItem() instanceof ItemRecordPage){
                    ItemStack storeItem = ((ItemRecordPage) itemStack_l.getItem()).getStoreItem(itemStack_l);
                    if (!storeItem.isEmpty()){
                        StoreHelper.addItemStack(cap_list,storeItem);
                        itemStack_l.shrink(1);
                        if (world.isClientSide ) {
                            entity.sendMessage(new StringTextComponent("储存"+":"+storeItem.getDisplayName().getString()), UUID.randomUUID());
                            entity.playSound(SoundEvents.BOOK_PAGE_TURN,1f,1f);
                        }
                    }
                }
            }else {
                if (!world.isClientSide ) {
                    INamedContainerProvider containerProvider = new ContainerProviderChronicler(this, itemStack);
                    NetworkHooks.openGui((ServerPlayerEntity) entity,
                            containerProvider,
                            (packetBuffer)->{
//                            发送物品列表信息到容器中
                                ListNBT listNBT = getListNBT(itemStack);
                                setRandomId(itemStack);
                                packetBuffer.writeUtf(getId(itemStack));
                                packetBuffer.writeItemStack(itemStack,false);
                                packetBuffer.writeInt(listNBT.size());
                                for (int i=0;i<listNBT.size();i++){
                                    CompoundNBT nbt = listNBT.getCompound(i);
                                    if (nbt !=null) {
                                        packetBuffer.writeNbt(nbt);
                                    }
                                }
                            });
                }
                    entity.playSound(SoundEvents.BOOK_PAGE_TURN,2f,1f);
                }
            }
        });
        return ActionResult.success(itemStack);
    }

    public static ListNBT getListNBT(ItemStack stack) {
        CapabilityItemList itemListCapability = getItemListCapability(stack);
        return itemListCapability.serializeNBT();
    }

    public static CapabilityItemList getItemListCapability(ItemStack itemStack){
        ICapabilityItemList cap_list = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY).orElse(null);
        if (cap_list == null || !(cap_list instanceof ICapabilityItemList)) {
            return new CapabilityItemList();
        }
        return (CapabilityItemList) cap_list;
    }

    public static void setRandomId(ItemStack stack){
        ICapabilityStellarisEnergy energy = stack.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
        if (!stack.isEmpty()){
            energy.setId(UUID.randomUUID().toString());
        }
    }

    public static void setId(ItemStack stack, String id){
        ICapabilityStellarisEnergy energy = stack.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
        if (!stack.isEmpty()){
            energy.setId(id);
        }
    }

    public static String getId(ItemStack stack){
        ICapabilityStellarisEnergy energy = stack.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
        if (!stack.isEmpty()){
            return  energy.getId();
        }
        return "";
    }
//    public SelectCompnent getSelectCompnent(ContainerChronicler container, ItemStack stack) {
//        CompoundNBT nbt = stack.getShareTag();
//        SelectCompnent selectCompnent = new SelectCompnent();
//        if (nbt != null) {
//            int page = nbt.getInt("page");
//            int slot = nbt.getInt("slot_select");
//            int slot_size = nbt.getInt("slot_size");
//            List<Integer> stack_list = Lists.newArrayList();
//            for (int i=0;i<slot_size;i++) {
//                int stack_unit = nbt.getInt("stack_list:"+i);
//                stack_list.add(stack_unit);
//            }
//        ICapabilityItemList capabilityItemList = stack.getCapability(ModCapability.ITEMLIST_CAPABILITY).orElse(null);
//            if (capabilityItemList!=null){
//                selectCompnent = new SelectCompnent(container,page,stack_list,capabilityItemList);
//                selectCompnent.selectSlot(slot);
//            }
//        }
//        return selectCompnent;
//    }
//
//    public void setSelectCompnent(ItemStack stack, SelectCompnent selectCompnent) {
//        CompoundNBT nbt = stack.getShareTag();
//        if (nbt !=null && selectCompnent != null) {
//            nbt.putInt("page",selectCompnent.page);
//            nbt.putInt("slot_select",selectCompnent.selectSlot);
//            nbt.putInt("stack_size",selectCompnent.stackList.size());
//            for (int i = 0; i<selectCompnent.stackList.size(); i++) {
//                nbt.putInt("stack_list:"+i,selectCompnent.stackList.get(i));
//            }
//        }
//
//    }

//    @Nullable
//    @Override
//    public CompoundNBT getShareTag(ItemStack stack) {
//        ICapabilityStellarisEnergy energy = stack.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
//        if (energy == null) {
//            return new CompoundNBT();
//        }
//        CompoundNBT nbt = energy.serializeNBT();
//        return nbt;
//    }
//
//    @Override
//    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
//        ICapabilityStellarisEnergy energy = stack.getCapability(ModCapability.ENERGY_CAPABILITY).orElse(null);
//        energy.deserializeNBT(nbt);
//    }

    //    @Override
//    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
//        SelectCompnent selectCompnent = new SelectCompnent();
//        if (nbt != null) {
//            int page = nbt.getInt("page");
//            int slot = nbt.getInt("slot_select");
//            int slot_size = nbt.getInt("slot_size");
//            List<Integer> stack_list = Lists.newArrayList();
//            for (int i=0;i<slot_size;i++) {
//                int stack_unit = nbt.getInt("stack_list:"+i);
//                stack_list.add(stack_unit);
//            }
//        ICapabilityItemList capabilityItemList = stack.getCapability(ModCapability.ITEMLIST_CAPABILITY).orElse(null);
//        if (capabilityItemList!=null){
//            selectCompnent = new SelectCompnent(page,stack_list,capabilityItemList);
//            selectCompnent.selectSlot(slot);
//            }
//        }
//        super.readShareTag(stack, nbt);
//    }

//    @Nullable
//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
//        return new ItemListProvider();
//    }

    private static class ContainerProviderChronicler implements INamedContainerProvider {
        public ContainerProviderChronicler(ItemChronicler itemChronicler, ItemStack itemStackChronicler) {
            this.itemChronicler = itemChronicler;
            this.itemStackChronicler = itemStackChronicler;

        }

        @Override
        public ITextComponent getDisplayName() {
            return itemStackChronicler.getDisplayName();
        }

        @Override
        public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            ContainerChronicler newContainerServerSide =
                    ContainerChronicler.createContainerServerSide(windowID, playerInventory,
                            itemChronicler.getItemListCapability(itemStackChronicler),
                            itemStackChronicler);
            return newContainerServerSide;
        }


        private ItemChronicler itemChronicler;
        private ItemStack itemStackChronicler;
    }

}

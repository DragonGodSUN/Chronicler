package net.mcbbs.lh_lshen.chronicler.items;

import com.google.common.collect.Lists;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.mcbbs.lh_lshen.chronicler.inventory.SelectCompnent;
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
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import javax.annotation.Nullable;
import java.util.List;

public class ItemChronicler extends Item {
    public ItemChronicler() {
        super(new Properties().tab(ItemGroup.TAB_TOOLS).fireResistant().stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        ItemStack itemStack = entity.getItemInHand(hand);
        ItemStack itemStack_l = entity.getOffhandItem();
        LazyOptional<ICapabilityItemList> capability = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY);
        capability.ifPresent((cap_list)->{
            if (entity.isShiftKeyDown()) {
                StoreHelper.addItemStack(cap_list,itemStack_l);
            }else {
                if (itemStack.getItem() instanceof ItemChronicler) {
                    if (!world.isClientSide ) {
                        INamedContainerProvider containerProvider = new ContainerProviderChronicler(this, itemStack);
                        NetworkHooks.openGui((ServerPlayerEntity) entity,
                                containerProvider,
                                (packetBuffer)->{
                                    ListNBT listNBT = getListNBT(itemStack);
                                    packetBuffer.writeItemStack(itemStack,true);
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

    public static void readListNBT(ItemStack stack, @Nullable ListNBT nbt) {
        if (nbt != null) {
            CapabilityItemList capabilityItemList = getItemListCapability(stack);
            capabilityItemList.deserializeNBT(nbt);
        }
    }


    public static CapabilityItemList getItemListCapability(ItemStack itemStack){
        ICapabilityItemList cap_list = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY).orElse(null);
        if (cap_list == null || !(cap_list instanceof ICapabilityItemList)) {
            return new CapabilityItemList();
        }
        return (CapabilityItemList) cap_list;
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

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        if (super.getShareTag(stack) == null) {
            return new CompoundNBT();
        }
        return super.getShareTag(stack);
    }

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

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemListProvider();
    }

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

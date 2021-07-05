package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicier;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import javax.annotation.Nullable;

public class ItemChronicle extends Item {
    public ItemChronicle() {
        super(new Properties().tab(ItemGroup.TAB_TOOLS).fireResistant().stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        ItemStack itemStack = entity.getItemInHand(hand);
        ItemStack itemStack_l = entity.getOffhandItem();
        if (!world.isClientSide) {
            INamedContainerProvider containerProvier = new ContainerProviderChronicler(this, itemStack);
            NetworkHooks.openGui((ServerPlayerEntity) entity,
                    containerProvier,
                    (packetBuffer)->{});
        }
//        LazyOptional<ICapabilityItemList> capability = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY);
//            capability.ifPresent((cap_list)->{
//                    if (entity.isShiftKeyDown()) {
//                        StoreHelper.addItemStack(cap_list,itemStack_l);
//                        if (world.isClientSide) {
//                            System.out.println("Store:"+itemStack_l.getDisplayName());
//                        }
//                    }else {
//                        for (Map.Entry<String,List<ItemStack>> entry : cap_list.getAllMap().entrySet()){
//                            System.out.println(entry.getKey());
//                            List<ItemStack> list = entry.getValue();
//                            for (ItemStack stack:list){
//                                entity.drop(stack.copy(),true);
//                                if (world.isClientSide) {
//                                    System.out.println(stack.getItem().getRegistryName().toString());
//                                }
//                            }
//                        }
//                    }
//
//            });
        return ActionResult.sidedSuccess(itemStack, world.isClientSide());
    }

    public CapabilityItemList getItemListCapability(ItemStack itemStack){
        ICapabilityItemList cap_list = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY).orElse(null);
        if (cap_list == null || !(cap_list instanceof ICapabilityItemList)) {
            return new CapabilityItemList();
        }
        return (CapabilityItemList) cap_list;
    }



    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        return false;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemListProvider();
    }

    private static class ContainerProviderChronicler implements INamedContainerProvider {
        public ContainerProviderChronicler(ItemChronicle itemChronicle, ItemStack itemStackChronicler) {
            this.itemChronicler = itemChronicle;
            this.itemStackChronicler = itemStackChronicler;
        }

        @Override
        public ITextComponent getDisplayName() {
            return itemStackChronicler.getDisplayName();
        }

        @Override
        public ContainerChronicier createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            ContainerChronicier newContainerServerSide =
                    ContainerChronicier.createContainerServerSide(windowID, playerInventory,
                            itemChronicler.getItemListCapability(itemStackChronicler),
                            itemStackChronicler);
            return newContainerServerSide;
        }

        private ItemChronicle itemChronicler;
        private ItemStack itemStackChronicler;
    }

}

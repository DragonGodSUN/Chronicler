package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemListProvider;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import javax.annotation.Nullable;

public class ItemChronicler extends Item {
    public ItemChronicler() {
        super(new Properties().tab(ItemGroup.TAB_TOOLS).fireResistant().stacksTo(1));
    }
    private final String BASE_NBT_TAG = "base";
    private final String CAPABILITY_NBT_TAG = "cap";

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        ItemStack itemStack = entity.getItemInHand(hand);
        ItemStack itemStack_l = entity.getOffhandItem();
        LazyOptional<ICapabilityItemList> capability = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY);
        capability.ifPresent((cap_list)->{
            if (entity.isShiftKeyDown()) {
                StoreHelper.addItemStack(cap_list,itemStack_l);
                if (world.isClientSide) {
                    System.out.println("Store:"+itemStack_l.getDisplayName());
                }
            }else {
                if (!world.isClientSide && itemStack.getItem() instanceof ItemChronicler) {
//                    CompoundNBT nbt = getShareTagSnapShot(itemStack);
//                    readShareTagSnapShot(itemStack,nbt);
                    INamedContainerProvider containerProvider = new ContainerProviderChronicler(this, itemStack);
                    NetworkHooks.openGui((ServerPlayerEntity) entity,
                            containerProvider,
                            (packetBuffer)->{
                                ListNBT listNBT = getListNBT(itemStack);
                                packetBuffer.writeItemStack(itemStack,true);
                                packetBuffer.writeInt(listNBT.size());
                                System.out.println(listNBT);
                                for (int i=0;i<listNBT.size();i++){
                                    CompoundNBT nbt = listNBT.getCompound(i);
                                    if (nbt !=null) {
                                        packetBuffer.writeNbt(nbt);
                                    }
                                }

                            });
                }
            }
        });
        return ActionResult.success(itemStack);
    }

    public ListNBT getListNBT(ItemStack stack) {
        CapabilityItemList itemListCapability = getItemListCapability(stack);
        return itemListCapability.serializeNBT();
    }

    public void readListNBT(ItemStack stack, @Nullable ListNBT nbt) {
        if (nbt != null) {
            CapabilityItemList capabilityItemList = getItemListCapability(stack);
            capabilityItemList.deserializeNBT(nbt);
        }
    }


    public CapabilityItemList getItemListCapability(ItemStack itemStack){
        ICapabilityItemList cap_list = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY).orElse(null);
        if (cap_list == null || !(cap_list instanceof ICapabilityItemList)) {
            return new CapabilityItemList();
        }
        return (CapabilityItemList) cap_list;
    }


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
            ContainerChronicier newContainerServerSide =
                    ContainerChronicier.createContainerServerSide(windowID, playerInventory,
                            itemChronicler.getItemListCapability(itemStackChronicler),
                            itemStackChronicler);
            if (newContainerServerSide!= null){
            System.out.println("creatmunu___________"+newContainerServerSide.slots);
            }
            return newContainerServerSide;
        }

        private ItemChronicler itemChronicler;
        private ItemStack itemStackChronicler;
    }

}

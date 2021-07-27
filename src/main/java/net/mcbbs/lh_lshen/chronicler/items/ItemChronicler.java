package net.mcbbs.lh_lshen.chronicler.items;

import net.mcbbs.lh_lshen.chronicler.capabilities.ModCapability;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityInscription;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityItemList;
import net.mcbbs.lh_lshen.chronicler.capabilities.impl.CapabilityStellarisEnergy;
import net.mcbbs.lh_lshen.chronicler.capabilities.provider.ItemChroniclerProvider;
import net.mcbbs.lh_lshen.chronicler.helper.DataHelper;
import net.mcbbs.lh_lshen.chronicler.helper.StoreHelper;
import net.mcbbs.lh_lshen.chronicler.inscription.IInscription;
import net.mcbbs.lh_lshen.chronicler.inscription.InscriptionRegister;
import net.mcbbs.lh_lshen.chronicler.inventory.ContainerChronicler;
import net.mcbbs.lh_lshen.chronicler.network.ChroniclerNetwork;
import net.mcbbs.lh_lshen.chronicler.network.packages.syn_data.SynItemNBTInHandMessage;
import net.mcbbs.lh_lshen.chronicler.tabs.ModGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemChronicler extends Item {

    public ItemChronicler() {
        super(new Properties().tab(ModGroup.GROUP_CHRONICLER).fireResistant().stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int tick, boolean flag) {
        if (entity instanceof PlayerEntity) {
            if (!world.isClientSide()) {
                ICapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(itemStack);
                energy.charge(1);
                ICapabilityInscription inscription = DataHelper.getInscriptionCapability(itemStack);
                IInscription iInscription = InscriptionRegister.getInscription(inscription.getInscription());
                if (iInscription!=null){
                    iInscription.tickEffect(entity,itemStack);
                }
                DataHelper.synChroniclerCaps(itemStack, (PlayerEntity) entity);
            }
        }
        super.inventoryTick(itemStack, world, entity, tick, flag);

    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (getId(oldStack).equals(getId(newStack))){
            return false;
        }
        return super.shouldCauseReequipAnimation(oldStack,newStack,slotChanged);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getMainHandItem();
        ItemStack itemStack_l = playerEntity.getOffhandItem();
        if (itemStack.getItem() instanceof ItemChronicler) {
        LazyOptional<ICapabilityItemList> capability = itemStack.getCapability(ModCapability.ITEMLIST_CAPABILITY);
        capability.ifPresent((cap_list)->{
            if (playerEntity.isShiftKeyDown()) {
                loadPage(itemStack_l, world, playerEntity,cap_list);
                loadInscription(itemStack_l,itemStack,world,playerEntity);
            }else {
                if (!world.isClientSide ()) {
                    if (!isOpen(itemStack,playerEntity)){
                        String id = UUID.randomUUID().toString();
                        setId(itemStack,id);
                    }
                    ChroniclerNetwork.sendToClientPlayer(new SynItemNBTInHandMessage(itemStack),playerEntity);
                    openGUI(itemStack, (ServerPlayerEntity) playerEntity);
                }

            playerEntity.playSound(SoundEvents.BOOK_PAGE_TURN,2f,1f);
            }
        });
            return ActionResult.success(itemStack);
        }
        return super.use(world,playerEntity,hand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltips, flag);
        boolean isKeyDown = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
        int type = 0;
        int size = 0;
        ICapabilityItemList list = DataHelper.getItemListCapability(itemStack);
        ICapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(itemStack);
        ICapabilityInscription inscription = DataHelper.getInscriptionCapability(itemStack);
        for (Map.Entry<String,List<ItemStack>> stacks:list.getAllMap().entrySet()){
            type++;
            for (ItemStack stack : stacks.getValue()){
                size++;
            }
        }
        tooltips.add(new StringTextComponent(getId(itemStack)));
        tooltips.add(new TranslationTextComponent("tooltip.chronicler_lh.chronicler.energy",""+TextFormatting.AQUA+energy.getEnergyPoint()));
        if (isKeyDown){
            tooltips.add(new TranslationTextComponent("tooltip.chronicler_lh.chronicler.energy.max",""+TextFormatting.AQUA+energy.getEnergyMax()));
        }
        if (!inscription.getInscription().isEmpty()) {
            tooltips.add(new StringTextComponent(I18n.get("tooltip.chronicler_lh.chronicler.inscription")
                    + I18n.get(ItemInscription.getKey(inscription.getInscription()))));
            if (isKeyDown){
                tooltips.add(new TranslationTextComponent("tooltip.chronicler_lh.chronicler.inscription.level",inscription.getLevel()));
            }
        }
        if (!isKeyDown){
            tooltips.add(new TranslationTextComponent("tooltip.chronicler_lh.chronicler.info"));
        }
        if (isKeyDown) {
            tooltips.add(new TranslationTextComponent("tooltip.chronicler_lh.chronicler.list.type", type));
            tooltips.add(new TranslationTextComponent("tooltip.chronicler_lh.chronicler.list.size", size));
        }
    }

    private void openGUI(ItemStack chronicler, ServerPlayerEntity serverPlayerEntity){
        INamedContainerProvider containerProvider = new ContainerProviderChronicler(chronicler);
        NetworkHooks.openGui(serverPlayerEntity,
                containerProvider,
//               发送物品列表信息到容器中
                (packetBuffer)->{
                    packetBuffer.writeUtf(getId(chronicler));
                    packetBuffer.writeItemStack(chronicler,false);

                    ListNBT listNBT = getListNBT(chronicler);
                    packetBuffer.writeInt(listNBT.size());
                    for (int i=0;i<listNBT.size();i++){
                        CompoundNBT nbt = listNBT.getCompound(i);
                        if (nbt !=null) {
                            packetBuffer.writeNbt(nbt);
                        }
                    }

                    packetBuffer.writeNbt(DataHelper.getStellarisEnergyCapability(chronicler).serializeNBT());
                    packetBuffer.writeNbt(DataHelper.getInscriptionCapability(chronicler).serializeNBT());
                });
    }

    private void loadInscription(ItemStack off, ItemStack chronicler, World world, PlayerEntity playerEntity){
        if (off.getItem() instanceof ItemInscription){
            ICapabilityInscription inscription = DataHelper.getInscriptionCapability(chronicler);
            ICapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(chronicler);
            String id = ItemInscription.getInscription(off);
            int level = ItemInscription.getLevel(off);
            if (!id.isEmpty()){
                if (!inscription.getInscription().isEmpty() && !playerEntity.level.isClientSide()){
                    ItemStack stack_new = ItemInscription.getSubStack(inscription.getInscription());
                    if (playerEntity.inventory.getFreeSlot()!=-1) {
                        playerEntity.inventory.add(stack_new.copy());
                    }else {
                        playerEntity.drop(stack_new.copy(),true);
                    }
                }
                energy.resetMax();
                inscription.setInscription(id);
                inscription.setLevel(level);
                off.shrink(1);
                if (world.isClientSide ) {
                    playerEntity.sendMessage(new TranslationTextComponent("message.chronicler_lh.chronicler.inscription.load", I18n.get(ItemInscription.getKey(id))), UUID.randomUUID());
                    playerEntity.playSound(SoundEvents.GRINDSTONE_USE,1f,1f);
                }
            }
        }
    }

    private void loadPage(ItemStack off, World world, PlayerEntity playerEntity, ICapabilityItemList cap_list){
        if (off.getItem() instanceof ItemRecordPage){
            ItemStack storeItem = ((ItemRecordPage) off.getItem()).getStoreItem(off);
            if (!storeItem.isEmpty()){
                if (!StoreHelper.hasItemStack(cap_list,storeItem)) {
                    StoreHelper.addItemStack(cap_list,storeItem);
                    if (!world.isClientSide) {
                        off.shrink(1);
                    }
                    if (world.isClientSide ) {
                    playerEntity.sendMessage(new TranslationTextComponent("message.chronicler_lh.chronicler.item_list.store",storeItem.getDisplayName().getString()), UUID.randomUUID());
                    playerEntity.playSound(SoundEvents.BOOK_PAGE_TURN,1f,1f);
                    }
                }
            }
        }
    }

    public static ListNBT getListNBT(ItemStack stack) {
        CapabilityItemList itemListCapability = DataHelper.getItemListCapability(stack);
        return itemListCapability.serializeNBT();
    }

    public static boolean isOpen(ItemStack itemStack, PlayerEntity player){
        if (player!=null&&itemStack!=null) {
            return !getId(itemStack).isEmpty() && isUnique(itemStack, player);
        }
        return false;
    }

    public static void setId(ItemStack stack, String id){
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        compoundNBT.putString("ID",id);
    }

    public static String getId(ItemStack stack){
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        return compoundNBT.getString("ID");
    }

    private static boolean isUnique(ItemStack stack, PlayerEntity playerEntity){
        for (int i=0;i<playerEntity.inventory.getContainerSize();i++){
            ItemStack inv_stack = playerEntity.inventory.getItem(i);
            if (inv_stack.getItem() instanceof ItemChronicler){
                if (!inv_stack.equals(stack)
                    &&getId(inv_stack).equals(getId(stack))){
                    return false;
                }
            }
        }
        return true;
    }

    public static void putCapsTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getOrCreateTag();

        CapabilityItemList cap_list = DataHelper.getItemListCapability(stack);
        CapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(stack);
        CapabilityInscription inscription = DataHelper.getInscriptionCapability(stack);

        ListNBT nbt_list = cap_list.serializeNBT();
        CompoundNBT nbt_energy = energy.serializeNBT();
        CompoundNBT nbt_inscription = inscription.serializeNBT();

        int size = nbt_list.size();
        baseTag.putInt("cap_list:size",size);
        for (int i=0;i<size;i++){
            CompoundNBT e = nbt_list.getCompound(i);
            baseTag.put("cap_list:"+i,e);
        }
        baseTag.put("energy",nbt_energy);
        baseTag.put("inscription",nbt_inscription);

        stack.setTag(baseTag);
    }

    public static void readCapsTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        stack.setTag(nbt);
        CapabilityItemList cap_list = DataHelper.getItemListCapability(stack);
        CapabilityStellarisEnergy energy = DataHelper.getStellarisEnergyCapability(stack);
        CapabilityInscription inscription = DataHelper.getInscriptionCapability(stack);

        ListNBT nbt_list = new ListNBT();
        int size = nbt.getInt("cap_list:size");
        for (int i = 0;i<size;i++){
            CompoundNBT eNBT = nbt.getCompound("cap_list:"+i);
            nbt_list.add(eNBT);
        }
        cap_list.deserializeNBT(nbt_list);

        CompoundNBT nbt_energy = nbt.getCompound("energy");
        energy.deserializeNBT(nbt_energy);

        CompoundNBT nbt_inscription = nbt.getCompound("inscription");
        inscription.deserializeNBT(nbt_inscription);
    }


//    @Nullable
//    @Override
//    public CompoundNBT getShareTag(ItemStack stack) {
//        CompoundNBT nbt = new CompoundNBT();
//        putCapsTag(stack);
//        if (stack.getTag()!=null) {
//            nbt.put("nbtTags",stack.getTag());
//        }
//        return nbt;
//    }
//
    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        readCapsTag(stack,nbt);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        ItemChroniclerProvider provider = new ItemChroniclerProvider();
//        if (nbt!=null) {
//            provider.deserializeNBT(nbt);
//        }
            return provider;
    }

    private static class ContainerProviderChronicler implements INamedContainerProvider {
        public ContainerProviderChronicler(ItemStack itemStackChronicler) {
            this.itemStackChronicler = itemStackChronicler;
        }

        @Override
        public ITextComponent getDisplayName() {
            return itemStackChronicler.getDisplayName();
        }

        @Override
        public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            ContainerChronicler newContainerServerSide =
                    ContainerChronicler.createContainerServerSide(windowID,
                            ItemChronicler.getId(itemStackChronicler),
                            playerInventory,
                            DataHelper.getItemListCapability(itemStackChronicler),
                            DataHelper.getStellarisEnergyCapability(itemStackChronicler),
                            DataHelper.getInscriptionCapability(itemStackChronicler),
                            itemStackChronicler);
            return newContainerServerSide;
        }

        private ItemStack itemStackChronicler;
    }

}

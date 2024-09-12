package wanion.avaritiaddons.network;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import wanion.avaritiaddons.Avaritiaddons;

public class InfinityChestClick implements IMessage {

    private int windowId;
    private int slotNumber;
    private int mouseButton;
    private int modifier;
    private ItemStack itemStack;
    private int stackSize;
    private short transactionID;

    public InfinityChestClick() {}

    public InfinityChestClick(final int windowId, final int slotNumber, final int mouseButton, final int modifier,
            final ItemStack itemStack, final short transactionID) {
        this.windowId = windowId;
        this.slotNumber = slotNumber;
        this.mouseButton = mouseButton;
        this.modifier = modifier;
        stackSize = (this.itemStack = itemStack) != null ? itemStack.stackSize : 0;
        this.transactionID = transactionID;
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        windowId = ByteBufUtils.readVarInt(buf, 4);
        slotNumber = ByteBufUtils.readVarShort(buf);
        if (slotNumber < 0 || slotNumber > 279) slotNumber = -999;
        mouseButton = ByteBufUtils.readVarInt(buf, 4);
        modifier = ByteBufUtils.readVarInt(buf, 4);
        itemStack = ByteBufUtils.readItemStack(buf);
        stackSize = ByteBufUtils.readVarInt(buf, 5);
        if (itemStack != null) itemStack.stackSize = stackSize;
        transactionID = (short) ByteBufUtils.readVarShort(buf);
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, windowId, 4);
        ByteBufUtils.writeVarShort(buf, slotNumber);
        ByteBufUtils.writeVarInt(buf, mouseButton, 4);
        ByteBufUtils.writeVarInt(buf, modifier, 4);
        ByteBufUtils.writeItemStack(buf, itemStack);
        ByteBufUtils.writeVarInt(buf, stackSize, 5);
        ByteBufUtils.writeVarShort(buf, transactionID);
    }

    public static class Handler implements IMessageHandler<InfinityChestClick, IMessage> {

        @Override
        public IMessage onMessage(final InfinityChestClick message, final MessageContext ctx) {
            final EntityPlayerMP entityPlayer = ctx.getServerHandler().playerEntity;
            entityPlayer.func_143004_u();
            if (entityPlayer.openContainer == null) return null;
            if (entityPlayer.openContainer.windowId == message.windowId
                    && entityPlayer.openContainer.isPlayerNotUsingContainer(entityPlayer)) {
                final ItemStack itemstack = entityPlayer.openContainer
                        .slotClick(message.slotNumber, message.mouseButton, message.modifier, entityPlayer);

                if (ItemStack.areItemStacksEqual(message.itemStack, itemstack)) {
                    Avaritiaddons.networkWrapper.sendTo(
                            new InfinityChestConfirmation(message.windowId, message.transactionID, true),
                            entityPlayer);
                    entityPlayer.isChangingQuantityOnly = true;
                    entityPlayer.openContainer.detectAndSendChanges();
                    entityPlayer.updateHeldItem();
                    entityPlayer.isChangingQuantityOnly = false;
                } else {
                    InfinityChestConfirmation.Handler.transactionMap
                            .put(entityPlayer.openContainer.windowId, message.transactionID);
                    Avaritiaddons.networkWrapper.sendTo(
                            new InfinityChestConfirmation(message.windowId, message.transactionID, false),
                            entityPlayer);
                    entityPlayer.openContainer.setPlayerIsPresent(entityPlayer, false);

                    final ItemStack[] itemStacks = new ItemStack[entityPlayer.openContainer.inventorySlots.size()];
                    for (int i = 0; i < entityPlayer.openContainer.inventorySlots.size(); ++i)
                        itemStacks[i] = (((Slot) entityPlayer.openContainer.inventorySlots.get(i)).getStack());
                    Avaritiaddons.networkWrapper.sendTo(new InfinityChestSyncAllSlots(itemStacks), entityPlayer);
                }
            }
            return null;
        }
    }
}

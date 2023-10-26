package wanion.avaritiaddons.network;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.block.chest.infinity.ContainerInfinityChest;

public class InfinityChestSyncAllSlots implements IMessage {

    private short slotCount;
    private ItemStack[] itemStacks;
    private int[] stackSizes;

    public InfinityChestSyncAllSlots() {}

    public InfinityChestSyncAllSlots(@Nonnull final ItemStack[] itemStacks) {
        slotCount = (short) (stackSizes = new int[(this.itemStacks = itemStacks).length]).length;
        for (int i = 0; i < slotCount; i++) stackSizes[i] = itemStacks[i] != null ? itemStacks[i].stackSize : 0;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slotCount = (short) ByteBufUtils.readVarShort(buf);
        itemStacks = new ItemStack[slotCount];
        stackSizes = new int[slotCount];
        for (int i = 0; i < slotCount; i++) {
            itemStacks[i] = ByteBufUtils.readItemStack(buf);
            stackSizes[i] = ByteBufUtils.readVarInt(buf, 5);
            if (itemStacks[i] != null) itemStacks[i].stackSize = stackSizes[i];
        }
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeVarShort(buf, slotCount);
        for (int i = 0; i < slotCount; i++) {
            ByteBufUtils.writeItemStack(buf, itemStacks[i]);
            ByteBufUtils.writeVarInt(buf, stackSizes[i], 5);
        }
    }

    public static class Handler implements IMessageHandler<InfinityChestSyncAllSlots, IMessage> {

        @Override
        public IMessage onMessage(final InfinityChestSyncAllSlots message, final MessageContext ctx) {
            final EntityPlayer entityPlayer = Avaritiaddons.proxy.getEntityPlayerFromContext(ctx);
            if (entityPlayer.openContainer instanceof ContainerInfinityChest)
                for (int i = 0; i < message.slotCount; i++)
                    ((Slot) (entityPlayer.openContainer.inventorySlots.get(i))).putStack(message.itemStacks[i]);
            return null;
        }
    }
}

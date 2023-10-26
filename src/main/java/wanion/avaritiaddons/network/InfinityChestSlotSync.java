package wanion.avaritiaddons.network;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.block.chest.infinity.ContainerInfinityChest;

public final class InfinityChestSlotSync implements IMessage {

    private ItemStack itemStack;
    private int slot;
    private int stackSize;

    public InfinityChestSlotSync() {}

    public InfinityChestSlotSync(final ItemStack itemStack, final int slot) {
        if (itemStack != null) this.stackSize = (this.itemStack = itemStack).stackSize;
        else this.itemStack = null;
        this.slot = slot;
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        itemStack = ByteBufUtils.readItemStack(buf);
        slot = ByteBufUtils.readVarShort(buf);
        stackSize = ByteBufUtils.readVarInt(buf, 5);
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, itemStack);
        ByteBufUtils.writeVarShort(buf, slot);
        ByteBufUtils.writeVarInt(buf, stackSize, 5);
    }

    public static class Handler implements IMessageHandler<InfinityChestSlotSync, IMessage> {

        @Override
        public IMessage onMessage(final InfinityChestSlotSync message, final MessageContext ctx) {
            final EntityPlayer entityPlayer = Avaritiaddons.proxy.getEntityPlayerFromContext(ctx);
            if (entityPlayer.openContainer instanceof ContainerInfinityChest)
                ((ContainerInfinityChest) entityPlayer.openContainer)
                        .syncData(message.itemStack, message.slot, message.stackSize);
            return null;
        }
    }
}

package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import wanion.avaritiaddons.common.slot.FakeSlot;
import wanion.avaritiaddons.common.slot.SpecialSlot;

public class ContainerExtremeAutoCrafter extends Container {

    private final TileEntityExtremeAutoCrafter tileEntityExtremeAutoCrafter;

    public ContainerExtremeAutoCrafter(@Nonnull final TileEntityExtremeAutoCrafter tileEntityExtremeAutoCrafter,
            final InventoryPlayer inventoryPlayer) {
        this.tileEntityExtremeAutoCrafter = tileEntityExtremeAutoCrafter;
        for (int y = 0; y < 9; y++) for (int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(tileEntityExtremeAutoCrafter, y * 9 + x, 8 + (18 * x), 18 + (18 * y)));
        for (int y = 0; y < 9; y++) for (int x = 0; x < 9; x++) addSlotToContainer(
                new FakeSlot(tileEntityExtremeAutoCrafter, 81 + y * 9 + x, 184 + (18 * x), 18 + (18 * y)));
        addSlotToContainer(new SpecialSlot(tileEntityExtremeAutoCrafter, 162, 256, 188));
        for (int y = 0; y < 3; y++) for (int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(inventoryPlayer, 9 + y * 9 + x, 8 + (18 * x), 194 + (18 * y)));
        for (int i = 0; i < 9; i++) addSlotToContainer(new Slot(inventoryPlayer, i, 8 + (18 * i), 252));
    }

    @Override
    public final ItemStack transferStackInSlot(final EntityPlayer entityPlayer, final int slot) {
        ItemStack itemstack = null;
        final Slot actualSlot = (Slot) this.inventorySlots.get(slot);
        if (actualSlot != null && actualSlot.getHasStack()) {
            ItemStack itemstack1 = actualSlot.getStack();
            itemstack = itemstack1.copy();
            if (slot > 162) {
                if (!mergeItemStack(itemstack1, 0, 81, false)) return null;
            } else if (slot < 81) {
                if (!mergeItemStack(itemstack1, 163, 199, true)) return null;
            } else if (slot == 162) if (!mergeItemStack(itemstack1, 163, 199, true)) return null;
            if (itemstack1.stackSize == 0) actualSlot.putStack(null);
            actualSlot.onSlotChanged();
        }
        return itemstack;
    }

    @Override
    public boolean canDragIntoSlot(final Slot slot) {
        return slot.slotNumber < 81 || slot.slotNumber > 162;
    }

    @Override
    public ItemStack slotClick(final int slot, final int mouseButton, final int modifier,
            final EntityPlayer entityPlayer) {
        if (slot > 80 && slot < 162) {
            if (modifier == 2) return null;
            final Slot actualSlot = (Slot) inventorySlots.get(slot);
            final boolean slotHasStack = actualSlot.getHasStack();
            final ItemStack playerStack = entityPlayer.inventory.getItemStack();
            if (slotHasStack && playerStack == null) {
                actualSlot.putStack(null);
                return null;
            } else if (playerStack != null) {
                final ItemStack slotStack = playerStack.copy();
                slotStack.stackSize = 0;
                actualSlot.putStack(slotStack);
                return slotStack;
            }
            return null;
        } else return super.slotClick(slot, mouseButton, modifier, entityPlayer);
    }

    @Override
    public boolean canInteractWith(final EntityPlayer entityPlayer) {
        return tileEntityExtremeAutoCrafter.isUseableByPlayer(entityPlayer);
    }
}

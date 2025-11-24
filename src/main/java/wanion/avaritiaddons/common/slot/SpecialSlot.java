package wanion.avaritiaddons.common.slot;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SpecialSlot extends Slot {

    public SpecialSlot(final IInventory inventory, final int id, final int x, final int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(final ItemStack itemStack) {
        return false;
    }

    @Override
    public void putStack(final ItemStack itemStack) {
        super.putStack(itemStack);
    }

    @Override
    public boolean canTakeStack(final EntityPlayer entityPlayer) {
        final ItemStack slotStack = getStack();
        final ItemStack playerStack = entityPlayer.inventory.getItemStack();
        return slotStack != null && slotStack.stackSize > 0
                && (playerStack == null || ItemStack.areItemStacksEqual(slotStack, playerStack));
    }
}

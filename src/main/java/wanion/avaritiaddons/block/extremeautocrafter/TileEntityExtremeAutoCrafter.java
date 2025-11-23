package wanion.avaritiaddons.block.extremeautocrafter;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import wanion.lib.common.MetaItem;

public class TileEntityExtremeAutoCrafter extends TileEntity implements ISidedInventory {

    private final ItemStack[] itemStacks = new ItemStack[163];
    private int outputStackSize = 0;
    private final InventoryCrafting craftingMatrix = new ExtremeCraftingMatrix();
    private static final int[] slotsForAllSides = new int[82];
    private boolean recipeChanged = true;
    private TIntIntMap patternMap = null;
    private int coolDownTime = 10;

    static {
        for (int i = 0; i < 81; i++) slotsForAllSides[i] = i;
        slotsForAllSides[81] = 162;
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;
        if (--coolDownTime != 0) return;
        else coolDownTime = 10;
        if (recipeChanged) {
            recipeChanged = false;
            final ItemStack output = ExtremeCraftingManager.getInstance().findMatchingRecipe(craftingMatrix, worldObj);
            final ItemStack slotStack = getStackInSlot(162);
            if (output != null && slotStack != null && ItemStack.areItemStacksEqual(output, slotStack)) {
                patternMap = MetaItem.getKeySizeMap(81, 162, itemStacks);
            } else if (slotStack != null && slotStack.stackSize > 0) {
                patternMap = null;
            } else if (output == null && slotStack != null && slotStack.stackSize == 0) {
                itemStacks[162] = null;
                patternMap = null;
                markDirty();
            } else if (output != null) {
                outputStackSize = output.stackSize;
                output.stackSize = 0;
                itemStacks[162] = output;
                patternMap = MetaItem.getKeySizeMap(81, 162, itemStacks);
                markDirty();
            }
        }
        if (patternMap != null && itemStacks[162] == null) {
            final ItemStack output = ExtremeCraftingManager.getInstance().findMatchingRecipe(craftingMatrix, worldObj);
            if (output == null) {
                patternMap = null;
                return;
            }
            outputStackSize = output.stackSize;
            output.stackSize = 0;
            itemStacks[162] = output;
        }
        if (patternMap == null) return;
        final ItemStack outputStack = itemStacks[162];
        if (outputStack == null || outputStack.stackSize + outputStackSize > outputStack.getMaxStackSize()
                || outputStackSize == 0)
            return;
        int maxAmount = matches(MetaItem.getSmartKeySizeMap(0, 81, itemStacks), patternMap);
        maxAmount = Math.min(maxAmount, (outputStack.getMaxStackSize() - outputStack.stackSize) / outputStackSize);
        if (maxAmount == 0) return;
        cleanInput(maxAmount);
        outputStack.stackSize += maxAmount * outputStackSize;
        markDirty();
    }

    private int matches(@Nonnull final TIntIntMap inputMap, @Nonnull final TIntIntMap patternMap) {
        if (inputMap.size() < patternMap.size() || !inputMap.keySet().containsAll(patternMap.keySet())) return 0;

        int amount = Integer.MAX_VALUE;
        for (final int key : patternMap.keys()) {
            amount = Math.min(amount, inputMap.get(key) / patternMap.get(key));
            if (amount <= 0) return 0;
        }
        return amount;
    }

    private void cleanInput(int maxAmount) {
        final TIntIntMap patternMap = new TIntIntHashMap(this.patternMap);
        patternMap.transformValues((v) -> v * maxAmount);
        for (int i = 0; i < 81 && !patternMap.isEmpty(); i++) {
            final ItemStack itemStack = itemStacks[i];
            final int key = MetaItem.get(itemStack);
            if (patternMap.containsKey(key)) {
                final int total = patternMap.get(key);
                final int dif = MathHelper.clamp_int(total, 1, itemStack.stackSize);
                if (!itemStack.getItem().hasContainerItem(itemStack)) itemStack.stackSize -= dif;
                if (dif - total == 0) patternMap.remove(key);
                else patternMap.put(key, total - dif);
                if (itemStack.stackSize == 0) itemStacks[i] = null;
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return 163;
    }

    @Override
    public ItemStack getStackInSlot(final int slot) {
        return itemStacks[slot];
    }

    @Override
    public ItemStack decrStackSize(final int slot, final int howMuch) {
        final ItemStack slotStack = itemStacks[slot];
        if (slotStack != null) {
            final int quantity = MathHelper
                    .clamp_int(MathHelper.clamp_int(howMuch, 1, slotStack.getMaxStackSize()), 1, slotStack.stackSize);
            final ItemStack newStack = slotStack.copy();
            newStack.stackSize = quantity;
            if ((slotStack.stackSize -= quantity) == 0) itemStacks[slot] = null;
            markDirty();
            return newStack;
        } else return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slot) {
        return itemStacks[slot];
    }

    @Override
    public void setInventorySlotContents(final int slot, final ItemStack itemStack) {
        if (slot > 80 && slot < 162) {
            recipeChanged = true;
        }
        itemStacks[slot] = itemStack;
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public final boolean isUseableByPlayer(final EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer
                .getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D)
                <= 64.0D;
    }

    @Override
    public final Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, nbttagcompound);
    }

    @Override
    public final void onDataPacket(final NetworkManager networkManager, final S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public final void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        readCustomNBT(nbtTagCompound);
    }

    @Override
    public final void writeToNBT(final NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        writeCustomNBT(nbtTagCompound);
    }

    public void readCustomNBT(final NBTTagCompound nbtTagCompound) {
        final NBTTagList nbtTagList = nbtTagCompound.getTagList("Contents", 10);
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            final NBTTagCompound slotCompound = nbtTagList.getCompoundTagAt(i);
            final int slot = slotCompound.getShort("Slot");
            if (slot >= 0 && slot < 163) setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(slotCompound));
        }
    }

    public NBTTagCompound writeCustomNBT(final NBTTagCompound nbtTagCompound) {
        final NBTTagList nbtTagList = new NBTTagList();
        boolean hasItems = false;
        for (int i = 0; i < 162; i++) {
            final ItemStack itemStack = getStackInSlot(i);
            if (itemStack != null) {
                final NBTTagCompound slotCompound = new NBTTagCompound();
                slotCompound.setShort("Slot", (short) i);
                nbtTagList.appendTag(itemStack.writeToNBT(slotCompound));
                hasItems = true;
            }
        }
        final ItemStack output = getStackInSlot(162);
        if (output != null && output.stackSize > 0) {
            final NBTTagCompound slotCompound = new NBTTagCompound();
            slotCompound.setShort("Slot", (short) 162);
            nbtTagList.appendTag(output.writeToNBT(slotCompound));
            hasItems = true;
        }
        if (hasItems) nbtTagCompound.setTag("Contents", nbtTagList);
        if (nbtTagCompound.hasNoTags()) return null;
        return nbtTagCompound;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(final int slot, final ItemStack itemStack) {
        return slot >= 0 && slot < 81;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(final int side) {
        return slotsForAllSides;
    }

    @Override
    public boolean canInsertItem(final int slot, final ItemStack itemStack, final int side) {
        return slot >= 0 && slot < 81;
    }

    @Override
    public boolean canExtractItem(final int slot, final ItemStack itemStack, final int side) {
        return slot == 162 && itemStacks[162] != null && itemStacks[162].stackSize > 0;
    }

    public class ExtremeCraftingMatrix extends InventoryCrafting {

        public ExtremeCraftingMatrix() {
            super(new Container() {

                @Override
                public boolean canInteractWith(final EntityPlayer entityPlayer) {
                    return false;
                }
            }, 9, 9);
        }

        @Override
        public ItemStack getStackInSlot(final int slot) {
            return itemStacks[slot + 81];
        }

        @Override
        public void setInventorySlotContents(final int slot, final ItemStack itemStack) {
            recipeChanged = true;
            itemStacks[slot + 81] = itemStack;
            markDirty();
        }

        @Override
        public ItemStack decrStackSize(int slot, final int howMuch) {
            if (itemStacks[slot += 81] == null) return null;
            final ItemStack itemStack = itemStacks[slot].copy();
            if ((itemStacks[slot].stackSize -= howMuch) == 0) itemStacks[slot] = null;
            itemStack.stackSize = howMuch;
            markDirty();
            return itemStack;
        }
    }
}

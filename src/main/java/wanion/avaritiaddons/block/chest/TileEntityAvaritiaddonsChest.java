package wanion.avaritiaddons.block.chest;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TileEntityAvaritiaddonsChest extends TileEntity implements IInventory {

    protected final InventoryAvaritiaddonsChest inventoryAvaritiaddonsChest;

    private int ticksSinceSync;
    private int facingSide = 0;

    float lidAngle;
    float prevLidAngle;
    private int numPlayersUsing;

    public TileEntityAvaritiaddonsChest(final int maxStackSize) {
        inventoryAvaritiaddonsChest = new InventoryAvaritiaddonsChest(this, maxStackSize);
    }

    @Override
    public final boolean isUseableByPlayer(final EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer
                .getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D)
                <= 64.0D;
    }

    @Override
    public final void openInventory() {
        if (worldObj == null) return;
        if (numPlayersUsing < 0) numPlayersUsing = 0;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, ++numPlayersUsing);
    }

    @Override
    public final void closeInventory() {
        if (worldObj != null) worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, --numPlayersUsing);
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
    public final boolean receiveClientEvent(final int argument, final int value) {
        if (argument == 1) {
            this.numPlayersUsing = value;
            return true;
        } else {
            return super.receiveClientEvent(argument, value);
        }
    }

    @Override
    public final void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        facingSide = nbtTagCompound.getInteger("FacingSide");
        readCustomNBT(nbtTagCompound);
    }

    @Override
    public final void writeToNBT(final NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("FacingSide", facingSide);
        writeCustomNBT(nbtTagCompound);
    }

    public void readCustomNBT(final NBTTagCompound nbtTagCompound) {
        final NBTTagList nbtTagList = nbtTagCompound.getTagList("Contents", 10);
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            final NBTTagCompound slotCompound = nbtTagList.getCompoundTagAt(i);
            final int slot = slotCompound.getShort("Slot");
            if (slot >= 0 && slot < 243) inventoryAvaritiaddonsChest
                    .setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(slotCompound));
        }
    }

    public NBTTagCompound writeCustomNBT(final NBTTagCompound nbtTagCompound) {
        final NBTTagList nbtTagList = new NBTTagList();
        boolean hasItems = false;
        for (int i = 0; i < inventoryAvaritiaddonsChest.getSizeInventory(); i++) {
            final ItemStack itemStack = inventoryAvaritiaddonsChest.getStackInSlot(i);
            if (itemStack != null) {
                final NBTTagCompound slotCompound = new NBTTagCompound();
                slotCompound.setShort("Slot", (short) i);
                nbtTagList.appendTag(itemStack.writeToNBT(slotCompound));
                hasItems = true;
            }
        }
        if (hasItems) nbtTagCompound.setTag("Contents", nbtTagList);
        if (nbtTagCompound.hasNoTags()) return null;
        return nbtTagCompound;
    }

    @Override
    public int getSizeInventory() {
        return 243;
    }

    @Override
    public final void updateEntity() {
        ++ticksSinceSync;
        float f;

        for (int i = 0; i < 243; i++) {
            if (inventoryAvaritiaddonsChest.contents[i] != null
                    && inventoryAvaritiaddonsChest.contents[i].stackSize <= 0) {
                inventoryAvaritiaddonsChest.contents[i] = null;
            }
        }

        if (!worldObj.isRemote && numPlayersUsing != 0 && (ticksSinceSync + xCoord + yCoord + zCoord) % 200 == 0) {
            numPlayersUsing = 0;
            f = 5.0F;
            List list = worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    AxisAlignedBB.getBoundingBox(
                            (float) xCoord - f,
                            (float) yCoord - f,
                            (float) zCoord - f,
                            (float) (xCoord + 1) + f,
                            (float) (yCoord + 1) + f,
                            (float) (zCoord + 1) + f));

            for (Object aList : list) {
                EntityPlayer entityplayer = (EntityPlayer) aList;

                if (entityplayer.openContainer instanceof ContainerAvaritiaddonsChest) {
                    IInventory iinventory = ((ContainerAvaritiaddonsChest) entityplayer.openContainer).tileEntityAvaritiaddonsChest;

                    if (iinventory == this) {
                        ++numPlayersUsing;
                    }
                }
            }
        }

        prevLidAngle = lidAngle;
        f = 0.1F;

        if (numPlayersUsing > 0 && lidAngle == 0.0F) worldObj.playSoundEffect(
                xCoord + 0.5D,
                yCoord + 0.5D,
                zCoord + 0.5D,
                "random.chestopen",
                0.5F,
                worldObj.rand.nextFloat() * 0.1F + 0.9F);

        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
            final float f1 = lidAngle;

            if (numPlayersUsing > 0) lidAngle += f;
            else lidAngle -= f;

            if (lidAngle > 1.0F) lidAngle = 1.0F;

            if (lidAngle < 0.5F && f1 >= 0.5F) worldObj.playSoundEffect(
                    xCoord + 0.5D,
                    yCoord + 0.5D,
                    zCoord + 0.5D,
                    "random.chestclosed",
                    0.5F,
                    worldObj.rand.nextFloat() * 0.1F + 0.9F);

            if (lidAngle < 0.0F) lidAngle = 0.0F;
        }
    }

    @Override
    public ItemStack getStackInSlot(final int slot) {
        return inventoryAvaritiaddonsChest.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(final int slot, final int howMuch) {
        return inventoryAvaritiaddonsChest.decrStackSize(slot, howMuch);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slot) {
        return inventoryAvaritiaddonsChest.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(final int slot, final ItemStack itemStack) {
        inventoryAvaritiaddonsChest.setInventorySlotContents(slot, itemStack);
    }

    @Override
    public int getInventoryStackLimit() {
        return inventoryAvaritiaddonsChest.getInventoryStackLimit();
    }

    @Override
    public boolean isItemValidForSlot(final int slot, final ItemStack itemStack) {
        return inventoryAvaritiaddonsChest.isItemValidForSlot(slot, itemStack);
    }

    @Override
    public final boolean hasCustomInventoryName() {
        return false;
    }

    protected final void setFacingSide(final int facingSide) {
        this.facingSide = facingSide;
    }

    protected final int getFacingSide() {
        return facingSide;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    protected abstract ResourceLocation getTexture();
}

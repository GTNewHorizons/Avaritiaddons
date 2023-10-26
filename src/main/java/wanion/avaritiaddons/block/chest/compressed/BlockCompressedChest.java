package wanion.avaritiaddons.block.chest.compressed;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import wanion.avaritiaddons.Avaritiaddons;
import wanion.avaritiaddons.block.chest.BlockAvaritiaddonsChest;

public final class BlockCompressedChest extends BlockAvaritiaddonsChest {

    public static final BlockCompressedChest instance = new BlockCompressedChest();

    private BlockCompressedChest() {
        super(Material.wood);
        setBlockName("CompressedChest").setHardness(5.0F).setStepSound(soundTypeWood).setHarvestLevel("axe", 2);
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int metadata) {
        return new TileEntityCompressedChest();
    }

    public boolean onBlockActivated(final World world, final int x, final int y, final int z,
            final EntityPlayer entityPlayer, final int side, final float hitX, final float hitY, final float hitZ) {
        if (!world.isRemote) FMLNetworkHandler
                .openGui(entityPlayer, Avaritiaddons.instance, Avaritiaddons.GUI_ID_COMPRESSED_CHEST, world, x, y, z);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iIconRegister) {
        this.blockIcon = iIconRegister.registerIcon("planks_big_oak");
    }
}

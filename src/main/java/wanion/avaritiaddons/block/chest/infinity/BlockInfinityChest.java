package wanion.avaritiaddons.block.chest.infinity;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.block.Block;
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

public final class BlockInfinityChest extends BlockAvaritiaddonsChest {

    public static final BlockInfinityChest instance = new BlockInfinityChest();

    private BlockInfinityChest() {
        super(Material.iron);
        setBlockName("InfinityChest").setStepSound(Block.soundTypeMetal).setHardness(50.0F).setResistance(2000.0F)
                .setLightLevel(1.0F).setHarvestLevel("pickaxe", 3);
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int metadata) {
        return new TileEntityInfinityChest();
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z,
            final EntityPlayer entityPlayer, final int side, final float hitX, final float hitY, final float hitZ) {
        if (!world.isRemote) FMLNetworkHandler
                .openGui(entityPlayer, Avaritiaddons.instance, Avaritiaddons.GUI_ID_INFINITY_CHEST, world, x, y, z);
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister iIconRegister) {
        this.blockIcon = iIconRegister.registerIcon("avaritia:block_infinity");
    }
}

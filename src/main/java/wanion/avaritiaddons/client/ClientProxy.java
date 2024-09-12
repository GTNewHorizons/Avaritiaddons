package wanion.avaritiaddons.client;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import wanion.avaritiaddons.CommonProxy;
import wanion.avaritiaddons.block.chest.RendererAvaritiaddonsChest;
import wanion.avaritiaddons.block.chest.compressed.BlockCompressedChest;
import wanion.avaritiaddons.block.chest.compressed.ItemRendererCompressedChest;
import wanion.avaritiaddons.block.chest.compressed.TileEntityCompressedChest;
import wanion.avaritiaddons.block.chest.infinity.BlockInfinityChest;
import wanion.avaritiaddons.block.chest.infinity.ItemRendererInfinityChest;
import wanion.avaritiaddons.block.chest.infinity.TileEntityInfinityChest;
import wanion.lib.WanionLib;

@SideOnly(Side.CLIENT)
public final class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        ClientRegistry
                .bindTileEntitySpecialRenderer(TileEntityCompressedChest.class, RendererAvaritiaddonsChest.instance);
        MinecraftForgeClient.registerItemRenderer(
                Item.getItemFromBlock(BlockCompressedChest.instance),
                new ItemRendererCompressedChest());
        ClientRegistry
                .bindTileEntitySpecialRenderer(TileEntityInfinityChest.class, RendererAvaritiaddonsChest.instance);
        MinecraftForgeClient.registerItemRenderer(
                Item.getItemFromBlock(BlockInfinityChest.instance),
                new ItemRendererInfinityChest());
        WanionLib.getClientTickHandler().registerAnimation(ClientConstants.INFINITY_CHEST_ANIMATION);
    }

    @Override
    public EntityPlayer getPlayerEntity(final MessageContext context) {
        return context.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(context);
    }

    @Override
    public EntityPlayer getEntityPlayerFromContext(MessageContext ctx) {
        return Minecraft.getMinecraft().thePlayer;
    }
}

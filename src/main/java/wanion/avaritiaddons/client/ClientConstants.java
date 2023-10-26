package wanion.avaritiaddons.client;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import wanion.avaritiaddons.common.Reference;
import wanion.lib.client.animation.ComplexHalfAnimation;

@SideOnly(Side.CLIENT)
public final class ClientConstants {

    private static final ResourceLocation[] INFINITY_CHEST_FRAMES = new ResourceLocation[] {
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/0.png"),
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/1.png"),
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/2.png"),
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/3.png"),
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/4.png"),
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/5.png"),
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/6.png"),
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/7.png"),
            new ResourceLocation(Reference.MOD_ID, "textures/entity/InfinityChest/8.png") };

    private static final int[] INFINITY_CHEST_ANIMATION_STAGES = { 0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 5, 6, 7, 8, 7,
            6, 5, 4, 4, 3, 3, 2, 2, 2, 1, 1, 1 };

    public static final ComplexHalfAnimation INFINITY_CHEST_ANIMATION = new ComplexHalfAnimation(
            INFINITY_CHEST_FRAMES,
            INFINITY_CHEST_ANIMATION_STAGES);

    public static final ResourceLocation COMPRESSED_CHEST_TEXTURE = new ResourceLocation(
            Reference.MOD_ID,
            "textures/entity/compressedChest.png");

    private ClientConstants() {}
}

package wanion.avaritiaddons;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import wanion.avaritiaddons.common.Reference;

public final class Config {

    public static final boolean hardCompressedChestRecipe;
    public static final boolean hardInfinityChestRecipe;
    // public static final boolean hardExtremeAutoCrafterRecipe;

    public static final List<String> thingsToRemoveFromInfinityCatalystRecipe;

    static {
        final Configuration config = new Configuration(
                new File("." + Reference.SLASH + "config" + Reference.SLASH + Reference.MOD_NAME + ".cfg"));
        hardCompressedChestRecipe = config.get(Configuration.CATEGORY_GENERAL, "hardCompressedChestRecipe", false)
                .getBoolean();
        hardInfinityChestRecipe = config.get(Configuration.CATEGORY_GENERAL, "hardInfinityChestRecipe", false)
                .getBoolean();
        // hardExtremeAutoCrafterRecipe = config.get(Configuration.CATEGORY_GENERAL, "hardExtremeAutoCrafterRecipe",
        // false).getBoolean();

        final String infinityCatalyst = "infinityCatalystRecipeTweaks";
        thingsToRemoveFromInfinityCatalystRecipe = Arrays
                .asList(config.get(infinityCatalyst, "thingsToRemove", new String[] {}).getStringList());
        config.setCategoryComment(
                infinityCatalyst,
                "Can be used the item name, or the OreDictionary name.\nFormat:\n\nItems : minecraft:wood#1 = Spruce Wood\nOreDictionary : stoneBlock");

        if (config.hasChanged()) config.save();
    }

    private Config() {}
}

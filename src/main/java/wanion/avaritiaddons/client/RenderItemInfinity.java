package wanion.avaritiaddons.client;

/*
 * Created by WanionCane(https://github.com/WanionCane). This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public final class RenderItemInfinity extends RenderItem {

    public void renderItemOverlayIntoGUI(FontRenderer fontRenderer, TextureManager textureManager, ItemStack itemStack,
            int x, int y, String someString) {
        if (itemStack != null) {
            final String humanReadableValue = itemStack.stackSize > 1 ? humanReadableValue(itemStack.stackSize) : null;
            if (humanReadableValue != null) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glPushMatrix();
                final float scaleFactor = humanReadableValue.length() == 1 || humanReadableValue.length() == 2 ? 0.9f
                        : humanReadableValue.length() == 3 ? 0.8f : 0.6f;
                GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
                final float inverseScaleFactor = 1.0f / scaleFactor;
                fontRenderer.drawStringWithShadow(
                        humanReadableValue,
                        (int) (((float) x + 16.0f * scaleFactor * inverseScaleFactor
                                - fontRenderer.getStringWidth(humanReadableValue) * scaleFactor) * inverseScaleFactor),
                        (int) (((float) y + 16.0f - (8.0f * scaleFactor)) * inverseScaleFactor),
                        0xffffff);
                GL11.glPopMatrix();
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

            if (itemStack.getItem().showDurabilityBar(itemStack)) {
                double health = itemStack.getItem().getDurabilityForDisplay(itemStack);
                int j1 = (int) Math.round(13.0D - health * 13.0D);
                int k = (int) Math.round(255.0D - health * 255.0D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_BLEND);
                Tessellator tessellator = Tessellator.instance;
                int l = 255 - k << 16 | k << 8;
                int i1 = (255 - k) / 4 << 16 | 16128;
                renderQuad(tessellator, x + 2, y + 13, 13, 2, 0);
                renderQuad(tessellator, x + 2, y + 13, 12, 1, i1);
                renderQuad(tessellator, x + 2, y + 13, j1, 1, l);
                // GL11.glEnable(GL11.GL_BLEND); // Forge: Disable Bled because it screws with a lot of things down the
                // line.
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private void renderQuad(Tessellator p_77017_1_, int p_77017_2_, int p_77017_3_, int p_77017_4_, int p_77017_5_,
            int p_77017_6_) {
        p_77017_1_.startDrawingQuads();
        p_77017_1_.setColorOpaque_I(p_77017_6_);
        p_77017_1_.addVertex(p_77017_2_, p_77017_3_, 0.0D);
        p_77017_1_.addVertex(p_77017_2_, p_77017_3_ + p_77017_5_, 0.0D);
        p_77017_1_.addVertex(p_77017_2_ + p_77017_4_, p_77017_3_ + p_77017_5_, 0.0D);
        p_77017_1_.addVertex(p_77017_2_ + p_77017_4_, p_77017_3_, 0.0D);
        p_77017_1_.draw();
    }

    private static String humanReadableValue(final int value) {
        if (value > 0 && value < 1000) return Integer.toString(value);
        else if (value >= 1000 && value < 1000000) return value / 1000 + "K";
        else if (value >= 1000000 && value <= 1000000000) return value / 1000000 + "M";
        else if (value >= 1000000000) return value / 1000000000 + "B";
        else return null;
    }
}

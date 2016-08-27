package de.paxii.clarinet.util.render;

import de.paxii.clarinet.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

public class GuiMethods extends Gui {
	public void drawGradientRectWithOutline(int xs, int ys, int xe, int ye, int c, int c1, int c2) {
		this.drawGradientRect(xs + 1, ys + 1, xe - 1, ye - 1, c1, c2);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		this.drawWHollowBorderedRect(xs * 2 + 1, ys * 2 + 1, xe * 2 - 1,
				ye * 2 - 1, 1, c);
		GL11.glPopMatrix();
	}

	public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(1F);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		drawGradientRect(x, y, x2, y2, col2, col3);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		float f4 = (float) (col2 >> 24 & 0xFF) / 255F;
		float f5 = (float) (col2 >> 16 & 0xFF) / 255F;
		float f6 = (float) (col2 >> 8 & 0xFF) / 255F;
		float f7 = (float) (col2 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
	}

	public static void drawRoundedRect(int x, int y, int x1, int y1, int borderC, int insideC) {
		x *= 2;
		x1 *= 2;
		y *= 2;
		y1 *= 2;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawVerticalLine(x, y + 3, y1 - 4, borderC);
		drawVerticalLine(x1 - 1, y + 3, y1 - 4, borderC);
		drawHorizontalLine(x + 4, x1 - 5, y, borderC);
		drawHorizontalLine(x + 4, x1 - 5, y1 - 1, borderC);
		drawVerticalLine(x + 1, y + 1, y + 4, borderC);
		drawHorizontalLine(x + 3, x + 2, y + 1, borderC);
		drawVerticalLine(x1 - 2, y + 1, y + 4, borderC);
		drawHorizontalLine(x1 - 4, x1 - 3, y + 1, borderC);
		drawVerticalLine(x + 1, y1 - 2, y1 - 5, borderC);
		drawHorizontalLine(x + 3, x + 2, y1 - 2, borderC);
		drawVerticalLine(x1 - 2, y1 - 2, y1 - 5, borderC);
		drawHorizontalLine(x1 - 3, x1 - 4, y1 - 2, borderC);
		drawRect(x + 2, y + 2, x1 - 2, y1 - 2, insideC);
		drawVerticalLine(x + 1, y + 3, y1 - 4, insideC);
		drawVerticalLine(x1 - 2, y + 3, y1 - 4, insideC);
		drawHorizontalLine(x + 4, x1 - 5, y + 1, insideC);
		drawHorizontalLine(x + 4, x1 - 5, y1 - 2, insideC);
		GL11.glScalef(2.0F, 2.0F, 2.0F);
	}

	public void drawGradientRectWithOutline(int xs, int ys, int xe, int ye, int c, int c1, int c2, int c3) {
		this.drawGradientRect(xs + 1, ys + 1, xe - 1, ye - 1, c1, c2);
		drawVerticalLine((xs + 1), (ys), (ye) - 1, c3);
		drawHorizontalLine(xs + 1, xe - 2, ys + 1, c3);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		this.drawWHollowBorderedRect(xs * 2 + 1, ys * 2 + 1, xe * 2 - 1,
				ye * 2 - 1, 1, c);
		GL11.glPopMatrix();
	}

	public void drawSmallRect(int x, int y, int x1, int y1, int CO1) {
		GL11.glPushMatrix();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawRect(x * 2, y * 2, x1 * 2, y1 * 2, CO1);
		GL11.glPopMatrix();
	}

	public void drawSmallWHollowBorderedRect(int x, int y, int x1, int y1, int CO1) {
		GL11.glPushMatrix();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawWHollowBorderedRect(x * 2, y * 2, x1 * 2, y1 * 2, CO1);
		GL11.glPopMatrix();
	}

	public void drawWHollowBorderedRect(int x, int y, int x1, int y1, int CO1) {
		drawVerticalLine(x, y, y1 - 1, CO1);
		drawVerticalLine(x1 - 1, y, y1 - 1, CO1);
		drawHorizontalLine(x + 1, x1 - 2, y, CO1);
		drawHorizontalLine(x + 1, x1 - 2, y1 - 1, CO1);
	}

	public void drawBarMethod(int x, int y, int x1, int y1, int CO1, int CO2, int CO3) {
		drawSmallWBorderedBarRect(x, y, x1, y1, CO1, CO2, CO3);
	}

	public void drawSmallWBorderedBarRect(int x, int y, int x1, int y1, int CO1, int CO2, int CO3) {
		GL11.glPushMatrix();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawWBorderedBarRect(x * 2, y * 2, x1 * 2, y1 * 2, CO1, CO2, CO3);
		GL11.glPopMatrix();
	}

	public void drawWBorderedBarRect(int x, int y, int x1, int y1, int CO1, int CO2, int CO3) {
		drawRect(x + 1, y + 1, x1 - 1, y1 - 1, CO2);
		drawRect(x + 1, y + 1, x1 - 1, y1 - 6, CO3);
		drawVerticalLine(x, y, y1 - 1, CO1);
		drawVerticalLine(x1 - 1, y, y1 - 1, CO1);
		drawHorizontalLine(x + 1, x1 - 2, y, CO1);
		drawHorizontalLine(x + 1, x1 - 2, y1 - 1, CO1);
	}

	public static void drawRect(int x, int y, int x2, int y2, int col1) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public void drawGradientRect2(int x, int y, int x2, int y2, int col1, int col2) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		float f4 = (float) (col2 >> 24 & 0xFF) / 255F;
		float f5 = (float) (col2 >> 16 & 0xFF) / 255F;
		float f6 = (float) (col2 >> 8 & 0xFF) / 255F;
		float f7 = (float) (col2 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
	}

	public void drawRoundedGradientRect(int x, int y, int x1, int y1, int size, int col1, int col2, int borderC) {
		x *= 2;
		y *= 2;
		x1 *= 2;
		y1 *= 2;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawVerticalLine(x, y + 1, y1 - 2, borderC);
		drawVerticalLine(x1 - 1, y + 1, y1 - 2, borderC);
		drawHorizontalLine(x + 2, x1 - 3, y, borderC);
		drawHorizontalLine(x + 2, x1 - 3, y1 - 1, borderC);
		drawHorizontalLine(x + 1, x + 1, y + 1, borderC);
		drawHorizontalLine(x1 - 2, x1 - 2, y + 1, borderC);
		drawHorizontalLine(x1 - 2, x1 - 2, y1 - 2, borderC);
		drawHorizontalLine(x + 1, x + 1, y1 - 2, borderC);
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		float f4 = (float) (col2 >> 24 & 0xFF) / 255F;
		float f5 = (float) (col2 >> 16 & 0xFF) / 255F;
		float f6 = (float) (col2 >> 8 & 0xFF) / 255F;
		float f7 = (float) (col2 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x1, y);
		GL11.glVertex2d(x, y);
		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x, y1);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glScalef(2F, 2F, 2F);
	}

	public static void drawBar(int x, int y, int x2, int y2, float l1, int col1, int col2, int col3, int var, boolean inverse) {
		int height = (y2 - y) / 2;

		if (inverse) {
			drawRect(x, y, x2, y2, col1);
			drawRect(x2 - var * 4 - 2, y, x2, y + height, col2);
			drawRect(x2 - var * 4 - 2, y + height, x2, y2, col3);
		} else {
			drawRect(x, y, x2, y2, col1);
			drawRect(x, y, x + var * 4 + 2, y + height, col2);
			drawRect(x, y + height, x + var * 4 + 2, y2, col3);
		}

		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawBorderedRect(int x, int y, int x2, int y2, float l1, int col1, int col2) {
		drawRect(x, y, x2, y2, col2);
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawStringWithMaxWidth(String s, int x, int y, int c, int maxWidth) {
		GL11.glPushMatrix();
		float scaleFactor = 1;

		for (float f1 = 1; Wrapper.getFontRenderer().getStringWidth(s) * f1 > maxWidth; f1 -= 0.01f) {
			scaleFactor = f1;
		}

		GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
		Wrapper.getFontRenderer().drawString(s, (int) (x * (1 / scaleFactor)),
				(int) (y * (1 / scaleFactor)), c);
		GL11.glPopMatrix();
	}

	public static void drawHollowBorderedRect(int x, int y, int x2, int y2, float l1, int col1) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public void drawGradientBorderedRect(int x, int y, int x2, int y2, float l1, int col1, int col2, int col3) {
		this.drawGradientRect(x, y, x2, y2, col2, col3);
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public void drawWHollowBorderedRect(int x, int y, int x1, int y1, int size, int borderC) {
		Gui.drawVerticalLine(x, y + 1, y1 - 2, borderC);
		Gui.drawVerticalLine(x1 - 1, y + 1, y1 - 2, borderC);
		Gui.drawHorizontalLine(x + 2, x1 - 3, y, borderC);
		Gui.drawHorizontalLine(x + 2, x1 - 3, y1 - 1, borderC);
		Gui.drawHorizontalLine(x + 1, x + 1, y + 1, borderC);
		Gui.drawHorizontalLine(x1 - 2, x1 - 2, y + 1, borderC);
		Gui.drawHorizontalLine(x1 - 2, x1 - 2, y1 - 2, borderC);
		Gui.drawHorizontalLine(x + 1, x + 1, y1 - 2, borderC);
	}

	public static void drawVerticalLine(int x, int y, int y2, float l1, int col1) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawHorizontalLine(int x, int x2, int y, float l1, int col1) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawDiagonalLine(int x, int x2, int y, float l1, int col1) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(y, x2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawCircle(int xx, int yy, int radius, int col) {
		float f = (float) (col >> 24 & 0xFF) / 255F;
		float f1 = (float) (col >> 16 & 0xFF) / 255F;
		float f2 = (float) (col >> 8 & 0xFF) / 255F;
		float f3 = (float) (col & 0xFF) / 255F;
		int sections = 70;
		double dAngle = 2 * Math.PI / sections;
		float x, y;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_LINE_LOOP);

		for (int i = 0; i < sections; i++) {
			x = (float) (radius * Math.cos(i * dAngle));
			y = (float) (radius * Math.sin(i * dAngle));
			GL11.glColor4f(f1, f2, f3, f);
			GL11.glVertex2f(xx + x, yy + y);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}

	public static void drawFilledCircle(int xx, int yy, float radius, int col) {
		float f = (float) (col >> 24 & 0xFF) / 255F;
		float f1 = (float) (col >> 16 & 0xFF) / 255F;
		float f2 = (float) (col >> 8 & 0xFF) / 255F;
		float f3 = (float) (col & 0xFF) / 255F;
		int sections = 50;
		double dAngle = 2 * Math.PI / sections;
		float x, y;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		for (int i = 0; i < sections; i++) {
			x = (float) (radius * Math.sin((i * dAngle)));
			y = (float) (radius * Math.cos((i * dAngle)));
			GL11.glColor4f(f1, f2, f3, f);
			GL11.glVertex2f(xx + x, yy + y);
		}

		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void drawMoreRoundedBorderedRect2(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		x *= 2;
		y *= 2;
		x1 *= 2;
		y1 *= 2;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawVerticalLine(x, y + 1, y1 - 2, borderC);
		drawVerticalLine(x1 - 1, y + 1, y1 - 2, borderC);
		drawHorizontalLine(x + 2, x1 - 3, y, borderC);
		drawHorizontalLine(x + 2, x1 - 3, y1 - 1, borderC);
		drawHorizontalLine(x + 1, x + 1, y + 1, borderC);
		drawHorizontalLine(x1 - 2, x1 - 2, y + 1, borderC);
		drawHorizontalLine(x1 - 2, x1 - 2, y1 - 2, borderC);
		drawHorizontalLine(x + 1, x + 1, y1 - 2, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
		GL11.glScalef(2F, 2F, 2F);
	}

	public static void drawTri(int i, int j, int k) {
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(i, j + 2);
		GL11.glVertex2d(i + 2, j - 2);
		GL11.glVertex2d(i - 2, j - 2);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glRotatef(-180F, 0.0F, 0.0F, 1.0F);
	}

	public static void drawFlagTri(int i, int j, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(i, j + 2);
		GL11.glVertex2d(i, j - 2);
		GL11.glVertex2d(i - 2, j);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawRightTri(int x, int y, int size, int color) {
		float f = (float) (color >> 24 & 0xff) / 255F;
		float f1 = (float) (color >> 16 & 0xff) / 255F;
		float f2 = (float) (color >> 8 & 0xff) / 255F;
		float f3 = (float) (color & 0xff) / 255F;
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x - size, y - size);
		GL11.glVertex2d(x - size, y + size);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glRotatef(-180F, 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}



	public static void drawUpTri(int x, int y, int size, int color) {
		float f = (float) (color >> 24 & 0xff) / 255F;
		float f1 = (float) (color >> 16 & 0xff) / 255F;
		float f2 = (float) (color >> 8 & 0xff) / 255F;
		float f3 = (float) (color & 0xff) / 255F;
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(270F, 0.0F, 0.0F, 1.0F);
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(-size, -size);
		GL11.glVertex2d(-size, size);
		GL11.glEnd();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}



	public static void drawDownTri(int x, int y, int size, int color) {
		float f = (float) (color >> 24 & 0xff) / 255F;
		float f1 = (float) (color >> 16 & 0xff) / 255F;
		float f2 = (float) (color >> 8 & 0xff) / 255F;
		float f3 = (float) (color & 0xff) / 255F;
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(-size, -size);
		GL11.glVertex2d(-size, size);
		GL11.glEnd();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public void color(int c) {
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, f);
	}

	public static void drawDownTri(int i, int j, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(i, j + 2);
		GL11.glVertex2d(i + 2, j - 2);
		GL11.glVertex2d(i - 2, j - 2);
		GL11.glEnd();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawDownLeft(int i, int j, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(i - 2, j + 2);
		GL11.glVertex2d(i + 2, j - 1);
		GL11.glVertex2d(i - 2, j - 2);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawDownRight(int i, int j, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(i + 2, j + 2);
		GL11.glVertex2d(i + 2, j - 2);
		GL11.glVertex2d(i - 2, j - 1);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawUpLeft(int i, int j, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(i - 2, j + 2);
		GL11.glVertex2d(i + 2, j - 1);
		GL11.glVertex2d(i - 2, j - 2);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawUpRight(int i, int j, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(i - 1, j + 2);
		GL11.glVertex2d(i + 1, j - 3);
		GL11.glVertex2d(i - 3, j - 2);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawUpTri(int i, int j, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
//		GL11.glVertex2d(i, j + 2);
//		GL11.glVertex2d(i + 2, j - 2);
//		GL11.glVertex2d(i - 2, j - 2);

		GL11.glVertex2d(i, j);
		GL11.glVertex2d(i + 2, j + 4);
		GL11.glVertex2d(i - 2, j + 4);
		GL11.glEnd();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawTriSight(int i, int j, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(i, j + 2);
		GL11.glVertex2d(i + 4, j - 4);
		GL11.glVertex2d(i - 4, j - 4);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawFullCircle(int i, int j, double d, int k) {
		float f = (float) (k >> 24 & 0xff) / 255F;
		float f1 = (float) (k >> 16 & 0xff) / 255F;
		float f2 = (float) (k >> 8 & 0xff) / 255F;
		float f3 = (float) (k & 0xff) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		for (int l = 0; l <= 360; l++) {
			double d1 = Math.sin(((double) l * Math.PI) / 180D) * d;
			double d2 = Math.cos(((double) l * Math.PI) / 180D) * d;
			GL11.glVertex2d((double) i + d1, (double) j + d2);
		}

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void DrawCircle(float f, float f1, float f2, int i, int j) {
		float f3 = (float) (j >> 24 & 0xff) / 255F;
		float f4 = (float) (j >> 16 & 0xff) / 255F;
		float f5 = (float) (j >> 8 & 0xff) / 255F;
		float f6 = (float) (j & 0xff) / 255F;
		float f7 = (float) ((Math.PI * 2D) / (double) i);
		float f8 = (float) Math.cos(f7);
		float f9 = (float) Math.sin(f7);
		GL11.glColor4f(f4, f5, f6, f3);
		float f11 = f2;
		float f12 = 0.0F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_LINE_LOOP);

		for (int k = 0; k < i; k++) {
			GL11.glVertex2f(f11 + f, f12 + f1);
			float f10 = f11;
			f11 = f8 * f11 - f9 * f12;
			f12 = f9 * f10 + f8 * f12;
		}

		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		drawVerticalLine(x, y, y1 - 1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x, x1 - 1, y, borderC);
		drawHorizontalLine(x, x1 - 1, y1 - 1, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
	}

	public static void drawWBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		drawVerticalLine(x, y, y1 - 1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x + 1, x1 - 2, y, borderC);
		drawHorizontalLine(x + 1, x1 - 2, y1 - 1, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
	}

	public static void drawWHollowBorderedRect2(int x, int y, int x1, int y1, int size, int borderC) {
		drawVerticalLine(x, y, y1 - 1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x + 1, x1 - 2, y, borderC);
		drawHorizontalLine(x + 1, x1 - 2, y1 - 1, borderC);
	}

	public void drawBorderedGradientRect(int x, int y, int x1, int y1, int size, int borderC, int insideC1, int insideC2) {
		drawVerticalLine(x, y, y1 - 1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x, x1 - 1, y, borderC);
		drawHorizontalLine(x, x1 - 1, y1 - 1, borderC);
		drawGradientRect(x + size, y + size, x1 - size, y1 - size, insideC1,
				insideC2);
	}

	public void drawWBorderedGradientRect(int x, int y, int x1, int y1, int size, int borderC, int insideC1, int insideC2) {
		drawVerticalLine(x, y, y1 - 1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x + 1, x1 - 2, y, borderC);
		drawHorizontalLine(x + 1, x1 - 2, y1 - 1, borderC);
		drawGradientRect(x + size, y + size, x1 - size, y1 - size, insideC1,
				insideC2);
	}

	public void drawBorderedRectWithString(String s, int x, int y, int x1, int y1, int size, int borderC, int insideC, int SC) {
		drawVerticalLine(x, y, y1 - 1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x, x1 - 1, y, borderC);
		drawHorizontalLine(x, x1 - 1, y1 - 1, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
		int S1 = ((x1 - x) / 2);
		int S2 = ((x1 - S1) - Wrapper.getFontRenderer().getStringWidth(s) / 2);
		int S3 = ((y1 - y) / 2);
		int S4 = (y1 - S3);
		Wrapper.getFontRenderer().drawString(s, S2, S4 - 4, SC);
	}

	public void drawBorderedGradientRectWithString(String s, int x, int y, int x1, int y1, int size, int borderC, int insideC1, int insideC2, int SC) {
		drawVerticalLine(x, y, y1 - 1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x, x1 - 1, y, borderC);
		drawHorizontalLine(x, x1 - 1, y1 - 1, borderC);
		drawGradientRect(x + size, y + size, x1 - size, y1 - size, insideC1,
				insideC2);
		int S1 = ((x1 - x) / 2);
		int S2 = ((x1 - S1) - Wrapper.getFontRenderer().getStringWidth(s) / 2);
		int S3 = ((y1 - y) / 2);
		int S4 = (y1 - S3);
		Wrapper.getFontRenderer().drawString(s, S2, S4 - 4, SC);
	}

	public void drawRectGui(String s, int X, int Y, boolean F) {
		drawRect(X, Y, X + 80, Y + 12, 0x43000000);
		drawRect(X + 80, Y, X + 86, Y + 12, F ? 0xF900FF00 : 0xF9FF0000);
		Wrapper.getFontRenderer().drawString(s, X + 3, Y + 2, 0xFFFFFF);
	}

	public void drawBorderedRectGui(String s, int X, int Y, boolean F) {
		drawBorderedRect(X, Y, X + 80, Y + 12, 1, 0xFF000000, 0x43000000);
		drawRect(X + 80, Y, X + 86, Y + 12, F ? 0xF900FF00 : 0xF9FF0000);
		Wrapper.getFontRenderer().drawString(s, X + 3, Y + 2, 0xFFFFFF);
	}

	public void drawBorderedRectGuiArraylist(String s, int X, int Y) {
		drawBorderedRect(X, Y, X + 80, Y + 12, 1, 0xFF000000, 0x43000000);
		Wrapper.getFontRenderer().drawString(s, X + 3, Y + 2, 0xFFFFFF);
	}

	public static void drawMoreRoundedBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		drawVerticalLine(x, y + 1, y1 - 2, borderC);
		drawVerticalLine(x1 - 1, y + 1, y1 - 2, borderC);
		drawHorizontalLine(x + 2, x1 - 3, y, borderC);
		drawHorizontalLine(x + 2, x1 - 3, y1 - 1, borderC);
		drawHorizontalLine(x + 1, x + 1, y + 1, borderC);
		drawHorizontalLine(x1 - 2, x1 - 2, y + 1, borderC);
		drawHorizontalLine(x1 - 2, x1 - 2, y1 - 2, borderC);
		drawHorizontalLine(x + 1, x + 1, y1 - 2, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
	}

	public void drawSmallStringWithShadow(String s, int x, int y, int c) {
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		Wrapper.getFontRenderer().drawString(s, x * 2, y * 2, c);
		GL11.glScalef(2F, 2F, 2F);
	}

	public void drawSmallString(String s, int x, int y, int c) {
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		Wrapper.getFontRenderer().drawString(s, x * 2, y * 2, c);
		GL11.glScalef(2F, 2F, 2F);
	}

	public int getOpenGLVar(String name) {
		try {
			try {
				Field f = GL11.class.getDeclaredField(name);
				return f.getInt(null);
			} catch (NoSuchFieldException e) {
				Field[] af = GL11.class.getDeclaredFields();

				if (!name.startsWith("GL_")) {
					name = "GL_" + name;
				}

				for (Field f : af)
					if (f.getName().toLowerCase().replace("_", "").equals(name)) {
						return f.getInt(null);
					}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public String getOpenGLVar(int id) {
		Field[] af = GL11.class.getDeclaredFields();

		for (Field f : af)
			try {
				if (f.getInt(null) == id) {
					return f.getName();
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		return null;
	}

	public void drawCircle(int x, int y, double r, int numseg, int c) {
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_LINE_LOOP);

		for (int i = 0; i <= numseg; i++) {
			double x2 = Math.sin((i * Math.PI / (numseg / 2))) * r;
			double y2 = Math.cos((i * Math.PI / (numseg / 2))) * r;
			GL11.glVertex2d(x + x2, y + y2);
		}

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawFilledCircle(int x, int y, double r, int numseg, int c) {
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		for (int i = 0; i <= numseg; i++) {
			double x2 = Math.sin((i * Math.PI / (numseg / 2))) * r;
			double y2 = Math.cos((i * Math.PI / (numseg / 2))) * r;
			GL11.glVertex2d(x + x2, y + y2);
		}

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawTriangle(int x, int y, double side, int c) {
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(x + (side / 2), y);
		GL11.glVertex2d(x - (side / 2), y);
		double a = Math.sqrt((side * side) - ((side / 2) * (side / 2)));
		GL11.glVertex2d(x, y + a);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawHLine(float par1, float par2, float par3, int par4) {
		if (par2 < par1) {
			float var5 = par1;
			par1 = par2;
			par2 = var5;
		}

		drawRect((int) par1, (int) par3, (int) par2 + 1, (int) par3 + 1,
				par4);
	}

	public void drawRect(double x, double y, double d, double e, int color) {
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		drawRect(x, y, d, e, red, green,
				blue, alpha);
	}

	public void drawRect(double x, double y, double x1, double y1, float r,
	                     float g, float b, float a) {
		start2dRendering();
		enableSmoothing();
		GL11.glColor4f(r, g, b, a);
		drawRect(x, y, x1, y1);
		end2dRendering();
		disableSmoothing();
	}

	public void drawRect(double x, double y, double x1, double y1) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x, y1);
		GL11.glVertex2d(x1, y1);
		GL11.glVertex2d(x1, y);
		GL11.glVertex2d(x, y);
		GL11.glEnd();
	}

	public void enableSmoothing() {
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
	}

	public void disableSmoothing() {
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public void start2dRendering() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void end2dRendering() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void drawOutlinedBox(AxisAlignedBB box) {
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(box.minX, box.minY, box.minZ);
		GL11.glVertex3d(box.minX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.minZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
		GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
		GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
		GL11.glVertex3d(box.minX, box.minY, box.maxZ);
		GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
		GL11.glEnd();
	}
}

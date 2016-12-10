package de.paxii.clarinet.util.render;

import de.paxii.clarinet.Wrapper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex3d;

public class GL11Helper {
  public static void drawBox(double x, double y, double z, double x2,
                             double y2, double z2) {
    glBegin(GL_QUADS);
    glVertex3d(x, y, z);
    glVertex3d(x, y2, z);
    glVertex3d(x2, y, z);
    glVertex3d(x2, y2, z);
    glVertex3d(x2, y, z2);
    glVertex3d(x2, y2, z2);
    glVertex3d(x, y, z2);
    glVertex3d(x, y2, z2);
    glEnd();
    glBegin(GL_QUADS);
    glVertex3d(x2, y2, z);
    glVertex3d(x2, y, z);
    glVertex3d(x, y2, z);
    glVertex3d(x, y, z);
    glVertex3d(x, y2, z2);
    glVertex3d(x, y, z2);
    glVertex3d(x2, y2, z2);
    glVertex3d(x2, y, z2);
    glEnd();
    glBegin(GL_QUADS);
    glVertex3d(x, y2, z);
    glVertex3d(x2, y2, z);
    glVertex3d(x2, y2, z2);
    glVertex3d(x, y2, z2);
    glVertex3d(x, y2, z);
    glVertex3d(x, y2, z2);
    glVertex3d(x2, y2, z2);
    glVertex3d(x2, y2, z);
    glEnd();
    glBegin(GL_QUADS);
    glVertex3d(x, y, z);
    glVertex3d(x2, y, z);
    glVertex3d(x2, y, z2);
    glVertex3d(x, y, z2);
    glVertex3d(x, y, z);
    glVertex3d(x, y, z2);
    glVertex3d(x2, y, z2);
    glVertex3d(x2, y, z);
    glEnd();
    glBegin(GL_QUADS);
    glVertex3d(x, y, z);
    glVertex3d(x, y2, z);
    glVertex3d(x, y, z2);
    glVertex3d(x, y2, z2);
    glVertex3d(x2, y, z2);
    glVertex3d(x2, y2, z2);
    glVertex3d(x2, y, z);
    glVertex3d(x2, y2, z);
    glEnd();
    glBegin(GL_QUADS);
    glVertex3d(x, y2, z2);
    glVertex3d(x, y, z2);
    glVertex3d(x, y2, z);
    glVertex3d(x, y, z);
    glVertex3d(x2, y2, z);
    glVertex3d(x2, y, z);
    glVertex3d(x2, y2, z2);
    glVertex3d(x2, y, z2);
    glEnd();
  }

  public static void drawOutlinedBox(double x, double y, double z, double x2,
                                     double y2, double z2, float l1) {
    glLineWidth(l1);
    glBegin(GL_LINES);
    glVertex3d(x, y, z);
    glVertex3d(x, y2, z);
    glVertex3d(x2, y, z);
    glVertex3d(x2, y2, z);
    glVertex3d(x2, y, z2);
    glVertex3d(x2, y2, z2);
    glVertex3d(x, y, z2);
    glVertex3d(x, y2, z2);
    glEnd();
    glBegin(GL_LINES);
    glVertex3d(x2, y2, z);
    glVertex3d(x2, y, z);
    glVertex3d(x, y2, z);
    glVertex3d(x, y, z);
    glVertex3d(x, y2, z2);
    glVertex3d(x, y, z2);
    glVertex3d(x2, y2, z2);
    glVertex3d(x2, y, z2);
    glEnd();
    glBegin(GL_LINES);
    glVertex3d(x, y2, z);
    glVertex3d(x2, y2, z);
    glVertex3d(x2, y2, z2);
    glVertex3d(x, y2, z2);
    glVertex3d(x, y2, z);
    glVertex3d(x, y2, z2);
    glVertex3d(x2, y2, z2);
    glVertex3d(x2, y2, z);
    glEnd();
    glBegin(GL_LINES);
    glVertex3d(x, y, z);
    glVertex3d(x2, y, z);
    glVertex3d(x2, y, z2);
    glVertex3d(x, y, z2);
    glVertex3d(x, y, z);
    glVertex3d(x, y, z2);
    glVertex3d(x2, y, z2);
    glVertex3d(x2, y, z);
    glEnd();
    glBegin(GL_LINES);
    glVertex3d(x, y, z);
    glVertex3d(x, y2, z);
    glVertex3d(x, y, z2);
    glVertex3d(x, y2, z2);
    glVertex3d(x2, y, z2);
    glVertex3d(x2, y2, z2);
    glVertex3d(x2, y, z);
    glVertex3d(x2, y2, z);
    glEnd();
    glBegin(GL_LINES);
    glVertex3d(x, y2, z2);
    glVertex3d(x, y, z2);
    glVertex3d(x, y2, z);
    glVertex3d(x, y, z);
    glVertex3d(x2, y2, z);
    glVertex3d(x2, y, z);
    glVertex3d(x2, y2, z2);
    glVertex3d(x2, y, z2);
    glEnd();
  }

  public static void enableDefaults() {
    Wrapper.getRenderer().disableLightmap();
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glEnable(GL11.GL_LINE_SMOOTH);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glEnable(GL13.GL_MULTISAMPLE);
    GL11.glEnable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
    GL11.glShadeModel(GL11.GL_SMOOTH);
    GL11.glLineWidth(1.8F);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glEnable(GL11.GL_LINE_SMOOTH);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glEnable(GL13.GL_MULTISAMPLE);
    GL11.glEnable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
    GL11.glShadeModel(GL11.GL_SMOOTH);
    GL11.glDepthMask(false);
  }

  public static void disableDefaults() {
    GL11.glDisable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_LINE_SMOOTH);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL13.GL_MULTISAMPLE);
    GL11.glDisable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
    GL11.glDepthMask(true);
    GL11.glDisable(GL13.GL_SAMPLE_ALPHA_TO_COVERAGE);
    GL11.glDisable(GL13.GL_MULTISAMPLE);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_LINE_SMOOTH);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_BLEND);
    Wrapper.getRenderer().enableLightmap();
  }

  public static void drawBox(double x, double y, double z, float width) {
    GL11.glPushMatrix();
    GL11.glTranslated(0, 0, -width);
    drawRect(-width, 1.0f, width, 0f);
    GL11.glPopMatrix();
    GL11.glPushMatrix();
    GL11.glTranslated(0, 0, width);
    drawRect(-width, 1.0f, width, 0f);
    GL11.glPopMatrix();
    GL11.glPushMatrix();
    GL11.glTranslated(width, 0, 0);
    GL11.glRotatef(90, 0, 1, 0);
    drawRect(-width, 1.0f, width, 0f);
    GL11.glPopMatrix();
    GL11.glPushMatrix();
    GL11.glTranslated(-width, 0, 0);
    GL11.glRotatef(90, 0, 1, 0);
    drawRect(-width, 1.0f, width, 0f);
    GL11.glPopMatrix();
    GL11.glPushMatrix();
    GL11.glTranslated(0, 0, -width);
    GL11.glRotatef(90, 1, 0, 0);
    drawRect(-width, width * 2, width, 0f);
    GL11.glPopMatrix();
    GL11.glPushMatrix();
    GL11.glTranslated(0, 1.0, -width);
    GL11.glRotatef(90, 1, 0, 0);
    drawRect(-width, width * 2, width, 0f);
    GL11.glPopMatrix();
  }

  public static void drawRect(float x, float y, float x1, float y1) {
    GL11.glBegin(GL11.GL_QUADS);
    GL11.glVertex2f(x, y);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x, y1);
    GL11.glEnd();
    GL11.glBegin(GL11.GL_QUADS);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
  }
}

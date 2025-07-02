package com.dariom.coloredbars.draw;

import static processing.core.PConstants.CENTER;

import com.dariom.coloredbars.domain.Button;
import processing.core.PApplet;
import processing.core.PFont;

public class GuiDrawer {

  private static final int FONT_SIZE = 8;

  private final PApplet sketch;
  private final PFont font;

  public GuiDrawer(PApplet sketch) {
    this.sketch = sketch;
    this.font = sketch.createFont("Arial", FONT_SIZE * sketch.displayDensity(), true);
  }

  public void drawButton(Button button) {
    // rectangle
    sketch.strokeWeight(0.8f);
    sketch.stroke(0);
    sketch.fill(200);
    sketch.rect(button.x(), button.y(), button.width(), button.height(), 5);

    // text
    sketch.textFont(font);
    sketch.textSize(FONT_SIZE * sketch.displayDensity());
    sketch.textAlign(CENTER, CENTER);
    sketch.fill(0);
    sketch.text(button.text(), button.x() + (float) button.width() / 2, button.y() + (float) button.height() / 2);
  }

}

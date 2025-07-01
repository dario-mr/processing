package com.dariom.coloredbars;

import processing.core.PApplet;
import processing.core.PConstants;

public class GuiDrawer {

  private final PApplet sketch;

  public GuiDrawer(PApplet sketch) {
    this.sketch = sketch;
  }

  void drawButton(String btnText, int x, int y, int btnWidth, int btnHeight) {
    // rectangle
    sketch.strokeWeight(0.8f);
    sketch.stroke(0);
    sketch.fill(200);
    sketch.rect(x, y, btnWidth, btnHeight, 5);

    // text
    sketch.fill(0);
    sketch.textAlign(PConstants.CENTER, PConstants.CENTER);
    sketch.text(btnText, x + (float) btnWidth / 2, y + (float) btnHeight / 2);
  }

}

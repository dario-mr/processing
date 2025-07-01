package com.dariom.coloredbars;

import java.util.ArrayList;
import java.util.Collections;
import processing.core.PApplet;

public class BarsDrawer {

  private final PApplet sketch;

  public BarsDrawer(PApplet sketch) {
    this.sketch = sketch;
  }

  void shuffleColorsAndDrawBars(ArrayList<Integer> colors, float barWidth) {
    Collections.shuffle(colors);
    drawBars(colors, barWidth);
  }

  void drawBars(ArrayList<Integer> colors, float barWidth) {
    sketch.background(255);

    for (int i = 0; i < colors.size(); i++) {
      int x = Math.round(i * barWidth);
      int nextX = Math.round((i + 1) * barWidth);
      int actualWidth = nextX - x;

      drawBar(x, colors.get(i), actualWidth);
    }
  }

  void drawBar(int x, int barColor, int barWidth) {
    sketch.noStroke();
    sketch.fill(barColor);
    sketch.rect(x, 0, barWidth, sketch.height);
  }

}

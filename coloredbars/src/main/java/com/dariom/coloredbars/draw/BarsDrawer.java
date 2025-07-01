package com.dariom.coloredbars.draw;

import static java.util.Collections.shuffle;

import java.util.List;
import processing.core.PApplet;

public class BarsDrawer {

  private final PApplet sketch;

  public BarsDrawer(PApplet sketch) {
    this.sketch = sketch;
  }

  public void shuffleColorsAndDrawBars(List<Integer> colors, float barWidth) {
    shuffle(colors);
    drawBars(colors, barWidth);
  }

  public void drawBars(List<Integer> colors, float barWidth) {
    sketch.background(255);

    for (int i = 0; i < colors.size(); i++) {
      int x = Math.round(i * barWidth);
      int nextX = Math.round((i + 1) * barWidth);
      int actualWidth = nextX - x;

      drawBar(x, colors.get(i), actualWidth);
    }
  }

  private void drawBar(int x, int barColor, int barWidth) {
    sketch.noStroke();
    sketch.fill(barColor);
    sketch.rect(x, 0, barWidth, sketch.height);
  }

}

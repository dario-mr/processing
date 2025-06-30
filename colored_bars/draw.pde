void shuffleColorsAndDrawBars(ArrayList<Integer> colors, float barWidth) {
  Collections.shuffle(colors);
  drawBars(colors, barWidth);
}

void drawBars(ArrayList<Integer> colors, float barWidth) {
  background(255);

  for (int i = 0; i < colors.size(); i++) {
    int x = Math.round(i * barWidth);
    int nextX = Math.round((i + 1) * barWidth);
    int actualWidth = nextX - x;

    drawBar(x, colors.get(i), actualWidth);
  }
}

void drawBar(int x, color barColor, int barWidth) {
  noStroke();
  fill(barColor);
  rect(x, 0, barWidth, height);
}

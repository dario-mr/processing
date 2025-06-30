import java.util.Collections;
import processing.sound.*;

private final int BASE_COLOR = color(255, 0, 0);
private final float GRADIENT_STEP = 1;
private final int DRAW_DELAY_MS = 10;

ArrayList<Integer> colors;
float barWidth;
SoundFile sortSound;

volatile boolean needsRedraw = false;

void setup() {
  fullScreen();
  noStroke();

  colors = generateColors(BASE_COLOR, GRADIENT_STEP);
  barWidth = (float) width / colors.size();
  sortSound = new SoundFile(this, "sort.wav");

  System.out.println("Window size: " + width + " x " + height);
  System.out.println("Numbers of colors generated: " + colors.size());
  System.out.println("Bar width: " + barWidth);

  shuffleColorsAndDrawBars(colors, barWidth);
}

void draw() {
  if (needsRedraw) {
    drawBars(colors, barWidth);
    sortSound.play();
    needsRedraw = false;
  }
}

void keyPressed() {
  if (key == 's') {
    thread("sortThread");
  }
}

void sortThread() {
  System.out.println("Started sorting");
  quickSort(colors, 0, colors.size() - 1);
  System.out.println("Finished sorting");
}

ArrayList<Integer> generateColors(int baseColor, float gradientStep) {
  ArrayList<Integer> colors = new ArrayList<Integer>();
  final float baseColorRed = red(baseColor);
  final float baseColorBlue = blue(baseColor);

  for (float i = 0; i < 255; i += gradientStep) {
    float green = i;
    colors.add(color(baseColorRed, green, baseColorBlue));
  }

  return colors;
}

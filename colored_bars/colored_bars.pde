import java.util.Collections;
import processing.sound.*;

// colors generation
private final int BASE_COLOR = color(255, 0, 0);
private final float GRADIENT_STEP = 1;

// draw delays
private final int BUBBLESORT_DRAW_DELAY_MS = 1;
private final int QUICKSORT_DRAW_DELAY_MS = 10;

// misc
ArrayList<Integer> colors;
float barWidth;
SoundFile sortSound;

// visible between threads
volatile boolean needsRedraw = false;
volatile boolean stopSorting = false;
volatile boolean isSorting = false;

// buttons size and position
int shuffleX = 10, shuffleY = 10, shuffleW = 100, shuffleH = 30;
int quicksortX = 10, quicksortY = 70, quicksortW = 100, quicksortH = 30;
int bubblesortX = 10, bubblesortY = 110, bubblesortW = 100, bubblesortH = 30;
int quitX = 10, quitY, quitW = 100, quitH = 30;

void setup() {
  fullScreen();
  noStroke();

  colors = generateColors(BASE_COLOR, GRADIENT_STEP);
  barWidth = (float) width / colors.size();
  sortSound = new SoundFile(this, "sort.wav");
  quitY = height - 50;

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

  drawButton(shuffleX, shuffleY, shuffleW, shuffleH, "Shuffle");
  drawButton(quicksortX, quicksortY, quicksortW, quicksortH, "Quick sort");
  drawButton(bubblesortX, bubblesortY, bubblesortW, bubblesortH, "Bubble sort");
  drawButton(quitX, quitY, quitW, quitH, "Quit");
}

void mousePressed() {
  // shuffle button
  if (mouseX > shuffleX && mouseX < shuffleX + shuffleW &&
    mouseY > shuffleY && mouseY < shuffleY + shuffleH) {
    stopSorting = true;
    shuffleColorsAndDrawBars(colors, barWidth);
  }

  // quicksort button
  if (!isSorting && mouseX > quicksortX && mouseX < quicksortX + quicksortW &&
    mouseY > quicksortY && mouseY < quicksortY + quicksortH) {
    stopSorting = false;
    thread("quicksortThread");
  }

  // bubblesort button
  if (!isSorting && mouseX > bubblesortX && mouseX < bubblesortX + bubblesortW &&
    mouseY > bubblesortY && mouseY < bubblesortY + bubblesortH) {
    stopSorting = false;
    thread("bubblesortThread");
  }

  // quit button
  if ( mouseX > quitX && mouseX < quitX + quitW &&
    mouseY > quitY && mouseY < quitY + quitH) {
    exit();
  }
}

void quicksortThread() {
  System.out.println("Sorting started");
  isSorting = true;
  quickSort(colors, 0, colors.size() - 1);
  isSorting = false;
  System.out.println("Sorting finished");
}

void bubblesortThread() {
  System.out.println("Sorting started");
  isSorting = true;
  bubbleSort(colors);
  isSorting = false;
  System.out.println("Sorting finished");
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

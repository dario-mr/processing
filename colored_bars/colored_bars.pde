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
SortType sortToRun = SortType.NONE;

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

  colors = generateColors(BASE_COLOR, GRADIENT_STEP);
  barWidth = (float) width / colors.size();
  sortSound = new SoundFile(this, "sort.wav");
  quitY = height - 50;

  println("Window size: " + width + " x " + height + " px");
  println("Number of generated colors: " + colors.size());
  println("Bar width: " + barWidth + " px");

  shuffleColorsAndDrawBars(colors, barWidth);
}

void draw() {
  if (needsRedraw) {
    drawBars(colors, barWidth);
    sortSound.play();
    needsRedraw = false;
  }

  drawButton("Shuffle", shuffleX, shuffleY, shuffleW, shuffleH);
  drawButton("Quick sort", quicksortX, quicksortY, quicksortW, quicksortH);
  drawButton("Bubble sort", bubblesortX, bubblesortY, bubblesortW, bubblesortH);
  drawButton("Quit", quitX, quitY, quitW, quitH);
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
    sortToRun = SortType.QUICK;
    thread("runSortThread");
  }

  // bubblesort button
  if (!isSorting && mouseX > bubblesortX && mouseX < bubblesortX + bubblesortW &&
    mouseY > bubblesortY && mouseY < bubblesortY + bubblesortH) {
    stopSorting = false;
    sortToRun = SortType.BUBBLE;
    thread("runSortThread");
  }

  // quit button
  if ( mouseX > quitX && mouseX < quitX + quitW &&
    mouseY > quitY && mouseY < quitY + quitH) {
    exit();
  }
}

void runSortThread() {
  isSorting = true;
  println("Sorting started");

  switch (sortToRun) {
  case QUICK:
    quickSort(colors, 0, colors.size() - 1);
    break;
  case BUBBLE:
    bubbleSort(colors);
    break;
  default:
    break;
  }

  println("Sorting finished");
  isSorting = false;
  sortToRun = SortType.NONE;
}

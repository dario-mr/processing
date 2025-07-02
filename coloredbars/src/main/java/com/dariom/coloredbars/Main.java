package com.dariom.coloredbars;

import static com.dariom.coloredbars.domain.SortType.BUBBLE;
import static com.dariom.coloredbars.domain.SortType.HEAP;
import static com.dariom.coloredbars.domain.SortType.MERGE;
import static com.dariom.coloredbars.domain.SortType.QUICK;

import com.dariom.coloredbars.color.ColorGenerator;
import com.dariom.coloredbars.domain.Button;
import com.dariom.coloredbars.domain.SortType;
import com.dariom.coloredbars.draw.BarsDrawer;
import com.dariom.coloredbars.draw.GuiDrawer;
import com.dariom.coloredbars.file.FileLoader;
import com.dariom.coloredbars.sort.Sort;
import com.dariom.coloredbars.sort.impl.BubbleSort;
import com.dariom.coloredbars.sort.impl.HeapSort;
import com.dariom.coloredbars.sort.impl.MergeSort;
import com.dariom.coloredbars.sort.impl.QuickSort;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import processing.core.PApplet;
import processing.sound.SoundFile;

public class Main extends PApplet {

  // TODO move properties to property file?

  // band colors generation
  private final int BASE_COLOR = color(255, 0, 0);
  private static final float GRADIENT_STEP = 0.5f;

  // re-draw delays
  private static final int BUBBLESORT_DRAW_DELAY_MS = 1;
  private static final int QUICKSORT_DRAW_DELAY_MS = 3;
  private static final int MERGESORT_DRAW_DELAY_MS = 3;
  private static final int HEAPSORT_DRAW_DELAY_MS = 2;

  // misc
  private List<Integer> colors;
  private float barWidth;
  private SoundFile sortSound;
  private Map<SortType, Sort> sortMap;

  // variables visible between threads
  private volatile boolean needsRedraw = false;
  private volatile boolean shouldStopSorting = false;
  private volatile boolean isSorting = false;

  // buttons
  private Button shuffleBtn;
  private Button bubbleSortBtn;
  private Button quickSortBtn;
  private Button mergeSortBtn;
  private Button heapSortBtn;
  private Button quitBtn;

  // constructor injections
  private ColorGenerator colorGenerator;
  private BarsDrawer barsDrawer;
  private GuiDrawer guiDrawer;
  private FileLoader fileLoader;

  public static void main(String[] args) {
    PApplet.main("com.dariom.coloredbars.Main");
  }

  @Override
  public void settings() {
    fullScreen();
    pixelDensity(displayDensity());
  }

  @Override
  public void setup() {
    // injection
    colorGenerator = new ColorGenerator(this);
    barsDrawer = new BarsDrawer(this);
    guiDrawer = new GuiDrawer(this);
    fileLoader = new FileLoader(this);
    setupSortAlgorithms();

    // buttons
    shuffleBtn = new Button("Shuffle", 10, 10, 100, 30);
    bubbleSortBtn = new Button("Bubble sort", 10, buttonY(1), 100, 30);
    quickSortBtn = new Button("Quick sort", 10, buttonY(2), 100, 30);
    mergeSortBtn = new Button("Merge sort", 10, buttonY(3), 100, 30);
    heapSortBtn = new Button("Heap sort", 10, buttonY(4), 100, 30);
    quitBtn = new Button("Quit", 10, height - 50, 100, 30);

    colors = colorGenerator.generateColors(BASE_COLOR, GRADIENT_STEP);
    barWidth = (float) width / colors.size();
    sortSound = fileLoader.loadSound("sort.wav");

    println("Window size: " + width + " x " + height + " px");
    println("Number of generated colors: " + colors.size());
    println("Bar width: " + barWidth + " px");

    barsDrawer.shuffleColorsAndDrawBars(colors, barWidth);
  }

  @Override
  public void draw() {
    if (needsRedraw) {
      barsDrawer.drawBars(colors, barWidth);
      sortSound.play();
      needsRedraw = false;
    }

    drawAllButtons();
  }

  @Override
  public void mouseReleased() {
    handleGeneralButton(shuffleBtn, () -> {
      shouldStopSorting = true;
      barsDrawer.shuffleColorsAndDrawBars(colors, barWidth);
    });

    handleGeneralButton(quitBtn, this::exit);

    handleSortButton(quickSortBtn, QUICK);
    handleSortButton(bubbleSortBtn, BUBBLE);
    handleSortButton(mergeSortBtn, MERGE);
    handleSortButton(heapSortBtn, HEAP);
  }

  private int buttonY(int index) {
    return 10 + index * 40;
  }

  private void handleGeneralButton(Button btn, Runnable action) {
    if (btn.isClicked(mouseX, mouseY)) {
      action.run();
    }
  }

  private void handleSortButton(Button btn, SortType type) {
    if (!isSorting && btn.isClicked(mouseX, mouseY)) {
      sort(type, colors);
    }
  }

  private void drawAllButtons() {
    guiDrawer.drawButton(shuffleBtn);
    guiDrawer.drawButton(quickSortBtn);
    guiDrawer.drawButton(bubbleSortBtn);
    guiDrawer.drawButton(mergeSortBtn);
    guiDrawer.drawButton(heapSortBtn);
    guiDrawer.drawButton(quitBtn);
  }

  private void sort(SortType sortType, List<Integer> colors) {
    new Thread(() -> {
      try {
        println("Sorting started");
        shouldStopSorting = false;
        isSorting = true;

        sortMap.get(sortType).sort(colors);
      } finally {
        isSorting = false;
        println("Sorting finished");
      }
    }).start();
  }

  private void setupSortAlgorithms() {
    sortMap = Map.of(
        BUBBLE, new BubbleSort(shouldStopSorting(), triggerRedrawWithDelay(BUBBLESORT_DRAW_DELAY_MS)),
        QUICK, new QuickSort(shouldStopSorting(), triggerRedrawWithDelay(QUICKSORT_DRAW_DELAY_MS)),
        MERGE, new MergeSort(shouldStopSorting(), triggerRedrawWithDelay(MERGESORT_DRAW_DELAY_MS)),
        HEAP, new HeapSort(shouldStopSorting(), triggerRedrawWithDelay(HEAPSORT_DRAW_DELAY_MS))
    );
  }

  private Supplier<Boolean> shouldStopSorting() {
    return () -> shouldStopSorting;
  }

  private Runnable triggerRedrawWithDelay(int delayMs) {
    return () -> {
      needsRedraw = true;
      delay(delayMs);
    };
  }

}

package com.dariom.coloredbars.sort;

import java.util.List;
import java.util.function.Supplier;

public class BubbleSort {

  private final Supplier<Boolean> shouldCancel;
  private final Runnable triggerRedraw;

  public BubbleSort(Supplier<Boolean> shouldCancel, Runnable triggerRedraw) {
    this.shouldCancel = shouldCancel;
    this.triggerRedraw = triggerRedraw;
  }

  public void bubbleSort(List<Integer> items) {
    int n = items.size();
    boolean swapped;

    for (int i = 0; i < n - 1; i++) {
      swapped = false;

      for (int j = 0; j < n - 1 - i; j++) {
        if (shouldCancel.get()) {
          break;
        }

        if (items.get(j) > items.get(j + 1)) {
          int temp = items.get(j);
          items.set(j, items.get(j + 1));
          items.set(j + 1, temp);
          swapped = true;

          // redraw UI
          triggerRedraw.run();
        }
      }

      if (!swapped) {
        break;
      }
    }
  }

}

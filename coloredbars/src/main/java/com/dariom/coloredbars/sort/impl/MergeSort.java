package com.dariom.coloredbars.sort.impl;

import com.dariom.coloredbars.sort.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MergeSort implements Sort {

  private final Supplier<Boolean> shouldCancel;
  private final Runnable triggerRedraw;

  public MergeSort(Supplier<Boolean> shouldCancel, Runnable triggerRedraw) {
    this.shouldCancel = shouldCancel;
    this.triggerRedraw = triggerRedraw;
  }

  @Override
  public void sort(List<Integer> items) {
    if (items == null || items.size() <= 1) {
      return;
    }

    sort(items, 0, items.size() - 1);
  }

  private void sort(List<Integer> items, int left, int right) {
    if (shouldCancel.get()) {
      return;
    }

    if (left >= right) {
      return;
    }

    int mid = left + (right - left) / 2;

    sort(items, left, mid);
    triggerRedraw.run();

    sort(items, mid + 1, right);
    triggerRedraw.run();

    merge(items, left, mid, right);
  }

  private void merge(List<Integer> items, int left, int mid, int right) {
    List<Integer> temp = new ArrayList<>();
    int i = left;
    int j = mid + 1;

    while (i <= mid && j <= right) {
      if (shouldCancel.get()) {
        return;
      }

      if (items.get(i) <= items.get(j)) {
        temp.add(items.get(i++));
      } else {
        temp.add(items.get(j++));
      }
    }

    while (i <= mid) {
      if (shouldCancel.get()) {
        return;
      }
      temp.add(items.get(i++));
    }
    while (j <= right) {
      if (shouldCancel.get()) {
        return;
      }
      temp.add(items.get(j++));
    }

    for (int k = 0; k < temp.size(); k++) {
      if (shouldCancel.get()) {
        return;
      }
      items.set(left + k, temp.get(k));
      triggerRedraw.run();
    }
  }

}

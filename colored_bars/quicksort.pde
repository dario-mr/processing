void quickSort(ArrayList<Integer> items, int low, int high) {
  if (stopSorting) {
    return;
  }

  if (low < high) {
    int pi = partition(items, low, high);
    quickSort(items, low, pi - 1);
    quickSort(items, pi + 1, high);
  }
}

int partition(ArrayList<Integer> list, int low, int high) {
  int pivot = list.get(high);
  int i = low - 1;

  for (int j = low; j < high; j++) {
    if (stopSorting) {
      break;
    }

    if (list.get(j) <= pivot) {
      i++;
      swap(list, i, j);
    }
  }
  swap(list, i + 1, high);
  return i + 1;
}

void swap(ArrayList<Integer> list, int i, int j) {
  int temp = list.get(i);
  list.set(i, list.get(j));
  list.set(j, temp);

  // redraw UI
  needsRedraw = true;
  delay(QUICKSORT_DRAW_DELAY_MS);
}

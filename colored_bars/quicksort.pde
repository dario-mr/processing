void quickSort(ArrayList<Integer> list, int low, int high) {
  if (low < high) {
    int pi = partition(list, low, high);
    quickSort(list, low, pi - 1);
    quickSort(list, pi + 1, high);
  }
}

int partition(ArrayList<Integer> list, int low, int high) {
  int pivot = list.get(high);
  int i = low - 1;

  for (int j = low; j < high; j++) {
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

  needsRedraw = true;
  delay(DRAW_DELAY_MS);
}

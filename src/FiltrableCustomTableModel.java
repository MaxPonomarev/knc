

interface FiltrableCustomTableModel {

  void deleteRows(int[] rows) throws FiltrableTableModelException;//если удаляется всё, то Exception

  void leaveRows(int[] rows);

  void revertRows();
}

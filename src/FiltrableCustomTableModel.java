

interface FiltrableCustomTableModel {

  void deleteRows(int[] rows) throws FiltrableTableModelException;//���� ��������� ��, �� Exception

  void leaveRows(int[] rows);

  void revertRows();
}

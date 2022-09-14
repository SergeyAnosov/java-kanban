package interfaces;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addToHistoryMap(Task task);
    void remove(int id);
    List<Task> getHistory();

}

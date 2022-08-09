package service;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addToMap(Task task);
    void remove(int id);
    List<Task> getHistory();

}

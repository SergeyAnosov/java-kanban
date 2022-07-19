package service;

import tasks.Task;


import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> taskHistory = new LinkedList<>();
    private final int HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        taskHistory.add(task);
        if (taskHistory.size() > HISTORY_SIZE) {
            taskHistory.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}

package tasks;

import constants.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    protected int epicId;
    private static int epicGenerator = 0;

    public Epic(String name, Status status, String extraInfo) {
        super(name, status, extraInfo);
        epicId = epicGenerator++;
    }

    public void addSubtask(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void setSubTaskIds(List<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    @Override
    public String toString() {
        return "E" + epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}

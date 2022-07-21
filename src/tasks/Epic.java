package tasks;

import constants.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    private static int epicGenerator = 0;

    public Epic(String name, Status status, String extraInfo) {
        super(name, status, extraInfo);
        epicGenerator++;
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
        return "E" + id;
    }

    public static int getEpicGenerator() {
        return epicGenerator;
    }
}

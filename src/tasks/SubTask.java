package tasks;

import constants.Status;

public class SubTask extends Task {
    private int epicId;
    private static int subTaskGenerator = 0;
    protected int subTaskId;

    public SubTask(String name, Status status, String extraInfo, int epicId) {
        super(name, status, extraInfo);
        this.epicId = epicId;
        subTaskId = subTaskGenerator++;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "S" + subTaskId;
    }

    public int getSubTaskId() {
        return subTaskId;
    }
}

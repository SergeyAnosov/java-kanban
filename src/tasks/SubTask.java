package tasks;

import constants.Status;

public class SubTask extends Task {
    private int epicId;
    private static int subTaskGenerator = 0;

    public SubTask(String name, Status status, String extraInfo, int epicId) {
        super(name, status, extraInfo);
        this.epicId = epicId;
        subTaskGenerator++;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "S" + id;
    }

    public static int getSubTaskGenerator() {
        return subTaskGenerator;
    }
}

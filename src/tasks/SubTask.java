package tasks;

import constants.Status;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, Status status, String extraInfo, int epicId) {
        super(name, status, extraInfo);
        this.epicId = epicId;
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
}

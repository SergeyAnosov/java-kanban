package tasks;

import constants.Status;

public class SubTask extends Task {
    protected int epicId;
    protected int subTaskId;



    public SubTask(String name, Status status, String extraInfo, int epicId) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;   
        this.epicId = epicId;
        subTaskId = getTaskIdGenerator();
        setTaskIdGenerator(subTaskId++);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "S" + subTaskId;
    }

    public int getId() {
        return subTaskId;
    }
}

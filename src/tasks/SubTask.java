package tasks;

import constants.Status;
import constants.TaskType;

public class SubTask extends Task {
    protected int epicId;
    protected int subTaskId;

    public SubTask(String name, Status status, String extraInfo, int epicId) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;   
        this.epicId = epicId;
        subTaskId = getTaskIdGenerator();
        setTaskIdGenerator(subTaskId + 1);
    }
    
    public int getId() {
        return subTaskId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    @Override
    public String toString() {
        return "S" + subTaskId;
    }

    public TaskType getTaskType() {
        return TaskType.SUB_TASK;
    }
    
}

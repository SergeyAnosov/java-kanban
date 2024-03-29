package tasks;

import constants.Status;
import constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;
    protected int subTaskId;

    public SubTask(String name, Status status, String extraInfo, int duration, LocalDateTime startTime, int epicId) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;
        this.epicId = epicId;
        subTaskId = getTaskIdGenerator();
        setTaskIdGenerator(subTaskId + 1);
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public SubTask(int subTaskId, String name, Status status, int epicId) {
        this.subTaskId = subTaskId;
        this.name = name;
        this.status = status;
        this.epicId = epicId;
    }

    public SubTask(int subTaskId, String name, Status status, int duration, LocalDateTime startTime, int epicId) {
        this.subTaskId = subTaskId;
        this.name = name;
        this.status = status;
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
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
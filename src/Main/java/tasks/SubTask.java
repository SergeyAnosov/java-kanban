package tasks;

import constants.Status;
import constants.TaskType;

import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;
    protected int subTaskId;
    protected LocalDateTime startTime;
    protected long duration;
    protected LocalDateTime endTime;

    public SubTask(String name, Status status, String extraInfo, int epicId, long duration, LocalDateTime startTime) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;   
        this.epicId = epicId;
        subTaskId = getTaskIdGenerator();
        setTaskIdGenerator(subTaskId + 1);
        this.startTime = startTime;
        this.duration = duration;
    }
    
    public int getId() {
        return subTaskId;
    }

    public int getEpicId() {
        return epicId;
    }
    
     public LocalDateTime getEndTime() {
        LocalDateTime  endTime = startTime.plusMinutes(duration);
        return endTime;
    }

    public long getDuration() {
        return duration;
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

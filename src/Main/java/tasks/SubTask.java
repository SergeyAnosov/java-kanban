package tasks;

import constants.Status;
import constants.TaskType;

import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;
    protected int subTaskId;    

    public SubTask(String name, Status status, String extraInfo, int duration, int year, int month, int day, int hour, int minutes, int epicId) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;   
        this.epicId = epicId;
        subTaskId = getTaskIdGenerator();
        setTaskIdGenerator(subTaskId + 1);
        this.duration = Duration.ofMinutes(duration);
        starTime = LocalDateTime.of(year, month, day, hour, minutes);        
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

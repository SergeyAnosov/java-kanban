package tasks;


import constants.Status;
import constants.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    protected int epicId;
    protected LocalDateTime endtime();

    public Epic(String name, Status status, String extraInfo) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;        
        epicId = getTaskIdGenerator();
        setTaskIdGenerator(epicId + 1);
    }
    
    public void setStartTime(LocalDateTime startTime) {
       this.startTime = startTime;
    }
    
     public void setEndTime(LocalDateTime endTime) {
       this.endTime = endTime;
    }
    
    public void setDuration(Duration duration) {
       this.duration = duration;
    }   
    
     public int getId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public void addSubtaskId(int subTaskId) {
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

    public TaskType getTaskType() {
        return TaskType.EPIC;
    }


}

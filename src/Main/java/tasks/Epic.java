package tasks;


import constants.Status;
import constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    protected int epicId;
    
    protected LocalDateTime startTime;
    protected long duration;
    protected LocalDateTime endTime;

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
    
    public LocalDateTime calculateEpicStartTime(List<SubTask> list) {
        LocalDateTime startTime = LocalDateTime.of(1000,0,0,0,0);
        for (SubTask sub : list) {
            if (sub.getStartTime().isAfter(startTime)) {
                startTime = sub.getStartTime();
            }
        }
       return startTime;
    }
    
     public LocalDateTime calculateEpicEndTime(List<SubTask> list) {
        LocalDateTime endTime = LocalDateTime.of(3000,0,0,0,0);
        for (SubTask sub : list) {
            if (sub.getEndTime().isBefore(endTime)) {
                endTime = sub.getEndTime();
            }
        }
       return endTime;
    }
    
    public Duration calculateEpicDuration(List<SubTask> list) {
        Duration epicDuration;
        for (SubTask sub : list) {
            epicDuration += sub.getDuration();
        }
        return epicDuration;
    }
    
    public void getDuration(long duration) {
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

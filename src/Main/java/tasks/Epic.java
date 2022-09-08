package tasks;


import constants.Status;
import constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    protected int epicId;
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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    
    public LocalDateTime calculateEpicStartTime(List<SubTask> list) {       
        if (list.isEmpty) {            
            return null;
        } else {
            LocalDateTime startTime = LocalDateTime.of(3000,1,1,0,0);
            for (SubTask subtask : list) {
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
            }
            return startTime;
        }
         return null;
    }
    
     public LocalDateTime calculateEpicEndTime(List<SubTask> list) {
        if (list.isEmpty) {            
            return null;
        } else {
            LocalDateTime endTime = LocalDateTime.of(1000,1,1,0,0);
            for (SubTask subtask : list) {
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
            return endTime;
        }
       return null;
    }
    
    public Duration calculateEpicDuration() {
        if (startTime != null && endTime != null) {        
        Duration epicDuration = Duration.between(startTime, endTime);        
        return epicDuration;
        }
        return null;
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

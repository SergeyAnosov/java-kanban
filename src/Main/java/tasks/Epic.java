package tasks;


import constants.Status;
import constants.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    protected int epicId;

    public Epic(String name, Status status, String extraInfo) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;        
        epicId = getTaskIdGenerator();
        setTaskIdGenerator(epicId + 1);
    }
    
    public LocalDateTime getStartTime() {
        List<SubTask> list = new ArrayList<>();
        for (Integer i : subTaskIds) {
            list.add(getSubTask(i));
        }
        LocalDateTime startIime = LocalDateTime.of(list.get(0).getStartTime());
        for (SubTask sub : list) {
            if (sub.getStartTime().isBefore(startTime)) {
                startTime = sub.getStartTime();
            }
        return startTime;
    }
    
    public Duration getDuration() {
        Duration epicDuration = 0;
        List<SubTask> list = new ArrayList<>();
        for (Integer i : subTaskIds) {
            list.add(getSubTask(i));
        }
        for (SubTask sub : list) {
            epicDuration += sub.getDuration();
        }
        return epicDuration;
    }
    
    public LocalDateTime getEndTime() {
        List<SubTask> list = new ArrayList<>();
        for (Integer i : subTaskIds) {
            list.add(getSubTask(i));
        }
        LocalDateTime endtIime = LocalDateTime.of(list.get(0).getEndTime());
        for (SubTask sub : list) {
            if (sub.getStartTime().isAfter(endTime)) {
                startTime = sub.getEndTime();
            }
        return endTime;        
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

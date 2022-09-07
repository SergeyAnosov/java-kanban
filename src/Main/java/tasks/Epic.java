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



            Comparator<SubTask> comparator = Comparator.comparing(Task::getStartTime);
            list.sort(comparator);

        /*LocalDateTime startTime = LocalDateTime.of(3000,1,1,0,0);
        for (SubTask sub : list) {
            if (sub.getStartTime().isBefore(startTime)) {
                LocalDateTime epicTime = sub.getStartTime();
            }
            }*/
            SubTask sub1 = list.get(0);
            LocalDateTime start = sub1.getStartTime();

                return start;




    }
    
     public LocalDateTime calculateEpicEndTime(List<SubTask> list) {
        LocalDateTime endTime = LocalDateTime.of(1000,1,1,0,0);
        for (SubTask sub : list) {
            if (sub.getEndTime().isAfter(endTime)) {
                endTime = sub.getEndTime();
            }
        }
       return endTime;
    }
    
    public Duration calculateEpicDuration(List<SubTask> list) {
        Duration epicDuration = Duration.ofMinutes(0);
        for (SubTask sub : list) {
            epicDuration = epicDuration.plus(sub.getDuration());
        }
        return epicDuration;
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

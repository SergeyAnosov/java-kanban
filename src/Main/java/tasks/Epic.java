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
    protected LocalDateTime endTime;

    public Epic(String name, Status status, String extraInfo) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;
        epicId = getTaskIdGenerator();
        setTaskIdGenerator(epicId + 1);
    }

    public Epic(int epicId, String name, Status status, String extraInfo) {
        this.epicId = epicId;
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;
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

    public void calculateEpicStartTime(List<SubTask> list) {
        if (startTime == null) {
            startTime = LocalDateTime.of(3000, 1, 1, 0, 0);
        }
        if (!list.isEmpty()) {
            for (SubTask subtask : list) {
                if (subtask != null) {
                    if (subtask.getStartTime().isBefore(startTime)) {
                        startTime = subtask.getStartTime();
                    }
                }
            }
        } else {
            startTime = null;
        }
    }

    public void calculateEpicEndTime(List<SubTask> list) {
        if (endTime == null) {
            endTime = startTime;
        }
        if (!list.isEmpty()) {
            for (SubTask subtask : list) {
                if (subtask != null) {
                    if (subtask.getEndTime().isAfter(endTime)) {
                        endTime = subtask.getEndTime();
                    }
                }
            }
        }
    }

    public void calculateEpicDuration() {
        if (startTime != null && endTime != null) {
            duration = Duration.between(startTime, endTime);
        }
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
        return "E" + epicId + " " + startTime + " " + endTime;
    }

    public TaskType getTaskType() {
        return TaskType.EPIC;
    }


}

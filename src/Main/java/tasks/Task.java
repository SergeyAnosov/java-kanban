package tasks;


import constants.Status;
import constants.TaskType;

import java.util.Objects;

public class Task {
    protected int taskId;
    protected String name;
    protected Status status;
    protected String extraInfo;
    private static int taskIdGenerator = 0;    
    
    protected LocalDateTime startTime;
    protected Duration duration;
    

    public Task(String name, Status status, String extraInfo, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;
        taskId = taskIdGenerator++;
        this.startTime = startTime;
        this.duration = duration;
       
    }

    public Task() {
    }
    
    public LocalDateTime getEndTime() {
        LocalDateTime  endTime = startTime.plusMinutes(duration);
        return endTime;
    }

    public int getId() {
        return taskId;
    }
    
    public void setId(int id) {
        taskId = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static void setTaskIdGenerator(int taskIdGenerator) {
        Task.taskIdGenerator = taskIdGenerator;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && Objects.equals(name, task.name) && status == task.status;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (status != null) {
            hash = hash + status.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "T" + taskId;
    }

    public static int getTaskIdGenerator() {
        return taskIdGenerator;
    }
}
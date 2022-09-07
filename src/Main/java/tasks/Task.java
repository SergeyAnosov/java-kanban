package tasks;


import constants.Status;
import constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected int taskId;
    protected String name;
    protected Status status;
    protected String extraInfo;
    private static int taskIdGenerator = 0; 
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"); 
    
    protected LocalDateTime startTime;
    protected Duration duration;
    protected LocalDateTime endTime;

    public Task(String name, Status status, String extraInfo, int time, int year, int month, int day, int hour, int minutes) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;
        taskId = taskIdGenerator++;
        this.startTime = startTime;
        duration = Duration.ofMinutes(time);
        starTime = LocalDateTime.of(year, month, day, hour, minutes);
        endTime = startTime.plus(duration);
    }

    public Task() {
    }

    public String getStartTime() {               
        return startTime.format(formatter);
    }

    public String getEndTime() {        
        return endTime.format(fromatter);
    }
    
    public Duration getDuration() {
        return duration;

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

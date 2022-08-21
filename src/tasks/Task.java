package tasks;

import constants.Status;

import java.util.Objects;

public class Task {
    protected int taskId;
    protected String name;
    protected Status status;
    protected String extraInfo;
    private static int taskIdGenerator = 0;

    public Task() {

    }

    public Task(String name, Status status, String extraInfo) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;
        taskId = taskIdGenerator++;
       
    }

    public int getId() {
        return taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public static void setTaskIdGenerator(int taskIdGenerator) {
        Task.taskIdGenerator = taskIdGenerator;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }
}

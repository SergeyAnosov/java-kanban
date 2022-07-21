package tasks;

import constants.Status;

import java.util.Objects;

public class Task {
    protected int taskId;
    protected String name;
    protected Status status;
    protected String extraInfo;
    private static int taskGenerator = 0;

    public Task(String name, Status status, String extraInfo) {
        this.name = name;
        this.status = status;
        this.extraInfo = extraInfo;
        taskId = taskGenerator++;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
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


}

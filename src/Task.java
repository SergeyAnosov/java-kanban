import java.util.Objects;

public class Task {
    private String name;
    private String extraInfo;
    private int id;
    private Status status;
    protected static int countOfTask = 0;

    public Task() {

        countOfTask += 1;
        this.id = countOfTask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static int getCountOfTask() {
        return countOfTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && status == task.status;
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
        String result = "Task{" +
                "name='" + name + '\'';
            if (extraInfo != null) {
                result = result + ", extraInfo.length=" + extraInfo.length();
            } else {
                result = result + ", extraInfo=null";
            }
            return result + ", id=" + id +
                ", status=" + status +
                '}';
    }
}

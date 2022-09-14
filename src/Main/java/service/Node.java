package service;

import tasks.Task;

public class Node {
    protected Task task;
    protected Node prev;
    protected Node next;

    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "task=" + task +
                ", prev=" + (prev != null ? prev.task : null) +
                ", next=" + (next != null ? next.task : null) +
                '}';
    }
}

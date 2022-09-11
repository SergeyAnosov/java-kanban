package Service;
import Interfaces.HistoryManager;
import tasks.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    protected final Map<Integer, Node> nodeMap = new HashMap<>();
    protected Node first;
    protected Node last;
    CustomLinkedList taskList = new CustomLinkedList();
   
    @Override
    public void addToHistoryMap(Task task) {
        if (task == null) {
            return;
        }
        taskList.removeNode(task.getId());
        taskList.linkLast(task);
        nodeMap.put(task.getId(), last);
    }

    @Override
    public void remove(int id) {
        taskList.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return taskList.getTasks();
        }

    public class CustomLinkedList {

        private void linkLast(Task task) {
            Node node = new Node(task, last, null);
            if (first == null) {
                last = node;
                first = node;

            } else {
                node.prev = last;
                last.next = node;
                last = node;
            }
        }

        private void removeNode(int id) {

            Node remove = nodeMap.remove(id);
            if (remove == null) {
                return;
            }
            if (remove.prev != null && remove.next != null) {
                Node left = remove.prev;
                Node right = remove.next;
                remove.prev = null;
                remove.next = null;
                left.next = right;
                right.prev = left;

            } else if (remove == first) {
                first = remove.next;
                //first.prev = null;
            } else if (remove == last) {
                last = remove.prev;
                last.next = null;
            }
        }

        private List<Task> getTasks() {
            List<Task> list = new ArrayList<>();

            Node node = first;
            while (node != null) {
                list.add(node.task);
                node = node.next;
            }
            return list;
        }



    }


}

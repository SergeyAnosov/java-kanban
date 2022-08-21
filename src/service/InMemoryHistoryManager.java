package service;
import tasks.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

   //ТЗ 5
   
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    protected Node first;
    protected Node last;
   
    @Override
    public void addToHistoryMap(Task task) {
        if (task == null) {
            return;
        }

        removeNode(task.getId());
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

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

    @Override
    public void remove(int id) {
        removeNode(id);
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
               first.prev = null;
            } else if (remove == last) {
               last = remove.prev;
               last.next = null;
         }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = new ArrayList<>();

            Node node = first;
            while (node != null) {
                list.add(node.task);
                node = node.next;
            }
       return list;
    }

    private static class Node {
        private Task task;
        private Node prev;
        private Node next;

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
}

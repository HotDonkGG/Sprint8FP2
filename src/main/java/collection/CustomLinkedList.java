package main.java.collection;

import main.java.model.Task;

import java.util.*;

public class CustomLinkedList { //перенёс, написал кастомлинкедлист, касаемо ноды не увидел в тз где она должна быть

    private Node first;
    private Node last;

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node element = first;
        while (element != null) {
            tasks.add(element.getItem());
            element = element.getNext();
        }
        return tasks;
    }

    public void clear() {
        first = null;
        last = null;
    }

    public Node linkLast(Task task) {
        Node newNode = new Node(task, null, last);
        if (first == null) {
            first = newNode;
        } else {
            last.setNext(newNode);
        }
        last = newNode;
        return newNode;
    }

    public void removeNode(Node node) {
        if (node.equals(first)) {
            first = node.getNext();
            if (node.getNext() != null) {
                node.getNext().setPrev(null);
            }
        } else {
            node.getPrev().setNext(node.getNext());
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            }
        }
    }
}
package taskmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Collection;

// CustomHashMap Class
class CustomHashMap<K, V> {
    private final Map<K, V> map;

    public CustomHashMap() {
        map = new HashMap<>();
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public V remove(K key) {
        return map.remove(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public void printAllEntries() {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int size() {
        return map.size();
    }

    public Collection<V> getAllValues() {
        return map.values();
    }
}

// Task Class
class Task {
    private String id;
    private String name;
    private String deadline;
    private String status;
    private int priority;

    public Task(String id, String name, String deadline, String status, int priority) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDeadline() { return deadline; }
    public String getStatus() { return status; }
    public int getPriority() { return priority; }

    public void setName(String name) { this.name = name; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public void setStatus(String status) { this.status = status; }
    public void setPriority(int priority) { this.priority = priority; }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", deadline='" + deadline + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                '}';
    }
}

// TaskManager Class
public class TaskManager {
    private CustomHashMap<String, Task> taskMap;

    public TaskManager() {
        taskMap = new CustomHashMap<>();
    }

    public void addTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void removeTask(String taskId) {
        if (taskMap.remove(taskId) == null) {
            System.out.println("Task not found.");
        }
    }

    public void editTask(String taskId, String newName, String newDeadline, String newStatus, int newPriority) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.setName(newName);
            task.setDeadline(newDeadline);
            task.setStatus(newStatus);
            task.setPriority(newPriority);
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    public void viewAllTasks() {
        taskMap.printAllEntries();
    }

    public void searchTaskByName(String name) {
        boolean found = false;
        for (Task task : taskMap.getAllValues()) {
            if (task.getName().equalsIgnoreCase(name)) {
                System.out.println(task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Task not found.");
        }
    }

    public void searchTaskByDate(String date) {
        boolean found = false;
        for (Task task : taskMap.getAllValues()) {
            if (task.getDeadline().equals(date)) {
                System.out.println(task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Task not found.");
        }
    }

    public void sortTasksByPriority() {
        taskMap.getAllValues().stream()
            .sorted((t1, t2) -> Integer.compare(t1.getPriority(), t2.getPriority()))
            .forEach(System.out::println);
    }

    public void sortTasksByDeadline() {
        taskMap.getAllValues().stream()
            .sorted((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()))
            .forEach(System.out::println);
    }

    public void markTaskAsCompleted(String taskId) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.setStatus("Completed");
            System.out.println("Task marked as completed.");
        } else {
            System.out.println("Task not found.");
        }
    }

    public void filterTasksByStatus(String status) {
        boolean found = false;
        for (Task task : taskMap.getAllValues()) {
            if (task.getStatus().equalsIgnoreCase(status)) {
                System.out.println(task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks with the given status.");
        }
    }

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            System.out.println("\nTask Manager Menu");
            System.out.println("1. Add Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. Edit Task");
            System.out.println("4. Remove Task");
            System.out.println("5. Search Task by Name");
            System.out.println("6. Search Task by Date");
            System.out.println("7. Sort Tasks by Priority");
            System.out.println("8. Sort Tasks by Deadline");
            System.out.println("9. Mark Task as Completed");
            System.out.println("10. Filter Tasks by Status");
            System.out.println("11. Exit");
            System.out.print("Choose an option: ");
            int menuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (menuChoice) {
                case 1:
                    System.out.print("Enter task ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter task name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter task deadline (yyyy-MM-dd): ");
                    String deadline = scanner.nextLine();
                    System.out.print("Enter task status: ");
                    String status = scanner.nextLine();
                    System.out.print("Enter task priority: ");
                    int priority = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Task task = new Task(id, name, deadline, status, priority);
                    manager.addTask(task);
                    System.out.println("Task added successfully.");
                    break;
                case 2:
                    manager.viewAllTasks();
                    break;
                case 3:
                    System.out.print("Enter task ID to edit: ");
                    String editId = scanner.nextLine();
                    System.out.print("Enter new task name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new task deadline (yyyy-MM-dd): ");
                    String newDeadline = scanner.nextLine();
                    System.out.print("Enter new task status: ");
                    String newStatus = scanner.nextLine();
                    System.out.print("Enter new task priority: ");
                    int newPriority = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    manager.editTask(editId, newName, newDeadline, newStatus, newPriority);
                    break;
                case 4:
                    System.out.print("Enter task ID to remove: ");
                    String removeId = scanner.nextLine();
                    manager.removeTask(removeId);
                    break;
                case 5:
                    System.out.print("Enter task name to search: ");
                    String searchName = scanner.nextLine();
                    manager.searchTaskByName(searchName);
                    break;
                case 6:
                    System.out.print("Enter task deadline to search (yyyy-MM-dd): ");
                    String searchDate = scanner.nextLine();
                    manager.searchTaskByDate(searchDate);
                    break;
                case 7:
                    manager.sortTasksByPriority();
                    break;
                case 8:
                    manager.sortTasksByDeadline();
                    break;
                case 9:
                    System.out.print("Enter task ID to mark as completed: ");
                    String completeId = scanner.nextLine();
                    manager.markTaskAsCompleted(completeId);
                    break;
                case 10:
                    System.out.print("Enter status to filter tasks by: ");
                    String filterStatus = scanner.nextLine();
                    manager.filterTasksByStatus(filterStatus);
                    break;
                case 11:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }

        scanner.close();
    }
}

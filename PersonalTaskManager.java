package taskmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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

public class PersonalTaskManager {
    private HashMap<String, Task> tasks = new HashMap<>();
    private HashMap<String, String> users = new HashMap<>();
    private HashMap<String, LinkedList<Task>> userTasks = new HashMap<>();

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        return true;
    }

    public boolean loginUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public boolean removeTask(String taskId) {
        return tasks.remove(taskId) != null;
    }

    public boolean editTask(String taskId, String newName, String newDeadline, String newStatus, int newPriority) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setName(newName);
            task.setDeadline(newDeadline);
            task.setStatus(newStatus);
            task.setPriority(newPriority);
            return true;
        }
        return false;
    }

    public void viewAllTasks() {
        tasks.values().forEach(System.out::println);
    }

    public Task searchTaskByName(String name) {
        return tasks.values().stream()
                .filter(task -> task.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Task searchTaskByDate(String date) {
        return tasks.values().stream()
                .filter(task -> task.getDeadline().equals(date))
                .findFirst()
                .orElse(null);
    }

    public void sortTasksByPriority() {
        tasks.values().stream()
                .sorted(Comparator.comparingInt(Task::getPriority))
                .forEach(System.out::println);
    }

    public void sortTasksByDeadline() {
        tasks.values().stream()
                .sorted(Comparator.comparing(Task::getDeadline))
                .forEach(System.out::println);
    }

    public boolean markTaskAsCompleted(String taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setStatus("Completed");
            return true;
        }
        return false;
    }

    public void filterTasksByStatus(String status) {
        tasks.values().stream()
                .filter(task -> task.getStatus().equalsIgnoreCase(status))
                .forEach(System.out::println);
    }

    public void assignTaskToUser(String username, Task task) {
        userTasks.computeIfAbsent(username, k -> new LinkedList<>()).add(task);
    }

    public void viewTasksAssignedToUser(String username) {
        LinkedList<Task> assignedTasks = userTasks.get(username);
        if (assignedTasks != null) {
            assignedTasks.forEach(System.out::println);
        } else {
            System.out.println("No tasks assigned to this user.");
        }
    }

    public void setTaskReminder(Task task) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Reminder: " + task);
            }
        }, new Date(task.getDeadline()));
    }

    public void notifyUpcomingTasks() {
        Date now = new Date();
        tasks.values().stream()
                .filter(task -> {
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd").parse(task.getDeadline()).after(now);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .forEach(task -> System.out.println("Upcoming Task: " + task));
    }

    public void exportTasksToCSV(String filePath) throws IOException {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            for (Task task : tasks.values()) {
                csvWriter.append(task.getId()).append(",")
                        .append(task.getName()).append(",")
                        .append(task.getDeadline()).append(",")
                        .append(task.getStatus()).append(",")
                        .append(String.valueOf(task.getPriority())).append("\n");
            }
        }
    }

    public void importTasksFromCSV(String filePath) throws IOException {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length == 5) {
                    Task task = new Task(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]));
                    tasks.put(task.getId(), task);
                }
            }
        }
    }

    public void taskAnalytics() {
        HashMap<String, Integer> analytics = new HashMap<>();
        for (Task task : tasks.values()) {
            String month = task.getDeadline().substring(0, 7);
            analytics.put(month, analytics.getOrDefault(month, 0) + 1);
        }
        analytics.forEach((month, count) -> System.out.println("Month: " + month + ", Tasks: " + count));
    }

    public void generateTaskReport() {
        tasks.values().forEach(task -> System.out.println("Task Report: " + task));
    }

    public static void main(String[] args) {
        PersonalTaskManager manager = new PersonalTaskManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Personal Task Manager");
        boolean loggedIn = false;
        String username = null;

        while (!loggedIn) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter username: ");
            username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (choice == 1) {
                if (manager.registerUser(username, password)) {
                    System.out.println("Registration successful!");
                } else {
                    System.out.println("Username already exists.");
                }
            } else if (choice == 2) {
                if (manager.loginUser(username, password)) {
                    System.out.println("Login successful!");
                    loggedIn = true;
                } else {
                    System.out.println("Invalid username or password.");
                }
            } else {
                System.out.println("Invalid option.");
            }
        }

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
            System.out.println("11. View Tasks Assigned to User");
            System.out.println("12. Notify Upcoming Tasks");
            System.out.println("13. Task Analytics");
            System.out.println("14. Generate Task Report");
            System.out.println("15. Export Tasks to CSV");//(for storing task data temporarily)
            System.out.println("16. Import Tasks from CSV");//(for reading task data temporarily)
            System.out.println("17. Exit");
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
                    manager.assignTaskToUser(username, task);
                    System.out.println("Task added and assigned successfully.");
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

                    if (manager.editTask(editId, newName, newDeadline, newStatus, newPriority)) {
                        System.out.println("Task updated successfully.");
                    } else {
                        System.out.println("Task not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter task ID to remove: ");
                    String removeId = scanner.nextLine();
                    if (manager.removeTask(removeId)) {
                        System.out.println("Task removed successfully.");
                    } else {
                        System.out.println("Task not found.");
                    }
                    break;
                case 5:
                    System.out.print("Enter task name to search: ");
                    String searchName = scanner.nextLine();
                    Task taskByName = manager.searchTaskByName(searchName);
                    if (taskByName != null) {
                        System.out.println("Task found: " + taskByName);
                    } else {
                        System.out.println("Task not found.");
                    }
                    break;
                case 6:
                    System.out.print("Enter task deadline to search (yyyy-MM-dd): ");
                    String searchDate = scanner.nextLine();
                    Task taskByDate = manager.searchTaskByDate(searchDate);
                    if (taskByDate != null) {
                        System.out.println("Task found: " + taskByDate);
                    } else {
                        System.out.println("Task not found.");
                    }
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
                    if (manager.markTaskAsCompleted(completeId)) {
                        System.out.println("Task marked as completed.");
                    } else {
                        System.out.println("Task not found.");
                    }
                    break;
                case 10:
                    System.out.print("Enter status to filter tasks by: ");
                    String filterStatus = scanner.nextLine();
                    manager.filterTasksByStatus(filterStatus);
                    break;
                case 11:
                    manager.viewTasksAssignedToUser(username);
                    break;
                case 12:
                    manager.notifyUpcomingTasks();
                    break;
                case 13:
                    manager.taskAnalytics();
                    break;
                case 14:
                    manager.generateTaskReport();
                    break;
                case 15:
                    System.out.print("Enter file path to export tasks to CSV: ");
                    String exportPath = scanner.nextLine();
                    try {
                        manager.exportTasksToCSV(exportPath);
                        System.out.println("Tasks exported successfully.");
                    } catch (IOException e) {
                        System.out.println("Error exporting tasks: " + e.getMessage());
                    }
                    break;
                case 16:
                    System.out.print("Enter file path to import tasks from CSV: ");
                    String importPath = scanner.nextLine();
                    try {
                        manager.importTasksFromCSV(importPath);
                        System.out.println("Tasks imported successfully.");
                    } catch (IOException e) {
                        System.out.println("Error importing tasks: " + e.getMessage());
                    }
                    break;
                case 17:
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


package taskmanager;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// User Class
class User {
    private final String username;
    private final String password; // In a real application, passwords should be hashed.

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}

// UserManager Class
class UserManager {
    private final Map<String, User> users;
    private User loggedInUser;

    public UserManager() {
        users = new HashMap<>();
        loggedInUser = null;
    }

    //Adding 'final' for a definite outcome. -B
    public void register(final String username, final String password) {
        if (users.containsKey(username)) {
            System.out.println("User already exists.");
            return;
        }
        users.put(username, new User(username, password));
        System.out.println("User registered successfully.");
    }

    //Refined use of 'optional' case which replaces the need of checking for null values or returning them. -B
    public Optional<User> getUser(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public boolean login(String username, String password) {
        Optional<User> userOpt = getUser(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            loggedInUser = userOpt.get();
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    public void logout() {
        loggedInUser = null;
        System.out.println("Logged out successfully.");
    }

    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}

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

    public void exportToCSV(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            System.out.println("Data exported to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    public void importFromCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    K key = (K) parts[0];
                    V value = (V) parts[1];
                    map.put(key, value);
                }
            }
            System.out.println("Data imported from CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error importing from CSV: " + e.getMessage());
        }
    }
}

// Task Class
class Task {
    private String id;
    private String name;
    private String deadline;
    private String status;
    private int priority;
    private String assignedTo;
    private String reminder;

    public Task(String id, String name, String deadline, String status, int priority) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.assignedTo = null;
        this.reminder = null;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDeadline() { return deadline; }
    public String getStatus() { return status; }
    public int getPriority() { return priority; }
    public String getAssignedTo() { return assignedTo; }
    public String getReminder() { return reminder; }

    public void setName(String name) { this.name = name; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public void setStatus(String status) { this.status = status; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setAssignedTo(String username) { this.assignedTo = username; }
    public void setReminder(String reminder) { this.reminder = reminder; }

    //Best practices - Use of String.format instead of the '+' operator. -B
    @Override
    public String toString() {
        return String.format("Task{id='%s', name='%s', deadline='%s', status='%s', priority=%d, assignedTo='%s', reminder='%s'}",
                id, name, deadline, status, priority, assignedTo, reminder);
    }
}

// TaskManager Class
public class TaskManager {
    private CustomHashMap<String, Task> taskMap;
    private UserManager userManager;

    public TaskManager() {
        taskMap = new CustomHashMap<>();
        userManager = new UserManager();
    }

    public void addTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    //Standardisation of error messages, use of 'final', and rephrasing error message semantics -B
    public void removeTask(final String taskId) {
        if (taskMap.remove(taskId) == null) {
            System.out.println("Task not found.");
        } else {
            System.out.println("Task removed successfully.");
        }
    }

    //adding 'final' for a definite outcome -B
    public void editTask(final String taskId, final String newName, final String newDeadline, final String newStatus, final int newPriority) {
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

    //Best practices - Use of 'final' and 'streams' -B
    public void searchTaskByName(final String name) {
        List<Task> tasks = taskMap.getAllValues().stream()
                .filter(task -> task.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
        if (tasks.isEmpty()) {
            System.out.println("Task not found.");
        } else {
            tasks.forEach(System.out::println);
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

    //Best practices - Use of constants for repeatedly used strings -B
    public static final String COMPLETED = "Completed";
    public static final String TASK_NOT_FOUND = "Task not found.";

    public void markTaskAsCompleted(final String taskId) {
        final Task task = taskMap.get(taskId);
        if (task != null) {
            task.setStatus(COMPLETED);
            System.out.println("Task marked as completed.");
        } else {
            System.out.println(TASK_NOT_FOUND);
        }
    }

    //Best practices - Use of 'final' and 'streams' -B
    public void filterTasksByStatus(final String status) {
        List<Task> tasks = taskMap.getAllValues().stream()
                .filter(task -> task.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
        if (tasks.isEmpty()) {
            System.out.println("No tasks with the given status.");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    public void assignTaskToUser(String taskId, String username) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.setAssignedTo(username);
            System.out.println("Task assigned to " + username + " successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    public void viewTasksAssignedToUser(String username) {
        boolean found = false;
        for (Task task : taskMap.getAllValues()) {
            if (username.equals(task.getAssignedTo())) {
                System.out.println(task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks assigned to " + username);
        }
    }

    public void setTaskReminder(String taskId, String reminder) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.setReminder(reminder);
            System.out.println("Reminder set for task.");
        } else {
            System.out.println("Task not found.");
        }
    }

    public void notifyUpcomingTasks() {
        System.out.println("Upcoming tasks:");
        for (Task task : taskMap.getAllValues()) {
            if (task.getReminder() != null) {
                System.out.println(task);
            }
        }
    }

    public void exportTasksToCSV(String filename) {
        taskMap.exportToCSV(filename);
    }

    public void importTasksFromCSV(String filename) {
        taskMap.importFromCSV(filename);
    }

    public void taskAnalytics() {
        Map<String, Long> statusCount = taskMap.getAllValues().stream()
            .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        System.out.println("Task Analytics:");
        statusCount.forEach((status, count) -> System.out.println(status + ": " + count));
    }

    public void generateTaskReport() {
        System.out.println("Generating task report...");
        // Example of generating a report based on hash map entries.
        taskMap.printAllEntries();
    }

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        boolean loggedIn = false;

        while (!exit) {
            if (!loggedIn) {
                System.out.println("\nUser Management Menu");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int userChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (userChoice) {
                    case 1:
                        System.out.print("Enter username: ");
                        String regUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String regPassword = scanner.nextLine();
                        manager.userManager.register(regUsername, regPassword);
                        break;
                    case 2:
                        System.out.print("Enter username: ");
                        String loginUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginPassword = scanner.nextLine();
                        loggedIn = manager.userManager.login(loginUsername, loginPassword);
                        break;
                    case 3:
                        exit = true;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option.");
                        break;
                }
            } else {
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
                System.out.println("11. Assign Task to User");
                System.out.println("12. View Tasks Assigned to User");
                System.out.println("13. Set Task Reminder");
                System.out.println("14. Notify Upcoming Tasks");
                System.out.println("15. Export Tasks to CSV");
                System.out.println("16. Import Tasks from CSV");
                System.out.println("17. Task Analytics");
                System.out.println("18. Generate Task Report");
                System.out.println("19. Logout");
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
                        System.out.print("Enter task ID to assign: ");
                        String assignId = scanner.nextLine();
                        System.out.print("Enter username to assign task to: ");
                        String username = scanner.nextLine();
                        manager.assignTaskToUser(assignId, username);
                        break;
                    case 12:
                        System.out.print("Enter username to view tasks: ");
                        String user = scanner.nextLine();
                        manager.viewTasksAssignedToUser(user);
                        break;
                    case 13:
                        System.out.print("Enter task ID to set reminder: ");
                        String reminderId = scanner.nextLine();
                        System.out.print("Enter reminder details: ");
                        String reminderDetails = scanner.nextLine();
                        manager.setTaskReminder(reminderId, reminderDetails);
                        break;
                    case 14:
                        manager.notifyUpcomingTasks();
                        break;
                    case 15:
                        System.out.print("Enter filename to export tasks: ");
                        String exportFilename = scanner.nextLine();
                        manager.exportTasksToCSV(exportFilename);
                        break;
                    case 16:
                        System.out.print("Enter filename to import tasks: ");
                        String importFilename = scanner.nextLine();
                        manager.importTasksFromCSV(importFilename);
                        break;
                    case 17:
                        manager.taskAnalytics();
                        break;
                    case 18:
                        manager.generateTaskReport();
                        break;
                    case 19:
                        manager.userManager.logout();
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                        break;
                }
            }
        }

        scanner.close();
    }
}


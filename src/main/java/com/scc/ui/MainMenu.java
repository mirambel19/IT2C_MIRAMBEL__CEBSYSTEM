package ui;

import dao.UserDao;
import model.User;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();
    private User currentUser = null;

    public void displayMainMenu() {
        while (true) {
            System.out.println("\n===== CAMPUS EQUIPMENT BORROWING SYSTEM =====");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> registerMenu();
                case "2" -> loginMenu();
                case "3" -> {
                    System.out.println("Exiting system... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private void registerMenu() {
        System.out.println("\n--- Register New Account ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.println("Select Role:");
        System.out.println("1. TEACHER");
        System.out.println("2. STUDENT");
        System.out.print("Enter choice: ");
        String roleChoice = scanner.nextLine().trim();

        String role = switch (roleChoice) {
            case "1" -> "TEACHER";
            case "2" -> "STUDENT";
            default -> {
                System.out.println("Invalid role! Defaulting to STUDENT.");
                yield "STUDENT";
            }
        };

        if (userDao.registerUser(username, password, role)) {
            System.out.println("Registration successful! Status: PENDING (waiting approval).");
        }
    }

    private void loginMenu() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        Optional<User> userOpt = userDao.login(username, password);
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            System.out.printf("Welcome, %s! Role: %s | Status: %s%n",
                    currentUser.username(), currentUser.role(), currentUser.status());
            userDashboard();
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    private void userDashboard() {
        while (currentUser != null) {
            System.out.println("\n===== USER DASHBOARD =====");
            System.out.println("1. View Profile");
            System.out.println("2. Logout");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                System.out.println("ID: " + currentUser.id());
                System.out.println("Username: " + currentUser.username());
                System.out.println("Role: " + currentUser.role());
                System.out.println("Status: " + currentUser.status());
            } else if (choice.equals("2")) {
                System.out.println("Logged out successfully!");
                currentUser = null;
                return;
            }
        }
    }
}
import java.util.*;

public class TollPayment {
    private static final Map<String, Double> tollRates    = new HashMap<>();
    private static final Set<String> exemptVehicles       = new HashSet<>();
    private static final List<String> tollBooths          = Arrays.asList("Booth A", "Booth B", "Booth C", "Booth D");

    private String  vehicleType;
    private String  vehicleNumber;
    private double  balance;
    private boolean hasFastTag;
    private List<String> transactionHistory;

    static {
        tollRates.put("Car", 5.0);
        tollRates.put("Truck", 10.0);
        tollRates.put("Bike", 2.0);
        tollRates.put("Bus", 8.0);
        tollRates.put("Van", 6.0);
        tollRates.put("SUV", 7.0);

        exemptVehicles.add("Ambulance");
        exemptVehicles.add("Police");
        exemptVehicles.add("Fire Truck");
    }

    public TollPayment(String vehicleType, String vehicleNumber, double balance, boolean hasFastTag) {
        this.vehicleType   = vehicleType.substring(0, 1).toUpperCase() + vehicleType.substring(1).toLowerCase();
        this.vehicleNumber = vehicleNumber;
        this.balance       = balance;
        this.hasFastTag    = hasFastTag;
        this.transactionHistory = new ArrayList<>();
    }

    public boolean payToll(String booth) {
        if (!tollBooths.contains(booth)) {
            System.out.println("Invalid booth name. Please enter a valid booth.");
            return false;
        }

        if (exemptVehicles.contains(vehicleType)) {
            System.out.println("Toll exempt for " + vehicleType + ". Free passage.");
            transactionHistory.add("Toll exempt at " + booth);
            return true;
        }

        Double tollAmount = tollRates.get(vehicleType);
        if (tollAmount == null) {
            System.out.println("Invalid vehicle type.");
            return false;
        }

        if (hasFastTag) {
            tollAmount *= 0.9; // 10% discount for FastTag users
        }

        if (balance >= tollAmount) {
            balance -= tollAmount;
            String transaction = "Paid " + tollAmount + " at " + booth + ". Remaining balance: " + balance;
            transactionHistory.add(transaction);
            System.out.println(transaction);
            return true;
        } else {
            System.out.println("Insufficient balance! Please recharge.");
            return false;
        }
    }

    public void recharge(double amount) {
        balance += amount;
        String transaction = "Recharged with " + amount + ". New balance: " + balance;
        transactionHistory.add(transaction);
        System.out.println(transaction);
    }

    public void toggleFastTag() {
        this.hasFastTag = !this.hasFastTag;
        System.out.println("FastTag status updated: " + (hasFastTag ? "Enabled" : "Disabled"));
    }

    public void showVehicleInfo() {
        System.out.println("Vehicle Number: " + vehicleNumber);
        System.out.println("Vehicle Type: " + vehicleType);
        System.out.println("Balance: " + balance);
        System.out.println("FastTag Enabled: " + (hasFastTag ? "Yes" : "No"));
    }

    public void showTransactionHistory() {
        System.out.println("Transaction History for " + vehicleNumber + ":");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String record : transactionHistory) {
                System.out.println(record);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Vehicle Type: ");
        String vehicleType = scanner.nextLine();
        System.out.print("Enter Vehicle Number: ");
        String vehicleNumber = scanner.nextLine();
        System.out.print("Enter Initial Balance: ");
        double balance = scanner.nextDouble();
        System.out.print("Does the vehicle have FastTag? (true/false): ");
        boolean hasFastTag = scanner.nextBoolean();
        scanner.nextLine(); // Fix for skipping input issue

        TollPayment vehicle = new TollPayment(vehicleType, vehicleNumber, balance, hasFastTag);

        while (true) {
            System.out.println("\n===== Toll Payment System =====");
            System.out.println("1. Pay Toll");
            System.out.println("2. Recharge Account");
            System.out.println("3. Toggle FastTag");
            System.out.println("4. Show Vehicle Info");
            System.out.println("5. Show Transaction History");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    System.out.println("Available Toll Booths: " + tollBooths);
                    System.out.print("Enter Booth Name: ");
                    String booth = scanner.nextLine();
                    vehicle.payToll(booth);
                    break;
                case 2:
                    System.out.print("Enter Recharge Amount: ");
                    if (scanner.hasNextDouble()) {
                        double amount = scanner.nextDouble();
                        vehicle.recharge(amount);
                    } else {
                        System.out.println("Invalid amount! Please enter a valid number.");
                        scanner.nextLine(); // Clear invalid input
                    }
                    break;
                case 3:
                    vehicle.toggleFastTag();
                    break;
                case 4:
                    vehicle.showVehicleInfo();
                    break;
                case 5:
                    vehicle.showTransactionHistory();
                    break;
                case 6:
                    System.out.println("Exiting Toll Payment System. Thank you!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
import java.util.*;

public class TollPayment {
    private static final Map<String, Double> tollRates = new HashMap<>();
    private static final Set<String> exemptVehicles = new HashSet<>();
    private String vehicleType;
    private String vehicleNumber;
    private double balance;
    private boolean hasFastTag;
    private List<String> transactionHistory;
    private static final List<String> tollBooths = Arrays.asList("Booth A", "Booth B", "Booth C", "Booth D");

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
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.balance = balance;
        this.hasFastTag = hasFastTag;
        this.transactionHistory = new ArrayList<>();
    }

    public boolean payToll(String booth) {
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

    public void toggleFastTag(boolean status) {
        this.hasFastTag = status;
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
        TollPayment vehicle1 = new TollPayment("Car", "ABC123", 10.0, true);
        vehicle1.showVehicleInfo();
        for (String booth : tollBooths) {
            vehicle1.payToll(booth);
        }
        vehicle1.recharge(20.0);
        vehicle1.payToll(tollBooths.get(1));
        vehicle1.showTransactionHistory();

        TollPayment emergencyVehicle = new TollPayment("Ambulance", "EMR001", 50.0, false);
        emergencyVehicle.showVehicleInfo();
        emergencyVehicle.payToll(tollBooths.get(2));
        emergencyVehicle.showTransactionHistory();
    }
}

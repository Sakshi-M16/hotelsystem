import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

public class HotelManagementSystem {

    private static Connection conn;

    public static void main(String[] args) {
        try {
            // Establish a connection to the SQLite database
            conn = DriverManager.getConnection("jdbc:sqlite:hotelmanagement.db");

            // Display available rooms
            displayAvailableRooms();

            // Take booking input
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the Room ID to book: ");
            int roomId = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over
            System.out.print("Enter customer name: ");
            String customerName = scanner.nextLine();
            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Enter number of adults: ");
            int numberOfAdults = scanner.nextInt();
            System.out.print("Enter number of children: ");
            int numberOfChildren = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            String checkInDate = scanner.nextLine();
            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            String checkOutDate = scanner.nextLine();

            // Calculate total price
            int numberOfNights = calculateNumberOfNights(checkInDate, checkOutDate);
            double totalPrice = numberOfNights * getRoomPrice(roomId);
            System.out.println("Total price for " + numberOfNights + " nights: " + totalPrice);

            // Process payment
            System.out.print("Enter payment amount: ");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter card number: ");
            String cardNumber = scanner.nextLine();
            System.out.print("Enter card expiration date (MM/YY): ");
            String cardExpiry = scanner.nextLine();
            System.out.print("Enter card CVV: ");
            String cardCVV = scanner.nextLine();

            // Simulate payment processing
            processPayment(paymentAmount, totalPrice);

            // Confirm booking
            if (processBooking(roomId, customerName, phoneNumber, numberOfAdults, numberOfChildren, checkInDate, checkOutDate, totalPrice)) {
                System.out.println("Booking confirmed for " + customerName + ". Thank you for choosing us!");
            } else {
                System.out.println("Error creating booking.");
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            // Close database connection
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Display available rooms
    private static void displayAvailableRooms() {
        try {
            String sql = "SELECT * FROM Rooms WHERE availability = 'Available'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Available Rooms:");
            while (rs.next()) {
                int roomId = rs.getInt("room_id");
                String roomType = rs.getString("room_type");
                double pricePerNight = rs.getDouble("price_per_night");
                System.out.println("Room ID: " + roomId + ", Type: " + roomType + ", Price per night: " + pricePerNight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Calculate number of nights between check-in and check-out
    private static int calculateNumberOfNights(String checkInDate, String checkOutDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date checkIn = sdf.parse(checkInDate);
        java.util.Date checkOut = sdf.parse(checkOutDate);
        long diffInMillis = checkOut.getTime() - checkIn.getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));  // Convert millis to days
    }

    // Get the price of a room by room ID
    private static double getRoomPrice(int roomId) {
        double price = 0;
        try {
            String sql = "SELECT price_per_night FROM Rooms WHERE room_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                price = rs.getDouble("price_per_night");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }

    // Process payment (Simulated)
    private static void processPayment(double paymentAmount, double totalPrice) {
        if (paymentAmount >= totalPrice) {
            System.out.println("Payment of " + paymentAmount + " processed successfully.");
        } else {
            System.out.println("Insufficient payment.");
        }
    }

    // Process booking and update the room availability
    private static boolean processBooking(int roomId, String customerName, String phoneNumber, int numberOfAdults, int numberOfChildren, String checkInDate, String checkOutDate, double totalPrice) {
        try {
            // Check if room is available
            String checkAvailabilitySql = "SELECT availability FROM Rooms WHERE room_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkAvailabilitySql);
            checkStmt.setInt(1, roomId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getString("availability").equals("Available")) {
                // Insert booking record
                String bookingSql = "INSERT INTO Bookings (customer_name, phone_number, check_in_date, check_out_date, room_id, total_price) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement bookingStmt = conn.prepareStatement(bookingSql);
                bookingStmt.setString(1, customerName);
                bookingStmt.setString(2, phoneNumber);
                bookingStmt.setString(3, checkInDate);
                bookingStmt.setString(4, checkOutDate);
                bookingStmt.setInt(5, roomId);
                bookingStmt.setDouble(6, totalPrice);
                bookingStmt.executeUpdate();

                // Update room availability
                String updateRoomSql = "UPDATE Rooms SET availability = 'Booked' WHERE room_id = ?";
                PreparedStatement updateRoomStmt = conn.prepareStatement(updateRoomSql);
                updateRoomStmt.setInt(1, roomId);
                updateRoomStmt.executeUpdate();

                return true;
            } else {
                System.out.println("Room is already booked.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

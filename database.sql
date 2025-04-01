CREATE TABLE Rooms (
    room_id INTEGER PRIMARY KEY,
    room_type TEXT NOT NULL,
    price_per_night REAL NOT NULL,
    amenities TEXT,
    bed_type TEXT NOT NULL,
    breakfast_inclusion TEXT CHECK (breakfast_inclusion IN ('Included', 'Not Included')),
    description TEXT,
    area_sqft INTEGER NOT NULL,
    availability TEXT CHECK (availability IN ('Available', 'Booked'))
);

CREATE TABLE Bookings (
    booking_id INTEGER PRIMARY KEY,
    customer_name TEXT NOT NULL,
    phone_number TEXT NOT NULL,
    number_of_adults INTEGER NOT NULL,
    number_of_children INTEGER NOT NULL,
    check_in_date TEXT NOT NULL,
    check_out_date TEXT NOT NULL,
    room_id INTEGER,
    total_price REAL NOT NULL,
    FOREIGN KEY (room_id) REFERENCES Rooms(room_id)
);

CREATE TABLE Payments (
    payment_id INTEGER PRIMARY KEY,
    booking_id INTEGER,
    amount_paid REAL NOT NULL,
    payment_status TEXT CHECK (payment_status IN ('Pending', 'Completed')),
    FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
);

PRAGMA foreign_keys = ON;

INSERT INTO Rooms VALUES (1, 'Standard Room', 8000, 'WiFi, TV, AC', 'Twin Bed', 'Not Included', 'A cozy room with basic amenities suitable for budget travelers.', 250, 'Available');
INSERT INTO Rooms VALUES (2, 'Deluxe Room', 16000, 'WiFi, TV, Mini Bar, AC', 'King Bed', 'Included', 'A luxurious room offering modern amenities with elegant furnishings. Ideal for a relaxing and comfortable stay.', 400, 'Available');
INSERT INTO Rooms VALUES (3, 'Executive Room', 20000, 'WiFi, TV, Mini Bar, AC, Work Desk, Lounge Access', 'King Bed', 'Included', 'A sophisticated room designed for business travelers with exclusive lounge access and premium comfort.', 500, 'Available');
INSERT INTO Rooms VALUES (4, 'Royal Suite', 30000, 'WiFi, TV, Mini Bar, Jacuzzi, AC, Spa Access, Ocean View', 'King Bed', 'Included', 'An opulent suite offering stunning views, spa access, and top-tier amenities for a majestic experience.', 700, 'Available');
INSERT INTO Rooms VALUES (5, 'Presidential Suite', 40000, 'WiFi, TV, Mini Bar, Jacuzzi, AC, Personal Butler', 'King Bed', 'Included', 'The most luxurious suite featuring lavish amenities with top-class services and privacy.', 1000, 'Available');

INSERT INTO Bookings VALUES (1, 'John Doe', '987654321','2025-04-01','1','0', '2025-04-05', 2, 64000);
INSERT INTO Bookings VALUES (2, 'Jane Smith','8978675634', '2025-04-10', '2','3','2025-04-15', 4, 150000);

INSERT INTO Payments VALUES (1, 1, 64000, 'Completed');
INSERT INTO Payments VALUES (2, 2, 150000, 'Pending');

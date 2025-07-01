# Honeycomb Java Application

Honeycomb is a desktop application for managing honey inventory and sales, featuring admin and customer roles. Admins can manage honey types, stock, and prices, while customers can browse and purchase honey.

## Features

- User authentication (admin and customer)
- Admin: manage honey types, stock, prices, and view transactions
- Customer: browse and purchase honey
- Persistent data storage in text files

## Getting Started

### Prerequisites

- Java JDK 8 or higher

### How to Run

1. Open a terminal in the project directory.
2. Compile the Java files:
   ```
   javac *.java Entities/*.java
   ```
3. Run the application:
   ```
   java Start
   ```

## File Structure

- `Start.java` — Application entry point
- `Frame.java` — Main window and login
- `RegisterFrame.java` — Registration window
- `Homepage.java` — Dashboard for admin/customer
- `Entities/Account.java` — User account logic
- `Data.txt` — User credentials and roles
- `honey_data.txt` — Honey inventory and prices
- `transactions.txt` — Transaction logs
- `Pics/` — Images for the UI

## Data Files

- **Data.txt:**  
  ```
  username<TAB>password<TAB>userType
  ```
- **honey_data.txt:**  
  ```
  honeyType,quantity,price
  ```
- **transactions.txt:**  
  ```
  timestamp - USER (username): action details
  ```

## Authors

- Kazi Sajid Islam

---


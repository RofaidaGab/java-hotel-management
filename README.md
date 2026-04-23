# 🏨 Hotel De Luna — Management System

A JavaFX-based desktop application for managing hotel operations including room reservations, customer records, and employee management. Data is persisted across sessions using CSV files.

---

## ✨ Features

- 🔐 **Authentication System** — Role-based login via `LoginAgent`; employees log in with credentials from `employees.csv`, a built-in admin account has full privileges
- 🛏️ **Room Availability View** — Select a date range and view room status instantly with a color-coded grid:
  - 🟢 **Green** — Fully available
  - 🔴 **Red** — Fully occupied
  - 🟠 **Orange** — Partially occupied (overlapping bookings)
- 📋 **Booking Details** — Click any room to see all customers booked for it, sorted alphabetically then by check-in date (via `Comparable`)
- 💰 **Auto Bill Calculation** — Total bill per customer is calculated on load based on room fee × number of nights
- 👥 **Employee Management** — View, edit, and delete employee records subject to a permission hierarchy
- 💾 **CSV Persistence** — All changes (add, edit, delete) are immediately written back to the corresponding CSV file

---

## 🔑 Authentication

| Account | Username | Password | Privileges |
|---|---|---|---|
| Administrator | `admin` | `admin` | Full access — can edit and delete any employee record |
| Regular Employee | _(from `employees.csv`)_ | _(from `employees.csv`)_ | Can view all, edit own record only |

Sample CSV files for customers, rooms, and employees are included in the repository for testing.

---

## 🏗️ Architecture & Design

### Navigation
Scene transitions are managed by a `SceneManager` class using Java's built-in `Stack` — supporting `.switchTo(scene)` and `.goBack()` for seamless navigation between views.

### Permission Hierarchy

| Action | Regular Employee | Administrator |
|---|---|---|
| View employees | ✅ | ✅ |
| Edit own record | ✅ | ✅ |
| Edit any record | ❌ | ✅ |
| Delete any record | ❌ | ✅ |

### Key Classes

| Class | Role |
|---|---|
| `LoginAgent` | Handles authentication and authorization logic |
| `Employee` | Implements `ILoginable` interface; tracks currently logged-in user |
| `SceneManager` | Manages scene switching using a Stack |
| `Customer` | Implements `Comparable` for sorted display |

---

## 💾 Data Storage

All data lives in three CSV files loaded into memory at startup:

| File | Contents |
|---|---|
| `customers.csv` | Customer name, phone, room, check-in/out dates |
| `rooms.csv` | Room number, type, nightly fee |
| `employees.csv` | Employee credentials, name, role |

---

## 🛠️ Tech Stack

| Tool | Purpose |
|---|---|
| Java | Core language |
| JavaFX | GUI framework |
| CSV files | Data persistence |
| Java Stack | Scene navigation |
| Comparable interface | Sorted customer display |

---

## 🚀 How to Run

### Prerequisites
- Java JDK 11 or higher
- JavaFX SDK ([download here](https://openjfx.io/))

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/YourUsername/hotel-de-luna.git
   cd hotel-de-luna
   ```

2. **Add the sample CSV files** to the project root (included in the repo)

3. **Run with JavaFX**
   ```bash
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp . Main
   ```
   Or open in IntelliJ IDEA / Eclipse and configure the JavaFX SDK in project settings.

4. **Login** with `admin` / `admin` or any employee credentials from `employees.csv`

---

## 🗂️ Project Structure

```
hotel-de-luna/
│
├── src/
│   ├── Main.java
│   ├── LoginAgent.java
│   ├── SceneManager.java
│   ├── Employee.java
│   ├── Customer.java
│   ├── Room.java
│   └── ...
│
└── data/
    ├── customers.csv
    ├── rooms.csv
    └── employees.csv

 
```

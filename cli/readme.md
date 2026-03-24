Finance Tracker CLI
A command-line personal finance tracker built with Java. This was developed as a learning project to practice core Java concepts including OOP, Collections, Streams, File I/O, and Exception Handling.
Features

Add income and expense transactions
View all transactions
Check current balance
Delete transactions by ID
Filter transactions by date range
View top 3 highest expenses
Data persists between sessions via CSV file storage

Technologies Used

Java 17
Gradle (Kotlin DSL)

How to Run
Prerequisites: JDK 17 installed on your machine

Clone the repository

git clone https://github.com/YOUR_USERNAME/finance-tracker.git

Run the app, by the loading the Gradle build file.


Project Structure
src/main/java/com/finance/
├── model/          # Transaction, Category, TransactionType
├── service/        # Business logic and filtering
├── storage/        # CSV file read and write
├── ui/             # CLI menu and user interaction
├── exception/      # Custom exception handling
└── Main.java       # Entry point
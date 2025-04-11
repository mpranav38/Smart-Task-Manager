# Smart Task Manager 📝

A Java Swing-based desktop application for managing personal tasks with priority and status.  
Uses SQLite as a local lightweight database.

## 🧰 Tech Stack
- Java (Swing)
- SQLite (via JDBC)
- Git

## 🚀 Features
- Add, view, and complete tasks
- Priority selector (High / Medium / Low)
- Persistent storage using SQLite
- Intuitive Swing GUI

## 🛠️ How to Run
1. Make sure you have JDK installed (Java 11+)
2. Download `sqlite-jdbc-3.49.1.0.jar` and place it in `/lib/`
3. Compile and run:
```bash
javac -cp \".;lib/sqlite-jdbc-3.49.1.0.jar\" src/SmartTaskManager.java
java -cp \".;lib/sqlite-jdbc-3.49.1.0.jar;src\" SmartTaskManager

package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gplx_database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create all your tables here
        db.execSQL("CREATE TABLE IF NOT EXISTS Users (user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, password_hash TEXT NOT NULL, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Questions (question_id INTEGER PRIMARY KEY, content TEXT NOT NULL, image_url TEXT, is_critical INTEGER NOT NULL DEFAULT 0, explanation TEXT, category TEXT)"); // Added a 'category' column
        db.execSQL("CREATE TABLE IF NOT EXISTS Answers (answer_id INTEGER PRIMARY KEY AUTOINCREMENT, question_id INTEGER NOT NULL, content TEXT NOT NULL, is_correct INTEGER NOT NULL, FOREIGN KEY(question_id) REFERENCES Questions(question_id) ON DELETE CASCADE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Quizzes (quiz_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, start_time DATETIME NOT NULL, end_time DATETIME, score INTEGER, is_passed INTEGER, FOREIGN KEY(user_id) REFERENCES Users(user_id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS QuizDetails (quiz_detail_id INTEGER PRIMARY KEY AUTOINCREMENT, quiz_id INTEGER NOT NULL, question_id INTEGER NOT NULL, user_answer_id INTEGER, is_correct INTEGER, FOREIGN KEY(quiz_id) REFERENCES Quizzes(quiz_id) ON DELETE CASCADE, FOREIGN KEY(question_id) REFERENCES Questions(question_id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS UserProgress (progress_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, question_id INTEGER NOT NULL, times_answered INTEGER NOT NULL DEFAULT 0, times_correct INTEGER NOT NULL DEFAULT 0, last_answered_at DATETIME, FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE CASCADE, FOREIGN KEY(question_id) REFERENCES Questions(question_id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS Bookmarks (bookmark_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, question_id INTEGER NOT NULL UNIQUE, bookmarked_at DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE CASCADE, FOREIGN KEY(question_id) REFERENCES Questions(question_id) ON DELETE CASCADE)");

        // --- IMPORTANT: ADD SAMPLE DATA ---
        // For the app to work, you need data. Let's add some sample users and questions.
        addSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Questions");
        // ... drop all other tables
        onCreate(db);
    }

    private void addSampleData(SQLiteDatabase db) {
        // Add a sample user
        ContentValues userValues = new ContentValues();
        userValues.put("username", "Minh");
        userValues.put("password_hash", "12345"); // In a real app, you would hash this
        db.insert("Users", null, userValues);

        // Add sample question topics
        ContentValues q1 = new ContentValues();
        q1.put("question_id", 101);
        q1.put("content", "Câu hỏi về quy định chung 1");
        q1.put("category", "Quy định chung");
        db.insert("Questions", null, q1);

        ContentValues q2 = new ContentValues();
        q2.put("question_id", 201);
        q2.put("content", "Câu hỏi về văn hóa giao thông 1");
        q2.put("category", "Văn hóa giao thông");
        db.insert("Questions", null, q2);

        ContentValues q3 = new ContentValues();
        q3.put("question_id", 301);
        q3.put("content", "Câu hỏi về kỹ thuật lái xe 1");
        q3.put("category", "Kỹ thuật lái xe");
        db.insert("Questions", null, q3);
        // Add more questions for each category to get accurate counts
    }


    // Method to get a user's name by ID
    public String getUserName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users", new String[]{"username"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            cursor.close();
            return username;
        }
        return "User"; // Default name
    }

    // Method to get all unique topics (categories) and their question counts
    public List<Topic> getAllTopics() {
        List<Topic> topicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to group questions by category and get the count for each
        String query = "SELECT category, COUNT(question_id) as count FROM Questions GROUP BY category";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                int questionCount = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
                // We'll just put a placeholder for time for now
                String time = (questionCount / 2) + " phút";
                topicList.add(new Topic(title, questionCount, time));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return topicList;
    }
}
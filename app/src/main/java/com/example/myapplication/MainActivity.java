package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView topicsRecyclerView;
    private TopicAdapter topicAdapter;
    private TabLayout tabLayout;
    private TextView greetingText;
    private View continuePracticingCard; // The whole card view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize the Database Helper
        dbHelper = new DatabaseHelper(this);

        // 2. Initialize Views
        greetingText = findViewById(R.id.greeting_text);
        tabLayout = findViewById(R.id.tab_layout);
        topicsRecyclerView = findViewById(R.id.topics_recyclerview);
        continuePracticingCard = findViewById(R.id.continue_practicing_card); // Make sure you add this ID to your <include> tag in activity_main.xml

        // 3. Load and Display Data
        loadGreeting();
        setupTabs();
        loadTopics();
        checkContinuePracticing();
    }

    private void loadGreeting() {
        // We assume user_id is 1 for this example
        String username = dbHelper.getUserName(1);
        greetingText.setText("Chào, " + username);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("B"));
        tabLayout.addTab(tabLayout.newTab().setText("C"));
        tabLayout.addTab(tabLayout.newTab().setText("C1"));
        tabLayout.addTab(tabLayout.newTab().setText("D1"));
        // Add OnTabSelectedListener later for filtering logic
    }

    private void loadTopics() {
        // Set up the RecyclerView
        topicsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get topics from the database
        List<Topic> topics = dbHelper.getAllTopics();

        // Create and set the adapter
        topicAdapter = new TopicAdapter(topics);
        topicsRecyclerView.setAdapter(topicAdapter);
    }

    private void checkContinuePracticing() {
        // TODO: Query UserProgress or Quizzes table to see if there's an unfinished session.
        // For now, we'll just hide it as an example.
        boolean hasUnfinishedQuiz = false; // This would come from a dbHelper method

        if (hasUnfinishedQuiz) {
            continuePracticingCard.setVisibility(View.VISIBLE);
            // Populate the views inside the card here
        } else {
            continuePracticingCard.setVisibility(View.GONE);
        }
    }
}
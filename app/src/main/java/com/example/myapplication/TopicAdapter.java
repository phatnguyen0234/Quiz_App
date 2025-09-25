package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topicList;

    public TopicAdapter(List<Topic> topicList) {
        this.topicList = topicList;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic currentTopic = topicList.get(position);
        holder.title.setText(currentTopic.getTitle());
        holder.questionCount.setText(currentTopic.getQuestionCount() + " câu");
        holder.time.setText(currentTopic.getTime());
        // You can set the rating and image here as well
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView questionCount;
        TextView time;
        // Add other views from your list_item_topic.xml like ImageView, rating TextView

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.topic_title);
            questionCount = itemView.findViewById(R.id.topic_question_count);
            time = itemView.findViewById(R.id.topic_time);
        }
    }
}
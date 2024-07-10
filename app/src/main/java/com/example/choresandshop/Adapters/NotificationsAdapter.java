package com.example.choresandshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.choresandshop.Model.Notification;
import com.example.choresandshop.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class NotificationsAdapter extends  RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {

    private Context context;
    private ArrayList<Notification> notifications;

    public NotificationsAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationsAdapter.NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_bar,parent,false);
        return new NotificationsAdapter.NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.NotificationsViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notificationName.setText(notification.getName());
        holder.isActive.setText(String.valueOf((notification.getActive()? "" : "Not ") + "Active"));
        holder.notificationBy.setText(String.valueOf(notification.getCreatedByEmail()));

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationsViewHolder extends RecyclerView.ViewHolder{

        private MaterialTextView notificationName;
        private MaterialTextView isActive;
        private MaterialTextView notificationBy;



        public NotificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationName = itemView.findViewById(R.id.notification_LBL_title);
            isActive = itemView.findViewById(R.id.notification_LBL_isActive);
            notificationBy = itemView.findViewById(R.id.notification_SIV_notificationBy);
        }
    }
    public void updateData(ArrayList<Notification> newNotifications) {
        this.notifications = newNotifications;
        notifyDataSetChanged();
    }

}

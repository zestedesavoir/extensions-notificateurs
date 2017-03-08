package com.zestedesavoir.android.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zestedesavoir.android.R;
import com.zestedesavoir.android.internal.utils.IntentUtil;
import com.zestedesavoir.android.notification.models.Notification;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerard Paligot
 */
class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    @NonNull
    private final Context context;

    @NonNull
    private final List<Notification> notifications = new ArrayList<>();

    NotificationsAdapter(@NonNull Context context) {
        this.context = context;
    }

    void addAll(List<Notification> notifications) {
        this.notifications.addAll(notifications);
        notifyDataSetChanged();
    }

    void update(List<Notification> notifications) {
        this.notifications.clear();
        addAll(notifications);
    }

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(NotificationsAdapter.ViewHolder viewHolder, final int i) {
        final Notification notification = notifications.get(i);
        String update = DateUtils.getRelativeTimeSpanString(context, DateTime.now().withMillis(notification.pubdate.getTime())).toString();

        viewHolder.tvMeta.setText(context.getString(R.string.notif_meta, notification.sender.username, update));
        viewHolder.tvTitle.setText(notification.title);
        switch (notification.contentType) {
            case "topic":
                viewHolder.ivType.setImageResource(R.drawable.ic_new_topic);
                break;
            case "post":
            case "privatepost":
            case "contentreaction":
                viewHolder.ivType.setImageResource(R.drawable.ic_new_message);
                break;
            case "publishablecontent":
                viewHolder.ivType.setImageResource(R.drawable.ic_new_tutorial);
                break;
        }

        if (!notification.isRead) {
            viewHolder.itemView.setBackgroundResource(R.color.is_not_read);
        } else {
            viewHolder.itemView.setBackgroundResource(R.color.window_background);
        }
        viewHolder.itemView.setOnClickListener(view -> context.startActivity(IntentUtil.createBrowserIntent(notification.url)));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_type)
        ImageView ivType;
        @BindView(R.id.tv_meta)
        TextView tvMeta;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
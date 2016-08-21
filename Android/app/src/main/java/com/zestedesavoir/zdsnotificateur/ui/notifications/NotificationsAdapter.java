package com.zestedesavoir.zdsnotificateur.ui.notifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;
import com.zestedesavoir.zdsnotificateur.ui.utils.IntentUtil;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Gerard Paligot
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> implements Filterable {
  private List<Notification> notifications = Collections.emptyList();
  private List<Notification> notificationsFiltered = Collections.emptyList();
  private Context context;
  private TypeFilter filter;

  public NotificationsAdapter(Context context) {
    this.context = context;
  }

  public void filter(Type constraint) {
    getFilter().filter(constraint.key());
  }

  public void filter(Type constraint, List<Notification> notifications) {
    this.notifications = notifications;
    this.notificationsFiltered = notifications;
    filter(constraint);
  }

  public void updateNotifications(List<Notification> notifications) {
    this.notificationsFiltered = notifications;
    notifyDataSetChanged();
  }

  public List<Notification> getNotifications() {
    return Collections.unmodifiableList(notifications);
  }

  @Override public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false));
  }

  @Override public void onBindViewHolder(NotificationsAdapter.ViewHolder viewHolder, final int i) {
    final Notification notification = notificationsFiltered.get(i);
    String update = DateUtils.getRelativeTimeSpanString(context, DateTime.now().withMillis(notification.pubdate.getTime()), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL).toString();

    viewHolder.tvMeta.setText("Par " + notification.sender().username() + ", " + update);
    viewHolder.tvTitle.setText(notification.title);

    Glide.with(context).load(notification.sender().avatar()).into(viewHolder.ivProfile);

    if (!notification.isRead) {
      viewHolder.llItem.setBackgroundResource(R.color.is_not_read);
    } else {
      viewHolder.llItem.setBackgroundResource(R.color.window_background);
    }
    viewHolder.llItem.setOnClickListener(view -> context.startActivity(IntentUtil.createBrowserIntent(notification)));
  }

  @Override public int getItemCount() {
    return notificationsFiltered.size();
  }

  @Override public Filter getFilter() {
    if (filter == null) {
      filter = new TypeFilter(this);
    }
    return filter;
  }

  public final class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ll_item_notification) LinearLayout llItem;
    @BindView(R.id.iv_profile) CircleImageView ivProfile;
    @BindView(R.id.tv_meta) TextView tvMeta;
    @BindView(R.id.tv_title) TextView tvTitle;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
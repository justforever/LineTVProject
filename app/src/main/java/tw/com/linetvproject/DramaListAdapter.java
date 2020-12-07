package tw.com.linetvproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DramaListAdapter extends RecyclerView.Adapter<DramaListAdapter.DramaViewHolder> {

    private WeakReference<MainActivity> activityWeakReference;
    private WeakReference<Context> contextWeakReference;
    private List<DramaBean> dramaBeanList;

    public DramaListAdapter(Context context, MainActivity activity) {
        this.contextWeakReference = new WeakReference<>(context);
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public void setDramaBeanList(List<DramaBean> dramaBeanList) {
        this.dramaBeanList = dramaBeanList;
    }

    @NonNull
    @Override
    public DramaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drama_list_item, parent, false);
        DramaViewHolder holder = new DramaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DramaViewHolder holder, int position) {
        DramaBean dramaBean = getItem(position);
        holder.tvTitle.setText(dramaBean.getName());
        if (null != this.contextWeakReference.get()) {
            Glide.with(this.contextWeakReference.get()).load(dramaBean.getThumb()).into(holder.ivThumb);
        }
        holder.tvRating.setText(String.valueOf(dramaBean.getRating()));
        holder.tvCreated.setText(DateUtil.convertDateFormat(dramaBean.getCreated_at()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != activityWeakReference.get()) {
                    activityWeakReference.get().goDetailActivity(dramaBean.getDrama_id());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dramaBeanList == null ? 0 : this.dramaBeanList.size();
    }

    private DramaBean getItem(int position) {
        return this.dramaBeanList.get(position);
    }

    public class DramaViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumb;
        TextView tvTitle;
        TextView tvRating;
        TextView tvCreated;

        public DramaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.iv_thumb);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvCreated = itemView.findViewById(R.id.tv_created);
        }
    }
}

package tw.com.linetvproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DramaDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_thumb)
    ImageView ivThumb;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_created)
    TextView tvCreated;
    @BindView(R.id.tv_views)
    TextView tvViews;

    private int dramaId;
    private DramaBean dramaBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dramaId = getIntent().getExtras().getInt("drama_id");

        this.getDramaBeanFromDB();

        setContentView(R.layout.activity_drama_detail);

        ButterKnife.bind(this);
    }

    private void getDramaBeanFromDB() {
        new Thread(() -> {
            dramaBean = DramaActions.getInstance(getApplicationContext()).findById(dramaId);
            Log.d("de", "getDramaBeanFromDB, "+dramaBean.getName());
            updateDramaDetail();
        }).start();
    }

    private void updateDramaDetail() {
        runOnUiThread(() -> {
            Glide.with(DramaDetailActivity.this).load(dramaBean.getThumb()).into(ivThumb);
            tvTitle.setText(dramaBean.getName());
            tvRating.setText("" + dramaBean.getRating());
            tvCreated.setText("" + DateUtil.convertDateFormat(dramaBean.getCreated_at()));
            tvViews.setText("" + dramaBean.getTotal_views());
        });
    }
}

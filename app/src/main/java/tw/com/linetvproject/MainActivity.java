package tw.com.linetvproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView rvDramaList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.LayoutManager layoutManager;
    private DramaListAdapter adapter;

    private DramaActions dramaActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        dramaActions = new DramaActions(getApplicationContext());

        this.initRecyclerView();
        this.initSwipeRefreshLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadData();
    }

    private void initSwipeRefreshLayout() {
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void initRecyclerView() {
        this.layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        this.adapter = new DramaListAdapter(this, MainActivity.this);
        this.rvDramaList.setLayoutManager(this.layoutManager);
        this.rvDramaList.setAdapter(this.adapter);
    }

    private void loadData() {
        RetrofitUtil.getClient().create(ApiService.class).getSampleData().enqueue(new Callback<DramaList>() {
            @Override
            public void onResponse(Call<DramaList> call, Response<DramaList> response) {
                DramaList list = response.body();
                int size = list.getData().size();
                for (int i = 0; i < size; i++) {
                    Log.d("de", list.getData().get(i).getName());
                    dramaActions.insertTask(list.getData().get(i));
                }


                refreshDramaList(list.getData());
                dismissLoading();
                showDbInfo();
            }

            @Override
            public void onFailure(Call<DramaList> call, Throwable t) {
                Log.d("de", "loadData failure, "+t.getMessage());
                getAllFromDB();
            }
        });
    }

    private void dismissLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getAllFromDB() {
        Log.d("de", "getAllFromDB");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("de", "getAllFromDb in thread");
                List<DramaBean> listFromDb = dramaActions.getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDramaList(listFromDb);
                        Log.d("de", "end getAllFromDB");
                    }
                });
            }
        }).start();
    }

    private void showDbInfo() {
        Log.d("de", "-----start showDbInfo-----");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("de", "in thread");
                List<DramaBean> listFromDb = dramaActions.getAll();
                int size = listFromDb.size();
                Log.d("de", "size: "+size);
                for (int i = 0; i < size; i++) {
                    Log.d("de", ""+listFromDb.get(i).getDrama_id()+", "+listFromDb.get(i).getName());
                }
                Log.d("de", "-----end showDbInfo-----");

            }
        }).start();
    }

    private void refreshDramaList(List<DramaBean> dramaBeanList) {
        adapter.setDramaBeanList(dramaBeanList);
        adapter.notifyDataSetChanged();
    }

    public void goDetailActivity(int drama_id) {
        Intent intent = new Intent(MainActivity.this, DramaDetailActivity.class);
        intent.putExtra("drama_id", drama_id);
        startActivity(intent);
    }
}
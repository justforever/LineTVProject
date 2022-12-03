package tw.com.linetvproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView rvDramaList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private SearchView searchView;

    private RecyclerView.LayoutManager layoutManager;
    private DramaListAdapter adapter;

    private DramaActions dramaActions;
    private List<DramaBean> allDramaBeans = new ArrayList<>();

    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind references
        ButterKnife.bind(this);

        dramaActions = new DramaActions(getApplicationContext());

        this.initRecyclerView();
        this.initSwipeRefreshLayout();

        this.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        keyword = SharedPreferenceUtil.getKeyword(getApplicationContext());
        filterDramaList();
    }

    private void initSwipeRefreshLayout() {
        this.swipeRefreshLayout.setOnRefreshListener(() -> loadData());
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
                    dramaActions.insertTask(list.getData().get(i));
                }

                allDramaBeans = list.getData();

                filterDramaList();
                dismissLoading();
//                showDbInfo();
            }

            @Override
            public void onFailure(Call<DramaList> call, Throwable t) {
                Log.d("de", "onFailure, "+t.getMessage());
                presentNoNetworkAlert();
//                getAllFromDB();
            }
        });
    }

    private void presentNoNetworkAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("沒有網路連線，暫時使用離線資料。請您使用下拉手勢更新資料")
                .setPositiveButton("OK", (dialog, which) -> {
                    getAllFromDB();
                    dismissLoading();
                }).show();
    }

    private void dismissLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getAllFromDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                allDramaBeans = dramaActions.getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDramaList(allDramaBeans);
                    }
                });
            }
        }).start();
    }

    private void showDbInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<DramaBean> listFromDb = dramaActions.getAll();
                int size = listFromDb.size();
                for (int i = 0; i < size; i++) {
                }
            }
        }).start();
    }

    private void filterDramaList() {
        List<DramaBean> filterDramaBeans = keyword != null && keyword.length() != 0 ? allDramaBeans.stream().filter((DramaBean dramaBean)
                -> dramaBean.getName().contains(keyword))
                .collect(Collectors.toList()) : allDramaBeans;

        if (filterDramaBeans == null) {
            filterDramaBeans = new ArrayList<>();
        }

        adapter.setDramaBeanList(filterDramaBeans);
        adapter.notifyDataSetChanged();
        if (searchView != null)
            searchView.clearFocus();
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

    private void updateKeyword() {
        SharedPreferenceUtil.saveKeyword(getApplicationContext(), keyword);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                filterDramaList();
                updateKeyword();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                keyword = "";
                updateKeyword();
                filterDramaList();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchView = (SearchView) menu.findItem(R.id.iv_search).getActionView();
        searchView.setQuery(keyword, false);
        if (keyword != null && keyword.length() != 0) {
            searchView.setIconified(false);
        }
        else {
            searchView.setIconified(true);
        }
        setupSearchView();
        filterDramaList();
        return super.onCreateOptionsMenu(menu);
    }
}
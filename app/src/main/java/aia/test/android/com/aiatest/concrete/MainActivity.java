package aia.test.android.com.aiatest.concrete;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aia.test.android.com.aiatest.R;
import aia.test.android.com.aiatest.adapter.SearchItemAdapter;
import aia.test.android.com.aiatest.constants.ImgurConstants;
import aia.test.android.com.aiatest.constants.StringConstants;
import aia.test.android.com.aiatest.interfaces.IImgurSenderObserver;
import aia.test.android.com.aiatest.model.Datum;
import aia.test.android.com.aiatest.model.ImgurDataModel;
import aia.test.android.com.aiatest.senders.ImgurServices;
import aia.test.android.com.aiatest.util.CommonUtils;
import aia.test.android.com.aiatest.util.GridSpacingItemDecoration;
import aia.test.android.com.aiatest.util.NetAccessCheckerUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IImgurSenderObserver {
    @BindView(R.id.edit_search)
    TextInputEditText editSearch;
    @BindView(R.id.icon_search)
    ImageView searchIcon;
    @BindView(R.id.recycler_search_items)
    RecyclerView searchRecycler;
    @BindView(R.id.textView)
    TextView empty_item;
    @BindView(R.id.inputlayout_edit_search)
    TextInputLayout searchInputLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.searchToggle)
    Switch searchToggle;
    SearchItemAdapter adapter;
    String searchQuery;
    GridLayoutManager layoutManager;
    private int currentPage = 0;
    public static final int PAGE_SIZE = 30;
    private boolean toggleButtonEnabled = false;
    private List<Datum> updatedData = new ArrayList<>();
    private ImgurDataModel imgurDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpEditText();
    }

    @OnCheckedChanged(R.id.searchToggle)
    public void onCheckedChanged(boolean isChecked) {
        if (isChecked) {
            toggleButtonEnabled = true;
            for (int i = 0; i < CommonUtils.getInstance().getImgurData().size(); i++) {
                if (CommonUtils.getInstance().isEvenOrNot(CommonUtils.getInstance().getImgurData().get(i).getScore(), CommonUtils.getInstance().getImgurData().get(i).getPoints(), CommonUtils.getInstance().getImgurData().get(i).getTopicId()))
                    updatedData.add(CommonUtils.getInstance().getImgurData().get(i));
            }
            if (updatedData.size() == 0)
                empty_item.setVisibility(View.GONE);
            else
                setAdapterForSearchitems(updatedData);
        } else {
            toggleButtonEnabled = false;
            setAdapterForSearchitems(CommonUtils.getInstance().getImgurData());
        }
        adapter.notifyDataSetChanged();
    }


    @OnClick(R.id.icon_search)
    void itemSearch() {
        empty_item.setVisibility(View.GONE);
        if (editSearch.getText().length() > 0) {
            searchInputLayout.setErrorEnabled(false);
            searchQuery = editSearch.getText().toString();
            currentPage = 1;
            updatedData.clear();
            CommonUtils.getInstance().getImgurData().clear();
            if (NetAccessCheckerUtil.isOnline(this)) {
                calImgurService(1);
            } else
                CommonUtils.getInstance().showAlertDialogs(this, getString(R.string.error_internet), getString(R.string.no_network_header));
        } else
            searchInputLayout.setError(getString(R.string.empty_field));
    }

    private void calImgurService(int currentPage) {
        progressBar.setVisibility(View.VISIBLE);
        ImgurServices.sendJsonRequest(this, ImgurConstants.IMGUR_BASE_URL + ImgurConstants.IMGUR_GALLERY_URL.replace(ImgurConstants.QUERY, searchQuery).replace(ImgurConstants.PAGE, currentPage + ""), this);
    }

    public void setDataOnCallImgurServiceFailure(String jsonResponse) {
        progressBar.setVisibility(View.GONE);
        searchToggle.setVisibility(View.GONE);
        JSONObject jsonObject;
        String responseCode = null;
        try {
            jsonObject = new JSONObject(jsonResponse);
            responseCode = jsonObject.getString(StringConstants.RESPONSE_STATUS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (responseCode.equals(ImgurConstants.AUTH_ERROR) || responseCode.equals(ImgurConstants.NO_INTERNET))
            CommonUtils.getInstance().showAlertDialogs(this, getString(R.string.error_internet), getString(R.string.no_network_header));
        else
            CommonUtils.getInstance().showAlertDialogs(this, getString(R.string.error_response), getString(R.string.error_header));

        searchRecycler.setVisibility(View.GONE);
    }

    private void setDataOnCallImgurServiceSuccess(String jsonResponse) {
        try {
            progressBar.setVisibility(View.GONE);

            CommonUtils.getInstance().hideKeyboardFromEditText(MainActivity.this, editSearch);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String responseCode = jsonObject.getString(StringConstants.RESPONSE_STATUS);
            if (responseCode.equals(ImgurConstants.SUCCESS)) {
                imgurDataModel = jsonMapper.readValue(jsonResponse, ImgurDataModel.class);
                if (imgurDataModel.getData().size() > 0 || (currentPage > 1 && imgurDataModel.getData().size() == 0)) {
                    empty_item.setVisibility(View.GONE);
                    searchToggle.setVisibility(View.VISIBLE);
                    searchRecycler.setVisibility(View.VISIBLE);
                    if (toggleButtonEnabled) {
                        for (int i = 0; i < imgurDataModel.getData().size(); i++) {
                            if (CommonUtils.getInstance().isEvenOrNot(imgurDataModel.getData().get(i).getScore(), imgurDataModel.getData().get(i).getPoints(), imgurDataModel.getData().get(i).getTopicId()))
                                updatedData.add(imgurDataModel.getData().get(i));
                        }
                        if (currentPage == 1)
                            setUpRecyclerView(updatedData);

                    } else {
                        if (currentPage == 1)
                            setUpRecyclerView(CommonUtils.getInstance().getImgurData());
                    }
                    if ((currentPage > 1 && imgurDataModel.getData().size() == 0))
                        CommonUtils.getInstance().setLastPage(true);
                    else {
                        CommonUtils.getInstance().setLastPage(false);
                    }
                    if (imgurDataModel.getData().size() > 0)
                        CommonUtils.getInstance().setImgurData(imgurDataModel.getData());

                } else {
                    CommonUtils.getInstance().setLastPage(true);
                    searchToggle.setVisibility(View.GONE);
                    searchRecycler.setVisibility(View.GONE);
                    empty_item.setVisibility(View.VISIBLE);
                }
//                CommonUtils.getInstance().setImgurData(imgurDataModel.getData());
            } else
                onNotifyFailedImgurSending(jsonResponse);

        } catch (Exception ex) {
            Log.d(StringConstants.TAG, ex.getMessage());
            onNotifyFailedImgurSending(jsonResponse);
        }
    }


    private void setUpEditText() {
        CommonUtils.getInstance().hideKeyboard(this);
    }

    private void setUpRecyclerView(List<Datum> data) {
        setAdapterForSearchitems(data);
        searchRecycler.setLayoutManager(layoutManager);
        searchRecycler.addItemDecoration(new GridSpacingItemDecoration(this, 2, CommonUtils.getInstance().dpToPx(this, 10), true));
        searchRecycler.setItemAnimator(new DefaultItemAnimator());
        searchRecycler.addOnScrollListener(recyclerViewOnScrollListener);

    }

    private void setAdapterForSearchitems(List<Datum> data) {
        layoutManager = new GridLayoutManager(MainActivity.this, 2);
        adapter = new SearchItemAdapter(this, data);
        searchRecycler.setAdapter(adapter);
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();//6
            int totalItemCount = layoutManager.getItemCount();//60
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!CommonUtils.getInstance().isLoading() && !CommonUtils.getInstance().isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    loadMoreItems();
                }
            }
        }
    };

    private void loadMoreItems() {
        CommonUtils.getInstance().setLoading(true);
        currentPage += 1;
        calImgurService(currentPage);
    }

    @Override
    public void onNotifySuccessfulImgurSending(String jsonResponse) {
        setDataOnCallImgurServiceSuccess(jsonResponse);
    }

    @Override
    public void onNotifyFailedImgurSending(String jsonResponse) {
        setDataOnCallImgurServiceFailure(jsonResponse);
    }


}

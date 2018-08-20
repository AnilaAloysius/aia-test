package aia.test.android.com.aiatest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aia.test.android.com.aiatest.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchItemHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_title)public TextView textViewTitle;
    @BindView(R.id.tv_post_date)public TextView textViewPostDate;
    @BindView(R.id.tv_additional_image_count)public TextView textViewAdditionalImage;
    @BindView(R.id.iv_image)public ImageView imgurImage;
    public SearchItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}

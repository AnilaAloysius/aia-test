package aia.test.android.com.aiatest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import aia.test.android.com.aiatest.R;
import aia.test.android.com.aiatest.constants.StringConstants;
import aia.test.android.com.aiatest.model.Datum;
import aia.test.android.com.aiatest.util.CommonUtils;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemHolder> {
    private Context context;
    private List<Datum> data;

    public SearchItemAdapter(Context context, List<Datum> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public SearchItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_items, parent, false);
        return new SearchItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemHolder holder, int position) {
        setData(holder, position);
    }

    private void setData(@NonNull SearchItemHolder holder, int position) {
        holder.textViewTitle.setText(data.get(position).getTitle());
        try {
            holder.textViewAdditionalImage.setText(data.get(position).getImagesCount().toString());
            holder.textViewAdditionalImage.setVisibility(View.VISIBLE);
            holder.textViewPostDate.setText(CommonUtils.getInstance().getDate(data.get(position).getImages().get(0).getDatetime()));
            populateData(holder, data.get(position).getImages().get(0).getType(), data.get(position).getImages().get(0).getLink());
        } catch (Exception ex) {
            holder.textViewAdditionalImage.setVisibility(View.GONE);
            holder.textViewPostDate.setText(CommonUtils.getInstance().getDate(data.get(position).getDatetime()));
            populateData(holder, data.get(position).getType(), data.get(position).getLink());
        }
    }

    private void populateData(final SearchItemHolder holder, final String type, final String link) {
        if (type.contains(StringConstants.GIF)) {
            Glide.with(context)
                    .load(link)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.imgurImage);
        } else if (type.contains(StringConstants.JPEG) || type.contains(StringConstants.PNG)) {
            Glide.with(context)
                    .load(link)
                    .into(holder.imgurImage);

        } else {
            Glide.with(context)
                    .load(Uri.parse(link))
                    .into(holder.imgurImage);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

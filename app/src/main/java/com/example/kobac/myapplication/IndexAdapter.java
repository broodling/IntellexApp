package com.example.kobac.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kobac.myapplication.details.DetailsActivity;
import com.example.kobac.myapplication.details.FontClass;

import java.util.ArrayList;


/**
 * Created by kobac on 19.7.17..
 */


public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHolder> {

    public static final String DETAILS_URL = "http://intellexweb.dev.intellex.rs/api/v1/catalogueDetails?id=";

    private Context context;
    CustomFilter filter;
    public ArrayList<IndexModel> indexList, filterList;

    public IndexAdapter(Context context, ArrayList<IndexModel> indexList) {
        this.indexList = indexList;
        this.context = context;
        this.filterList = indexList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.index__item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final IndexAdapter.ViewHolder holder, final int position) {

        final IndexModel indexModel = indexList.get(position);

        holder.indexTitle.setText(indexModel.getIndexTitle());
        holder.indexTitle.setTypeface(FontClass.getHelvetica(context));

        holder.indexDescription.setText(indexModel.getIndexDescription());
        Glide.with(holder.indexImage.getContext()).load(indexModel.getIndexImage()).into(holder.indexImage);
        holder.indexId = indexModel.indexID;
        holder.indexImageUrl = indexModel.getIndexImage();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> map = new ArrayList<>();
                for (final IndexModel item : indexList) {
                    map.add(DETAILS_URL + item.getIndexID());
                }
                Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.BUNDLE_POSITION, position);
                intent.putExtra(DetailsActivity.BUNDLE_MAP, map);
                holder.itemView.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return indexList.size();
    }

    public void addAll(ArrayList<IndexModel> indexArray) {
        indexList.addAll(indexArray);
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView indexImage;
        public TextView indexTitle;
        public TextView indexDescription;
        public String indexId;

        public String indexImageUrl;

        public ViewHolder(final View itemView) {
            super(itemView);


            indexImage = itemView.findViewById(R.id.indexImage);
            indexTitle = itemView.findViewById(R.id.indexTitle);
            indexDescription = itemView.findViewById(R.id.indexDescription);

        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }
}


package com.example.kobac.myapplication.galery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kobac.myapplication.R;
import com.example.kobac.myapplication.details.DetailsModel;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.ArrayList;


/**
 * Created by kobac on 2.8.17..
 */


public abstract class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    public static final int ITEM_TYPE_GALLERY = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    private Context context;
    private ArrayList<GalleryModel> galleryList;
    private DetailsModel headerModel;



    public GalleryAdapter(Context context, ArrayList<GalleryModel> galleryList, final DetailsModel headerModel) {
        this.galleryList = galleryList;
        this.headerModel = headerModel;
        this.context = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_GALLERY:
                View galleryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.details__gallery_image, parent, false);
                return new GalleryViewHolder(galleryView);

            case ITEM_TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.details__header, parent, false);
                return new HeaderViewHolder(headerView);
        }

        return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.details__header, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_GALLERY) {
            GalleryViewHolder galleryViewHolder = (GalleryViewHolder) holder;

            GalleryModel galleryModel = galleryList.get(position);
            Glide.with(galleryViewHolder.imageGallery.getContext()).load(galleryModel.getUrl()).into(galleryViewHolder.imageGallery);
            galleryViewHolder.imageUrl = galleryModel.getUrl();

        } else if (itemType == ITEM_TYPE_HEADER) {

            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            TextView indexTitle = headerViewHolder.title.findViewById(R.id.detailsTitle);
            indexTitle.setText(headerModel.getIndexClient());

            TextView indexClient = headerViewHolder.client.findViewById(R.id.client_name);
            indexClient.setText(headerModel.getIndexClient());

            TextView indexYear = headerViewHolder.year.findViewById(R.id.year);
            indexYear.setText(headerModel.getIndexYear());

            JustifiedTextView indexText = headerViewHolder.description.findViewById(R.id.description);
            indexText.setText(headerModel.getIndexText());

            TextView indexServices = headerViewHolder.services.findViewById(R.id.services);
            indexServices.setText(headerModel.getIndexServices());
            if (headerModel.getIndexServices().equals("")) {
                indexServices.setVisibility(View.GONE);
            }

            Glide.with(headerViewHolder.imageView.getContext()).load(headerModel.getFrontImage()).into(headerViewHolder.imageView);

        }
    }


    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        }

        return ITEM_TYPE_GALLERY;
    }


    /**
     * General ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * Gallery ViewHolder.
     */
    public class GalleryViewHolder extends ViewHolder {
        public ImageView imageGallery;
        public String imageUrl;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            imageGallery = itemView.findViewById(R.id.imageGallery);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickView(imageUrl);

                }
            });
        }
    }

    /**
     * Header ViewHolder.
     */
    public class HeaderViewHolder extends ViewHolder {
        public TextView title, year, services, client;
        public JustifiedTextView description;
        public ImageView imageView;


        public HeaderViewHolder(final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.detailsTitle);
            description = itemView.findViewById(R.id.description);
            year = itemView.findViewById(R.id.year);
            services = itemView.findViewById(R.id.services);
            client = itemView.findViewById(R.id.client_name);
            imageView = itemView.findViewById(R.id.imageIcon);


        }
    }


    abstract protected void onClickView(final String imageUrl);


}

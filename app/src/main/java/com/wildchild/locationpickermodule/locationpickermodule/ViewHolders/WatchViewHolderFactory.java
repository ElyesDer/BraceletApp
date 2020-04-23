package com.wildchild.locationpickermodule.locationpickermodule.ViewHolders;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RecyclerOnItemClickListener;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RowType;

import static com.wildchild.locationpickermodule.R.drawable.v1;

public class WatchViewHolderFactory {

    public static class WatchViewHolderV1 extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ConstraintLayout constraintLayout;
        RecyclerOnItemClickListener itemClickListener;
        public Context context;

        public WatchViewHolderV1(Context context, View itemView, final RecyclerOnItemClickListener itemClickListener) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewV1);
            this.textView = (TextView) itemView.findViewById(R.id.textViewV1);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.item_constraint_layoutv1);
            this.context = context;

            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(v -> itemClickListener.onItemClick(v, getAdapterPosition()));

        }
    }

    public static class WatchViewHolderV2 extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ConstraintLayout constraintLayout;
        RecyclerOnItemClickListener itemClickListener;
        public Context context;

        public WatchViewHolderV2(Context context, View itemView, final RecyclerOnItemClickListener itemClickListener) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewV2);
            this.textView = (TextView) itemView.findViewById(R.id.textViewV2);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.item_constraint_layoutv2);
            this.itemClickListener = itemClickListener;
            this.context = context;


            itemView.setOnClickListener(v -> itemClickListener.onItemClick(v, getAdapterPosition()));
        }
    }

    public static RecyclerView.ViewHolder create(ViewGroup parent, int viewType, RecyclerOnItemClickListener itemClickListener) {
        switch (viewType) {
            case RowType.v1:
                View buttonTypeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_v1, parent, false);
                return new WatchViewHolderFactory.WatchViewHolderV1(parent.getContext(), buttonTypeView, itemClickListener);

            case RowType.v2:
                View textTypeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_v2, parent, false);
                return new WatchViewHolderFactory.WatchViewHolderV2(parent.getContext(), textTypeView, itemClickListener);

            default:
                return null;
        }

    }
}
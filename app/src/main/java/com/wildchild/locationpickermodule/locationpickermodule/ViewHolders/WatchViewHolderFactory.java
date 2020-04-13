package com.wildchild.locationpickermodule.locationpickermodule.ViewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RecyclerOnItemClickListener;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RowType;

public class WatchViewHolderFactory {

    public static class WatchViewHolderV1 extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ConstraintLayout constraintLayout;
        RecyclerOnItemClickListener itemClickListener;

        public WatchViewHolderV1(View itemView , final RecyclerOnItemClickListener itemClickListener) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewV1);
            this.textView = (TextView) itemView.findViewById(R.id.textViewV1);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.item_constraint_layoutv1);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v,getAdapterPosition());
                }
            });

        }
    }

    public static class WatchViewHolderV2 extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ConstraintLayout constraintLayout;
        RecyclerOnItemClickListener itemClickListener;

        public WatchViewHolderV2(View itemView , final RecyclerOnItemClickListener itemClickListener) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewV2);
            this.textView = (TextView) itemView.findViewById(R.id.textViewV2);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.item_constraint_layoutv2);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

    public static RecyclerView.ViewHolder create(ViewGroup parent, int viewType , RecyclerOnItemClickListener itemClickListener) {
        System.out.println("Creating : "+viewType);
        switch (viewType) {
            case RowType.v1:
                View buttonTypeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_v1, parent, false);
                return new WatchViewHolderFactory.WatchViewHolderV1(buttonTypeView , itemClickListener);

            case RowType.v2:
                View textTypeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_v2, parent, false);
                return new WatchViewHolderFactory.WatchViewHolderV2(textTypeView , itemClickListener);

            default:
                return null;
        }

    }
}
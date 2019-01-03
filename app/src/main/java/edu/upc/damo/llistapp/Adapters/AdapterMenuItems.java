package edu.upc.damo.llistapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.upc.damo.llistapp.R;

public class AdapterMenuItems extends RecyclerView.Adapter<AdapterMenuItems.ViewHolderMenuItems>{

    private OnItemClickListener mListener;
    private String[] mItems;
    private int[] mItemsImage;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ this.mListener = listener; }

    public static class ViewHolderMenuItems extends RecyclerView.ViewHolder {

        private ImageView mIVitem;
        private TextView mTVitem;

        public ViewHolderMenuItems(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mIVitem = itemView.findViewById(R.id.iv_menuItem);
            mTVitem = itemView.findViewById(R.id.tv_menuItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public AdapterMenuItems(String[] items, int[] itemsImage){
        this.mItems = items;
        this.mItemsImage = itemsImage;
    }

    @Override
    public ViewHolderMenuItems onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_menu_item, parent,false);
        return new ViewHolderMenuItems(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderMenuItems holder, int position) {
        holder.mTVitem.setText(mItems[position]);
        holder.mIVitem.setImageResource(mItemsImage[position]);
    }

    @Override
    public int getItemCount() { return mItems.length; }
}

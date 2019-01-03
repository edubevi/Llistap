package edu.upc.damo.llistapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import edu.upc.damo.llistapp.R;

public class AdapterEines extends RecyclerView.Adapter<AdapterEines.ViewHolderEines>{

    private OnItemClickListener mListener;
    private String[] mItemsName, mItemsDesc;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ this.mListener = listener; }

    public static class ViewHolderEines extends RecyclerView.ViewHolder{

        private TextView mTVnomEina, mTVdescEina;

        public ViewHolderEines(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTVnomEina = itemView.findViewById(R.id.tv_nomEina);
            mTVdescEina = itemView.findViewById(R.id.tv_descEina);

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

    public AdapterEines(String[] itemNames, String[] itemDesc){
        this.mItemsName = itemNames;
        this.mItemsDesc = itemDesc;
    }

    @Override
    public ViewHolderEines onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_eines, parent,false);
        return new ViewHolderEines(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEines holder, int position) {
        holder.mTVnomEina.setText(mItemsName[position]);
        holder.mTVdescEina.setText(mItemsDesc[position]);
    }

    @Override
    public int getItemCount() { return mItemsName.length; }


}

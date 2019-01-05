package edu.upc.damo.llistapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upc.damo.llistapp.Models.ModelEines;
import edu.upc.damo.llistapp.R;

public class AdapterEines extends RecyclerView.Adapter<AdapterEines.ViewHolderEines>{

    private OnItemClickListener listener;
    private String[] items_name, items_desc;

    public interface OnItemClickListener { void onItemClick(int position);}

    public void setOnItemClickListener(OnItemClickListener listener){ this.listener = listener; }

    static class ViewHolderEines extends RecyclerView.ViewHolder{

        private TextView tv_nom, tv_descripcio;

        ViewHolderEines(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tv_nom = itemView.findViewById(R.id.tv_nomEina);
            tv_descripcio = itemView.findViewById(R.id.tv_descEina);

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

    public AdapterEines(ModelEines model){
        this.items_name = model.getItemsName();
        this.items_desc = model.getItemsDesc();
    }

    @Override
    public ViewHolderEines onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_eines, parent,false);
        return new ViewHolderEines(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEines holder, int position) {
        holder.tv_nom.setText(items_name[position]);
        holder.tv_descripcio.setText(items_desc[position]);
    }

    @Override
    public int getItemCount() { return items_name.length; }
}

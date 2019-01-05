package edu.upc.damo.llistapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import edu.upc.damo.llistapp.Models.ModelAssistencia;
import edu.upc.damo.llistapp.Objectes.Assistencia;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class AdapterAssistencies extends RecyclerView.Adapter<
        AdapterAssistencies.ViewHolderAssistencies> {

    private List<Assistencia> assistencies;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

    static class ViewHolderAssistencies extends RecyclerView.ViewHolder {

        private ImageButton ib_delete;
        private TextView tv_date;

        ViewHolderAssistencies(View itemView, final OnItemClickListener listener) {
            super(itemView);
            ib_delete = itemView.findViewById(R.id.ib_deleteAssistencia);
            tv_date = itemView.findViewById(R.id.tv_dataAssist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onItemClick(position);
                    }
                }
            });

            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    public AdapterAssistencies(ModelAssistencia model) { assistencies = model.getAssistencies(); }
    public void setAssistencies(List<Assistencia> list) { assistencies = list;}

    @Override
    public ViewHolderAssistencies onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_assistencies, parent, false);
        return new ViewHolderAssistencies(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolderAssistencies holder, int position) {
        Assistencia assist = assistencies.get(position);
        holder.tv_date.setText(Utils.getFormatDate(assist.getDate()));
    }

    @Override
    public int getItemCount() { return assistencies.size(); }
}

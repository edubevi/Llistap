package edu.upc.damo.llistapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.upc.damo.llistapp.Entitats.Assistencia;
import edu.upc.damo.llistapp.R;

public class AdapterAssistencies extends RecyclerView.Adapter<
        AdapterAssistencies.ViewHolderAssistencies> {

    private List<Assistencia> mListAssistencies = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.mListener = listener; }

    public static class ViewHolderAssistencies extends RecyclerView.ViewHolder {

        private ImageButton mIBdelete;
        private TextView mTVdate;

        public ViewHolderAssistencies(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mIBdelete = itemView.findViewById(R.id.ib_deleteAssistencia);
            mTVdate = itemView.findViewById(R.id.tv_dataAssist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onItemClick(position);
                    }
                }
            });

            mIBdelete.setOnClickListener(new View.OnClickListener() {
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

    public AdapterAssistencies(List<Assistencia> list) {
        this.mListAssistencies = list;
    }

    public void setmListAssistencies(List<Assistencia> list){
        this.mListAssistencies = list;
    }

    @Override
    public ViewHolderAssistencies onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_assistencies, parent, false);
        return new ViewHolderAssistencies(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderAssistencies holder, int position) {
        Assistencia assist = mListAssistencies.get(position);
        holder.mTVdate.setText(getFormatDate(assist.getDate()));
    }

    @Override
    public int getItemCount() { return mListAssistencies.size(); }

    public Assistencia getAssistenciaFromList(int pos){ return mListAssistencies.get(pos);}
    public void deleteAssistenciaFromList(int pos) { mListAssistencies.remove(pos); }
    public void insertAssistenciaToList(Assistencia a) { mListAssistencies.add(a); }


    //Auxiliar methods
    private String getFormatDate(long date){

        SimpleDateFormat sdf = new SimpleDateFormat("EE d-MM-y H:m:s", new Locale("ca","ES"));
        Date formattedDate = new Date(date);
        return sdf.format(formattedDate);
    }

}

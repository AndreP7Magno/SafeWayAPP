package safewayapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import safewayapp.Activity.HistoricoAtividadesActivity;
import safewayapp.Api.response.Record;
import safewayapp.R;
import safewayapp.ViewHolder.HistoricoAtividadesItemHolder;

public class HistoricoAtividadesAdapter extends RecyclerView.Adapter<HistoricoAtividadesItemHolder>{

    public List<Record> mDataset;
    private HistoricoAtividadesActivity mContext;

    public HistoricoAtividadesAdapter(List<Record> dataSet, HistoricoAtividadesActivity context) {
        mDataset = dataSet;
        mContext = context;
    }

    @NonNull
    @Override
    public HistoricoAtividadesItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)  {
        View vw = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listview_historico_atividades_itens, viewGroup, false);

        HistoricoAtividadesItemHolder viewHolder = new HistoricoAtividadesItemHolder(vw);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoAtividadesItemHolder holder, int position) {

        Date data = null;
        String formattedDate = null;
        try {
            data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(mDataset.get(position).getData());
            formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtData.setText(formattedDate);
        holder.txtSeveridadeAtividades.setText(mDataset.get(position).getSeveridade());
        holder.txtLatitude.setText(mDataset.get(position).getLatitute());
        holder.txtLongitude.setText(mDataset.get(position).getLongitude());
        holder.txtDescricaoAtividades.setText(mDataset.get(position).getDescricao());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

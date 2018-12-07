package safewayapp.Adapter;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import safewayapp.Activity.HistoricoAtividadesActivity;
import safewayapp.Api.response.Record;
import safewayapp.R;
import safewayapp.ViewHolder.HistoricoAtividadesItemHolder;

public class HistoricoAtividadesAdapter extends RecyclerView.Adapter<HistoricoAtividadesItemHolder> {

    public List<Record> mDataset;
    private HistoricoAtividadesActivity mContext;
    public Geocoder coder;

    public HistoricoAtividadesAdapter(List<Record> dataSet, HistoricoAtividadesActivity context) {
        mDataset = dataSet;
        mContext = context;
        coder = new Geocoder(mContext);
    }

    @NonNull
    @Override
    public HistoricoAtividadesItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
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

        List<Address> addresses = null;
        try {
            addresses = coder.getFromLocation(Double.parseDouble(mDataset.get(position).getLatitute()), Double.parseDouble(mDataset.get(position).getLongitude()), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String enderecoFinal = "Não Informado";
        if (addresses != null) {
            String endereco = addresses.get(0).getAddressLine(0);
            String[] separadorEndereco = endereco.split("-");
            String[] separadorFinal = separadorEndereco[1].trim().split(",");
            enderecoFinal = separadorEndereco[0].trim() + " - " + separadorFinal[0].trim();
        }

        holder.txtEnderecoAtividades.setText(enderecoFinal);
        holder.txtDescricaoAtividades.setText(mDataset.get(position).getDescricao());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

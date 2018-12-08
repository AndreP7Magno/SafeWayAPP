package safewayapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import safewayapp.Activity.HistoricoChamadosActivity;
import safewayapp.Api.response.Contact;
import safewayapp.R;

public class ItemContatoAdapter extends RecyclerView.Adapter<ItemContatoAdapter.ItemContatoItemHolder> {

    public List<Contact> mDataset;
    private HistoricoChamadosActivity mContext;

    public ItemContatoAdapter(List<Contact> dataSet, HistoricoChamadosActivity context) {
        mDataset = dataSet;
        mContext = context;
    }

    @NonNull
    @Override
    public ItemContatoItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View vw = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listview_historico_chamados_contatos_itens, viewGroup, false);

        ItemContatoItemHolder viewHolder = new ItemContatoItemHolder(vw);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemContatoItemHolder holder, int position) {
        holder.txtNomeItemContato.setText(mDataset.get(position).getNome());
        holder.txtTelefoneItemContato.setText(mDataset.get(position).getTelefone());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ItemContatoItemHolder extends RecyclerView.ViewHolder {

        public TextView txtNomeItemContato;
        public TextView txtTelefoneItemContato;

        public ItemContatoItemHolder(View itemView) {
            super(itemView);
            this.txtNomeItemContato = itemView.findViewById(R.id.txtNomeItemContato);
            this.txtTelefoneItemContato = itemView.findViewById(R.id.txtTelefoneItemContato);
        }
    }
}

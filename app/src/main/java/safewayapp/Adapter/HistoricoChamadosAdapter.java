package safewayapp.Adapter;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import safewayapp.Activity.HistoricoChamadosActivity;
import safewayapp.Api.response.Contact;
import safewayapp.Api.response.EmergencyCall;
import safewayapp.R;

public class HistoricoChamadosAdapter extends RecyclerView.Adapter<HistoricoChamadosAdapter.HistoricoChamadosItemHolder> {
    private static final int UNSELECTED = -1;

    public List<EmergencyCall> mDataset;
    private HistoricoChamadosActivity mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public Geocoder coder;
    private int selectedItem = UNSELECTED;

    public HistoricoChamadosAdapter(List<EmergencyCall> dataSet, RecyclerView recyclerView, HistoricoChamadosActivity context) {
        mDataset = dataSet;
        mContext = context;
        mRecyclerView = recyclerView;
        coder = new Geocoder(mContext);
    }


    @NonNull
    @Override
    public HistoricoChamadosItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View vw = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_listview_historico_chamados_itens, viewGroup, false);

        HistoricoChamadosItemHolder viewHolder = new HistoricoChamadosItemHolder(vw);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoChamadosItemHolder holder, int position) {
        Date data = null;
        String formattedDate = null;
        try {
            data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(mDataset.get(position).getData());
            formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtDataChamados.setText(formattedDate);

        List<Address> addresses = null;
        try {
            addresses = coder.getFromLocation(Double.parseDouble(mDataset.get(position).getLatitude()), Double.parseDouble(mDataset.get(position).getLongitude()), 1);
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
        holder.txtEnderecoChamdados.setText(enderecoFinal);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class HistoricoChamadosItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {

        public TextView txtDataChamados;
        public TextView txtEnderecoChamdados;
        public CardView cardViewHistoricoChamados;
        private ExpandableLayout expandableLayout;
        private RecyclerView recyclerContatos;

        public HistoricoChamadosItemHolder(View itemView) {
            super(itemView);
            this.expandableLayout = itemView.findViewById(R.id.expandable_layout);
            this.txtDataChamados = itemView.findViewById(R.id.txtDataChamados);
            this.txtEnderecoChamdados = itemView.findViewById(R.id.txtEnderecoChamados);
            this.cardViewHistoricoChamados = itemView.findViewById(R.id.cardViewHistoricoChamados);

            this.recyclerContatos = itemView.findViewById(R.id.recyclerContatos);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(this);
            cardViewHistoricoChamados.setOnClickListener(this);
            expandableLayout.setDuration(0);
        }

        public void bind() {
            int position = getAdapterPosition();
            boolean isSelected = position == selectedItem;

            cardViewHistoricoChamados.setSelected(isSelected);
            expandableLayout.setExpanded(isSelected, false);
        }

        @Override
        public void onClick(View v) {
            HistoricoChamadosItemHolder holder = (HistoricoChamadosItemHolder) mRecyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {
                holder.cardViewHistoricoChamados.setSelected(false);
                holder.txtEnderecoChamdados.setSelected(false);
                holder.expandableLayout.collapse();
            }

            int position = getAdapterPosition();
            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                cardViewHistoricoChamados.setSelected(true);
                expandableLayout.expand();
                selectedItem = position;
            }
        }


        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            if (state == 3 && HistoricoChamadosAdapter.this.mRecyclerView != null && this.getAdapterPosition() != -1) {
                HistoricoChamadosAdapter.this.mRecyclerView.smoothScrollToPosition(this.getAdapterPosition());

                recyclerContatos.setHasFixedSize(true);

                mLayoutManager = new LinearLayoutManager(mContext);
                recyclerContatos.setLayoutManager(mLayoutManager);

                EmergencyCall contato = mDataset.get(this.getAdapterPosition());

                mAdapter = new ItemContatoAdapter(contato.contatos, mContext);
                recyclerContatos.setAdapter(mAdapter);
            }
        }
    }
}


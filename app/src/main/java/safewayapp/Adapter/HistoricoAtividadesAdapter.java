package safewayapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import safewayapp.Activity.HistoricoAtividadesActivity;
import safewayapp.Api.response.Record;
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
    public HistoricoAtividadesItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoAtividadesItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

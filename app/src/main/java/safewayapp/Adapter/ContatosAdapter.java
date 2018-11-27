package safewayapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import safewayapp.Fragment.ContactFragment;
import safewayapp.Persistence.Contato;
import safewayapp.R;
import safewayapp.Repository.IContatoDataSource;
import safewayapp.ViewHolder.ContatosItemHolder;

/**
 * Created by Pichau on 05/10/2018.
 */

public class ContatosAdapter extends RecyclerView.Adapter<ContatosItemHolder> {

    public List<Contato> mDataset;
    private IContatoDataSource mDataSource;
    private ContactFragment mContext;

    public ContatosAdapter(List<Contato> dataSet, IContatoDataSource dataSource, ContactFragment context) {
        mDataset = dataSet;
        mDataSource = dataSource;
        mContext = context;
    }

    @Override
    public ContatosItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View vw = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listview_contatos_itens, viewGroup, false);

        ContatosItemHolder viewHolder = new ContatosItemHolder(vw);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContatosItemHolder holder, int position) {
        holder.txtNome.setText(mDataset.get(position).getNome());
        holder.txtTelefone.setText(mDataset.get(position).getTelefone());
        //holder.viewForeground.setTag(mDataset.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void removeItem(int position) {
        mDataset.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Contato item, int position) {
        mDataset.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}

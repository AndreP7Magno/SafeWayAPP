package safewayapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import Fragment.ContactFragment;
import safewayapp.Persistence.Contato;
import safewayapp.R;
import safewayapp.Repository.IContatoDataSource;
import safewayapp.ViewHolder.ContatosItemHolder;

/**
 * Created by Pichau on 05/10/2018.
 */

public class ContatosAdapter extends RecyclerView.Adapter<ContatosItemHolder> {

    private List<Contato> mDataset;
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

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext.getContext(), "Edit", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext.getContext(), "Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

package safewayapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import safewayapp.Model.Contatos;
import safewayapp.R;
import safewayapp.ViewHolder.ContatosItemHolder;

/**
 * Created by Pichau on 05/10/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<ContatosItemHolder> {

    private List<Contatos> mContatos;
    private List<Contatos> mOriginalListContatos;
    private Context context;
    private Contatos mContato;

    public RecyclerAdapter(Context context, List<Contatos> contatos){
        super();
        this.mContatos = contatos;
        this.mOriginalListContatos = contatos;
        this.context = context;
    }

    @Override
    public ContatosItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.layout_listview_contatos_itens, parent, false);

        ContatosItemHolder holder = new ContatosItemHolder(context, convertView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContatosItemHolder holder, int position) {
        holder.position = position;
        mContato = mContatos.get(position);

        holder.txtNome.setText(mContato.getNome());
        holder.txtTelefone.setText(mContato.getTelefone());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mContatos.size();
    }
}

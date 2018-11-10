package safewayapp.ViewHolder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import safewayapp.Adapter.RecyclerAdapter;
import safewayapp.R;

/**
 * Created by Pichau on 05/10/2018.
 */

public class ContatosItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public CardView cardView;
    public TextView txtNome;
    public TextView txtTelefone;
    public ImageButton btnEdit;
    public ImageButton btnDelete;

    public RecyclerAdapter listaRecyclerAdapter;
    private Context context;
    public int position;

    public ContatosItemHolder(Context context, View convertView, RecyclerAdapter recyclerAdapter) {
        super(convertView);
        this.context = context;
        this.cardView = (CardView) convertView.findViewById(R.id.cardViewContatos);
        this.txtNome = (TextView) convertView.findViewById(R.id.txtItemNome);
        this.txtTelefone = (TextView) convertView.findViewById(R.id.txtItemTelefone);
        this.btnEdit = (ImageButton) convertView.findViewById(R.id.btnEdit);
        this.btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
        convertView.setTag(this);
        this.listaRecyclerAdapter = recyclerAdapter;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

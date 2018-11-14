package safewayapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import safewayapp.R;

/**
 * Created by Pichau on 05/10/2018.
 */

public class ContatosItemHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public TextView txtNome;
    public TextView txtTelefone;

    public ContatosItemHolder(@NonNull View itemView) {
        super(itemView);
        this.cardView = itemView.findViewById(R.id.cardViewContatos);
        this.txtNome = itemView.findViewById(R.id.txtItemNome);
        this.txtTelefone = itemView.findViewById(R.id.txtItemTelefone);
    }
}

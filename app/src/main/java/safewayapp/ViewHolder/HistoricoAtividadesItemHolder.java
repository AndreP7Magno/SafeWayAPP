package safewayapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import safewayapp.R;

public class HistoricoAtividadesItemHolder extends RecyclerView.ViewHolder {

    public TextView txtData;
    public TextView txtSeveridadeAtividades;
    public TextView txtEnderecoAtividades;
    public TextView txtDescricaoAtividades;

    public HistoricoAtividadesItemHolder(View itemView) {
        super(itemView);
        this.txtData = itemView.findViewById(R.id.txtData);
        this.txtSeveridadeAtividades = itemView.findViewById(R.id.txtSeveridadeAtividades);
        this.txtEnderecoAtividades = itemView.findViewById(R.id.txtEnderecoAtividades);
        this.txtDescricaoAtividades = itemView.findViewById(R.id.txtDescricaoAtividades);
    }
}

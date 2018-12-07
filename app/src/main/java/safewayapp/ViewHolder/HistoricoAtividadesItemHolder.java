package safewayapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import safewayapp.R;

public class HistoricoAtividadesItemHolder extends RecyclerView.ViewHolder {

    public TextView txtData;
    public TextView txtSeveridadeAtividades;
    public TextView txtLatitude;
    public TextView txtLongitude;
    public TextView txtDescricaoAtividades;

    public HistoricoAtividadesItemHolder(View itemView) {
        super(itemView);
        this.txtData = itemView.findViewById(R.id.txtData);
        this.txtSeveridadeAtividades = itemView.findViewById(R.id.txtSeveridadeAtividades);
        this.txtLatitude = itemView.findViewById(R.id.txtLatitude);
        this.txtLongitude = itemView.findViewById(R.id.txtLongitude);
        this.txtDescricaoAtividades = itemView.findViewById(R.id.txtDescricaoAtividades);
    }
}

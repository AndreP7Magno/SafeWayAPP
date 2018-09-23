package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.R;

public class HomeFragment extends Fragment {

    @BindView(R.id.fabAddReport)
    FloatingActionButton fabAddReport;

    @BindView(R.id.layoutFabReportarAssedio)
    LinearLayout layoutFabReportarAssedio;

    @BindView(R.id.layoutFabReportarSituacaPerigo)
    LinearLayout layoutFabReportarSituacaoPerigo;

    @BindView(R.id.fabReportarAssedio)
    FloatingActionButton fabReportarAssedio;

    @BindView(R.id.fabReportarSituacaoPerigo)
    FloatingActionButton fabReportarSituacaoPerigo;

    private boolean fabExpanded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        closeSubMenusFab();

        fabReportarAssedio.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Assedio", Toast.LENGTH_SHORT).show();
            }
        });

        fabReportarSituacaoPerigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Perigo", Toast.LENGTH_SHORT).show();
            }
        });

        fabAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
    }

    private void closeSubMenusFab(){
        layoutFabReportarAssedio.setVisibility(View.INVISIBLE);
        layoutFabReportarSituacaoPerigo.setVisibility(View.INVISIBLE);
        fabAddReport.setImageResource(R.drawable.ic_add_white_24dp);
        fabExpanded = false;
    }

    private void openSubMenusFab(){
        layoutFabReportarAssedio.setVisibility(View.VISIBLE);
        layoutFabReportarSituacaoPerigo.setVisibility(View.VISIBLE);
        fabAddReport.setImageResource(R.drawable.ic_close_white_24dp);
        fabExpanded = true;
    }
}

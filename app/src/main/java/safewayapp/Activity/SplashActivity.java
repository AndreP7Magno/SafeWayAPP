package safewayapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import safewayapp.Component.DaggerSplashComponent;
import safewayapp.Helper.DialogHelper;
import safewayapp.Helper.GPSHelper;
import safewayapp.Module.AppModule;
import safewayapp.Module.RoomModule;
import safewayapp.R;

public class SplashActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.getSupportActionBar().hide();

        GPSHelper.getInstance().Inicializa(getApplicationContext());

        DaggerSplashComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        verificaPerimissons();
        if (!sharedPreferences.getBoolean("shortcut", false))
            addShortcut();
    }

    private void verificaPerimissons() {
        try {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    executaValidacaoToken();
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    DialogHelper.getInstance().ShowError(SplashActivity.this, "Permissão negada para:\n" + deniedPermissions.toString());
                }
            };

            new TedPermission().with(getApplicationContext())
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("Se você recusar a permissão, você não poderá usar este serviço\n\nPor favor ative as permissões em [Configuração] > [Permissão]")
                    .setPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.SEND_SMS)
                    .setDeniedCloseButtonText("Fechar")
                    .setGotoSettingButtonText("Configuração")
                    .check();
        }catch (Exception ex)
        {
            String msg = ex.getMessage();
        }
    }

    private void executaValidacaoToken(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String Token = sharedPreferences.getString("Token","");
                String CPF = sharedPreferences.getString("CPF","");
                Date DataExpiracao = new Date(sharedPreferences.getLong("DataExpericao", 0));
                Date currentTime = Calendar.getInstance().getTime();
                if(CPF != "" && currentTime.before(DataExpiracao)) {
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it);
                    finish();
                }else{
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        }, 2000);
    }

    private void addShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(),
                MainActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "SafeWay - Perigo");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_shortcut_logo));

        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("shortcut", true);
        editor.commit();
    }
}

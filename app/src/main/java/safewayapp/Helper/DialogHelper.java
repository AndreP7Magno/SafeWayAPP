package safewayapp.Helper;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import safewayapp.R;

public class DialogHelper {
    private static final DialogHelper ourInstance = new DialogHelper();

    public static DialogHelper getInstance() {
        return ourInstance;
    }
    private MaterialDialog dialog;
    public DialogHelper() {
    }

    private AlertDialog.Builder getBuilder(int icon, String title, String msg, Activity activity){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity.getApplicationContext(), R.style.CustomDialogTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(msg);

        return builder;
    }

    public void showGPSSettingDialog(final Activity activity) {
        AlertDialog.Builder builder = getBuilder(R.drawable.ic_dialog_alert,
                activity.getApplicationContext().getResources().getString(R.string.txtGPS),
                activity.getApplicationContext().getResources().getString(R.string.msg_ativar_gps),
                activity);
        builder.setPositiveButton(activity.getApplicationContext().getResources().getString(R.string.txtConfiguracoes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.getApplicationContext().startActivity(intent);
            }
        });

        builder.setNegativeButton(activity.getApplicationContext().getResources().getString(R.string.txtVoltar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        showDialogWithTheme(builder, activity);
    }

    private void showDialogWithTheme(AlertDialog.Builder builder, Activity activity) {
        if(! activity.isFinishing()) {
            Dialog d = builder.show();
            setDialogTheme(d, activity);
        }
    }

    private void setDialogTheme(Dialog d, Activity activity) {
        int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) d.findViewById(textViewId);
        tv.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorPrimary));

        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.colorPrimary));

        Button positiveButton = ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setBackgroundResource(R.drawable.safeway_btn_dialog);
        Button negativeButton = ((AlertDialog) d).getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setBackgroundResource(R.drawable.safeway_btn_dialog);
    }

    public void ShowError(Activity activity, int idResource)
    {
        dialog = new  MaterialDialog
                .Builder(activity)
                .title("Erro!")
                .content(idResource).build();

        dialog.show();
    }

    public void ShowAlert(Activity activity, int idResource, MaterialDialog.SingleButtonCallback callback)
    {
        dialog = new MaterialDialog
                .Builder(activity)
                .title("Atenção!")
                .titleColor(activity.getResources().getColor(R.color.colorPrimary))
                .positiveText("Ok")
                .negativeText("Cancelar")
                .positiveColor(activity.getResources().getColor(R.color.colorPrimary))
                .onPositive(callback)
                .content(idResource).build();

        dialog.show();
    }

    public void ShowAlertPositiveNegative(Activity activity, int idResource, MaterialDialog.SingleButtonCallback callbackPositivo, MaterialDialog.SingleButtonCallback callbackNegativo)
    {
        dialog = new MaterialDialog
                .Builder(activity)
                .title("Atenção!")
                .titleColor(activity.getResources().getColor(R.color.colorPrimary))
                .positiveText("Sim")
                .negativeText("Cancelar")
                .positiveColor(activity.getResources().getColor(R.color.colorPrimary))
                .onPositive(callbackPositivo)
                .onNegative(callbackNegativo)
                .onNeutral(callbackNegativo)
                .onAny(callbackNegativo)
                .content(idResource).build();

        dialog.show();
    }

    public void ShowAlert(Activity activity, String msg, MaterialDialog.SingleButtonCallback callback)
    {
        dialog = new MaterialDialog
                .Builder(activity)
                .title("Atenção!")
                .titleColor(activity.getResources().getColor(R.color.colorPrimary))
                .positiveText("Ok")
                .negativeText("Cancelar")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .positiveColor(activity.getResources().getColor(R.color.colorPrimary))
                .onPositive(callback)
                .content(msg).build();

        dialog.show();
    }

    public void ShowError(Activity activity, String msg)
    {
        dialog = new  MaterialDialog
                .Builder(activity)
                .title("Erro!")
                .content(msg).build();

        dialog.show();
    }


    public void ShowProgress(Activity activity, int idResource)
    {
        dialog = new MaterialDialog.Builder(activity).title("Aguarde!").content(idResource).build();

        dialog.show();
    }

    public void Dismiss()
    {
        if(dialog.isShowing())
            dialog.dismiss();
    }
}

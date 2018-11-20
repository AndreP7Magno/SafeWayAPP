package safewayapp.Helper;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import safewayapp.R;

public class DialogHelper {
    private static final DialogHelper ourInstance = new DialogHelper();

    public static DialogHelper getInstance() {
        return ourInstance;
    }
    private MaterialDialog dialog;
    private DialogHelper() {
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

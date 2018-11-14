package safewayapp.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogHelper {
    ProgressDialog mProgressDialog;
    private Activity mContext;

    public ProgressDialogHelper() {

    }

    public ProgressDialogHelper(Activity context) {
        this.mContext=context;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setIndeterminate(true);
    }
    public ProgressDialogHelper(Activity context, String title, String message) {
        this.mContext=context;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    public void setProgressDialog(ProgressDialog mProgressDialog) {
        //make sure the previous dialog is hidden
        hide();
        this.mProgressDialog = mProgressDialog;
    }

    public void show() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void create(Context context, String title, String message) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(context, title, message);
    }

    public void hide() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    public void dismiss(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            });
            mProgressDialog = null;
        }
    }

    public void onDestroy() {
        hide();
    }
}

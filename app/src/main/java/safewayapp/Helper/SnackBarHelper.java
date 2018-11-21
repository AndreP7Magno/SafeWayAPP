package safewayapp.Helper;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import safewayapp.R;

public class SnackBarHelper {
    private static int mTextColor = Color.WHITE;
    private static int mBackgroundColor = R.color.colorPrimary;
    private static int mTime = Snackbar.LENGTH_SHORT;
    private static CoordinatorLayout mCoordinatorLayout;

    private static final SnackBarHelper ourInstance = new SnackBarHelper();

    public static SnackBarHelper getInstance(CoordinatorLayout coordinatorLayout) {
        mCoordinatorLayout = coordinatorLayout;
        return ourInstance;
    }

    private SnackBarHelper() {
    }

    public void setTextColor(int textColor)
    {
        mTextColor = textColor;
    }

    public void show(int resource, int time)
    {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout,
                resource, time);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(mBackgroundColor);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(mTextColor);

        snackbar.show();
    }

    public void show(String msg, int time)
    {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout,
                msg, time);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(mBackgroundColor);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(mTextColor);

        snackbar.show();
    }


    public void showBottomNaviagtion(String msg, int time)
    {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout,
                msg, time);
        View snackBarView = snackbar.getView();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                snackbar.getView().getLayoutParams();
        params.setMargins(0, 0, 0, 12);

        snackbar.getView().setLayoutParams(params);
        snackBarView.setBackgroundResource(mBackgroundColor);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(mTextColor);

        snackbar.show();
    }
}

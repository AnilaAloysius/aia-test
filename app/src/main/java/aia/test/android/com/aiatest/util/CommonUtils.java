package aia.test.android.com.aiatest.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import aia.test.android.com.aiatest.constants.StringConstants;
import aia.test.android.com.aiatest.model.Datum;

public class CommonUtils {
    private volatile static CommonUtils instance = null;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private List<Datum> imgurData = new ArrayList<>();


    public static CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }

    public String getDate(long milliSeconds) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(milliSeconds * 1000L);
        String date = DateFormat.format(StringConstants.DateTimeFormat, cal).toString();

        return date;
    }


    /**
     * Converting dp to pixel
     */
    public int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * Hides the soft keyboard while app launches
     */
    public void hideKeyboard(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public void hideKeyboardFromEditText(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void showAlertDialogs(final Context context, String alertMessage, String header) {
        new AlertDialog.Builder(context)
                .setTitle(header)
                .setMessage(alertMessage)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setImgurData(List<Datum> imgurData) {
        Collections.sort(imgurData, new Comparator<Datum>() {
            public int compare(Datum dateTimeID1, Datum dateTimeID2) {
                return dateTimeID1.getDatetime().compareTo(dateTimeID2.getDatetime());
            }
        });
        Collections.reverse(imgurData);
        this.imgurData.addAll(imgurData);
    }

    public List<Datum> getImgurData() {
        return imgurData;
    }

    public boolean isEvenOrNot(Integer score, Integer points, Integer topicId) {
        boolean evenStatus = false;
        if ((score + points + topicId) % 2 == 0) {
            evenStatus = true;
            return evenStatus;
        }
        return evenStatus;
    }
}

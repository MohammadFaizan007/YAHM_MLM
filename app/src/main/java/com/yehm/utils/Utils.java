package com.yehm.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.yehm.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class Utils {
    public static final int DIALOG_NETWORK_ERROR = 10;
    public static final int DIALOG_NO_CONNECTION = 100;

    static private ProgressDialog mProgressDialog;
    static private AlertDialog mErrorDialog;

    public static String getEmail(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        if (accounts != null && accounts.length > 0)
            return accounts[0].name;
        else
            return null;
    }

//    public static void showNoAccountDialog(Context context) {
//        AlertDialog.Builder noEmailDialog = new AlertDialog.Builder(context);
//        noEmailDialog.setMessage(context.getResources().getString(R.string.no_account_message))
//                .setNeutralButton(android.R.string.ok, null)
//                .show();
//    }

    public static boolean isEmailAddress(String text) {
        String pattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
        return text.matches(pattern);
    }

    // fragment.isAdded() checks in FragmentManager's backstack;
    // this function checks in FragmentManager's maintain list
    //
    // sometimes we have fragment managed by FM, but in its backstack;
    // this function helps find such fragments
    //
    // HINT: currently has no use but maybe will if we have some
    // fragment backstack issues in future
    public static boolean isFragmentAdded(Fragment frg, FragmentManager fm) {
        if (frg == null || frg.getClass() == null
                || frg.getClass().getName() == null)
            return false;

        if (fm.getFragments() == null)
            return false;

        for (Fragment f : fm.getFragments()) {
            if (f != null && f.getClass() != null
                    && f.getClass().getName() != null)
                if (f.getClass().getName().equals(frg.getClass().getName())
                        || f.getTag() != null
                        && f.getTag().equals(frg.getClass().getName()))
                    return true;
        }
        return false;
    }

    public static void startVibration(final Context context) {
        AsyncTask<Integer, Void, Void> vibration = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... params) {
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null)
                    v.vibrate(params[0]);
                return null;
            }
        };

        // Vibrate for 2000 milliseconds
        AsyncTaskExecutionHelper.executeParallel(vibration, 2000);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static Bitmap getCompressedBitmap(String imagePath) {

        float maxHeight = 350.0f;
        float maxWidth = 500.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        assert scaledBitmap != null;
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);

        byte[] byteArray = out.toByteArray();

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static void playDefaultNotificationSound(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    public static String changeDateFormat(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        return dateFormat.format(c.getTime());
    }


    public static void blinkLED(Context context) {
        NotificationManager notifMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification();
        notif.ledARGB = Color.argb(255, 0, 255, 0);
        notif.flags |= Notification.FLAG_SHOW_LIGHTS;
        notif.ledOnMS = 300;
        notif.ledOffMS = 200;
        notifMgr.notify(999, notif);
    }


    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            View v = activity.getCurrentFocus();
            if (v != null) {
                IBinder binder = activity.getCurrentFocus()
                        .getWindowToken();
                if (binder != null) {
                    inputMethodManager.hideSoftInputFromWindow(binder, 0);
                }
            }
        }
    }

    public static Dialog createSimpleDialog1(Context context, String title,
                                             String msg, String btnLabel1, final Method method1) {
        String chars = title;
        SpannableString str = new SpannableString(chars);

        str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.success)), 0,
                chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);
        builder.setMessage(msg);

        builder.setPositiveButton(btnLabel1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (method1 != null)
                            method1.execute();
                    }
                });

        Dialog d = builder.show();
        int dividerId = d.getContext().getResources()
                .getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        try {
            divider.setBackgroundColor(ContextCompat.getColor(context, R.color.success));
        } catch (Exception e) {
        }

        return d;

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public interface Method {
        void execute();
    }

}
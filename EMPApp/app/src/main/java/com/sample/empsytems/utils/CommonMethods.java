package com.sample.empsytems.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.sample.empsytems.R;
import com.sample.empsytems.ui.interfaces.onAlertCallbackListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMethods {

    private static AlertDialog.Builder builder = null;

    public static void startActivity(Context context, Class destClass, boolean shouldFinish) {
        Intent intent = new Intent(context, destClass);
        context.startActivity(intent);
        if (shouldFinish) {
            ((Activity) context).finish();
        }
    }

    public static void showAlertMessage(Context context, String strMessage) {

        if (builder == null) {
            builder = new AlertDialog.Builder(context);
            builder.setMessage(strMessage)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            builder = null;
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public static void showAlertMessageCallback(Context context, String strMessage, final onAlertCallbackListener onAlertCallbackListener) {

        if (builder == null) {
            builder = new AlertDialog.Builder(context);
            builder.setMessage(strMessage)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(onAlertCallbackListener!=null){
                                onAlertCallbackListener.onClickOkay();
                            }
                            builder = null;
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public static int getVehicleIconByPosition(int iVehicleType, int position) {
        if (iVehicleType == Utility.TAG_VEHICLE_CAR) {
            switch (position) {
                case 0:
                    return R.drawable.ic_car_audi;

                case 1:
                    return R.drawable.ic_car_bmw;

                case 2:
                    return R.drawable.ic_car_bently;

                case 3:
                    return R.drawable.ic_car_ferrari;

                case 4:
                    return R.drawable.ic_car_ford;

                case 5:
                    return R.drawable.ic_car_ktm;

                case 6:
                    return R.drawable.ic_car_lambo;

                case 7:
                    return R.drawable.ic_car_mercedes;

                case 8:
                    return R.drawable.ic_car_mini;
            }
        } else if (iVehicleType == Utility.TAG_VEHICLE_MOTORBIKE) {
            switch (position) {
                case 0:
                    return R.drawable.ic_bike_honda;

                case 1:
                    return R.drawable.ic_bike_harley;
            }
        }
        return R.drawable.ic_no_vehicle;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

package android.lehman.sillektis.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Class from Sillektis at android.lehman.sillektis.data
 * Created by Paulo-Lehman on 5/5/2015.
 */
public class SillektisContract {
    public static final String CONTENT_AUTHORITY = "android.lehman.sillektis";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DEBTOR = "debtor";
    public static final String PATH_PAYMENT = "payment";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.setToNow();
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the debtor table */
    public static final class DebtorEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEBTOR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEBTOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEBTOR;

        public static final String TABLE_NAME = "debtor";

//        public static final String COLUMN_ID_DEBTOR = "id_debtor";

        public static final String COLUMN_NAME_DEBTOR = "name_debtor";

        public static final String COLUMN_AMOUNT_DEBTOR = "amount_debtor";

        public static final String COLUMN_EMAIL_DEBTOR = "email_debtor";

        public static final String COLUMN_PHONE_DEBTOR = "phone_debtor";

        public static final String COLUMN_DATE_DEBTOR = "date_debtor";

        public static final String COLUMN_PAID_DEBTOR = "paid_debtor";


        public static Uri buildDebtorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildDebtorWithStartDate(
                String debtorSetting, long startDate) {
            long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(debtorSetting)
                    .appendQueryParameter(COLUMN_DATE_DEBTOR, Long.toString(normalizedDate)).build();
        }

//        public static Uri buildDebtorWithDate(String debtorSetting, long date) {
//            return CONTENT_URI.buildUpon().appendPath(debtorSetting)
//                    .appendPath(Long.toString(normalizeDate(date))).build();
//        }
//
//        public static String getPaymentSettingFromUri(Uri uri) {
//            return uri.getPathSegments().get(1);
//        }
//
//        public static long getDateFromUri(Uri uri) {
//            return Long.parseLong(uri.getPathSegments().get(2));
//        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE_DEBTOR);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }
    }

    /*
        Inner class that defines the table contents of the payment table
     */
    public static final class PaymentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PAYMENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PAYMENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PAYMENT;

        public static final String TABLE_NAME = "payment";

//        public static final String COLUMN_ID_PAYMENT = "id_payment";

        public static final String COLUMN_AMOUNT_PAYMENT = "amount_payment";

        public static final String COLUMN_DATE_PAYMENT = "date_payment";

        public static final String COLUMN_ID_DEBTOR = "id_debtor";

        public static Uri buildPaymentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /*
        *  TODO: Build relationship between Debtor (1:n) Payment
        */
        public static Uri buildPaymentLocation(String debtorSetting) {
            return CONTENT_URI.buildUpon().appendPath(debtorSetting).build();
        }

        public static Uri buildPaymentWithStartDate(
                String locationSetting, long startDate) {
            long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COLUMN_DATE_PAYMENT, Long.toString(normalizedDate)).build();
        }
    }
}

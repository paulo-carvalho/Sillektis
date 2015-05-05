package android.lehman.sillektis.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class SillektisContentProvider extends ContentProvider {

    private static final String TAG = SillektisContentProvider.class.getSimpleName();

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SillektisDbHelper mOpenHelper;

    static final int PAYMENT = 100;
    static final int DEBTOR = 300;

    private static final SQLiteQueryBuilder sPaymentByDebtorSettingQueryBuilder;

    static{
        sPaymentByDebtorSettingQueryBuilder = new SQLiteQueryBuilder();
        sPaymentByDebtorSettingQueryBuilder.setTables(
                SillektisContract.PaymentEntry.TABLE_NAME + " INNER JOIN " +
                        SillektisContract.DebtorEntry.TABLE_NAME +
                        " ON " + SillektisContract.PaymentEntry.TABLE_NAME +
                        "." + SillektisContract.PaymentEntry.COLUMN_ID_DEBTOR +
                        " = " + SillektisContract.DebtorEntry.TABLE_NAME +
                        "." + SillektisContract.DebtorEntry._ID);
    }

    private static final String sDebtorSelection =
            SillektisContract.DebtorEntry.TABLE_NAME+
                    "." + SillektisContract.DebtorEntry.COLUMN_NAME_DEBTOR + " = ? ";

    private static final String sPaymentSelection =
            SillektisContract.PaymentEntry.TABLE_NAME +
                    "." + SillektisContract.PaymentEntry._ID + " > ? ";

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the PAYMENT, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and DEBTOR integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SillektisContract.CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        matcher.addURI(authority, SillektisContract.PATH_PAYMENT, PAYMENT);
        matcher.addURI(authority, SillektisContract.PATH_DEBTOR, DEBTOR);

        // 3) Return the new matcher!
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new SillektisDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.
     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PAYMENT:
                return SillektisContract.PaymentEntry.CONTENT_TYPE;
            case DEBTOR:
                return SillektisContract.DebtorEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case PAYMENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SillektisContract.PaymentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case DEBTOR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SillektisContract.DebtorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PAYMENT: {
                normalizeDate(values);
                long _id = db.insert(SillektisContract.PaymentEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = SillektisContract.PaymentEntry.buildPaymentUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case DEBTOR: {
                normalizeDate(values);
                long _id = db.insert(SillektisContract.DebtorEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = SillektisContract.DebtorEntry.buildDebtorUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rows;

        if (selection == null) {
            selection = "1";
        }
        switch (match) {
            case PAYMENT: {
                rows = db.delete(SillektisContract.PaymentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case DEBTOR: {
                rows = db.delete(SillektisContract.DebtorEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rows;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rows;

        switch (match) {
            case PAYMENT: {
                rows = db.update(SillektisContract.PaymentEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case DEBTOR: {
                rows = db.update(SillektisContract.DebtorEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    private void normalizeDate(ContentValues values) {
        if (values.containsKey(SillektisContract.PaymentEntry.COLUMN_DATE_PAYMENT)) {
            long dateValue = values.getAsLong(SillektisContract.PaymentEntry.COLUMN_DATE_PAYMENT);
            values.put(SillektisContract.PaymentEntry.COLUMN_DATE_PAYMENT, SillektisContract.normalizeDate(dateValue));
        }
    }

    /*
     *  bulkInsert(): insert multiple rows at once
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PAYMENT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(SillektisContract.PaymentEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}

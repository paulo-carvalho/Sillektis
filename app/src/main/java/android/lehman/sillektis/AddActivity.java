package android.lehman.sillektis;

import android.app.Activity;
import android.content.Intent;
import android.lehman.sillektis.data.SillektisContract;
import android.lehman.sillektis.data.SillektisDbHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Class from Sillektis at android.lehman.sillektis
 * Created by Paulo-Lehman on 5/4/2015.
 */
public class AddActivity extends Activity {

    SillektisDbHelper sillektisDbHelper = new SillektisDbHelper(this);
    private static final String DEBTOR_NOTPAID = "0";
    public static final String IS_ADDED = "is_added";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Button addDebtor = (Button) findViewById(R.id.addButton);
        addDebtor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nameDebtor = (TextView) findViewById(R.id.nameDebtor);
                TextView emailDebtor = (TextView) findViewById(R.id.emailDebtor);
                TextView phoneDebtor = (TextView) findViewById(R.id.phoneDebtor);
                TextView amountDebtor = (TextView) findViewById(R.id.amountDebtor);

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                HashMap<String, String> entries = new HashMap<>();
                entries.put("table", SillektisContract.DebtorEntry.TABLE_NAME);
                entries.put(SillektisContract.DebtorEntry.COLUMN_NAME_DEBTOR, nameDebtor.getText().toString());
                entries.put(SillektisContract.DebtorEntry.COLUMN_EMAIL_DEBTOR, emailDebtor.getText().toString());
                entries.put(SillektisContract.DebtorEntry.COLUMN_PHONE_DEBTOR, phoneDebtor.getText().toString());
                entries.put(SillektisContract.DebtorEntry.COLUMN_AMOUNT_DEBTOR, amountDebtor.getText().toString());
                entries.put(SillektisContract.DebtorEntry.COLUMN_DATE_DEBTOR, dateFormat.format(date));
                entries.put(SillektisContract.DebtorEntry.COLUMN_PAID_DEBTOR, DEBTOR_NOTPAID);

                sillektisDbHelper.insert(entries);

                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

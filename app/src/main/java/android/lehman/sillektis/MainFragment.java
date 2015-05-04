package android.lehman.sillektis;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class from Sillektis at android.lehman.sillektis
 * Created by Paulo-Lehman on 5/3/2015.
 */
public class MainFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

    private ArrayAdapter<String> debtorsAdapter;

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (D) { Log.d(TAG, "Starting onCreateView");}
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        List<String> debtors = new ArrayList<>();
        debtors.add("Debtor 1");

        debtorsAdapter = new ArrayAdapter<>(
                getActivity(), // context
                R.layout.debtor_list_item, // layout to use for each item
                R.id.debtor_list_item_name, // textview to update with each item
                debtors); // data
//        editTextLatLong = (EditText)rootView.findViewById(R.id.latLon);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_debtors);
        listView.setAdapter(debtorsAdapter); //define the design
        listView.setOnItemClickListener(new ItemClickListener()); //define functionality

        if (D) { Log.d(TAG, "onCreateView completed");}
        return rootView;
    }

    /*  Class: ItemClickListener
     *  Description: encapsulates the onClick event for each item of the list.
     *  Methods:
       *  + onItemClick
     */
    public class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            String debtorName = (String)adapter.getItemAtPosition(position);

            Intent intent = new Intent(view.getContext(), ViewDebtorActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}

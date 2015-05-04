package android.lehman.sillektis;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class from Sillektis at android.lehman.sillektis
 * Created by Paulo-Lehman on 5/3/2015.
 */
public class AddFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

    public AddFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (D) { Log.d(TAG, "Starting onCreateView");}
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);


        if (D) { Log.d(TAG, "onCreateView completed");}
        return rootView;
    }
}

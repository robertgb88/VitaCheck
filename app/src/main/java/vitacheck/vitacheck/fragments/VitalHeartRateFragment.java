package vitacheck.vitacheck.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import vitacheck.vitacheck.MainActivity;
import vitacheck.vitacheck.R;

/**
 * Created by ERIC on 12/5/2015.
 */
public class VitalHeartRateFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    /*video on recylerView can be found here: https://www.youtube.com/watch?v=Wq2o4EbM74k   */
    private RecyclerView recyclerView;
    private VitalHeartRateAdapter adapter;
    private List<VitalHeartRateInfo> vitalHeartRateList = new ArrayList<VitalHeartRateInfo>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    
    public VitalHeartRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //((MainActivity) getActivity()).setTitle("Heart Rate");

        vitalHeartRateList = new ArrayList<VitalHeartRateInfo>();

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_vitals_heart_rate, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.vitalsHeartRateList);

        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.vitalsHeartRateSwipeRefreshContainer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        
        /*sets the layout on how we want the data to be displayed
        * if we wanted to we could set it to grid or staggered layout but in this case
        * we wanted it to be list going from top to bottom so we use LinearLayout*/
        ParseObject.registerSubclass(VitalHeartRateInfo.class);
        ParseQuery<VitalHeartRateInfo> query = new ParseQuery<VitalHeartRateInfo>("vital_heart_rate");
        query.findInBackground(new FindCallback<VitalHeartRateInfo>() {
            @Override
            public void done(List<VitalHeartRateInfo> objects, com.parse.ParseException e) {
                if (e != null) {
                    Toast.makeText(getView().getContext(), "Error " + e, Toast.LENGTH_SHORT).show();
                }

                for (VitalHeartRateInfo hrObject : objects) {
                    if (hrObject.getUserId().equals(GlobalVariable.getUserId(getActivity()))) {
                        VitalHeartRateInfo newHR = new VitalHeartRateInfo();
                        newHR.setParseId(hrObject.getObjectId());
                        newHR.setHeartRate(hrObject.getHeartRate());
                        newHR.setUploadDate(hrObject.getCreatedAt());
                        vitalHeartRateList.add(newHR);
                    }
                }
                /*have to make adapter and set here because if set outside done method and after
                data will not appear because it sets the data before it is pulled from parse
                https://www.youtube.com/watch?v=ZlUjJSE7YW4&list=PLBA5zvAwnCrCdIilqLW7tTA2Uuao1u8iH&index=5
                start at 10 min to see explanation.     -eric*/
                adapter = new VitalHeartRateAdapter(getActivity(), vitalHeartRateList);
                recyclerView.setAdapter(adapter); //sets adapter to recyclerview
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                
            }
        });
        return layout;
    }

    @Override
    public void onRefresh() {
        vitalHeartRateList = new ArrayList<VitalHeartRateInfo>();
        ParseObject.registerSubclass(VitalHeartRateInfo.class);
        ParseQuery<VitalHeartRateInfo> query = new ParseQuery<VitalHeartRateInfo>("vital_heart_rate");
        query.findInBackground(new FindCallback<VitalHeartRateInfo>() {
            @Override
            public void done(List<VitalHeartRateInfo> objects, com.parse.ParseException e) {
                if (e != null) {Toast.makeText(getView().getContext(), "Error " + e, Toast.LENGTH_SHORT).show();}
                for (VitalHeartRateInfo hrObject : objects) {
                    if (!(vitalHeartRateList.contains(hrObject))) ;
                    VitalHeartRateInfo newHeartRate = new VitalHeartRateInfo();
                    newHeartRate.setParseId(hrObject.getObjectId());
                    newHeartRate.setHeartRate(hrObject.getHeartRate());
                    newHeartRate.setUploadDate(hrObject.getCreatedAt());
                    vitalHeartRateList.add(newHeartRate);
                }
                adapter = new VitalHeartRateAdapter(getActivity(), vitalHeartRateList);
                recyclerView.setAdapter(adapter); //sets adapter to recyclerview
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
    
} //end of fragment class
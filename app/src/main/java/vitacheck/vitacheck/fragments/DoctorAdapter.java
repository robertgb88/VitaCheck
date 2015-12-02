package vitacheck.vitacheck.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vitacheck.vitacheck.R;

/**
 * Created by ERIC on 11/17/2015.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private final Context doctorContext;
    /*i used emptyList() so that there is no null expect error if data does not load*/
    private List<DoctorInfo> doctorList;//= Collections.emptyList();

    public DoctorAdapter(Context context, List<DoctorInfo> data){
        /*inflates the recyclerView doctor_row xml file*/
        inflater=LayoutInflater.from(context);
        doctorContext=context;
        this.doctorList=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    /*called when recyclerView needs a new RecyclerView.ViewHolder of a given type to represent an item*/
        /*view represents the root of the recyclerview_doctor_row xml file*/
        View view=inflater.inflate(R.layout.recyclerview_doctor_row,parent, false);

        /*passes the view (root) to the view holder class. view holder class is at the bottom*/
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*gets data, notice current is a doctor info object class*/
        DoctorInfo current = doctorList.get(position);

        /*sets data that will be displayed in the recyclerview_doctor_row xml file*/
        holder.doctorName.setText(current.getName());
        holder.doctorTypeSubText.setText(current.getDoctorType());
    }

    @Override
    public int getItemCount() {   return doctorList.size();}

    public void deleteDoctor(int position){
        /*look up notifyDataSetChanged() method (use as last resort) instead use
        * more specific changes, read more on it you'll find out*/
        doctorList.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        /*stuff that i want to be displayed in the recyclerview*/
        TextView doctorName;
        TextView doctorTypeSubText;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            doctorName= (TextView) itemView.findViewById(R.id.doctorNameField);
            doctorTypeSubText= (TextView) itemView.findViewById(R.id.doctorTypeField);
            doctorName.setOnClickListener(this);
            doctorName.setOnLongClickListener(this);
            doctorTypeSubText.setOnClickListener(this);
            doctorTypeSubText.setOnLongClickListener(this);
        }

        @Override
        /*video on how to handle recycler clicks found here: https://www.youtube.com/watch?v=zE1E1HOK_E4   */
        public void onClick(View v) {
            //Link on how to get the position of clicked item: http://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
            int clickPosition=this.getAdapterPosition();
            DoctorInfo current = doctorList.get(clickPosition);
            //Link on how to use bundles: http://www.101apps.co.za/index.php/articles/passing-data-between-activities.html
            Bundle bundle = new Bundle();
            bundle.putString("parseID",current.getParseId());
            Intent myIntent = new Intent(doctorContext ,DoctorActivity.class); //video on starting new activity in onClick: https://www.youtube.com/watch?v=K9F6U7yN2vI
            myIntent.putExtras(bundle);
            doctorContext.startActivity(myIntent); //or just look at Michael's MainActivity.java class
        }

        @Override
        public boolean onLongClick(View v) {
            //deleteDoctor(getPosition());
            return false;
        }
    }

}//end of DoctorAdapter class

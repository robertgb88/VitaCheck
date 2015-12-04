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
 * Created by Robert on 12/3/2015.
 */
public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private final Context medicineContext;
    /*i used emptyList() so that there is no null expect error if data does not load*/
    private List<MedicineInfo> medicineList;//= Collections.emptyList();

    public MedicineAdapter(Context context, List<MedicineInfo> data){
        /*inflates the recyclerView doctor_row xml file*/
        inflater=LayoutInflater.from(context);
        medicineContext=context;
        this.medicineList=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    /*called when recyclerView needs a new RecyclerView.ViewHolder of a given type to represent an item*/
        /*view represents the root of the recyclerview_doctor_row xml file*/
        View view=inflater.inflate(R.layout.recyclerview_medicine_row,parent, false);

        /*passes the view (root) to the view holder class. view holder class is at the bottom*/
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*gets data, notice current is a doctor info object class*/
        MedicineInfo current = medicineList.get(position);

        /*sets data that will be displayed in the recyclerview_doctor_row xml file*/
        holder.medicineName.setText(current.getName());
        holder.noteSubText.setText(current.getNote());
    }

    @Override
    public int getItemCount() {   return medicineList.size();}

    public void deleteMedicine(int position){
        /*look up notifyDataSetChanged() method (use as last resort) instead use
        * more specific changes, read more on it you'll find out*/
        medicineList.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        /*stuff that i want to be displayed in the recyclerview*/
        TextView medicineName;
        TextView noteSubText;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            medicineName= (TextView) itemView.findViewById(R.id.medicineNameField);
            noteSubText= (TextView) itemView.findViewById(R.id.noteTypeField);
            medicineName.setOnClickListener(this);
            medicineName.setOnLongClickListener(this);
            noteSubText.setOnClickListener(this);
            noteSubText.setOnLongClickListener(this);
        }

        @Override
        /*video on how to handle recycler clicks found here: https://www.youtube.com/watch?v=zE1E1HOK_E4   */
        public void onClick(View v) {
            //Link on how to get the position of clicked item: http://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
            int clickPosition=this.getAdapterPosition();
            MedicineInfo current = medicineList.get(clickPosition);
            //Link on how to use bundles: http://www.101apps.co.za/index.php/articles/passing-data-between-activities.html
            Bundle bundle = new Bundle();
            bundle.putString("parseID",current.getParseId());
            Intent myIntent = new Intent(medicineContext ,MedicineActivity.class); //video on starting new activity in onClick: https://www.youtube.com/watch?v=K9F6U7yN2vI
            myIntent.putExtras(bundle);
            medicineContext.startActivity(myIntent); //or just look at Michael's MainActivity.java class
        }

        @Override
        public boolean onLongClick(View v) {
            //deleteMedicine(getPosition());
            return false;
        }
    }

}//end of DoctorAdapter class

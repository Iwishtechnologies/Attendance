package com.iwish.myapplication.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.iwish.myapplication.R;
import com.iwish.myapplication.Takepicture;
import java.util.List;



public class AttendanceAdapter extends  RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{


    Context context;
    List<Attendancedata> attendancedata;
    private static final int CAMERA_REQUEST = 1888;
    com.mindorks.paracamera.Camera camera;
    public static final int REQUSET_IMAGE_CAPTURE = 101;



    public AttendanceAdapter(Context context, List<Attendancedata> attendancedata) {
        this.context = context;
        this.attendancedata = attendancedata;


    }


    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.attendence_row_design,parent,false);
        ViewHolder viewHolder =new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceAdapter.ViewHolder holder, final int position) {
        holder.name.setText(attendancedata.get(position).getName());
        holder.id.setText("ID : "+ attendancedata.get(position).getId());
        holder.outcamera.isClickable();
        Glide.with(context)
                .load("http://173.212.226.143/hospital/"+attendancedata.get(position).getPhoto())
                .into(holder.photo);
//        holder.photo.;
        holder.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(context,Takepicture.class);
                cameraIntent.putExtra("worker",attendancedata.get(position).getId());
                cameraIntent.putExtra("in/out","in");
                context.startActivity(cameraIntent);
                attendancedata.get(position).setState(true);
                Animatoo.animateInAndOut(context);
                holder.camera.setVisibility(View.GONE);
                holder.right.setVisibility(View.VISIBLE);
            }
        });


        holder.outcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attendancedata.get(position).getState()){
                Intent cameraIntent = new Intent(context,Takepicture.class);
                cameraIntent.putExtra("worker",attendancedata.get(position).getId());
                cameraIntent.putExtra("in/out","out");
                context.startActivity(cameraIntent);
                Animatoo.animateInAndOut(context);
                holder.outcamera.setVisibility(View.GONE);
                holder.outright.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(context, "In Attendance is Absent", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attendancedata.get(position).getState()){
                    Intent cameraIntent = new Intent(context,Takepicture.class);
                    cameraIntent.putExtra("worker",attendancedata.get(position).getId());
                    cameraIntent.putExtra("in/out","Live");
                    context.startActivity(cameraIntent);
                    Animatoo.animateInAndOut(context);
//                    holder.outcamera.setVisibility(View.GONE);
//                    holder.outright.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(context, "In Attendance is Absent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return attendancedata.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id,name,feedback;
        ImageView right,camera,photo,outcamera,outright,live;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id=itemView.findViewById(R.id.id);
            name=itemView.findViewById(R.id.name);
            right=itemView.findViewById(R.id.right);
            camera=itemView.findViewById(R.id.camera);
            feedback=itemView.findViewById(R.id.feedback);
            photo=itemView.findViewById(R.id.photo);
            outcamera=itemView.findViewById(R.id.outcamera);
            outright=itemView.findViewById(R.id.outright);
            live=itemView.findViewById(R.id.live);

        }
    }



//    public void camera(){
//        camera = new Camera.Builder()
//                .resetToCorrectOrientation(true)
//                .setTakePhotoRequestCode(1)
//                .setDirectory("pics")
//                .setName("ali_" + System.currentTimeMillis())
//                .setImageFormat(Camera.IMAGE_JPEG)
//                .setCompression(75)
//                .setImageHeight(1000)
//                .build((Attendance_list) context);
//    }

}


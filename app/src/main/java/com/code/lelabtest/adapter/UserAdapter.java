package com.code.lelabtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code.lelabtest.R;
import com.code.lelabtest.activity.UserDetailsActivity;
import com.code.lelabtest.model.UserInfo;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private String TAG = UserAdapter.class.getSimpleName();
    private Context context;
    private List<UserInfo> userList;
    private UserInfo userInfo;

    public UserAdapter(Context context, List<UserInfo> userList) {

        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        userInfo = userList.get(position);
        holder.txtUserName.setText(userList.get(position).getName()+"\n"+userList.get(position).getPhone());
        //Log.e(TAG,"city: "+userList.get(position).getAddress().getCity());
        holder.userInfo = userInfo;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtUserName;
        private UserInfo userInfo;


        public ViewHolder(View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.txt_user_name);

            itemView.setOnClickListener(view -> {

                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("name",""+userInfo.getName());
                intent.putExtra("username",""+userInfo.getUsername());
                intent.putExtra("email",""+userInfo.getEmail());
                intent.putExtra("website",""+userInfo.getWebsite());
                intent.putExtra("Address",userInfo.getAddress());
                context.startActivity(intent);
            });
        }
    }
}

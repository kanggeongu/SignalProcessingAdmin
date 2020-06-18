package com.example.signalprocessingadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class reportAdapter extends RecyclerView.Adapter<reportAdapter.CustomViewHolder> {

    private ArrayList<ReportViewData> arrayList;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private int contentNum;

    public reportAdapter(ArrayList<ReportViewData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public reportAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_report, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull reportAdapter.CustomViewHolder holder, int position) {

        ReportViewData reportViewData = arrayList.get(position);

        // 신고당한 사람 이름, 글 내용
        holder.textViewUserEmail.setText(reportViewData.getUserEmail());
        holder.textViewContent.setText(reportViewData.getContent());

        // 제재
        impose(holder, holder.textViewUserEmail.getText().toString());

        // 삭제
        delete(holder, reportViewData.getID());
    }

    // 제재
    private void impose(@NonNull reportAdapter.CustomViewHolder holder, final String userEmail) {
        holder.buttonImpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                databaseReference.child("Restricted").child("Users").child(userEmail.replace('.', '_')).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RestrictedUserData restrictedUserData = dataSnapshot.getValue(RestrictedUserData.class);
                        if (restrictedUserData == null) {
                            restrictedUserData = new RestrictedUserData(userEmail, -1);
                        }

                        CustomDialogImpose customDialogImpose = new CustomDialogImpose(v.getContext(), restrictedUserData.getUserEmail(), restrictedUserData.getEndDate());
                        customDialogImpose.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(v.getContext(), "와이파이 오류", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // 삭제
    private void delete(@NonNull reportAdapter.CustomViewHolder holder, final String report_ID) {
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("신고 내용 삭제")
                        .setMessage("해당 유저가 문제가 없습니까?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String ID = restrictedContent();
                                databaseReference.child("Restricted").child(ID).child(report_ID).removeValue();
                                Toast.makeText(v.getContext(), "삭제하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(), "취소하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    private String restrictedContent() {
        contentNum = ((ReportActivity)ReportActivity.context).ContentNum;

        String ret = "";
        switch (contentNum) {
            case 1:
                ret = "Articles";
                break;
            case 2:
                ret = "Comments";
                break;
            case 3:
                ret = "Cocomments";
                break;
            case 4:
                ret = "NameContestIdeas";
                break;
            case 5:
                ret = "Messages";
                break;
        }

        return ret;
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView textViewUserEmail, textViewContent;
        protected Button buttonImpose, buttonDelete;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewUserEmail = (TextView)itemView.findViewById(R.id.textViewUserEmail);
            this.textViewContent = (TextView)itemView.findViewById(R.id.textViewContent);
            this.buttonImpose = (Button)itemView.findViewById(R.id.buttonImpose);
            this.buttonDelete = (Button)itemView.findViewById(R.id.buttonDelete);
        }
    }
}

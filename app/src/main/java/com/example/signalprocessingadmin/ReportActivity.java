package com.example.signalprocessingadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewReport;
    private Button buttonArticle, buttonComment, buttonCocomment, buttonNameContest, buttonMessage;

    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ReportViewData> arrayList;
    private reportAdapter reportAdapter1;

    public int ContentNum;
    private ProgressDialog progressDialog;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        context = this;
        initPalette();

        ContentNum = 1;
        func();

        buttonArticle.setBackgroundColor(Color.rgb(255,0,0));
        buttonComment.setBackgroundColor(Color.rgb(255,255,0));
        buttonCocomment.setBackgroundColor(Color.rgb(255,255,0));
        buttonNameContest.setBackgroundColor(Color.rgb(255,255,0));
        buttonMessage.setBackgroundColor(Color.rgb(255,255,0));

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                func();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initPalette() {
        buttonArticle = (Button)findViewById(R.id.buttonArticle);
        buttonComment = (Button)findViewById(R.id.buttonComment);
        buttonCocomment = (Button)findViewById(R.id.buttonCocomment);
        buttonNameContest = (Button)findViewById(R.id.buttonNameContest);
        buttonMessage = (Button)findViewById(R.id.buttonMessage);

        recyclerViewReport = (RecyclerView)findViewById(R.id.recyclerViewReport);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewReport.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
    }

    private String restrictedContent() {
        String ret = "";
        switch (ContentNum) {
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

    private void func() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("정보를 불러오는 중입니다");
        progressDialog.show();

        arrayList = new ArrayList<>();
        reportAdapter1 = new reportAdapter(arrayList);
        recyclerViewReport.setAdapter(reportAdapter1);

        String ID = restrictedContent();

        databaseReference.child("Restricted").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReportData reportData = snapshot.getValue(ReportData.class);
                    ReportViewData reportViewData = new ReportViewData(snapshot.getKey(), reportData.getUserEmail(), reportData.getContent());
                    arrayList.add(reportViewData);
                }

                reportAdapter1.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View view) {
        buttonArticle.setBackgroundColor(Color.rgb(255,255,0));
        buttonComment.setBackgroundColor(Color.rgb(255,255,0));
        buttonCocomment.setBackgroundColor(Color.rgb(255,255,0));
        buttonNameContest.setBackgroundColor(Color.rgb(255,255,0));
        buttonMessage.setBackgroundColor(Color.rgb(255,255,0));

        switch (view.getId()) {
            case R.id.buttonArticle:
                buttonArticle.setBackgroundColor(Color.rgb(255,0,0));
                ContentNum = 1;
                break;
            case R.id.buttonComment:
                buttonComment.setBackgroundColor(Color.rgb(255,0,0));
                ContentNum = 2;
                break;
            case R.id.buttonCocomment:
                buttonCocomment.setBackgroundColor(Color.rgb(255,0,0));
                ContentNum = 3;
                break;
            case R.id.buttonNameContest:
                buttonNameContest.setBackgroundColor(Color.rgb(255,0,0));
                ContentNum = 4;
                break;
            case R.id.buttonMessage:
                buttonMessage.setBackgroundColor(Color.rgb(255,0,0));
                ContentNum = 5;
                break;
        }

        func();
    }
}

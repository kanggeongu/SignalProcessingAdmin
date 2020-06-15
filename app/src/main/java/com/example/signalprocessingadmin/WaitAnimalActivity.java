package com.example.signalprocessingadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WaitAnimalActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference mRef=mDatabase.getReference();
    private List<WaitAnimalViewData> items=new ArrayList<WaitAnimalViewData>();
    private AllDataAdapter radapter;

    private RecyclerView rview;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Button buttonRequestedAnimalView, buttonNameContestView;

    private String tag = "namenamename";

    String func_ID = "Waits";
    String univ = "경북대학교";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_animal);

        func_ID = "Waits";
        univ = "경북대학교";

        initPalette();
        buttonRequestedAnimalView.setBackgroundColor(Color.rgb(255, 0,0));
        buttonNameContestView.setBackgroundColor(Color.rgb(255,255,0));

        func();

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
        rview = (RecyclerView)findViewById(R.id.recyclerViewWaitAnimal);
        rview.setLayoutManager(new LinearLayoutManager(this));
        radapter = new AllDataAdapter();
        rview.setAdapter(radapter);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout_wait_animal);

        buttonRequestedAnimalView = (Button)findViewById(R.id.buttonRequestedAnimalView);
        buttonNameContestView = (Button)findViewById(R.id.buttonNameContestView);
    }

    public void onClick(View view) {
        buttonNameContestView.setBackgroundColor(Color.rgb(255,255,0));
        buttonRequestedAnimalView.setBackgroundColor(Color.rgb(255,255,0));

        switch (view.getId()) {
            case R.id.buttonRequestedAnimalView:
                buttonRequestedAnimalView.setBackgroundColor(Color.rgb(255,0,0));
                func_ID = "Waits";
                break;
            case R.id.buttonNameContestView:
                buttonNameContestView.setBackgroundColor(Color.rgb(255,0,0));
                func_ID = "NameContests";
                break;
        }
        func();
    }

    private void func() {
        Log.v("123123", func_ID);
        if (func_ID.equals("Waits")) {
            func0();
        }
        else {
            func1();
        }
    }

    private void func0() {
        Log.v(tag, "0");
        final ProgressDialog pdialog=new ProgressDialog(this);
        pdialog.setTitle("정보를 불러오는 중입니다");
        pdialog.show();

        mRef.child("Waits").child(univ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    WaitItem waitItem = snapshot.getValue(WaitItem.class);
                    if (waitItem.getStatus().equals("심사중")) {
                        WaitAnimalViewData waitAnimalViewData = new WaitAnimalViewData(waitItem.getWriter(), waitItem.getName(), waitItem.getMean(),
                                waitItem.getLocation(), waitItem.getFeature(), waitItem.getGender(), waitItem.getPicture(), waitItem.getStatus(),
                                waitItem.getWriter(), snapshot.getKey());
                        items.add(waitAnimalViewData);
                    }
                }
                Collections.reverse(items);
                radapter.notifyDataSetChanged();
                pdialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void func1() {
        Log.v(tag, "1");
        final ProgressDialog pdialog=new ProgressDialog(this);
        pdialog.setTitle("정보를 불러오는 중입니다");
        pdialog.show();
        items.clear();

        mRef.child("NameContests").child(univ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean f = false;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    final NameContestData nameContestData = snapshot.getValue(NameContestData.class);
                    Log.v(tag, nameContestData.getID());
                    if (nameContestData.getVoters().size() == 0) continue;
                    if (!nameContestData.getStatus().equals("심사중")) continue;

                    String endTime = nameContestData.getEndTime();
                    Long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String curTime = simpleDateFormat.format(mDate);

                    Log.v(tag, endTime + "->" + curTime);
                    if(Long.parseLong(endTime) > Long.parseLong(curTime)) continue;

                    f = true;
                    Log.v(tag, nameContestData.getID());

                    mRef.child("NameContests").child(univ).child(nameContestData.getID()).child("Ideas").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                            int size = -1;
                            String name = "", reason = "";
                            Log.v(tag, "hihi" + dataSnapshot1.getKey());

                            for(DataSnapshot snapshot1:dataSnapshot1.getChildren()) {
                                NameContestIdea nameContestIdea = snapshot1.getValue(NameContestIdea.class);
                                Log.v(tag, "idid" + nameContestIdea.getID());
                                if (size < nameContestIdea.getVoters().size()) {
                                    size = nameContestIdea.getVoters().size();
                                    name = nameContestIdea.getAnimalName();
                                    reason = nameContestIdea.getReason();
                                }
                                Log.v(tag, "이룸 : " + nameContestIdea.getAnimalName());
                                Log.v(tag, "이유 : " + nameContestIdea.getReason());
                            }

                            WaitAnimalViewData waitAnimal = new WaitAnimalViewData(nameContestData.getUserName(), name, reason,
                                    nameContestData.getLocation(), nameContestData.getCharacteristic(),
                                    nameContestData.getGender(), nameContestData.getImage(), "심사중", nameContestData.getUserName(), nameContestData.getID());
                            items.add(waitAnimal);
                            radapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                if (!f) {
                    pdialog.dismiss();
                    radapter.notifyDataSetChanged();
                    Log.v(tag, "false");
                }
                else {
                    Collections.reverse(items);
                    radapter.notifyDataSetChanged();
                    pdialog.dismiss();
                    Log.v(tag, "true");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class AllDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_wait_animal,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final WaitAnimalViewData item=items.get(position);
            final String name=item.getName();

            ((CustomViewHolder)holder).textname.setText(name);
            Glide.with(WaitAnimalActivity.this).load(item.getPicture()).into(((CustomViewHolder) holder).img);
            (((CustomViewHolder)holder).img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(WaitAnimalActivity.this);
                    View newView= LayoutInflater.from(WaitAnimalActivity.this).inflate(R.layout.show_image,null,false);
                    builder.setView(newView);
                    final ImageView imgView=(ImageView)newView.findViewById(R.id.showimage_img);
                    final AlertDialog dialog=builder.create();
                    Glide.with(WaitAnimalActivity.this).load(item.getPicture()).into(imgView);
                    dialog.show();
                }
            });

            permit(holder, item);
            Deny(holder, item);
        }

        private void addAnimalBook(final WaitAnimalViewData waitAnimal) {
            Long now = System.currentTimeMillis();

            AnimalBook animalBook = new AnimalBook(waitAnimal.getName(), waitAnimal.getMean(),
                    waitAnimal.getLocation(), waitAnimal.getGender(), waitAnimal.getPicture(), Long.toString(now));

            mRef.child("AnimalBooks").child(univ).child(animalBook.getAnimalID()).setValue(animalBook);

            Content content = new Content(waitAnimal.getUserID(), waitAnimal.getFeature());
            mRef.child("AnimalBooks").child(univ).child(animalBook.getAnimalID()).child("Contents").child(Long.toString(now)).setValue(content);

            mRef.child(func_ID).child(univ).child(waitAnimal.getNameContestID()).child("status").setValue("심사완료");

            Toast.makeText(WaitAnimalActivity.this, "추가되었습니다", Toast.LENGTH_SHORT).show();
        }

        private void permit(@NonNull RecyclerView.ViewHolder holder, final WaitAnimalViewData waitAnimal) {
            ((CustomViewHolder)holder).buttonAddAnimalBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("동물도감에 추가")
                            .setMessage("동물도감에 추가합니다")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addAnimalBook(waitAnimal);
                                    Toast.makeText(WaitAnimalActivity.this, "동물도감에 추가하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(WaitAnimalActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            });
        }

        private void Deny(@NonNull RecyclerView.ViewHolder holder, final WaitAnimalViewData waitAnimal) {
            ((CustomViewHolder)holder).buttonDeleteNameContestData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("거부함")
                            .setMessage("문제가 있나요?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mRef.child("NameContests").child(univ).child(waitAnimal.getNameContestID()).child("status").setValue("거부");
                                    Toast.makeText(WaitAnimalActivity.this, "거부되었습니다", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(WaitAnimalActivity.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView textname;
            protected ImageView img;
            protected Button buttonAddAnimalBook, buttonDeleteNameContestData;

            public CustomViewHolder(View view) {
                super(view);
                textname=(TextView)view.findViewById(R.id.item_waitanimal_name);
                img=(ImageView)view.findViewById(R.id.item_waitanimal_img);
                buttonAddAnimalBook = (Button)view.findViewById(R.id.buttonAddAnimalBook);
                buttonDeleteNameContestData = (Button)view.findViewById(R.id.buttonDeleteNameContestData);
            }
        }
    }
}

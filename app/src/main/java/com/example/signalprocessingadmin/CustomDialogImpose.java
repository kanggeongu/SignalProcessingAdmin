package com.example.signalprocessingadmin;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class CustomDialogImpose extends Dialog {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private TextView textViewUserEmail, textViewEndDate;
    private Button buttonComplete, buttonCancel;

    private CalendarView calendarView;
    private int Year, Month, DayOfMonth;

    public CustomDialogImpose(@NonNull Context context, String userEmail, int endDate) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custome_dialog_impose);

        Calendar calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH) + 1;
        DayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        initPalette();

        textViewUserEmail.setText(userEmail);

        int date = endDate;
        String text = "";
        if(date == -1) {
            text = "아직 제제받은 적이 없습니다";
        }
        else {
            text = date + "";
        }

        textViewEndDate.setText(text);
    }

    private String int_to_String_day(int m) {
        String ret = "";
        if (m < 10) ret = "0";
        ret += m;
        return ret;
    }

    private void initPalette() {
        calendarView = (CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Year = year;
                Month = month + 1;
                DayOfMonth = dayOfMonth;
            }
        });

        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);
        textViewEndDate = (TextView)findViewById(R.id.textViewEndDate);

        buttonComplete = (Button)findViewById(R.id.buttonComplete);
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endDate = Integer.toString(Year) + int_to_String_day(Month) + int_to_String_day(DayOfMonth);

                RestrictedUserData restrictedUserData = new RestrictedUserData(textViewUserEmail.getText().toString(), Integer.parseInt(endDate));
                databaseReference.child("Restricted").child("Users").child(restrictedUserData.getUserEmail().replace('.', '_')).setValue(restrictedUserData);

                Toast.makeText(v.getContext(),  endDate+ "성공적으로 제재되었습니다", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        buttonCancel = (Button)findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "취소하였습니다", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}

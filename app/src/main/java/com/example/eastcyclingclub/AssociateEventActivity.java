package com.example.eastcyclingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eastcyclingclub.R.id;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Calendar;

public class AssociateEventActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    Spinner eventType;
    ArrayAdapter<CharSequence> eventAdapter;
    ArrayList<CharSequence> eventTypeList;

    Button createEventTypeBTN, returnToEventsBTN;

    TextView date;
    EditText numParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_event);


        date = findViewById(R.id.eventDate);
        numParticipants = findViewById(R.id.numParticipants);
        createEventTypeBTN = findViewById(R.id.createEventTypeButtton);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("events");

        // Dropdown menu for the event type, pulling from firebase depending on which are being offered or not
        eventType = findViewById(R.id.pickEventType);
        eventTypeList = new ArrayList<>();
        eventAdapter  = new ArrayAdapter<CharSequence>(AssociateEventActivity.this, R.layout.spnr_eventtype, eventTypeList);
        eventAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        eventType.setAdapter(eventAdapter);
        showData();

        // Return to events button
        returnToEventsBTN = findViewById(R.id.returnToEventsButton);
        returnToEventsBTN.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ClubEventsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            finish();
        });


        //logic for showing a date picker dialog and selecting date that event will be held
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(AssociateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(intMonthToWord(month)+ " " + String.valueOf(dayOfMonth) + " " + String.valueOf(year));
                    }
                }, year, month, day);

                dialog.show();
            }
        });


        createEventTypeBTN.setOnClickListener(view -> {

            //grab information from user input

            String dateEvent = date.getText().toString();
            String participants = numParticipants.getText().toString();
            String selectedEventType = eventType.getSelectedItem().toString();

            //TODO -> edit the following logic below so that each event created of an event type is assigned to the CYCLING
            //TODO -> CLUB OWNER'S ACCOUNT. Make sure all fields are specific and valid

//            // Checks if at least one option is entered: if so, allows event creation, if not, outputs warning message
//            if ((!minAgeText.isEmpty() || !maxAgeText.isEmpty() || !paceText.isEmpty()) && (!selectedDifficultyLevel.equals("Select Difficulty Level") && !selectedEventType.equals("Select Event Type"))) {
//
//                // Create an instance of the EventCreateHelperClass with event details
//                EventListHelperClass helper = new EventListHelperClass(selectedEventType, selectedDifficultyLevel, minAgeText, paceText, maxAgeText);
//
//                // Store the event information in the Firebase Realtime Database under the "events" node with a unique key
//                reference.child(selectedEventType).setValue(helper);
//                Toast.makeText(CreateEventActivity.this, "Event created successfully!", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
//                finish();
//            }
//            else {
//                AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
//                builder.setTitle("Try again!");
//                builder.setMessage("Please specify event type, difficulty level, and at least one of the fields");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
        });
    }
    private String intMonthToWord(int x) {
        if (x == 0) {
            return "January";
        }
        if (x == 1) {
            return "February";
        }
        if (x == 2) {
            return "March";
        }
        if (x == 3) {
            return "April";
        }
        if (x == 4) {
            return "May";
        }
        if (x == 5) {
            return "June";
        }
        if (x == 6) {
            return "July";
        }
        if (x == 7) {
            return "August";
        }
        if (x == 8) {
            return "September";
        }
        if (x == 9) {
            return "October";
        }
        if (x == 10) {
            return "November";
        }
        if (x == 11) {
            return "December";
        }
        return null;
    }

    private void showData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    EventListHelperClass eventListHelperClass = item.getValue(EventListHelperClass.class);
                    eventTypeList.add(eventListHelperClass.getEventType());
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
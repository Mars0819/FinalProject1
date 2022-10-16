package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static java.util.Calendar.MINUTE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.util.List;

public class MainActivity extends AppCompatActivity  implements  PopupDialogClickListener {

    Button btnSave, btnCancel, btnDelete, btnUpdate;
    EditText edtName;
    TextView tanggal, waktu;
    RecyclerView rvTask;
    DoAdapter doAdapter;
    SQLiteDBHanldler dbHandler;
    List<DoModel> Task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new SQLiteDBHanldler(getApplicationContext());
        Task = dbHandler.getAllTask();

        rvTask = (RecyclerView) findViewById(R.id.rvCountry);
        rvTask.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        doAdapter = new DoAdapter(Task, this);
        rvTask.setAdapter(doAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addmenu) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.dialog_input, null);
            AlertDialog.Builder adBuilder = new AlertDialog.Builder(MainActivity.this);
            adBuilder.setView(popupView);

            btnSave = (Button) popupView.findViewById(R.id.btnSave);
            edtName = (EditText) popupView.findViewById(R.id.edtName);
            tanggal = (TextView) popupView.findViewById(R.id.date);
            waktu = (TextView) popupView.findViewById(R.id.time);
            btnCancel = (Button) popupView.findViewById(R.id.btnCancel);

            setDate();
            setTime();

            AlertDialog dialog = adBuilder.create();
            dialog.setCancelable(true);
            dialog.show();

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtName.getText().toString().length() == 0) {
                        edtName.setError("MasukanTask");
                    } else {
                        DoModel newTask = new DoModel(0, edtName.getText().toString(), tanggal.getText().toString(), waktu.getText().toString());
                        dbHandler.addTask(newTask);
                        dialog.dismiss();
                        Task = dbHandler.getAllTask();
                        doAdapter.updateAndRefreshData(Task);
                        Toast.makeText(MainActivity.this, "Data Berhasil Di Simpan", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        }
        return true;
    }

    @Override
    public void onDelete(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.cancel, null);
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(MainActivity.this);
        adBuilder.setView(popupView);

        btnDelete = (Button) popupView.findViewById(R.id.btnDelete);
        btnCancel = (Button) popupView.findViewById(R.id.btnCancel);
        AlertDialog dialog = adBuilder.create();
        dialog.setCancelable(true);
        dialog.show();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteTask(Task.get(position));
                doAdapter.removeItem(position);
                Toast.makeText(MainActivity.this, "Data telah di hapus", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public void onUpdate(int position) {
        DoModel allTask = Task.get(position);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.update, null);
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(MainActivity.this);
        adBuilder.setView(popupView);

        btnSave = (Button) popupView.findViewById(R.id.btnSave);
        edtName = (EditText) popupView.findViewById(R.id.edtName);
        tanggal = (TextView) popupView.findViewById(R.id.date);
        waktu = (TextView) popupView.findViewById(R.id.time);
        btnCancel = (Button) popupView.findViewById(R.id.btnCancel);

        setDate();
        setTime();
        notification();
        AlertDialog dialog = adBuilder.create();
        dialog.setCancelable(true);
        dialog.show();
        edtName.setText(String.valueOf(allTask.getName()));
        tanggal.setText(String.valueOf(allTask.getDate()));
        waktu.setText(String.valueOf(allTask.getTime()));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().length() == 0) {
                    edtName.setError("MasukanTask");
                } else {
                    DoModel allTask = Task.get(position);
                    DoModel newTask = new DoModel(allTask.getId(), edtName.getText().toString(), tanggal.getText().toString(), waktu.getText().toString());
                    dbHandler.updateTask(newTask);
                    dialog.dismiss();
                    Task = dbHandler.getAllTask();
                    doAdapter.updateAndRefreshData(Task);
                    Toast.makeText(MainActivity.this, "Data Berhasil Di Simpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Reminders";
            String description = "Hey, Wake Up";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("Notify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    private void setDate() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final long date = System.currentTimeMillis();
        SimpleDateFormat dateSdf = new SimpleDateFormat("d MMMM");
        String dateString = dateSdf.format(date);
        tanggal.setText(dateString);

        SimpleDateFormat timeSdf = new SimpleDateFormat("hh : mm a");
        String timeString = timeSdf.format(date);
        waktu.setText(timeString);

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        tanggal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getLayoutInflater().getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String newMonth = getMonth(monthOfYear + 1);
                                tanggal.setText(dayOfMonth + " " + newMonth);
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                if (cal.before(cal)) {
                                    cal.add(Calendar.DATE, 1);
                                }
                                Intent i = new Intent(MainActivity.this, MyBroadcastReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(date);

            }
        });
        ;

    }

    private void setTime() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        final long date = System.currentTimeMillis();
        SimpleDateFormat dateSdf = new SimpleDateFormat("d MMMM");
        String dateString = dateSdf.format(date);
        tanggal.setText(dateString);

        SimpleDateFormat timeSdf = new SimpleDateFormat("hh : mm a");
        String timeString = timeSdf.format(date);
        waktu.setText(timeString);

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        waktu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getLayoutInflater().getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String time;
                                @SuppressLint("DefaultLocale") String minTime = String.format("%02d", minute);
                                if (hourOfDay >= 0 && hourOfDay < 12) {
                                    time = hourOfDay + " : " + minTime + " AM";
                                } else {
                                    if (hourOfDay != 12) {
                                        hourOfDay = hourOfDay - 12;
                                    }
                                    time = hourOfDay + " : " + minTime + " PM";
                                }
                                waktu.setText(time);
                                cal.set(Calendar.HOUR, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                cal.set(Calendar.SECOND, 0);
                                Log.d(TAG, "onTimeSet: Time has been set successfully");

                                if (cal.before(cal)) {
                                    cal.add(Calendar.DATE, 1);
                                }
                                Intent i = new Intent(MainActivity.this, MyBroadcastReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                            }
                        }, cal.get(Calendar.HOUR), cal.get(MINUTE), false);
                timePickerDialog.show();

            }
        });
    }
}
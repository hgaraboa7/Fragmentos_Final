package com.example.fragmentos_1.vista.actividades;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fragmentos_1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SeleccionFechaActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView txtFechaSeleccionada, txtHoraSeleccionada;
    private Button btnSeleccionarHora, btnAsignarFechaHora;

    private Calendar fechaHoraSeleccionada = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_fecha);

        calendarView = findViewById(R.id.calendarView);
        txtFechaSeleccionada = findViewById(R.id.txtFechaSeleccionada);
        txtHoraSeleccionada = findViewById(R.id.txtHoraSeleccionada);
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora);
        btnAsignarFechaHora = findViewById(R.id.btnAsignarFechaHora);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                fechaHoraSeleccionada.set(year, month, dayOfMonth);
                txtFechaSeleccionada.setText("Fecha: " + dateFormat.format(fechaHoraSeleccionada.getTime()));
            }
        });

        btnSeleccionarHora.setOnClickListener(v -> {
            int hour = fechaHoraSeleccionada.get(Calendar.HOUR_OF_DAY);
            int minute = fechaHoraSeleccionada.get(Calendar.MINUTE);
            new TimePickerDialog(SeleccionFechaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    fechaHoraSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    fechaHoraSeleccionada.set(Calendar.MINUTE, minute);
                    txtHoraSeleccionada.setText("Hora: " + timeFormat.format(fechaHoraSeleccionada.getTime()));
                }
            }, hour, minute, true).show();
        });

        btnAsignarFechaHora.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("fechaHoraMilis", fechaHoraSeleccionada.getTimeInMillis());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}

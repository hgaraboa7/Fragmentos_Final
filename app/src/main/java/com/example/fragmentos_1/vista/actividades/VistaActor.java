package com.example.fragmentos_1.vista.actividades;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fragmentos_1.R;
import com.example.fragmentos_1.room.AppDatabase;
import com.example.fragmentos_1.room.ActorEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VistaActor extends AppCompatActivity {

    private ImageView imgActor;
    private EditText edtNombre, edtFechaNacimiento;
    private Button btnEditar, btnGuardar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance();

    private AppDatabase db;
    private ExecutorService executorService;
    private int actorId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actor);

        imgActor = findViewById(R.id.imgDetalleActor);
        edtNombre = findViewById(R.id.edtNombreActor);
        edtFechaNacimiento = findViewById(R.id.edtFechaNacimiento);
        btnEditar = findViewById(R.id.btnEditar);
        btnGuardar = findViewById(R.id.btnGuardar);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        cargarDatosActor(actorId);

        byte[] imagenBytes = getIntent().getByteArrayExtra("imagenActor");
        if (imagenBytes != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length, options);

            if (bitmap != null) {
                imgActor.setImageBitmap(bitmap);
            } else {
                imgActor.setVisibility(View.GONE);
            }
        } else {
            imgActor.setVisibility(View.GONE);
        }

        setFieldsEnabled(false);

        btnEditar.setOnClickListener(v -> setFieldsEnabled(true));

        btnGuardar.setOnClickListener(v -> {
            guardarActor(actorId, edtNombre.getText().toString(), edtFechaNacimiento.getText().toString());
            setFieldsEnabled(false);
            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
        });

        edtFechaNacimiento.setOnClickListener(v -> mostrarDatePicker());
    }

    private void setFieldsEnabled(boolean enabled) {
        edtNombre.setEnabled(enabled);
        edtFechaNacimiento.setEnabled(enabled);
        btnGuardar.setEnabled(enabled);
        btnEditar.setEnabled(!enabled);
    }

    private void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            edtFechaNacimiento.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void cargarDatosActor(int id) {
        executorService.execute(() -> {
            ActorEntity actor = db.actorDao().obtenerActor(id);
            if (actor != null) {
                runOnUiThread(() -> {
                    edtNombre.setText(actor.nombre);
                    edtFechaNacimiento.setText(actor.biografia);
                });
            }
        });
    }

    private void guardarActor(int id, String nombre, String biografia) {
        executorService.execute(() -> {
            ActorEntity actor = new ActorEntity();
            actor.id = id;
            actor.nombre = nombre;
            actor.biografia = biografia;

            db.actorDao().insertarActor(actor);
        });
    }
}

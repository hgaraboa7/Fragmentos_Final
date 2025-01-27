package com.example.fragmentos_1.vista.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import com.example.fragmentos_1.R;
import com.example.fragmentos_1.modelo.entidades.Actor;
import com.example.fragmentos_1.vista.actividades.VistaActor;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ActorAdapter extends ArrayAdapter<Actor> {

    public ActorAdapter(@NonNull Context context, @NonNull List<Actor> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.actor_element, parent, false);
        }

        Actor actor = getItem(position);

        if (actor != null) {
            TextView txtNombre = convertView.findViewById(R.id.txtNombreActor);
            TextView txtFechaNacimiento = convertView.findViewById(R.id.txtFechaNacimientoActor);

            txtNombre.setText(actor.getNombre());
            txtFechaNacimiento.setText(actor.getFechaNacimiento().toString());

            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), VistaActor.class);
                intent.putExtra("actor", actor);

                if (actor.getImagenBitmap() != null) {
                    byte[] imagenBytes = convertirBitmapAByteArray(actor.getImagenBitmap());
                    intent.putExtra("imagenActor", imagenBytes);
                }

                getContext().startActivity(intent);
            });
        }

        return convertView;
    }

    private byte[] convertirBitmapAByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }
}

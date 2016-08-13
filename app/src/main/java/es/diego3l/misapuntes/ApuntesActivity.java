package es.diego3l.misapuntes;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ApuntesActivity extends AppCompatActivity {
    private ListView listView;
    private ApuntesDBAdapter mDbAdapter;
    private ApuntesSimpleCursorAdapter mCursorAdapter;
    private int contador = 0;
    private TextView textoContador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apuntes);

        listView = (ListView) findViewById(R.id.apuntes_list_view);
        findViewById(R.id.apuntes_list_view);
        listView.setDivider(null);
        mDbAdapter = new ApuntesDBAdapter(this);
        mDbAdapter.open();

        if (savedInstanceState == null) {
            //limpiar todos los datos
            mDbAdapter.deleteAllReminders();
            //Add algunos datos
            mDbAdapter.createReminder("Visitar el Centro de recogida", true);
            contador++;
            mDbAdapter.createReminder("Comprobar el Correo", false);
            contador++;
            mDbAdapter.createReminder("Hacer la compra semanal", false);
            contador++;
        }

        //El arrayAdapter es ahora nuestro controlador
        //Modelo Vista Controlador (Model, View, Controller)
        // El Adapter es el controller
        // La Vista está claro lo que es
        // Y el array es nuestro Modelo, lo que sería la BBDD SQlite
        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.apuntes_row,
                R.id.row_text, new String[]{"Primer Apunte", "Segundo Apunte", "Tercer Apunte", "Cuarto Apunte",
                "Quinto Apunte", "Sexto Apunte", "Septimo Apunte", "Octavo Apunte en el cual vamos a explicar"});*/

        Cursor cursor = mDbAdapter.fetchAllReminders();

        //desde las columnas definidas en la base de datos
        String[] from = new String[]{
                ApuntesDBAdapter.COL_CONTENT
        };

        //a la id de views en el layout
        int[] to = new int[]{
                R.id.row_text
        };

        mCursorAdapter = new ApuntesSimpleCursorAdapter(
                //context
                ApuntesActivity.this,
                //el layout de la fila
                R.layout.apuntes_row,
                //cursor
                cursor,
                //desde columnas definidas en la base de datos
                from,
                //a las ids de views en el layout
                to,
                //flag ~ no usado
                0);

        //el cursorAdapter (controller) está ahora actualizando la listView (vista)
        //con datos desde la base de datos (modelo)
        textoContador = (TextView) findViewById(R.id.text_contador);

        if (contador == 0){
            textoContador.setText("Contador Apuntes: " + contador + ". Nada pendiente.");
        } else if (contador == 1){
            textoContador.setText("Contador Apuntes: " + contador + " mensaje pendiente.");
        } else {
            textoContador.setText("Contador Apuntes: " + contador + " mensajes pendientes.");
        }

        listView.setAdapter(mCursorAdapter);

        //cuando pulsamos en un item individual en la listview
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ApuntesActivity.this, "pulsado " + position,
                        Toast.LENGTH_SHORT).show();
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApuntesActivity.this);
                ListView modeListView = new ListView(ApuntesActivity.this);
                String[] modes = new String[] { "Editar Aviso", "Borrar Aviso"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(ApuntesActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();

                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //editar aviso
                        if (position == 0) {
                            Toast.makeText(ApuntesActivity.this, "editar "
                                + masterListPosition, Toast.LENGTH_SHORT).show();
                            //borrar aviso
                        } else {
                            Toast.makeText(ApuntesActivity.this, "borrar "
                                + masterListPosition, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apuntes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_nuevo:
                //crear nuevo aviso
                Log.d(getLocalClassName(), "Crear Nuevo Apunte");
                Toast.makeText( getApplicationContext(), "Has pulsado en Nuevo Apunte", Toast.LENGTH_SHORT ).show();
                return true;
            case R.id.action_salir:
                finish();
                return true;
            default:
                return false;
        }
    }
}

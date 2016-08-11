package es.diego3l.misapuntes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ApuntesActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apuntes);

        listView = (ListView) findViewById(R.id.apuntes_list_view);
        //El arrayAdapter es ahora nuestro controlador
        //Modelo Vista Controlador (Model, View, Controller)
        // El Adapter es el controller
        // La Vista está claro lo que es
        // Y el array es nuestro Modelo, lo que sería la BBDD SQlite
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.apuntes_row,
                R.id.row_text, new String[]{"Primer Apunte", "Segundo Apunte", "Tercer Apunte", "Cuarto Apunte",
                "Quinto Apunte", "Sexto Apunte", "Septimo Apunte", "Octavo Apunte en el cual vamos a explicar"});

        listView.setAdapter(arrayAdapter);
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

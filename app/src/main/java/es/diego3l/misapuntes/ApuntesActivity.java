package es.diego3l.misapuntes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
                R.id.row_text, new String[]{"Primer Apunte", "Segundo Apunte", "Tercer Apunte", "Cuarto Apunte"});

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

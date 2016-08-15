package es.diego3l.misapuntes;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ApuntesActivity extends AppCompatActivity {
    private ListView listView;
    private ApuntesDBAdapter mDbAdapter;
    private ApuntesSimpleCursorAdapter mCursorAdapter;
    private int contador = 0;
    private TextView textoContador;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apuntes);
        //Incluimos icono en la barra de acción
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        listView = (ListView) findViewById(R.id.apuntes_list_view);
        findViewById(R.id.apuntes_list_view);
        listView.setDivider(null);
        mDbAdapter = new ApuntesDBAdapter(this);
        mDbAdapter.open();

        /*if (savedInstanceState == null) {
            //limpiar todos los datos
            mDbAdapter.deleteAllReminders();
            //Add algunos datos
            mDbAdapter.createReminder("Visitar el Centro de recogida", true);
            contador++;
            mDbAdapter.createReminder("Comprobar el Correo", false);
            contador++;
            mDbAdapter.createReminder("Hacer la compra semanal", false);
            contador++;
        }*/

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
                //String[] modes = new String[] { "Editar Aviso", "Borrar Aviso", "comodín"};
                String[] modes = new String[] { "Editar Aviso", "Borrar Aviso"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(ApuntesActivity.this,
                        android.R.layout.simple_list_item_2, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();

                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //editar aviso
                        if (position == 0) {
                            /*Toast.makeText(ApuntesActivity.this, "editar "
                                + masterListPosition, Toast.LENGTH_SHORT).show();*/
                            int nId = getIdFromPosition(masterListPosition);
                            Apunte apunte = mDbAdapter.fetchReminderById(nId);
                            fireCustomDialog(apunte);
                            //borrar aviso
                        } else {
                            /*Toast.makeText(ApuntesActivity.this, "borrar "
                                + masterListPosition, Toast.LENGTH_SHORT).show();*/
                            mDbAdapter.deleteReminder(getIdFromPosition(masterListPosition));
                            //contador--;
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                        }
                        dialog.dismiss();
                    }
                });

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { //honeycomb tiene un valor de 11, por la api
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_apunte:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (listView.isItemChecked(nC)) {
                                    mDbAdapter.deleteReminder(getIdFromPosition(nC));
                                    //contador--;
                                }
                            }
                            mode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return true;
                    }

                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
        }

        /*if (contador == 0){
            textoContador.setText("Contador Apuntes: " + contador + ". Nada pendiente.");
        } else if (contador == 1){
            textoContador.setText("Contador Apuntes: " + contador + " mensaje pendiente.");
        } else {
            textoContador.setText("Contador Apuntes: " + contador + " mensajes pendientes.");
        }*/
    }

    private int getIdFromPosition(int nC) {
        return (int) mCursorAdapter.getItemId(nC);
    }

    public void contadorArriba(){
        contador++;
    }
    public void contadorAbajo(){
        contador--;
    }
    public void contadorACero(){
        contador = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apuntes, menu);
        return true;
    }

    //Nuestro método particular
    private void fireCustomDialog(final Apunte apunte) {
        //custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);

        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (apunte != null);

        //esto es para un edit
        if (isEditOperation) {
            titleView.setText("Editar Aviso");
            checkBox.setChecked(apunte.getImportant() == 1);
            editCustom.setText(apunte.getContent());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.azul_neutro));
        }

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reminderText = editCustom.getText().toString();
                if (isEditOperation) {
                    Apunte reminderEdited = new Apunte(apunte.getId(),
                            reminderText, checkBox.isChecked() ? 1 : 0);
                    mDbAdapter.updateReminder(reminderEdited);
                    //esto es para nuevo aviso
                } else {
                    //contador++;

                    mDbAdapter.createReminder(reminderText, checkBox.isChecked());
                }
                mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                dialog.dismiss();
            }
        });

        Button buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_nuevo:
                //crear nuevo aviso
                /*Log.d(getLocalClassName(), "Crear Nuevo Apunte");
                Toast.makeText( getApplicationContext(), "Has pulsado en Nuevo Apunte", Toast.LENGTH_SHORT ).show();*/
                fireCustomDialog(null);
                return true;
            case R.id.action_salir:
                finish();
                return true;
            default:
                return false;
        }
    }
}

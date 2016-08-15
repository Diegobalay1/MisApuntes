package es.diego3l.misapuntes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dlolh on 13/08/2016.
 */
public class ApuntesSimpleCursorAdapter extends SimpleCursorAdapter {

    public ApuntesSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    //Para usar ViewHolder, debemos sobrescribir los siguientes 2 métodos pertenecientes a esa Clase


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.colImp = cursor.getColumnIndexOrThrow(ApuntesDBAdapter.COL_IMPORTANT);
            holder.listTab = view.findViewById(R.id.row_tab);
            holder.listRow = view.findViewById(R.id.row_text);
            view.setTag(holder);
        }
        if (cursor.getInt(holder.colImp) > 0) {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.naranja));
        } else {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.rosa));
        }
        /*if (cursor.getInt(holder.colImp) > 0) {
            holder.listRow.setBackgroundColor(context.getResources().getColor(R.color.naranja_claro));
        } else {
            holder.listRow.setBackgroundColor(context.getResources().getColor(R.color.blanco_roto));
        }*/

    }

    //clase interna estática
    static class ViewHolder {
        //almacenar por la id de la columna
        int colImp;
        //almacenar por la vista
        View listTab;
        View listRow;
    }
}

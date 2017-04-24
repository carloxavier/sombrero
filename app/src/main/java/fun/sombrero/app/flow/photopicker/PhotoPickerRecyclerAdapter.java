package fun.sombrero.app.flow.photopicker;


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fun.sombrero.app.R;

public class PhotoPickerRecyclerAdapter extends RecyclerView.Adapter<PhotoPickerRecyclerAdapter.ViewHolder> {

    CursorAdapter cursorAdapter;
    Context context;
    LayoutInflater inflater;
    SimpleCursorAdapter.ViewBinder viewBinder;
    String[] originalFrom;
    int[] from;
    int[] to;


    public PhotoPickerRecyclerAdapter(Context context, Cursor cursor, String[] from, int[] to) {
        this.context = context;
        this.originalFrom = from;
        this.to = to;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.cursorAdapter = createCursorAdapter(context, cursor);
        findColumns(cursor, this.originalFrom);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursorAdapter.getCursor().moveToPosition(position);
        cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }

    public void setViewBinder(SimpleCursorAdapter.ViewBinder viewBinder) {
        this.viewBinder = viewBinder;
    }

    @NonNull
    private CursorAdapter createCursorAdapter(final Context context, final Cursor cursor) {
        return new CursorAdapter(context, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return inflater.inflate(R.layout.list_photo_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                final SimpleCursorAdapter.ViewBinder binder = PhotoPickerRecyclerAdapter.this.viewBinder;
                final int count = PhotoPickerRecyclerAdapter.this.to.length;
                final int[] from = PhotoPickerRecyclerAdapter.this.from;
                final int[] to = PhotoPickerRecyclerAdapter.this.to;

                for (int i = 0; i < count; i++) {
                    final View v = view.findViewById(to[i]);
                    if (v != null) {
                        if (binder != null) {
                            binder.setViewValue(v, cursor, from[i]);
                        }
                    }
                }
            }
        };
    }

    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (this.from == null || this.from.length != count) {
                this.from = new int[count];
            }
            for (i = 0; i < count; i++) {
                this.from[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            this.from = null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_item);
        }
    }
}

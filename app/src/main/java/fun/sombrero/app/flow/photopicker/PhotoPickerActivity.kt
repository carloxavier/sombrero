package `fun`.sombrero.app.flow.photopicker

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.widget.CursorAdapter
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class PhotoPickerActivity :
        AppCompatActivity(),
        LoaderManager.LoaderCallbacks<Cursor>,
        PhotoPickerContract.View {

    val projection = arrayOf(MediaStore.Files.FileColumns._ID, MediaStore.Images.Media.DISPLAY_NAME)
    val fromColumns = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
    val toFields = intArrayOf(R.id.text1)

    var presenter = PhotoPickerPresenter()

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
//        data?.moveToFirst()
//        while (data?.moveToNext()!!) {
//            Log.d("PhotoPickerActivity",data.getString(0))
//        }

        val cursorAdapter = SimpleCursorAdapter(this, R.layout.simple_list_item_1, cursor,
                                                fromColumns, toFields, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        photoList.adapter = cursorAdapter
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        return CursorLoader(this, baseUri, projection, null, null, MediaStore.Images.ImageColumns._ID + " DESC")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(`fun`.sombrero.app.R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener(View.OnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        })

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else {

            supportLoaderManager.initLoader(0, null, this)
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0) {
            supportLoaderManager.initLoader(0, null, this)
        }
    }

}

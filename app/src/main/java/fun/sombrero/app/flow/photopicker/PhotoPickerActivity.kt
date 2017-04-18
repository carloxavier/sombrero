package `fun`.sombrero.app.flow.photopicker

import `fun`.sombrero.app.R
import `fun`.sombrero.app.R.layout.activity_image_grid
import `fun`.sombrero.app.R.layout.list_photo_item
import `fun`.sombrero.app.domain.LocalImageStoreLoader
import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.CursorAdapter
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_grid.*
import kotlinx.android.synthetic.main.list_photo_item.view.*


class PhotoPickerActivity :
        AppCompatActivity(),
        PhotoPickerContract.View {

    companion object {
        val PERMISSION_REQUEST_CODE = 0
    }

    val fromColumns = arrayOf(MediaStore.Images.Media.DATA)
    val toFields = intArrayOf(R.id.image_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_image_grid)
        setSupportActionBar(toolbar_grid)

        setupPhotoGallery()
    }

    private fun setupPhotoGallery() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        else
            initPhotoLoader()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE)
            initPhotoLoader()
    }

    private fun initPhotoLoader() {
        LocalImageStoreLoader(this, supportLoaderManager) {
            data -> onLoadFinished(data)
        }.execute()
    }

    private fun onLoadFinished(dataCursor: Cursor?) {
        SimpleCursorAdapter(
                    this,
                    list_photo_item,
                    dataCursor,
                    fromColumns,
                    toFields,
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
                .let {

                    it.setViewBinder {
                            view, cursor, columnIndex -> bindView(view, cursor, columnIndex) }
                    gridView.adapter = it
                }
    }

    private fun bindView(view: View, cursor: Cursor, currentColumnIndex: Int): Boolean {
        var idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        if (currentColumnIndex === idColumnIndex) {
            var imagePath = "file://"+cursor.getString(currentColumnIndex)

            if (view.image_item != null) {
                Picasso.with(this).load(imagePath).fit().centerCrop().into(view.image_item)
            }

            return true
        }
        return false
    }
}

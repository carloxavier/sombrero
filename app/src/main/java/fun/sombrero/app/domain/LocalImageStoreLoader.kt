package `fun`.sombrero.app.domain

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader

class LocalImageStoreLoader(val context: Context, val loaderManager: LoaderManager, val onFinished: (Cursor?) -> Unit)
        : LoaderManager.LoaderCallbacks<Cursor> {

    val projection = arrayOf(MediaStore.Images.Media._ID,
                             MediaStore.Images.Media.DISPLAY_NAME,
                             MediaStore.Images.Media.DATA)

    override fun onLoaderReset(loader: Loader<Cursor>?) {}

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        return CursorLoader(context, baseUri, projection, null, null, MediaStore.Images.ImageColumns._ID + " DESC")
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
        onFinished(cursor)
    }

    fun execute() {
        loaderManager.initLoader(0, null, this)
    }
}

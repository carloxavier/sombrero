package `fun`.sombrero.app.flow.photopicker

import android.support.v4.app.LoaderManager

object PhotoPickerContract {

    interface View

    interface Presenter {
        fun loadPhotos(loaderManager: LoaderManager)
    }
}

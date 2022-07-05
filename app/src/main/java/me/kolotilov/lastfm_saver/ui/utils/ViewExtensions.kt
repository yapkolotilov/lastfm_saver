package me.kolotilov.lastfm_saver.ui.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.textfield.TextInputLayout
import me.kolotilov.lastfm_saver.models.AlbumImage
import me.kolotilov.lastfm_saver.ui.common.recycler.EmptyPagingException
import java.io.File

/**
 * Text.
 */
var TextInputLayout.text: String
    get() = editText?.text?.toString().orEmpty()
    set(value) {
        editText?.setText(value)
    }

/**
 * Sets on enter click listener.
 *
 * @param listener Listener.
 */
fun TextInputLayout.setOnEnterClickListener(listener: () -> Unit) {
    editText?.setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            listener()
            true
        } else
            false
    }
}

/**
 * Loads image considering it's source.
 *
 * @param image Image.
 */
fun RequestManager.loadImage(image: AlbumImage?): RequestBuilder<Drawable> {
    return when {
        image?.filePath != null -> load(File(image.filePath))
        image?.uri != null      -> load(image.uri.toUri())
        else                    -> load(null as Uri?)
    }
}

/**
 * Clears focus and closes keyboard.
 */
fun View.clearFocusAndCloseKeyboard() {
    clearFocus()
    val imm: InputMethodManager = ContextCompat.getSystemService(
        context,
        InputMethodManager::class.java
    )!!
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Tree-like operator for viewBinding.
 *
 * @param body Body.
 */
inline operator fun <VB : ViewBinding> VB.invoke(body: VB.() -> Unit) {
    body()
}

fun CombinedLoadStates.switchViews(
    recycler: RecyclerView,
    placeholder: View? = null,
    loader: View? = null,
    error: View? = null
) {
    recycler.isVisible = false
    placeholder?.isVisible = false
    loader?.isVisible = false
    error?.isVisible = false

    val refresh = refresh
    val showLoader = refresh is LoadState.Loading
    val showPlaceholder = refresh is LoadState.Error && refresh.error is EmptyPagingException
    val showError = !showPlaceholder && refresh is LoadState.Error
    when {
        showLoader && loader != null -> loader.isVisible = true
        showPlaceholder && placeholder != null -> placeholder.isVisible = true
        showError && error != null -> error.isVisible = true
        else -> recycler.isVisible = true
    }
}

fun Number.dp(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun shimmerDrawable(): ShimmerDrawable {
    val shimmer = Shimmer.ColorHighlightBuilder()
        .build()
    return ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
}
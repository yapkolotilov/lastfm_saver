package me.kolotilov.lastfm_saver.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.withStyledAttributes
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.databinding.ViewPlaceholderBinding
import me.kolotilov.lastfm_saver.ui.utils.invoke

/**
 * Placeholder View.
 */
class PlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ViewPlaceholderBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL

        context.withStyledAttributes(attrs, R.styleable.PlaceholderView) {
            icon = getResourceId(R.styleable.PlaceholderView_icon, -1).takeIf { it != -1 }
            title = getString(R.styleable.PlaceholderView_text).orEmpty()
        }
    }

    /**
     * Icon.
     */
    @DrawableRes
    var icon: Int? = null
        set(value) {
            field = value
            binding {
                if (value != null)
                    iconImage.setImageResource(value)
                else
                    iconImage.setImageIcon(null)
            }
        }

    /**
     * Title text.
     */
    var title: String = ""
        set(value) {
            field = value
            binding {
                titleText.text = value
            }
        }

}
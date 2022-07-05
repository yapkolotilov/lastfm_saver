package me.kolotilov.lastfm_saver.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

/**
 * Adapter delegate.
 */
interface AdapterDelegate<T : Any> {

    /**
     * Base ViewHolder.
     */
    abstract class ViewHolder<T : Any>(itemView: View): RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: T, position: Int)
    }

    fun matches(item: T): Boolean

    fun createViewHolder(parent: ViewGroup): ViewHolder<T>
}

private class AdapterDelegateImpl<T : Any, I : T, VB : ViewBinding>(
    private val itemClass: KClass<I>,
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val initView: VB.(ItemDelegate<I>) -> Unit,
    private val bind: VB.(item: T) -> Unit
) : AdapterDelegate<T> {

    inner class ViewHolder(
        private val binding: VB
    ) : AdapterDelegate.ViewHolder<T>(binding.root) {

        private inner class ItemDelegateImpl : ItemDelegate<I> {

            override lateinit var item: I
            override var position: Int = -1

            override fun invalidate() {
                bind(item, position)
            }
        }

        private val _itemDelegate = ItemDelegateImpl()

        init {
            @Suppress("UNCHECKED_CAST")
            binding.initView(_itemDelegate)
        }

        @Suppress("UNCHECKED_CAST")
        override fun bind(item: T, position: Int) {
            _itemDelegate.item = item as I
            _itemDelegate.position = position
            binding.bind(item)
        }
    }

    override fun matches(item: T): Boolean {
        return itemClass.isInstance(item)
    }

    override fun createViewHolder(parent: ViewGroup): AdapterDelegate.ViewHolder<T> {
        val binding = inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}

interface ItemDelegate<T : Any> {

    val item: T
    val position: Int

    fun invalidate()
}

@Suppress("UNCHECKED_CAST")
fun <T : Any, I : T, VB : ViewBinding> adapterDelegate(
    itemClass: KClass<I>,
    inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    initView: VB.(ItemDelegate<I>) -> Unit,
    bind: VB.(item: I) -> Unit
): AdapterDelegate<T> {
    return AdapterDelegateImpl(
        itemClass = itemClass,
        inflate = inflate,
        initView = initView,
        bind = { bind(it as I) }
    )
}

inline fun <T : Any, reified I : T, VB : ViewBinding> adapterDelegate(
    noinline inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    noinline initView: VB.(ItemDelegate<I>) -> Unit = {},
    noinline bind: VB.(item: I) -> Unit = {}
): AdapterDelegate<T> {
    return adapterDelegate(
        itemClass = I::class,
        inflate,
        initView,
        bind
    )
}
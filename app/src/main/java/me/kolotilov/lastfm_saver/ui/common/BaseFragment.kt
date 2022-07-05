package me.kolotilov.lastfm_saver.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.presentation.common.BaseViewModel
import org.koin.core.component.KoinComponent

/**
 * Base Fragment class.
 *
 * @param layout Layout.
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), KoinComponent {

    /**
     * View Scope.
     */
    protected val viewScope get() = viewLifecycleOwner.lifecycleScope

    /**
     * ViewBinding.
     */
    protected lateinit var viewBinding: VB
        private set

    /**
     * ViewModel.
     */
    protected abstract val viewModel: BaseViewModel

    /**
     * Toolbar ID.
     */
    @IdRes
    protected open val toolbarId: Int? = null

    protected abstract val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readArgs()
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = inflate(inflater, container, false)
        return viewBinding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
        interact()
        viewModel.onAttached()
    }

    /**
     * Read arguments here.
     */
    protected open fun readArgs() {}

    @CallSuper
    protected open fun initViews() {
        if (toolbarId != null) {
            requireActivity().let { it as AppCompatActivity }.setSupportActionBar(
                requireView().findViewById(R.id.toolbar)
            )
        }
    }

    /**
     * Pass UI events to viewModel.
     */
    protected open fun interact() = Unit

    /**
     * Subscribe to viewModel events.
     */
    @CallSuper
    protected open fun subscribe() {
        viewModel.message.subscribe {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Subscribes to ViewModel events.
     */
    protected fun <T> Flow<T>.subscribe(body: suspend (T) -> Unit) {
        viewScope.launch {
            collect(body)
        }
    }
}
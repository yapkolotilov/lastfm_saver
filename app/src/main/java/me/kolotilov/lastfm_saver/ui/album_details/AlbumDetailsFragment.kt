package me.kolotilov.lastfm_saver.ui.album_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.databinding.FragmentAlbumDetailsBinding
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.presentation.album_details.AlbumDetailsViewModel
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.ui.common.BaseFragment
import me.kolotilov.lastfm_saver.ui.common.DividerItemDecoration
import me.kolotilov.lastfm_saver.ui.common.recycler.DelegateAdapter
import me.kolotilov.lastfm_saver.ui.utils.invoke
import me.kolotilov.lastfm_saver.ui.utils.loadImage
import me.kolotilov.lastfm_saver.ui.utils.shimmerDrawable
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Album details screen.
 */
class AlbumDetailsFragment : BaseFragment<FragmentAlbumDetailsBinding>() {

    override val toolbarId: Int = R.id.toolbar

    override val viewModel: AlbumDetailsViewModel by viewModel()
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAlbumDetailsBinding
        get() = FragmentAlbumDetailsBinding::inflate

    private lateinit var adapter: DelegateAdapter<ListItem>

    override fun readArgs() {
        super.readArgs()
        val args: AlbumDetailsFragmentArgs by navArgs()
        val id = AlbumId(args.artist, args.album)
        viewModel.init(id)
    }

    override fun initViews() {
        super.initViews()
        viewBinding {
            recycler.addItemDecoration(DividerItemDecoration(requireContext()))

            adapter = albumDetailsAdapter()
            recycler.adapter = adapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subscribe() {
        super.subscribe()
        viewBinding {
            val shimmers = listOf(
                buttonShimmer, nameShimmer, playCountShimmer
            )

            viewModel.data.subscribe { data ->
                errorContainer.isVisible = false
                successContainer.isVisible = false
                when (data) {
                    is AlbumDetailsData.Loading -> {
                        successContainer.isVisible = true
                        toolbar.title = getString(R.string.title_album_details)

                        shimmers.forEach {
                            it.showShimmer(true)
                        }
                    }
                    is AlbumDetailsData.Success -> {
                        shimmers.forEach {
                            it.hideShimmer()
                        }
                        successContainer.isVisible = true
                        toolbar.title = data.name

                        Glide.with(requireContext())
                            .loadImage(data.image)
                            .placeholder(shimmerDrawable())
                            .error(R.drawable.ic_error)
                            .into(albumImage)

                        artistNameText.text = data.artist
                        playCountText.text = data.playCount
                    }
                    else -> {
                        errorContainer.isVisible = true
                    }
                }
            }

            viewModel.saveButtonText.subscribe { text ->
                saveButton.text = text
            }

            viewModel.tracks.subscribe { tracks ->
                adapter.items = tracks
                adapter.notifyDataSetChanged()
            }

            viewModel.loading.subscribe {
                errorContainer.isRefreshing = false
            }
        }
    }

    override fun interact() {
        super.interact()
        viewBinding {
            saveButton.setOnClickListener {
                viewModel.onSaveClicked()
            }
            errorContainer.setOnRefreshListener {
                viewModel.refresh()
            }
        }
    }
}
package com.github.andreyasadchy.xtra.ui.videos

import android.text.format.DateUtils
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.andreyasadchy.xtra.R
import com.github.andreyasadchy.xtra.model.helix.video.Video
import com.github.andreyasadchy.xtra.ui.common.OnChannelSelectedListener
import com.github.andreyasadchy.xtra.util.*
import kotlinx.android.synthetic.main.fragment_videos_list_item.view.*

class VideosAdapter(
        private val fragment: Fragment,
        private val clickListener: BaseVideosFragment.OnVideoSelectedListener,
        private val channelClickListener: OnChannelSelectedListener,
        private val showDownloadDialog: (Video) -> Unit) : BaseVideosAdapter(
        object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                    oldItem.view_count == newItem.view_count &&
                            oldItem.thumbnail_url == newItem.thumbnail_url &&
                            oldItem.title == newItem.title &&
                            oldItem.duration == newItem.duration
        }) {

    override val layoutId: Int = R.layout.fragment_videos_list_item

    override fun bind(item: Video, view: View) {
        val channelListener: (View) -> Unit = { channelClickListener.viewChannel(item.user_id, item.user_login, item.user_name, item.channelLogo) }
        with(view) {
            val position = positions?.get(item.id.toLong())
            setOnClickListener { clickListener.startVideo(item, position?.toDouble()) }
            setOnLongClickListener { showDownloadDialog(item); true }
            thumbnail.loadImage(fragment, item.thumbnail, diskCacheStrategy = DiskCacheStrategy.NONE)
            date.text = item.createdAt?.let { TwitchApiHelper.formatTime(context, it) }
            views.text = item.view_count?.let { TwitchApiHelper.formatViewsCount(context, it, context.prefs().getBoolean(C.UI_VIEWCOUNT, false)) }
            duration.text = item.duration?.let { DateUtils.formatElapsedTime(TwitchApiHelper.getDuration(it)) }
            type.text = TwitchApiHelper.getType(context, item.videoType)
            position.let {
                if (it != null && item.duration != null) {
                    progressBar.progress = (it / (TwitchApiHelper.getDuration(item.duration) * 10L)).toInt()
                    progressBar.visible()
                } else {
                    progressBar.gone()
                }
            }
            userImage.apply {
                setOnClickListener(channelListener)
                loadImage(fragment, item.channelLogo, circle = true)
            }
            username.apply {
                setOnClickListener(channelListener)
                text = item.user_name
            }
            title.text = item.title
            gameName.text = item.game
            options.setOnClickListener {
                PopupMenu(context, it).apply {
                    inflate(R.menu.media_item)
                    setOnMenuItemClickListener { showDownloadDialog(item); true }
                    show()
                }
            }
        }
    }
}
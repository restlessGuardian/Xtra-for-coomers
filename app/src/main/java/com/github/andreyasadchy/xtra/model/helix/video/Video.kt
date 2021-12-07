package com.github.andreyasadchy.xtra.model.helix.video

import android.os.Parcelable
import com.github.andreyasadchy.xtra.model.offline.Downloadable
import com.github.andreyasadchy.xtra.util.TwitchApiHelper
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    override val id: String,
    val stream_id: String = "",
    val user_id: String,
    val user_login: String,
    val user_name: String,
    override val title: String,
    val description: String? = "",
    @SerializedName("created_at")
        val createdAt: String,
    @SerializedName("published_at")
        val publishedAt: String = "",
    val thumbnail_url: String,
    val viewable: String = "",
    val view_count: Int,
    val language: String = "",
    val type: String = "",
    val duration: String,
    @SerializedName("muted_segments")
        val mutedSegments: List<MutedSegment>? = null,
    val game_name: String = "",
    var profileImageURL: String = "") : Parcelable, Downloadable {

    @Parcelize
    data class MutedSegment(
            val duration: Int,
            val offset: Int) : Parcelable

        override val thumbnail: String
            get() = TwitchApiHelper.getTemplateUrl(thumbnail_url, "video")
        override val channelName: String
                get() = user_name
        override val channelLogo: String
                get() = TwitchApiHelper.getTemplateUrl(profileImageURL, "profileimage")
        override val game: String
                get() = game_name
        override val uploadDate: String
                get() = createdAt
        override val videoType: String
            get() = type
}
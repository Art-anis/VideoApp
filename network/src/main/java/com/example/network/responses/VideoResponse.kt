package com.example.network.responses

import com.google.gson.annotations.SerializedName

//response для списка
data class VideoResponse(
    @SerializedName("id") var id: String? = null,
    @SerializedName("duration") var duration: String? = null,
    @SerializedName("projectId") var projectId: String? = null,
    @SerializedName("accountKey") var accountKey: String? = null,
    @SerializedName("region") var region: String? = null,
    @SerializedName("captions") var captions: ArrayList<String> = arrayListOf(),
    @SerializedName("key") var key: String? = null,
    @SerializedName("channelKey") var channelKey: String? = null,
    @SerializedName("privateLink") var privateLink: String? = null,
    @SerializedName("hlsLink") var hlsLink: String? = null,
    @SerializedName("planType") var planType: Int? = null,
    @SerializedName("mp4Url") var mp4Url: String? = null,
    @SerializedName("mp4Urls") var mp4Urls: ArrayList<String> = arrayListOf(),
    @SerializedName("formats") var formats: Formats? = Formats(),
    @SerializedName("hlsUrl") var hlsUrl: String? = null,
    @SerializedName("hlsUrlWeb") var hlsUrlWeb: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("options") var options: Options? = Options(),
    @SerializedName("tags") var tags: ArrayList<String> = arrayListOf(),
    @SerializedName("version") var version: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created") var created: String? = null
)

//не используется
data class Formats(
    @SerializedName("hls") var hls: Boolean? = null,
    @SerializedName("mp4") var mp4: Boolean? = null
)

//не используется
data class Options(
    @SerializedName("autoplay") var autoplay: Boolean? = null,
    @SerializedName("playerColor") var playerColor: String? = null,
    @SerializedName("playerSkin") var playerSkin: String? = null,
    @SerializedName("controlsColor") var controlsColor: String? = null,
    @SerializedName("seekButtons") var seekButtons: Boolean? = null,
    @SerializedName("volumeControl") var volumeControl: Boolean? = null,
    @SerializedName("preload") var preload: String? = null,
    @SerializedName("fullscreenControl") var fullscreenControl: Boolean? = null,
    @SerializedName("controls") var controls: Boolean? = null,
    @SerializedName("stickyControls") var stickyControls: Boolean? = null,
    @SerializedName("defaultQuality") var defaultQuality: String? = null,
    @SerializedName("qualityControl") var qualityControl: Boolean? = null,
    @SerializedName("speedControl") var speedControl: Boolean? = null,
    @SerializedName("fastForward") var fastForward: Boolean? = null,
    @SerializedName("bigPlayControl") var bigPlayControl: Boolean? = null,
    @SerializedName("playControl") var playControl: Boolean? = null,
    @SerializedName("volume") var volume: Int? = null,
    @SerializedName("loop") var loop: Boolean? = null,
    @SerializedName("muted") var muted: Boolean? = null,
    @SerializedName("modal") var modal: Boolean? = null
)

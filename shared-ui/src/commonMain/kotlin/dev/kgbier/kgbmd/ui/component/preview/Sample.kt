package dev.kgbier.kgbmd.ui.component.preview

import dev.kgbier.kgbmd.domain.model.CastCredit
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.Image
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.NameDetails
import dev.kgbier.kgbmd.domain.model.NameProfile
import dev.kgbier.kgbmd.domain.model.TitleDetails

val sampleImage = Image("")
val sampleMoviePoster = MoviePoster(MediaEntityId(""), "Title", null, sampleImage)
val sampleNameProfile = NameProfile(MediaEntityId(""), "Name", null)
val sampleCastCredit = CastCredit(
    nameProfile = sampleNameProfile,
    roles = listOf("Main Actor")
)

val sampleRatingsBreakdown = TitleDetails.RatingsBreakdown(
    listOf(
        TitleDetails.RatingsBreakdown.RatingValue("1", 1000),
        TitleDetails.RatingsBreakdown.RatingValue("2", 2000),
        TitleDetails.RatingsBreakdown.RatingValue("3", 3000),
        TitleDetails.RatingsBreakdown.RatingValue("4", 4000),
        TitleDetails.RatingsBreakdown.RatingValue("5", 5000),
        TitleDetails.RatingsBreakdown.RatingValue("6", 6000),
        TitleDetails.RatingsBreakdown.RatingValue("7", 7000),
        TitleDetails.RatingsBreakdown.RatingValue("8", 6000),
        TitleDetails.RatingsBreakdown.RatingValue("9", 5000),
        TitleDetails.RatingsBreakdown.RatingValue("10", 4000),
    )
)

val sampleTopCast = TitleDetails.TopCast(
    castTotal = 200,
    topCast = listOf(
        sampleCastCredit,
        sampleCastCredit,
        sampleCastCredit,
    ),
    hasMore = true,
    groupingId = CreditGroupingId(""),
)

val sampleTitleDetails = TitleDetails(
    id = MediaEntityId(""),
    name = "Title",
    poster = sampleImage,
    contentRating = "M",
    genre = "Genre",
    principalCreditsByGroup = mapOf(
        "Director" to listOf(sampleNameProfile),
        "Writers" to listOf(sampleNameProfile, sampleNameProfile),
        "Stars" to listOf(sampleNameProfile, sampleNameProfile, sampleNameProfile),
    ),
    description = "Some description",
    yearReleased = "2020",
    rating = TitleDetails.Rating("7.5", "10", "123,400"),
    ratingsBreakdown = sampleRatingsBreakdown,
    duration = "1h 30m",
    episodeMetadata = null,
    genres = listOf("Horror", "Comedy"),
    topEpisodes = null,
    topCast = sampleTopCast,
    watchOption = TitleDetails.WatchOption.Cinema,
//    watchOption = TitleDetails.WatchOption.Watchable(
//        title = "Netflix",
//        description = "S1-8",
//        image = sampleImage,
//    ),
)

val sampleNameDetails = NameDetails(
    id = MediaEntityId(""),
    name = "Firstname Lastname",
    headshot = sampleImage,
    description = "Biography here",
    topCredits = listOf(
        NameDetails.TopCredit(poster = sampleMoviePoster, role = "Role"),
        NameDetails.TopCredit(poster = sampleMoviePoster, role = "Role"),
        NameDetails.TopCredit(poster = sampleMoviePoster, role = "Role"),
    ),
)

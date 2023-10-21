package no.hiof.friluftslivcompanionapp.models

import com.google.android.gms.maps.model.LatLng

data class DummyTrip(
    val city: String,
    val county: String,
    val type: String,
    val description: String,
    val duration: String,
    val difficulty: Int,
    val distance: String,
    val nodes: List<LatLng>
) {
    companion object {
        fun getDummyData(): List<DummyTrip> {
            return listOf(
                DummyTrip(
                    "Oslo", "Viken", "Hike",
                    "A hike around Sognsvann",
                    "1.5 hours", 2, "3.3 km",
                    listOf(
                        LatLng(59.97139, 10.66824),
                        LatLng(59.97412, 10.67958),
                        LatLng(59.97054, 10.68596),
                        LatLng(59.96544, 10.67981),
                        LatLng(59.96512, 10.67053),
                        LatLng(59.97007, 10.66217)
                    )
                ),
                DummyTrip(
                    "Bergen", "Vestland", "Climb",
                    "Climbing the famous Ulriken",
                    "5 hours", 4, "7 km",
                    listOf(
                        LatLng(60.3715, 5.3546),
                        LatLng(60.3810, 5.3625)
                    )
                ),
                DummyTrip(
                    "Tromsø", "Troms og Finnmark", "Nordic Skiing",
                    "Experience the Arctic landscape",
                    "6 hours", 4, "15 km",
                    listOf(
                        LatLng(69.6325, 18.9560),
                        LatLng(69.6625, 19.0260)
                    )
                ),
                DummyTrip(
                    "Trondheim", "Trøndelag", "Hike",
                    "Hiking in Bymarka", "2 hours", 2, "5 km",
                    listOf(
                        LatLng(63.4095, 10.3682),
                        LatLng(63.4195, 10.3782),
                        LatLng(63.4295, 10.3882)
                    )
                ),
                DummyTrip(
                    "Stavanger", "Rogaland", "Climb",
                    "Climbing the Pulpit Rock",
                    "4 hours", 3, "8 km",
                    listOf(
                        LatLng(58.9865, 6.1881),
                        LatLng(58.9867, 6.1887)
                    )
                )
            )
        }
    }
}



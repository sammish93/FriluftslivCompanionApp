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
                DummyTrip("Oslo", "Viken", "Hike", "A challenging mountain hike", "3 hours", 5, "10 km", listOf(LatLng(59.9139, 10.7522))),
                DummyTrip("Bergen", "Vestland", "Climb", "Climbing the famous Ulriken", "5 hours", 4, "7 km", listOf(LatLng(60.3913, 5.3221))),
                DummyTrip("Tromsø", "Troms og Finnmark", "Nordic Skiing", "Experience the Arctic landscape", "6 hours", 4, "15 km", listOf(LatLng(69.6496, 18.9560))),
                DummyTrip("Trondheim", "Trøndelag", "Hike", "Hiking in Bymarka", "2 hours", 2, "5 km", listOf(LatLng(63.4305, 10.3951))),
                DummyTrip("Stavanger", "Rogaland", "Climb", "Climbing the Pulpit Rock", "4 hours", 3, "8 km", listOf(LatLng(58.969976, 5.733107)))
            )
        }
    }
}



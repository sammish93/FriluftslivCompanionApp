package no.hiof.friluftslivcompanionapp.models

import com.google.firebase.firestore.DocumentId


// Should strongly consider making this class a singleton.
// A new user might not have a lifelist or trip activity, thus '?'.
data class User(
    // Here the variables are going to be based on what Firebase Auth allow (e.g. username vs email,
    // or just settling fo a user's first name). Additionally, how does Firebase handle a GUID/UUID?
    @DocumentId val userId : String = "",
    var username : String = "",
    var email: String? = null,
    var preferences: UserPreferences = UserPreferences(),
    var lifelist: Lifelist? = null,
    var tripActivity: TripActivity? = null,
    var yearlyTripCount: Int = 0,
    var yearlySpeciesCount: Int = 0
)

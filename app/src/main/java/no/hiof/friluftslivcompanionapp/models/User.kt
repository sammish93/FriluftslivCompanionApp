package no.hiof.friluftslivcompanionapp.models

// Should strongly consider making this class a singleton.
// A new user might not have a lifelist or trip activity, thus '?'.
data class User(
    // Here the variables are going to be based on what Firebase Auth allow (e.g. username vs email,
    // or just settling fo a user's first name). Additionally, how does Firebase handle a GUID/UUID?
    var preferences: UserPreferences,
    var lifelist: Lifelist?,
    var tripActivity: TripActivity?
)

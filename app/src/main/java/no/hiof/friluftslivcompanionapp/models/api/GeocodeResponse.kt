package no.hiof.friluftslivcompanionapp.models.api

data class GeocodeResponse(
    val results: List<Result>
){
    data class Result(
        val address_components: List<AddressComponent>
    )
    data class AddressComponent(
        val short_name: String, val types: List<String>
    )

}

package no.hiof.friluftslivcompanionapp.viewmodel

import no.hiof.friluftslivcompanionapp.domain.LocationFormatter
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import org.junit.Assert
import org.junit.Test

class FloraFaunaViewModelTestClass {
    @Test
    fun testSearchBirdsByYourLocation() {
        val viewModel = FloraFaunaViewModel()
        val testLocations = listOf("Oslo", "Viken", "Innlandet", "Trøndelag")
        for (location in testLocations) {
            val result = LocationFormatter.getRegionCodeByLocation(location)
            val regionCode = result.first
            val message = result.second

            when (location) {
                "Oslo" -> {
                    Assert.assertEquals("NO-03", regionCode)
                    Assert.assertEquals("Success", message)
                }
                "Viken" -> {
                    Assert.assertEquals("NO-01,NO-02,NO-06", regionCode)
                    Assert.assertEquals("Success", message)
                    }
                "Innlandet" -> {
                    Assert.assertEquals("NO-04,NO-05", regionCode)
                    Assert.assertEquals("Success", message)
                    }
                "Trøndelag" -> {
                    Assert.assertEquals("NO-16,NO-17", regionCode)
                    Assert.assertEquals("Success", message)
                }

            }
        }
    }
}

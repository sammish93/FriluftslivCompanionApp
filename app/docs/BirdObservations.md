# BirdObservations Class

The `BirdObservations` class acts as a domain layer in the application architecture, 
serving as an intermediary between the data layer (`EBirdApi`) and the presentation layer. 
It is designed to fetch and process bird observation data.

## Methods

### getRecentObservationsInOslo(language: SupportedLanguage): List<Bird>?

This method fetches recent bird observations in Oslo in the specified language.

#### Parameters:
- `language`: The language in which the bird observations should be fetched.

#### Returns:
- A list of `Bird` objects representing the recent bird observations in Oslo, or `null` if the fetch fails.

#### Usage:
```kotlin
val birdObservations = BirdObservations()
val birdsInEnglish = birdObservations.getRecentObservationsInOslo(SupportedLanguage.EN)
```

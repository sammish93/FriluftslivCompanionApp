package no.hiof.friluftslivcompanionapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import no.hiof.friluftslivcompanionapp.data.states.UserState
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
/*
class MockUserViewModel : UserViewModel() {
    private val _state = MutableStateFlow(UserState(lastKnownLocation = null))
    override fun updateLanguage(language: SupportedLanguage) {
        _state.update { currentState ->
            currentState.copy(
                language = language
            )
        }
    }

    override fun getLanguage(): SupportedLanguage {
        return  _state.value.language
    }
}*/
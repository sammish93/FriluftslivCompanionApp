package no.hiof.friluftslivcompanionapp.data.repositories

sealed class OperationResult< out T> {
    data class Success< out T>(val data: T) : OperationResult<T>()
    class Error(val exception: Exception) : OperationResult<Nothing>()
}



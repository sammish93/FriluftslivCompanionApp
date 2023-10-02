package no.hiof.friluftslivcompanionapp.data.network

/**
 * A sealed class `Result` represents a generic wrapper around the result of a network operation.
 * It can have two states: `Success` and `Failure`.
 *
 * - `Success` represents the successful completion of the operation and holds the resulting value.
 * - `Failure` represents the failure of the operation and holds an error message.
 *
 * @param T The type of the value that is wrapped by a `Success` instance.
 */
sealed class Result<out T> {
    data class Success<out T>(val value: T): Result<T>()
    data class Failure(val message: String?): Result<Nothing>()
}

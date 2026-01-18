package dev.kgbier.kgbmd.util

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

/**
 *  Remember to [cooperativelyCancel] any [runCatching] results of suspending code.
 *
 *  Kotlin Coroutines' concept of Structured Concurrency depends on faithful propagation of [CancellationException]
 *  at all levels of execution.
 *  Running suspending code within a [runCatching] block risks interrupting ordinary cancellation and cleanup
 *  of closed coroutine scopes.
 *  Not doing so may result in wasteful use of resources and unnecessary noise.
 *
 *  Inspired by: https://betterprogramming.pub/the-silent-killer-thats-crashing-your-coroutines-9171d1e8f79b
 */
suspend inline fun <T> Result<T>.cooperativelyCancel(): Result<T> = onFailure {

    // Check to see if this job should still be running (if we received a Cancellation, is it ours?)
    // If this job is no longer running, rethrow and propagate Cancellation.
    currentCoroutineContext().ensureActive()

    // This job is still running, it has not been cancelled yet.

    // If a Cancellation was caught, is out-of-band, and should probably be handled as an ordinary error.
    // Emitting CancellationException (including TimeoutCancellationException)
    // to external coroutines is uncooperative and should be avoided.
}
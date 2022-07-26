package me.kolotilov.lastfm_saver.domain

import kotlinx.coroutines.*

fun cancelableActionRunner(delay: Long, onComplete: suspend () -> Unit = {}): CancelableAction.Runner {

    return object : CancelableAction.Runner {

        private var action: CancelableAction = CancelableAction.empty()
        private var job: Job? = null

        override suspend fun submit(action: CancelableAction) {
            this.action += action
            job?.cancel()
            action.perform()

            CoroutineScope(currentCoroutineContext()).launch {
                job = launch {
                    submit()
                }
            }
        }

        override suspend fun cancel() {
            action.cancel()
            job?.cancel()
            action = CancelableAction.empty()
        }

        private suspend fun submit() {
            delay(delay)
            onComplete()
            action = CancelableAction.empty()
        }
    }
}
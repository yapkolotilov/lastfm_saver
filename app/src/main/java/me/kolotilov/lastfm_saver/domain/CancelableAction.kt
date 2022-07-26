package me.kolotilov.lastfm_saver.domain

/**
 * Cancelable Action.
 */
interface CancelableAction {

    interface Runner {

        suspend fun submit(action: CancelableAction)

        suspend fun cancel()
    }

    companion object {

        fun empty(): CancelableAction {
            return object : CancelableAction {
                override suspend fun perform() = Unit
                override suspend fun cancel() = Unit
            }
        }
    }

    /**
     * Perform action.
     */
    suspend fun perform()

    /**
     * Cancel action.
     */
    suspend fun cancel()
}

/**
 * Combines two actions.
 */
operator fun CancelableAction.plus(other: CancelableAction): CancelableAction {
    return CompositeAction(this, other)
}

private class CompositeAction(
    private val firstAction: CancelableAction,
    private val secondAction: CancelableAction
) : CancelableAction {

    override suspend  fun perform() {
        firstAction.perform()
        secondAction.perform()
    }

    override suspend  fun cancel() {
        firstAction.cancel()
        secondAction.cancel()
    }
}
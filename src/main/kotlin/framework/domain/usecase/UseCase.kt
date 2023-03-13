package framework.domain.usecase

import framework.domain.model.EntityResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext

/**
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author: kaylen.pillay
 **/

abstract class UseCase<REQ, RES>(
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun execute(request: REQ? = null): EntityResult<RES> = withContext(dispatcher) {
        try {
            onExecuteUseCase(request)
        } catch (exception: Exception) {
            if (exception !is TimeoutCancellationException && exception is CancellationException) throw exception
            onExecutionException(data = null, exception = exception)
        }
    }

    protected abstract suspend fun onExecuteUseCase(request: REQ?): EntityResult<RES>

    protected abstract fun onExecutionException(data: RES?, exception: Exception?): EntityResult<RES>
}
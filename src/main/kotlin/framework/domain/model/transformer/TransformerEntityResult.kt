package framework.domain.model.transformer

import framework.api.model.DTOResult
import framework.domain.model.EntityResult

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

object TransformerEntityResult {

    fun <D, E> DTOResult<D>.toEntityResult(
        onTransformDTO: (D?) -> E
    ): EntityResult<E> {
        return when(this) {
            is DTOResult.TransactionComplete -> EntityResult.Successful(data = onTransformDTO.invoke(data))
            is DTOResult.TransactionFailed -> EntityResult.Failure(
                errorData = onTransformDTO.invoke(errorData),
                exception = exception
            )
        }
    }

}
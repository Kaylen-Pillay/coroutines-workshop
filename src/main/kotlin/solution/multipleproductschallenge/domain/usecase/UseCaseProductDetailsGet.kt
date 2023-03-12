package solution.multipleproductschallenge.domain.usecase

import common.api.repository.RepositoryProductList
import common.domain.model.response.EntityResponseProductDetailsGet
import common.domain.model.response.transformer.TransformerEntityResponseProductDetailsGet.toEntityResponseProductDetailsGet
import common.utils.Plid
import framework.domain.model.EntityResult
import framework.domain.model.transformer.TransformerEntityResult.toEntityResult
import framework.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher

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

class UseCaseProductDetailsGet(
    private val repository: RepositoryProductList,
    dispatcher: CoroutineDispatcher
) : UseCase<Plid, EntityResponseProductDetailsGet>(dispatcher) {

    override suspend fun onExecuteUseCase(request: Plid?): EntityResult<EntityResponseProductDetailsGet> {
        if (request == null) throw IllegalArgumentException("Please provide a plid to fetch product details")

        val result = repository.getProductDetails(request).toEntityResult { response ->
            response.toEntityResponseProductDetailsGet()
        }

        return result
    }

    override fun onExecutionException(
        data: EntityResponseProductDetailsGet?,
        exception: Exception?
    ): EntityResult<EntityResponseProductDetailsGet> {
        return EntityResult.Failure(
            errorData = data ?: EntityResponseProductDetailsGet(),
            exception = exception
        )
    }
}
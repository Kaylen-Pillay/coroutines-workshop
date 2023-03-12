package solution.multipleproductschallenge.domain.interactor

import common.domain.model.request.EntityRequestProductListGet
import common.domain.model.response.EntityCombinedResponseProductListGet
import common.domain.model.response.EntityResponseProductListGet
import framework.domain.interactor.Interactor
import framework.domain.model.EntityResult
import kotlinx.coroutines.*
import solution.multipleproductschallenge.domain.usecase.UseCaseProductDetailsGet
import solution.multipleproductschallenge.domain.usecase.UseCaseProductListGet

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

class InteractorProductListGet(
    private val useCaseProductListGet: UseCaseProductListGet,
    private val useCaseProductDetailsGet: UseCaseProductDetailsGet,
    dispatcher: CoroutineDispatcher
) : Interactor<EntityRequestProductListGet, EntityCombinedResponseProductListGet>(dispatcher) {

    override suspend fun onExecuteInteractor(
        request: EntityRequestProductListGet?
    ): EntityResult<EntityCombinedResponseProductListGet> {
        if (request == null) throw IllegalArgumentException("Request required to fetch product list")

        val productListResult = useCaseProductListGet.execute(request)
        return processProductListResult(productListResult)
    }

    override fun onExecutionException(
        data: EntityCombinedResponseProductListGet?,
        exception: Exception?
    ): EntityResult<EntityCombinedResponseProductListGet> {
        return EntityResult.Failure(
            errorData = data ?: EntityCombinedResponseProductListGet(),
            exception = exception
        )
    }

    private suspend fun processProductListResult(
        result: EntityResult<EntityResponseProductListGet>
    ): EntityResult<EntityCombinedResponseProductListGet> {
        return when (result) {
            is EntityResult.Successful -> performProductDetailGet(result.data)
            is EntityResult.Failure -> {
                EntityResult.Failure(
                    errorData = EntityCombinedResponseProductListGet(),
                    exception = result.exception
                )
            }
        }
    }

    private suspend fun performProductDetailGet(
        productListResponse: EntityResponseProductListGet
    ): EntityResult<EntityCombinedResponseProductListGet> = withContext(currentCoroutineContext()) {
        val productDetailItems = productListResponse.plids.map { plid ->
            async { useCaseProductDetailsGet.execute(plid) }
        }.awaitAll()

        EntityResult.Successful(
            data = EntityCombinedResponseProductListGet(
                title = productListResponse.title,
                items = productDetailItems
            )
        )
    }
}
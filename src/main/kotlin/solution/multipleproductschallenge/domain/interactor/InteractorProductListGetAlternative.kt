package solution.multipleproductschallenge.domain.interactor

import common.domain.model.request.EntityRequestProductListGet
import common.domain.model.response.EntityCombinedResponseProductListGet
import common.domain.model.response.EntityResponseProductDetailsGet
import common.domain.model.response.EntityResponseProductListGet
import framework.domain.model.EntityResult
import kotlinx.coroutines.*
import solution.multipleproductschallenge.domain.usecase.UseCaseProductDetailsGet
import solution.multipleproductschallenge.domain.usecase.UseCaseProductListGet
import java.util.*

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

class InteractorProductListGetAlternative(
    private val useCaseProductListGet: UseCaseProductListGet,
    private val useCaseProductDetailsGet: UseCaseProductDetailsGet
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    private val emptyFailedResponse = EntityCombinedResponseProductListGet()

    private val productDetailsResultList = Collections.synchronizedList(
        mutableListOf<EntityResult<EntityResponseProductDetailsGet>>()
    )

    fun execute(
        request: EntityRequestProductListGet,
        onComplete: (EntityResult<EntityCombinedResponseProductListGet>) -> Unit
    ) {
        val onCoroutineExceptionHandle = CoroutineExceptionHandler { _, error ->
            onComplete.invoke(EntityResult.Failure(emptyFailedResponse, error as? Exception))
        }
        coroutineScope.launch(onCoroutineExceptionHandle) {
            val productListResult = useCaseProductListGet.execute(request)
            processProductListResult(productListResult, onComplete)
        }
    }

    private suspend fun processProductListResult(
        result: EntityResult<EntityResponseProductListGet>,
        onProductListResult: (EntityResult<EntityCombinedResponseProductListGet>) -> Unit
    ) {
        when (result) {
            is EntityResult.Successful -> performProductDetailGet(result.data, onProductListResult)
            is EntityResult.Failure -> {
                onProductListResult(
                    EntityResult.Failure(
                        errorData = EntityCombinedResponseProductListGet(),
                        exception = result.exception
                    )
                )
            }
        }
    }

    private suspend fun performProductDetailGet(
        productListResponse: EntityResponseProductListGet,
        onProductListResult: (EntityResult<EntityCombinedResponseProductListGet>) -> Unit
    ) {
        val jobs = productListResponse.plids.map { plid ->
            val exceptionHandler = CoroutineExceptionHandler { _, error ->
                productDetailsResultList.add(
                    EntityResult.Failure(
                        EntityResponseProductDetailsGet(),
                        error as Exception
                    )
                )
            }
            coroutineScope.launch(exceptionHandler) {
                val result = useCaseProductDetailsGet.execute(plid)
                productDetailsResultList.add(result)
            }
        }.toTypedArray()
        joinAll(*jobs)
        val result = EntityResult.Successful(
            data = EntityCombinedResponseProductListGet(
                title = productListResponse.title,
                items = productDetailsResultList
            ).copy()
        )
        onProductListResult.invoke(result)
    }

}

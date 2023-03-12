package solution.multipleproductschallenge.domain.usecase

import common.api.repository.RepositoryProductList
import common.domain.model.request.EntityRequestProductListGet
import common.domain.model.request.transformer.TransformerEntityRequestProductListGet.toDTORequestProductListGet
import common.domain.model.response.EntityResponseProductListGet
import common.domain.model.response.transformer.TransformerEntityResponseProductListGet.toEntityResponseProductListGet
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

class UseCaseProductListGet(
    private val repository: RepositoryProductList,
    dispatcher: CoroutineDispatcher
) : UseCase<EntityRequestProductListGet, EntityResponseProductListGet>(dispatcher) {

    override suspend fun onExecuteUseCase(
        request: EntityRequestProductListGet?
    ): EntityResult<EntityResponseProductListGet> {
        val result = repository.getProductList(request.toDTORequestProductListGet()).toEntityResult { response ->
            response.toEntityResponseProductListGet()
        }

        return result
    }

    override fun onExecutionException(
        data: EntityResponseProductListGet?,
        exception: Exception?
    ): EntityResult<EntityResponseProductListGet> {

        return EntityResult.Failure(
            errorData = data ?: EntityResponseProductListGet(),
            exception = exception
        )
    }
}
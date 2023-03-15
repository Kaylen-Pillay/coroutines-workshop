package solution.multipleproductschallenge.domain.databridge

import common.api.repository.RepositoryProductList
import common.domain.databridge.DataBridgeMultipleProductsChallenge
import common.domain.model.request.EntityRequestProductListGet
import common.domain.model.response.EntityCombinedResponseProductListGet
import framework.domain.databridge.DataBridge
import framework.domain.model.EntityResult
import kotlinx.coroutines.Dispatchers
import solution.multipleproductschallenge.domain.interactor.InteractorProductListGetAlternative
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

class DataBridgeMultipleProductsChallengeSolutionAlternativeImpl(
    repository: RepositoryProductList
) : DataBridgeMultipleProductsChallenge, DataBridge() {

    private val useCaseProductListGet = UseCaseProductListGet(
        repository = repository,
        dispatcher = Dispatchers.IO
    )

    private val useCaseProductDetailsGet = UseCaseProductDetailsGet(
        repository = repository,
        dispatcher = Dispatchers.IO
    )

    private val interactorProductListGet = InteractorProductListGetAlternative(
        useCaseProductListGet = useCaseProductListGet,
        useCaseProductDetailsGet = useCaseProductDetailsGet
    )

    override fun getProductList(
        request: EntityRequestProductListGet,
        onComplete: (EntityResult<EntityCombinedResponseProductListGet>) -> Unit
    ) {
        interactorProductListGet.execute(request, onComplete)
    }

}
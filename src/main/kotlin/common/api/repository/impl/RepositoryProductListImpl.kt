package common.api.repository.impl

import common.api.model.request.DTORequestProductListGet
import common.api.model.response.DTOResponseProductDetailGet
import common.api.model.response.DTOResponseProductListGet
import common.api.repository.RepositoryProductList
import common.utils.Plid
import framework.api.model.DTOResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

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

private const val NUMBER_OF_PRODUCTS_PER_LIST = 10
private const val TIME_TAKEN_FOR_GET_PRODUCT_LIST = 2000L
private const val TIME_TAKEN_FOR_GET_PRODUCT_DETAIL = 1000L
private const val TIMEOUT_FOR_GET_PRODUCT_DETAIL = 500L
private const val SHOULD_ENABLE_TIMEOUT_FOR_PRODUCT_DETAIL = false
private const val SHOULD_FAIL_GET_PRODUCT_LIST = false

class RepositoryProductListImpl : RepositoryProductList {

    override suspend fun getProductList(request: DTORequestProductListGet): DTOResult<DTOResponseProductListGet> {
        delay(TIME_TAKEN_FOR_GET_PRODUCT_LIST)

        if (SHOULD_FAIL_GET_PRODUCT_LIST) throw Exception("RepositoryProductListImpl - getProductList could not be retrieved.")

        val data = DTOResponseProductListGet(
            title = if (request.customerName == null) "Recommended Products" else "Products recommend for ${request.customerName}",
            plids = List(NUMBER_OF_PRODUCTS_PER_LIST) {
                Plid("PLID000$it")
            }
        )

        return DTOResult.TransactionComplete(data = data)
    }

    override suspend fun getProductDetails(plid: Plid): DTOResult<DTOResponseProductDetailGet> {
        return if (SHOULD_ENABLE_TIMEOUT_FOR_PRODUCT_DETAIL) {
            withTimeout(TIMEOUT_FOR_GET_PRODUCT_DETAIL) {
                val randomDelay = (100..TIME_TAKEN_FOR_GET_PRODUCT_DETAIL).random()
                performGetProductDetails(delayTime = randomDelay, plid = plid)
            }
        } else {
            performGetProductDetails(delayTime = TIME_TAKEN_FOR_GET_PRODUCT_DETAIL, plid = plid)
        }
    }

    private suspend fun performGetProductDetails(delayTime: Long, plid: Plid): DTOResult<DTOResponseProductDetailGet> {
        delay(delayTime)
        val data = DTOResponseProductDetailGet(
            title = "Product Title for [${plid.value}]",
            subtitle = "Product Subtitle for [${plid.value}]",
            image = "Product Image URL for [${plid.value}]",
            price = "Product price for [${plid.value}]",
            stockCount = (1..1000).random()
        )

        return DTOResult.TransactionComplete(data = data)
    }

}
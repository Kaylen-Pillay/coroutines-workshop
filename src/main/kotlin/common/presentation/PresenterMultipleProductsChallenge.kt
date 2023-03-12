package common.presentation

import common.domain.databridge.DataBridgeMultipleProductsChallenge
import common.domain.model.request.EntityRequestProductListGet
import framework.domain.model.EntityResult
import framework.log.WorkshopLogger.logObjectInfo

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

class PresenterMultipleProductsChallenge(
    private val dataBridge: DataBridgeMultipleProductsChallenge
) {

    interface Callback {
        fun onStart()
        fun onEnd()
    }

    fun onInit(callback: Callback) {
        callback.onStart()

        val request = EntityRequestProductListGet(
            customerName = "Kaylen",
            customerId = "1234"
        )
        dataBridge.getProductList(request) { result ->
            when (result) {
                is EntityResult.Successful -> logObjectInfo(result.data)
                is EntityResult.Failure -> logObjectInfo(result.errorData)
            }
            callback.onEnd()
        }
    }

}
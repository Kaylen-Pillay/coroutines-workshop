package common.presentation

import com.andreapivetta.kolor.*
import common.domain.databridge.DataBridgeMultipleProductsChallenge
import common.domain.model.request.EntityRequestProductListGet
import common.domain.model.response.EntityCombinedResponseProductListGet
import common.domain.model.response.EntityResponseProductDetailsGet
import framework.domain.model.EntityResult
import framework.log.WorkshopLogger
import framework.log.WorkshopLogger.logException
import framework.log.WorkshopLogger.logInfo
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

        val startInstant = logInfo(message = "PresenterMultipleProductsChallenge - [Start] getProductList")
        dataBridge.getProductList(request) { result ->
            logInfo(
                message = "PresenterMultipleProductsChallenge - [End] getProductList",
                comparedInstant = startInstant,
                comparedMessage = "Time since [Start] getProductList"
            )
            when (result) {
                is EntityResult.Successful -> handleOnGetProductListSuccessful(result)
                is EntityResult.Failure -> handleOnGetProductListFailure(result)
            }
            callback.onEnd()
        }
    }

    private fun handleOnGetProductListSuccessful(
        result: EntityResult.Successful<EntityCombinedResponseProductListGet>
    ) {
        logInfo(message = "Get Product details has completed successfully")
        logProductList(result.data)
        logObjectInfo(
            any = result.data.items.filterIsInstance<EntityResult.Successful<EntityResponseProductDetailsGet>>().map {
                it.data.title
            },
            message = "Product List Item Titles"
        )
    }

    private fun handleOnGetProductListFailure(
        result: EntityResult.Failure<EntityCombinedResponseProductListGet>
    ) {
        logInfo(message = "Get Product details has failed")
        result.exception?.logException()
    }

    private fun logProductList(responseProductListGet: EntityCombinedResponseProductListGet) {
        val messagePrefix = Kolor.foreground("Product List Result", Color.BLUE)
        val messageSuffix = Kolor.foreground("END;", Color.BLUE)
        val displayMessage = """
            ${WorkshopLogger.getCurrentTimeStamp()} - PresenterMultipleProductsChallenge : $messagePrefix ${"{".lightYellow()}
                ${"Title".green()}: "${responseProductListGet.title}",
                ${"Product Items Result".green()}: ${"{".lightYellow()}
                    ${"Number of Items Successfully received".lightMagenta()}: ${
            responseProductListGet.items.count { result -> result is EntityResult.Successful }
        },
                    ${"Number of Items Failed to be received".lightMagenta()}: ${
            responseProductListGet.items.count { result -> result is EntityResult.Failure }
        },
                ${"}".lightYellow()}
            ${"}".lightYellow()} $messageSuffix
        """.trimIndent()
        println(displayMessage)
    }

}
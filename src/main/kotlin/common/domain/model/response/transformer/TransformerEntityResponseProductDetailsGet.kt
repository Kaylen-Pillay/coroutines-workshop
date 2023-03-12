package common.domain.model.response.transformer

import common.api.model.response.DTOResponseProductDetailGet
import common.domain.model.response.EntityResponseProductDetailsGet

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

object TransformerEntityResponseProductDetailsGet {

    fun DTOResponseProductDetailGet?.toEntityResponseProductDetailsGet(): EntityResponseProductDetailsGet {
        if (this == null) return EntityResponseProductDetailsGet()

        return EntityResponseProductDetailsGet(
            title = this@toEntityResponseProductDetailsGet.title ?: String(),
            subtitle = this@toEntityResponseProductDetailsGet.subtitle ?: String(),
            image = this@toEntityResponseProductDetailsGet.image ?: String(),
            price = this@toEntityResponseProductDetailsGet.price ?: String(),
            stockCount = this@toEntityResponseProductDetailsGet.stockCount?.toString() ?: String()
        )
    }

}
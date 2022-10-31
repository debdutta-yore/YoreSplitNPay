package co.yore.splitnpay.libs

import androidx.compose.foundation.shape.GenericShape

val TriangleShape = GenericShape { size, _ ->
    // 1)
    moveTo(size.width / 2f, 0f)

    // 2)
    lineTo(size.width, size.height)

    // 3)
    lineTo(0f, size.height)
}

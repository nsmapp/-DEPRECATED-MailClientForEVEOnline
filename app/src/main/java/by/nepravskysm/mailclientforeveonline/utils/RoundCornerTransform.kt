package by.nepravskysm.mailclientforeveonline.utils

import com.squareup.picasso.Transformation
import android.graphics.*
import android.graphics.Bitmap
import android.graphics.Shader
import android.graphics.BitmapShader

class RoundCornerTransform (private var radius: Float = 15f): Transformation{



    override fun key(): String {
        return "round_circle"
    }

    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawRoundRect(0f,0f, source.width.toFloat(), source.width.toFloat(), radius, radius, paint)

        if (source != output)
            source.recycle()

        return output
    }
}
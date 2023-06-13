package com.example.objectdetection

import android.content.Context
import android.graphics.*
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.objectdetection.feature_object_detection.domain.model.ObjectDetectionScreenState
import com.example.objectdetection.ml.SsdMobilenetV11Metadata1
import kotlinx.coroutines.runBlocking
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.ByteArrayOutputStream

class TensorFlowAnalyzer(
    private val callback: FrameAnalysisCallback,
    private val context: Context
): ImageAnalysis.Analyzer {

    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )

    private val model = SsdMobilenetV11Metadata1.newInstance(context)

    private val imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(
            300,
            300,
            ResizeOp.ResizeMethod.BILINEAR ))
        .build()

    private val paint = Paint()

    private val colors = listOf(
        Color.BLUE,
        Color.GREEN,
        Color.RED,
        Color.CYAN,
        Color.GRAY,
        Color.BLACK,
        Color.DKGRAY,
        Color.MAGENTA,
    )

    private var labels = FileUtil.loadLabels(context, "labels.txt")


    override fun analyze(image: ImageProxy) = runBlocking {
        var originalBitmap = image.toBitmap()

        val matrix = Matrix()
        matrix.postRotate(90f)
        val rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)


        var mutableBitmap = rotatedBitmap.copy(Bitmap.Config.ARGB_8888, true)

        val objectsList = ArrayList<String>()

        if(image.format in supportedImageFormats) {

            val tensorImage = TensorImage.fromBitmap(rotatedBitmap).also { image ->
                imageProcessor.process(image)
            }



            val outputs = model.process(tensorImage)
            val locations = outputs.locationsAsTensorBuffer.floatArray
            val classes = outputs.classesAsTensorBuffer.floatArray
            val scores = outputs.scoresAsTensorBuffer.floatArray
            val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer.floatArray

            val canvas = Canvas(mutableBitmap)

            val height = rotatedBitmap.height
            val width = rotatedBitmap.width

            paint.textSize = height/15f
            paint.strokeWidth = height/120f

            var x = 0

            scores.forEachIndexed { index, fl ->
                x = index
                x *=4
                if(fl > 0.5) {
                    paint.color = colors[index % 8]
                    paint.style = Paint.Style.STROKE
                    canvas.drawRect(
                        locations[x+1] * width,
                        locations[x] * height,
                        locations[x+3] * width,
                        locations[x+2] * height,
                        paint
                    )
                    paint.style = Paint.Style.FILL
                    canvas.drawText(
                        labels[classes[index].toInt()],
                        locations[x+1] * width,
                        locations[x] * height,
                        paint
                    )

                    objectsList.add(labels[classes[index].toInt()])
                }
            }


        }
        callback.onFrameAnalyzed(
            ObjectDetectionScreenState(
                analyzedBitmap = mutableBitmap,
                detectedObjects = objectsList
            )
        )
        image.close()
    }

}

interface FrameAnalysisCallback {
    fun onFrameAnalyzed(state: ObjectDetectionScreenState)
}
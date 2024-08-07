package utils

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

data class Parameters(val inputPath: String, val outputPath: String, val width: Int = 0, val height: Int = 0)

enum class Prompt(val text: String) {
    ENTER_WIDTH("Enter rectangle width:"),
    ENTER_HEIGHT("Enter rectangle height:"),

    ENTER_NAME("Enter output image name:")
}

enum class GRADIENT {
    X_GRADIENT, Y_GRADIENT
}

data class Pixel(val x: Int, val y: Int, val energy: Double = 0.0) {
    override fun toString(): String {
        return "x: $x, y: $y"
    }
}

fun Int.squared() = this * this

object Utils {
    val openImage = { inputPath: String -> ImageIO.read(File(inputPath)) }
    val saveImage = { outputPath: String, image: BufferedImage -> ImageIO.write(image, "png", File(outputPath)) }
}

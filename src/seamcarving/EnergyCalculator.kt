package seamcarving

import utils.GRADIENT
import utils.Pixel
import utils.Utils.openImage
import utils.Utils.saveImage
import utils.squared
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.sqrt
import utils.Parameters

class EnergyCalculator(inputPath: String) {
    private val inputImage: BufferedImage
    private var energies: MutableList<MutableList<Double>>

    init {
        inputImage = openImage(inputPath)
        energies = MutableList(inputImage.width) { MutableList(inputImage.height) { 0.0 } }
    }

    fun convertImage() {
        calculateEnergies()
        val maxEnergy = energies.maxOf { it.max() }
        changePixelsRgb(maxEnergy)
    }

    private fun calculateEnergies() {
        for (x in 0 until inputImage.width) {
            for (y in 0 until inputImage.height) {
                val xGradient = calculatePixelEnergy(x, y, GRADIENT.X_GRADIENT)
                val yGradient = calculatePixelEnergy(x, y, GRADIENT.Y_GRADIENT)
                energies[x][y] = sqrt((xGradient + yGradient).toDouble())
            }
        }
    }

    private fun calculatePixelEnergy(x: Int, y: Int, gradient: GRADIENT): Int {
        val prevPixel: Pixel
        val nextPixel: Pixel
        val currentPixel: Pixel
        if (gradient == GRADIENT.X_GRADIENT) {
            currentPixel = Pixel(if (x ==0 ) 1 else if (x == inputImage.width - 1) inputImage.width - 2 else x, y)
            prevPixel = Pixel(currentPixel.x - 1, y)
            nextPixel = Pixel(currentPixel.x + 1, y)
        } else {
            currentPixel = Pixel(x, if (y == 0 ) 1 else if (y == inputImage.height - 1) inputImage.height - 2 else y)
            prevPixel = Pixel(x, currentPixel.y - 1)
            nextPixel = Pixel( x,currentPixel.y + 1)
        }
        return getRgbDiff(prevPixel, nextPixel)
    }

    private fun getRgbDiff(prevPixel: Pixel, nextPixel: Pixel): Int {
        val prevPixelColor = Color(inputImage.getRGB(prevPixel.x, prevPixel.y))
        val nextPixelColor = Color(inputImage.getRGB(nextPixel.x, nextPixel.y))

        val redDiff = prevPixelColor.red - nextPixelColor.red
        val greenDiff = prevPixelColor.green - nextPixelColor.green
        val blueDiff = prevPixelColor.blue - nextPixelColor.blue

        return redDiff.squared() + greenDiff.squared() + blueDiff.squared()
    }

    private fun changePixelsRgb(maxEnergy: Double) {
        for (x in 0 until inputImage.width) {
            for (y in 0 until inputImage.height) {
                val intensity = (255.0 * energies[x][y] / maxEnergy).toInt()
                inputImage.setRGB(x, y, Color(intensity, intensity, intensity).rgb)
            }
        }
    }

    fun save(outputPath: String) {
        saveImage(outputPath, inputImage)
    }
}

fun main(args: Array<String>) {
    val params = Parameters(args[1], args.last())
    val energyCalculator = EnergyCalculator(params.inputPath)
    energyCalculator.convertImage()
    energyCalculator.save(params.outputPath)
}
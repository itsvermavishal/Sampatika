package com.wealth.sampatika.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.wealth.sampatika.ui.plan.model.GoalPlan
import java.io.File
import java.io.FileOutputStream
import android.content.ContentValues
import android.provider.MediaStore

fun exportGoalToPdf(context: Context, goal: GoalPlan) {

    val pdfDocument = PdfDocument()
    val pageInfo =
        PdfDocument.PageInfo.Builder(600, 800, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint()

    var y = 50

    canvas.drawText("Goal Plan Report", 50f, y.toFloat(), paint)
    y += 40

    canvas.drawText("Title: ${goal.title}", 50f, y.toFloat(), paint)
    y += 30

    canvas.drawText("Target: ₹${goal.totalAmount}", 50f, y.toFloat(), paint)
    y += 30

    canvas.drawText("Completed: ₹${goal.completedAmount}", 50f, y.toFloat(), paint)
    y += 30

    canvas.drawText("Remaining: ₹${goal.remainingAmount}", 50f, y.toFloat(), paint)
    y += 40

    goal.progressHistory.forEach {
        canvas.drawText(
            "Month ${it.monthIndex}: ₹${it.paidAmount}",
            50f,
            y.toFloat(),
            paint
        )
        y += 25
    }

    pdfDocument.finishPage(page)

    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
        "${goal.title}.pdf"
    )

    pdfDocument.writeTo(FileOutputStream(file))
    Toast.makeText(
        context,
        "Saved to: ${file.absolutePath}",
        Toast.LENGTH_LONG
    ).show()
    pdfDocument.close()
}



fun generateAndSavePdf(context: Context, goal: GoalPlan) {

    val safeFileName = goal.title
        .replace("[^a-zA-Z0-9_]".toRegex(), "_")

    val fileName =
        "${safeFileName}_${System.currentTimeMillis()}.pdf"

    val pdfDocument = PdfDocument()

    val pageWidth = 1080
    val pageHeight = 1920

    var pageNumber = 1
    var pageInfo =
        PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()

    var page = pdfDocument.startPage(pageInfo)
    var canvas = page.canvas

    val titlePaint = Paint().apply {
        textSize = 60f
        isFakeBoldText = true
    }

    val normalPaint = Paint().apply {
        textSize = 40f
    }

    var y = 120

    // 🔹 HEADER
    canvas.drawText("Sampatika Goal Report", 100f, y.toFloat(), titlePaint)
    y += 120

    canvas.drawText("Title: ${goal.title}", 100f, y.toFloat(), normalPaint)
    y += 60

    canvas.drawText(
        "Total Amount: ₹${"%.2f".format(goal.totalAmount)}",
        2f, y.toFloat(), normalPaint
    )
    y += 60

    canvas.drawText(
        "Completed: ₹${"%.2f".format(goal.completedAmount)}",
        2f, y.toFloat(), normalPaint
    )
    y += 60

    canvas.drawText(
        "Remaining: ₹${"%.2f".format(goal.remainingAmount)}",
        2f, y.toFloat(), normalPaint
    )
    y += 100

    canvas.drawText("Monthly History:", 100f, y.toFloat(), titlePaint)
    y += 80

    // 🔹 Monthly rows with pagination
    goal.progressHistory.forEach { month ->

        if (y > pageHeight - 100) {

            pdfDocument.finishPage(page)

            pageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            y = 120
        }

        canvas.drawText(
            "Month ${month.monthIndex}  |  Expected ₹${"%.2f".format(month.expectedAmount)}  |  Paid ₹${"%.2f".format(month.paidAmount)}",
            100f,
            y.toFloat(),
            normalPaint
        )

        y += 60
    }

    pdfDocument.finishPage(page)

    // 🔥 Save using MediaStore (Android 10+ safe)
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_DOWNLOADS + "/Sampatika"
        )
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }

    val resolver = context.contentResolver

    val uri = resolver.insert(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        contentValues
    )

    uri?.let {

        resolver.openOutputStream(it)?.use { output ->
            pdfDocument.writeTo(output)
        }

        contentValues.clear()
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        resolver.update(it, contentValues, null, null)

        Toast.makeText(
            context,
            "PDF Saved in Documents/Sampatika",
            Toast.LENGTH_LONG
        ).show()
    }

    pdfDocument.close()
}
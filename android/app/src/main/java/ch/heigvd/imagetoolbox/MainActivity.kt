package ch.heigvd.imagetoolbox

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import ch.heigvd.imagetoolbox.ui.theme.ImageToolBoxTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageToolBoxTheme {
                AppScreen()
            }
        }
    }
}

private enum class Shape { RECT, CIRCLE }
private enum class BgMode { NONE, WHITE, HEX }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppScreen() {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()

    var inputUri by remember { mutableStateOf<Uri?>(null) }
    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var shape by remember { mutableStateOf(Shape.RECT) }
    var rectWcm by remember { mutableStateOf("1.8") }
    var rectHcm by remember { mutableStateOf("1.8") }
    var gapMm by remember { mutableStateOf("10") }
    var marginMm by remember { mutableStateOf("7") }
    var dpi by remember { mutableStateOf("300") }

    var mirrorH by remember { mutableStateOf(true) } // coché par défaut
    var mirrorV by remember { mutableStateOf(false) }

    var boostOn by remember { mutableStateOf(false) }
    var boostFactor by remember { mutableStateOf(1.0f) }

    var logoBgMode by remember { mutableStateOf(BgMode.WHITE) } // anti-halo conseillé
    var logoBgHex by remember { mutableStateOf("#FFFFFF") }

    var status by remember { mutableStateOf("") }
    var isWorking by remember { mutableStateOf(false) }
    var outputPdfFile by remember { mutableStateOf<File?>(null) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        inputUri = uri
        outputPdfFile = null
        status = ""
        previewBitmap = uri?.let { loadBitmap(ctx, it, maxDim = 900) }
    }

    fun applyPresetMeringues() {
        shape = Shape.RECT
        rectWcm = "1.8"
        rectHcm = "1.8"
        gapMm = "10"
        marginMm = "7"
        dpi = "300"
        mirrorH = true
        mirrorV = false
        boostOn = false
        boostFactor = 1.0f
        logoBgMode = BgMode.WHITE
        logoBgHex = "#FFFFFF"
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Générateur A4 (PDF)") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(14.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { pickImage.launch("image/*") }) { Text("Choisir un logo") }
                Spacer(Modifier.width(12.dp))
                Text(if (inputUri == null) "(aucun fichier)" else "Logo sélectionné ✓")
            }

            previewBitmap?.let { bmp ->
                Card {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(bmp.asImageBitmap(), contentDescription = "preview", modifier = Modifier.size(90.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Aperçu")
                    }
                }
            }

            Button(onClick = { applyPresetMeringues() }) { Text("Preset meringues (1.8 cm)") }

            // Forme
            Card {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Forme")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = shape == Shape.RECT, onClick = { shape = Shape.RECT })
                        Text("Rect")
                        Spacer(Modifier.width(18.dp))
                        RadioButton(selected = shape == Shape.CIRCLE, onClick = { shape = Shape.CIRCLE })
                        Text("Cercle")
                    }
                }
            }

            if (shape == Shape.RECT) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    NumField("Largeur (cm)", rectWcm, modifier = Modifier.weight(1f)) { rectWcm = it }
                    NumField("Hauteur (cm)", rectHcm, modifier = Modifier.weight(1f)) { rectHcm = it }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                NumField("Gap (mm)", gapMm, modifier = Modifier.weight(1f)) { gapMm = it }
                NumField("Marge (mm)", marginMm, modifier = Modifier.weight(1f)) { marginMm = it }
            }

            NumField("DPI", dpi, intOnly = true) { dpi = it }

            // Options
            Card {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = mirrorH, onCheckedChange = { mirrorH = it })
                        Text("Miroir horizontal")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = mirrorV, onCheckedChange = { mirrorV = it })
                        Text("Miroir vertical")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(checked = boostOn, onCheckedChange = { boostOn = it })
                        Spacer(Modifier.width(10.dp))
                        Text("Couleurs plus vives")
                    }
                    if (boostOn) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Facteur: ${"%.1f".format(boostFactor)}")
                            Spacer(Modifier.width(10.dp))
                            Slider(
                                value = boostFactor,
                                onValueChange = { boostFactor = it },
                                valueRange = 1.0f..2.0f
                            )
                        }
                    }

                    Text("Fond logo (anti-halo)")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = logoBgMode == BgMode.NONE, onClick = { logoBgMode = BgMode.NONE })
                        Text("Aucun")
                        Spacer(Modifier.width(12.dp))
                        RadioButton(
                            selected = logoBgMode == BgMode.WHITE,
                            onClick = { logoBgMode = BgMode.WHITE; logoBgHex = "#FFFFFF" }
                        )
                        Text("Blanc")
                        Spacer(Modifier.width(12.dp))
                        RadioButton(selected = logoBgMode == BgMode.HEX, onClick = { logoBgMode = BgMode.HEX })
                        Text("Couleur")
                    }
                    if (logoBgMode == BgMode.HEX) {
                        OutlinedTextField(
                            value = logoBgHex,
                            onValueChange = { logoBgHex = it },
                            label = { Text("#RRGGBB") },
                            singleLine = true,
                            modifier = Modifier.width(170.dp)
                        )
                    }
                }
            }

            // Génération
            Button(
                enabled = inputUri != null && !isWorking,
                onClick = {
                    val uri = inputUri ?: return@Button
                    isWorking = true
                    status = "Génération..."
                    outputPdfFile = null

                    scope.launch {
                        try {
                            val bmpFull = withContext(Dispatchers.IO) { loadBitmap(ctx, uri, maxDim = 2500) }
                                ?: error("Impossible de lire l'image.")

                            val params = Params(
                                shape = shape,
                                rectWcm = rectWcm.toDoubleOrNull() ?: 1.8,
                                rectHcm = rectHcm.toDoubleOrNull() ?: 1.8,
                                gapMm = gapMm.toDoubleOrNull() ?: 10.0,
                                marginMm = marginMm.toDoubleOrNull() ?: 7.0,
                                dpi = (dpi.toIntOrNull() ?: 300).coerceIn(72, 1200),
                                mirrorH = mirrorH,
                                mirrorV = mirrorV,
                                boost = if (boostOn) boostFactor else null,
                                logoBg = when (logoBgMode) {
                                    BgMode.NONE -> null
                                    BgMode.WHITE -> Color.WHITE
                                    BgMode.HEX -> parseHexColorOrNull(logoBgHex) ?: Color.WHITE
                                }
                            )

                            val out = withContext(Dispatchers.Default) { generateA4Pdf(ctx, bmpFull, params) }
                            outputPdfFile = out
                            status = "PDF prêt: ${out.name}\nAstuce: imprimer à 100% (taille réelle)."
                        } catch (ex: Exception) {
                            status = "Erreur: ${ex.message}"
                        } finally {
                            isWorking = false
                        }
                    }
                }
            ) { Text(if (isWorking) "..." else "Générer PDF") }

            if (status.isNotBlank()) Text(status)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val outFile = outputPdfFile
                Button(enabled = outFile != null && outFile.exists(), onClick = { sharePdf(ctx, outFile!!) }) {
                    Text("Partager")
                }
                Button(enabled = outFile != null && outFile.exists(), onClick = { openPdf(ctx, outFile!!) }) {
                    Text("Ouvrir / Imprimer")
                }
            }
        }
    }
}

@Composable
private fun NumField(
    label: String,
    value: String,
    intOnly: Boolean = false,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { s ->
            val filtered =
                if (intOnly) s.filter { it.isDigit() }
                else s.filter { it.isDigit() || it == '.' || it == ',' }.replace(',', '.')
            onChange(filtered)
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

private data class Params(
    val shape: Shape,
    val rectWcm: Double,
    val rectHcm: Double,
    val gapMm: Double,
    val marginMm: Double,
    val dpi: Int,
    val mirrorH: Boolean,
    val mirrorV: Boolean,
    val boost: Float?,
    val logoBg: Int?
)

private fun mmToPx(mm: Double, dpi: Int): Int = ((mm / 25.4) * dpi).toInt()
private fun cmToPx(cm: Double, dpi: Int): Int = mmToPx(cm * 10.0, dpi)

private fun generateA4Pdf(ctx: Context, srcBitmap: Bitmap, p: Params): File {
    // A4 en pouces: 8.27 x 11.69
    val pageW = (8.27 * p.dpi).toInt()
    val pageH = (11.69 * p.dpi).toInt()

    val margin = mmToPx(p.marginMm, p.dpi)
    val gap = mmToPx(p.gapMm, p.dpi)

    val tileW = if (p.shape == Shape.RECT) cmToPx(p.rectWcm, p.dpi) else cmToPx(p.rectWcm, p.dpi)
    val tileH = if (p.shape == Shape.RECT) cmToPx(p.rectHcm, p.dpi) else tileW

    val usableW = pageW - 2 * margin
    val usableH = pageH - 2 * margin

    val cols = max(0, ((usableW + gap) / (tileW + gap)))
    val rows = max(0, ((usableH + gap) / (tileH + gap)))
    require(cols > 0 && rows > 0) { "Logo trop grand ou marges trop grandes (0 tuile)." }

    val prepared = prepareLogo(srcBitmap, tileW, tileH, p)

    val doc = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(pageW, pageH, 1).create()
    val page = doc.startPage(pageInfo)
    val canvas = page.canvas
    canvas.drawColor(Color.WHITE)

    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val x = margin + c * (tileW + gap)
            val y = margin + r * (tileH + gap)
            canvas.drawBitmap(prepared, x.toFloat(), y.toFloat(), null)
        }
    }

    doc.finishPage(page)

    val outDir = File(ctx.cacheDir, "exports").apply { mkdirs() }
    val outFile = File(outDir, "a4_${System.currentTimeMillis()}.pdf")
    FileOutputStream(outFile).use { doc.writeTo(it) }
    doc.close()
    return outFile
}

private fun prepareLogo(src: Bitmap, targetW: Int, targetH: Int, p: Params): Bitmap {
    val dst = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
    val c = Canvas(dst)

    // fond logo (anti-halo)
    p.logoBg?.let { c.drawColor(it) }

    val srcW = src.width.toFloat()
    val srcH = src.height.toFloat()
    val scale = max(targetW / srcW, targetH / srcH)
    val dx = (targetW - srcW * scale) / 2f
    val dy = (targetH - srcH * scale) / 2f

    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    if (p.boost != null && p.boost > 1.0f) {
        val cm = ColorMatrix().apply { setSaturation(p.boost) }
        paint.colorFilter = ColorMatrixColorFilter(cm)
    }

    if (p.shape == Shape.CIRCLE) {
        val temp = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
        val tc = Canvas(temp)
        if (p.logoBg != null) tc.drawColor(p.logoBg)

        tc.drawBitmap(src, Matrix().apply {
            postScale(scale, scale)
            postTranslate(dx, dy)
        }, paint)

        val out = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
        val oc = Canvas(out)
        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        oc.drawCircle(targetW / 2f, targetH / 2f, min(targetW, targetH) / 2f, maskPaint)
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        oc.drawBitmap(temp, 0f, 0f, maskPaint)
        maskPaint.xfermode = null

        temp.recycle()
        return applyMirrors(out, p.mirrorH, p.mirrorV)
    } else {
        c.drawBitmap(src, Matrix().apply {
            postScale(scale, scale)
            postTranslate(dx, dy)
        }, paint)
        return applyMirrors(dst, p.mirrorH, p.mirrorV)
    }
}

private fun applyMirrors(bmp: Bitmap, mirrorH: Boolean, mirrorV: Boolean): Bitmap {
    if (!mirrorH && !mirrorV) return bmp
    val m = Matrix().apply {
        val sx = if (mirrorH) -1f else 1f
        val sy = if (mirrorV) -1f else 1f
        postScale(sx, sy, bmp.width / 2f, bmp.height / 2f)
    }
    val out = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, m, true)
    if (out != bmp) bmp.recycle()
    return out
}

private fun parseHexColorOrNull(s: String): Int? {
    val t = s.trim().let { if (!it.startsWith("#")) "#$it" else it }
    return try {
        if (t.length == 7) Color.parseColor(t) else null
    } catch (_: Exception) {
        null
    }
}

private fun loadBitmap(ctx: Context, uri: Uri, maxDim: Int): Bitmap? {
    val input = ctx.contentResolver.openInputStream(uri) ?: return null
    val bmp = BitmapFactory.decodeStream(input)
    input.close()

    val w = bmp.width
    val h = bmp.height
    val scale = maxDim.toFloat() / max(w, h).toFloat()
    if (scale >= 1f) return bmp

    val nw = (w * scale).toInt()
    val nh = (h * scale).toInt()
    val resized = Bitmap.createScaledBitmap(bmp, nw, nh, true)
    if (resized != bmp) bmp.recycle()
    return resized
}

private fun sharePdf(ctx: Context, file: File) {
    val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
    val i = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    ctx.startActivity(Intent.createChooser(i, "Partager le PDF"))
}

private fun openPdf(ctx: Context, file: File) {
    val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
    val i = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    ctx.startActivity(i)
}

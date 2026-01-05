# ImageToolBox

A small monorepo containing:

- **desktop/** → Java (Maven) CLI + JavaFX GUI for basic image processing + A4 PDF logo tiling
- **android/** → Android app (Kotlin + Jetpack Compose) to generate A4 PDF sheets directly from a phone

---

## Table of Contents

- [Authors](#authors)
- [Project Overview](#project-overview)
  - [Desktop (CLI + GUI)](#desktop-cli--gui)
  - [Android App](#android-app)
- [Implemented Commands (Desktop CLI)](#implemented-commands-desktop-cli)
- [Build Instructions](#build-instructions)
  - [Desktop](#desktop)
  - [Android](#android)
- [Usage Instructions](#usage-instructions)
  - [Desktop CLI](#desktop-cli)
  - [Desktop GUI (JavaFX)](#desktop-gui-javafx)
  - [Android App](#android-app-1)
- [A4 Logo Tiling Feature (tileA4)](#a4-logo-tiling-feature-tilea4)
- [Use of AI Tools](#use-of-ai-tools)

---

## Authors

- Romain Durussel  
- Abram Zweifel  
- HEIG-VD, Class C, 2025–2026

---

## Project Overview

ImageToolBox started as a Java-based command-line toolkit for basic image processing operations.
It has since evolved into a **desktop tool (CLI + GUI)** and a **phone-friendly Android app** to generate A4 PDF sheets for printing (e.g., mirrored edible-print logos).

### Desktop (CLI + GUI)

The desktop project demonstrates:

- Image manipulation via `BufferedImage`, `Raster`, `WritableRaster`
- Efficient stream-based file I/O
- Modular CLI using **Picocli**
- Packaging with **Maven** (shade plugin)
- Automatic generation of native **A4 PDF** sheets (PDFBox)
- A simple **JavaFX GUI** for non-technical users

Location: `desktop/`

### Android App

The Android project provides a user-friendly UI to:

- Pick a logo image from the phone
- Configure size, margins, gap, DPI, mirroring, optional color boost, background handling
- Generate an A4 PDF and **share/open/print** directly from the phone

Location: `android/`

---

## Implemented Commands (Desktop CLI)

- `grayscale`  
  Converts a color image to grayscale.

- `invert`  
  Inverts all color channels.

- `rotate`  
  Rotates an image by 90°, 180°, or 270°.

- `mirror`  
  Applies horizontal and/or vertical mirroring.

- `tileA4` (Advanced Feature)  
  Creates complete A4 sheets with repeated logos:
  - Multiple inputs (`-I file1,file2,...`)
  - One logo per row (up to 7 rows)
  - Circular & rectangular modes
  - User-defined size in cm
  - Mirroring options
  - Native PDF output if output ends with `.pdf`
  - Optional background handling for logos (useful to avoid halos on transparent PNG)

---

## Build Instructions

### Desktop

```bash
cd desktop
./mvnw clean package
````

Outputs (typical):

* `desktop/target/ImageToolBox-1.0-SNAPSHOT.jar` (or shaded jar, depending on configuration)

### Android

From Android Studio:

* Open the **`android/`** folder as a project
* Run on an emulator or real device

From terminal (optional):

```bash
cd android
./gradlew assembleDebug
```

---

## Usage Instructions

### Desktop CLI

From the repository root:

```bash
cd desktop
```

**Grayscale**

```bash
java -jar target/ImageToolBox-1.0-SNAPSHOT.jar \
  -i image/input/input1.jpg -o image/output/grayscale.jpg grayscale
```

**Invert**

```bash
java -jar target/ImageToolBox-1.0-SNAPSHOT.jar \
  -i image/input/input1.jpg -o image/output/invert.jpg invert
```

**Rotate 90°**

```bash
java -jar target/ImageToolBox-1.0-SNAPSHOT.jar \
  -i image/input/input1.jpg -o image/output/rotate.jpg rotate -a 90
```

**Help**

```bash
java -jar target/ImageToolBox-1.0-SNAPSHOT.jar -h
```

**Version**

```bash
java -jar target/ImageToolBox-1.0-SNAPSHOT.jar -V
```

> Tip: If your build produces a `*-shaded.jar`, prefer that one (it includes dependencies).

---

### Desktop GUI (JavaFX)

The GUI is meant for non-technical users to generate A4 sheets without using the command line.

**Recommended (IDE):**

* Open the `desktop/` project in IntelliJ
* Run: `ch.heigvd.gui.ImageToolBoxLauncher` (or the GUI main class)

**From terminal (if your jar includes everything):**

```bash
cd desktop
java -cp target/ImageToolBox-1.0-SNAPSHOT.jar ch.heigvd.gui.ImageToolBoxLauncher
```

---

### Android App

1. Install/run the app from Android Studio on a phone
2. Select a logo
3. Adjust settings (size, mirror, gap, margin, etc.)
4. Generate the A4 PDF
5. Share / open / print from the phone

This is especially useful when the user prints PDFs directly from their phone.

---

## A4 Logo Tiling Feature (tileA4)

Example: Generate a PDF A4 page with rectangular mirrored logos:

```bash
cd desktop
java -jar target/ImageToolBox-1.0-SNAPSHOT.jar \
  -i image/input/logo1.png \
  -o image/output/planche.pdf \
  tileA4 \
  --shape rect \
  --rect-width-cm 1.8 \
  --rect-height-cm 1.8 \
  --gap-mm 10 \
  --margin-mm 7 \
  --mirror-horizontal
```

This produces:

* A native A4 PDF (210 × 297 mm)
* An automatic grid (columns × rows computed from your parameters)
* Proportional logos (no distortion) with consistent spacing

**Printing note:** print at **100% scale / actual size** (do not "fit to page") to keep real-world dimensions.

---

## Use of AI Tools

ChatGPT was used as a support tool to:

* Explore and validate Java imaging techniques
* Suggest improvements for `tileA4`
* Help implement native A4 PDF generation (PDFBox)
* Assist in structuring a beginner-friendly JavaFX GUI
* Assist with the Android Jetpack Compose UI and the PDF generation/sharing flow
* Help improve documentation and repo structure

All final code was manually integrated and adapted by the authors.
# pos_printer_unity

[![pub package](https://img.shields.io/pub/v/pos_printer_unity.svg)](https://pub.dev/packages/pos_printer_unity)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A unified Flutter plugin for **POS terminal thermal printers**. One API, multiple terminal brands, zero headaches.

## Features

- **Unified API** — Single `PosPrinterUnity` class for all terminal printers
- **Auto-detection** — Automatically detects the terminal brand via device model
- **Text printing** — Print formatted text directly
- **Bitmap printing** — Print images (PNG/JPEG) as bitmaps
- **Paper feed** — Control paper feeding
- **Status check** — Query printer status (ready, out of paper, etc.)
- **Extensible** — Easy adapter pattern to add new terminal brands

## Supported Terminals

| Brand | Models | Status |
|-------|--------|--------|
| **Positivo** | L3, L400 | ✅ Supported |
| **PAX** | A910, A920, A920Pro, A930, A800, A35 | ✅ Supported |
| Gertec (GPOS) | GPOS700, GPOS720 | 🔜 Planned |
| POSMP | APOS A8, DX8000 | 🔜 Planned |
| Ingenico | SafraPay terminals | 🔜 Planned |

## Getting Started

### Installation

```yaml
dependencies:
  pos_printer_unity: ^0.0.1
```

Then run:

```bash
flutter pub get
```

### Basic Usage

```dart
import 'package:pos_printer_unity/pos_printer_unity.dart';

final printer = PosPrinterUnity();

// Initialize (auto-detects terminal brand)
final initResult = await printer.init();
if (!initResult.success) {
  print('Error: ${initResult.errorMessage}');
  return;
}

// Print text
final result = await printer.printText('Hello from POS Printer Unity!');
print('Print ${result.success ? "OK" : "FAILED"}');

// Print a bitmap image
final bytes = await rootBundle.load('assets/receipt.png');
final bitmapResult = await printer.printBitmap(bytes.buffer.asUint8List());

// Feed paper
await printer.feedPaper(lines: 5);

// Check status
final status = await printer.getStatus();
print('Printer status: ${status.name}');
```

### Specify a Brand

```dart
// Force Positivo printer
final result = await printer.init(brand: PrinterBrand.positivo);
```

## API Reference

### `PosPrinterUnity`

| Method | Description |
|--------|-------------|
| `init({PrinterBrand?})` | Initialize the printer (auto-detect or specify brand) |
| `printText(String)` | Print a text string |
| `printBitmap(Uint8List)` | Print a bitmap image from bytes |
| `feedPaper({int lines})` | Feed paper by N lines |
| `getStatus()` | Get current printer status |
| `getDetectedBrand()` | Get the auto-detected terminal brand |

### `PrinterBrand`

```dart
enum PrinterBrand { positivo, pax, gertec, posmp, ingenico, auto }
```

### `PrinterStatus`

```dart
enum PrinterStatus { ready, printing, error, outOfPaper, notInitialized, unknown }
```

### `PrintResult`

```dart
class PrintResult {
  final bool success;
  final String? errorMessage;
  final int? errorCode;
}
```

## Platform Support

| Platform | Supported |
|----------|-----------|
| Android  | ✅        |
| iOS      | ❌        |
| Web      | ❌        |

> This plugin is designed exclusively for **Android POS terminals** with built-in thermal printers.

## Adding a New Terminal Brand

1. Create an adapter implementing `IPrinterAdapter`:

```java
public class MyBrandAdapter implements IPrinterAdapter {
    @Override public void init(PrinterAdapterCallback callback) { /* ... */ }
    @Override public void printBitmap(Bitmap bitmap, PrinterAdapterCallback callback) { /* ... */ }
    @Override public void printText(String text, PrinterAdapterCallback callback) { /* ... */ }
    @Override public void feedPaper(int lines) { /* ... */ }
    @Override public String getStatus() { /* ... */ }
    @Override public String getBrand() { return "MY_BRAND"; }
}
```

2. Add the vendor SDK (`.aar`) to the local Maven repo under `android/repo/`
3. Register model detection in `PrinterFactory.java`
4. Add the brand to `PrinterBrand` enum in Dart

## License

MIT License — see [LICENSE](LICENSE) for details.

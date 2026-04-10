import 'dart:typed_data';

import 'models/print_result.dart';
import 'models/printer_brand.dart';
import 'models/printer_status.dart';
import 'pos_printer_unity_platform_interface.dart';

/// **PosPrinterUnity** — A unified API for POS terminal printers.
///
/// Use this class to initialize, query, and print on any supported POS
/// terminal. The plugin automatically detects the terminal brand or you
/// can specify one explicitly.
///
/// ### Quick start
/// ```dart
/// final printer = PosPrinterUnity();
///
/// // Initialize (auto-detects terminal brand)
/// final initResult = await printer.init();
/// if (!initResult.success) {
///   print('Error: ${initResult.errorMessage}');
///   return;
/// }
///
/// // Print a bitmap
/// final bytes = await rootBundle.load('assets/receipt.png');
/// final result = await printer.printBitmap(bytes.buffer.asUint8List());
/// print(result);
/// ```
class PosPrinterUnity {
  PosPrinterUnityPlatform get _platform => PosPrinterUnityPlatform.instance;

  /// Initialize the printer.
  ///
  /// Pass [brand] to force a specific terminal brand, or leave it as
  /// [PrinterBrand.auto] to let the plugin detect it automatically based
  /// on `Build.MODEL`.
  Future<PrintResult> init({PrinterBrand brand = PrinterBrand.auto}) {
    return _platform.initPrinter(brand: brand);
  }

  /// Get the current printer status.
  Future<PrinterStatus> getStatus() {
    return _platform.getStatus();
  }

  /// Print a bitmap image.
  ///
  /// [imageBytes] should be PNG or JPEG encoded bytes.
  Future<PrintResult> printBitmap(Uint8List imageBytes) {
    return _platform.printBitmap(imageBytes);
  }

  /// Print plain text.
  Future<PrintResult> printText(String text) {
    return _platform.printText(text);
  }

  /// Feed paper by [lines] lines (default 3).
  Future<PrintResult> feedPaper({int lines = 3}) {
    return _platform.feedPaper(lines: lines);
  }

  /// Get the currently detected printer brand.
  ///
  /// Returns `null` if the printer has not been initialized yet.
  Future<PrinterBrand?> getDetectedBrand() {
    return _platform.getDetectedBrand();
  }
}

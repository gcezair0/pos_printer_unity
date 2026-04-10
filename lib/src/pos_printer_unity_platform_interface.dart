import 'dart:typed_data';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'models/print_result.dart';
import 'models/printer_brand.dart';
import 'models/printer_status.dart';
import 'pos_printer_unity_method_channel.dart';

/// The platform interface for [PosPrinterUnity].
///
/// Platform implementations should extend this class rather than implement it.
abstract class PosPrinterUnityPlatform extends PlatformInterface {
  PosPrinterUnityPlatform() : super(token: _token);

  static final Object _token = Object();

  static PosPrinterUnityPlatform _instance = MethodChannelPosPrinterUnity();

  /// The default instance of [PosPrinterUnityPlatform] to use.
  static PosPrinterUnityPlatform get instance => _instance;

  /// Set the instance of [PosPrinterUnityPlatform] to use.
  static set instance(PosPrinterUnityPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  /// Initialize the printer with the given [brand].
  ///
  /// If [brand] is [PrinterBrand.auto], the plugin will try to detect
  /// the printer based on the device model.
  Future<PrintResult> initPrinter({PrinterBrand brand = PrinterBrand.auto});

  /// Get the current printer status.
  Future<PrinterStatus> getStatus();

  /// Print a bitmap image from raw bytes (PNG/JPEG encoded).
  Future<PrintResult> printBitmap(Uint8List imageBytes);

  /// Print plain text.
  Future<PrintResult> printText(String text);

  /// Feed paper by [lines] lines.
  Future<PrintResult> feedPaper({int lines = 3});

  /// Get the currently detected printer brand.
  Future<PrinterBrand?> getDetectedBrand();
}

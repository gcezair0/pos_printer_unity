import 'package:flutter/services.dart';

import 'models/print_result.dart';
import 'models/printer_brand.dart';
import 'models/printer_status.dart';
import 'pos_printer_unity_platform_interface.dart';

/// The MethodChannel implementation of [PosPrinterUnityPlatform].
class MethodChannelPosPrinterUnity extends PosPrinterUnityPlatform {
  /// The method channel used to interact with the native platform.
  final MethodChannel _channel = const MethodChannel(
    'com.guilherme.pos_printer/methods',
  );

  @override
  Future<PrintResult> initPrinter({
    PrinterBrand brand = PrinterBrand.auto,
  }) async {
    try {
      final result = await _channel.invokeMethod<Map>('initPrinter', {
        'brand': brand.value,
      });
      if (result != null) {
        return PrintResult.fromMap(result);
      }
      return PrintResult.ok();
    } on PlatformException catch (e) {
      return PrintResult.failure(
        message: e.message ?? 'Failed to initialize printer',
        code: int.tryParse(e.code),
      );
    }
  }

  @override
  Future<PrinterStatus> getStatus() async {
    try {
      final status = await _channel.invokeMethod<String>('getStatus');
      return PrinterStatus.fromString(status);
    } on PlatformException {
      return PrinterStatus.unknown;
    }
  }

  @override
  Future<PrintResult> printBitmap(Uint8List imageBytes) async {
    try {
      final result = await _channel.invokeMethod<Map>('printBitmap', {
        'imageBytes': imageBytes,
      });
      if (result != null) {
        return PrintResult.fromMap(result);
      }
      return PrintResult.ok();
    } on PlatformException catch (e) {
      return PrintResult.failure(
        message: e.message ?? 'Failed to print bitmap',
        code: int.tryParse(e.code),
      );
    }
  }

  @override
  Future<PrintResult> printText(String text) async {
    try {
      final result = await _channel.invokeMethod<Map>('printText', {
        'text': text,
      });
      if (result != null) {
        return PrintResult.fromMap(result);
      }
      return PrintResult.ok();
    } on PlatformException catch (e) {
      return PrintResult.failure(
        message: e.message ?? 'Failed to print text',
        code: int.tryParse(e.code),
      );
    }
  }

  @override
  Future<PrintResult> feedPaper({int lines = 3}) async {
    try {
      final result = await _channel.invokeMethod<Map>('feedPaper', {
        'lines': lines,
      });
      if (result != null) {
        return PrintResult.fromMap(result);
      }
      return PrintResult.ok();
    } on PlatformException catch (e) {
      return PrintResult.failure(
        message: e.message ?? 'Failed to feed paper',
        code: int.tryParse(e.code),
      );
    }
  }

  @override
  Future<PrinterBrand?> getDetectedBrand() async {
    try {
      final brand = await _channel.invokeMethod<String>('getDetectedBrand');
      if (brand == null) return null;
      return PrinterBrand.values.firstWhere(
        (b) => b.value == brand,
        orElse: () => PrinterBrand.auto,
      );
    } on PlatformException {
      return null;
    }
  }
}

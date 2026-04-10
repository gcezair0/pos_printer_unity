import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/services.dart';
import 'package:pos_printer_unity/pos_printer_unity.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  // ignore: unused_local_variable
  const MethodChannel channel = MethodChannel(
    'com.guilherme.pos_printer/methods',
  );

  // ignore: unused_local_variable
  late PosPrinterUnity printer;

  setUp(() {
    printer = PosPrinterUnity();
  });

  group('PrintResult', () {
    test('PrintResult.ok() returns success', () {
      final result = PrintResult.ok();
      expect(result.success, true);
      expect(result.status, PrinterStatus.ready);
      expect(result.errorMessage, isNull);
    });

    test('PrintResult.failure() returns failure', () {
      final result = PrintResult.failure(
        message: 'No paper',
        code: 240,
        status: PrinterStatus.outOfPaper,
      );
      expect(result.success, false);
      expect(result.errorMessage, 'No paper');
      expect(result.errorCode, 240);
      expect(result.status, PrinterStatus.outOfPaper);
    });

    test('PrintResult.fromMap() parses correctly', () {
      final map = {
        'success': true,
        'status': 'READY',
        'errorMessage': null,
        'errorCode': null,
      };
      final result = PrintResult.fromMap(map);
      expect(result.success, true);
      expect(result.status, PrinterStatus.ready);
    });
  });

  group('PrinterStatus', () {
    test('fromString returns correct enum values', () {
      expect(PrinterStatus.fromString('READY'), PrinterStatus.ready);
      expect(PrinterStatus.fromString('BUSY'), PrinterStatus.busy);
      expect(
        PrinterStatus.fromString('OUT_OF_PAPER'),
        PrinterStatus.outOfPaper,
      );
      expect(PrinterStatus.fromString('OVERHEAT'), PrinterStatus.overheat);
      expect(PrinterStatus.fromString('LOW_VOLTAGE'), PrinterStatus.lowVoltage);
      expect(
        PrinterStatus.fromString('NOT_INITIALIZED'),
        PrinterStatus.notInitialized,
      );
      expect(PrinterStatus.fromString('ERROR'), PrinterStatus.error);
      expect(PrinterStatus.fromString('SOMETHING_ELSE'), PrinterStatus.unknown);
      expect(PrinterStatus.fromString(null), PrinterStatus.unknown);
    });
  });

  group('PrinterBrand', () {
    test('enum values have correct string values', () {
      expect(PrinterBrand.positivo.value, 'POSITIVO');
      expect(PrinterBrand.pax.value, 'PAX');
      expect(PrinterBrand.gertec.value, 'GERTEC');
      expect(PrinterBrand.posmp.value, 'POSMP');
      expect(PrinterBrand.ingenico.value, 'INGENICO');
      expect(PrinterBrand.auto.value, 'AUTO');
    });
  });
}

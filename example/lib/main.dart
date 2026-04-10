import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pos_printer_unity/pos_printer_unity.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'POS Printer Unity Demo',
      theme: ThemeData(colorSchemeSeed: Colors.blue, useMaterial3: true),
      home: const PrinterDemoPage(),
    );
  }
}

class PrinterDemoPage extends StatefulWidget {
  const PrinterDemoPage({super.key});

  @override
  State<PrinterDemoPage> createState() => _PrinterDemoPageState();
}

class _PrinterDemoPageState extends State<PrinterDemoPage> {
  final PosPrinterUnity _printer = PosPrinterUnity();

  String _status = 'Not initialized';
  String _brand = '-';
  bool _isLoading = false;

  Future<void> _initPrinter() async {
    setState(() => _isLoading = true);

    // Auto-detect the terminal brand
    final result = await _printer.init();

    if (result.success) {
      final brand = await _printer.getDetectedBrand();
      setState(() {
        _status = 'Ready ✅';
        _brand = brand?.value ?? 'Unknown';
      });
    } else {
      setState(() {
        _status = 'Error: ${result.errorMessage}';
      });
    }

    setState(() => _isLoading = false);
  }

  Future<void> _printTestText() async {
    setState(() => _isLoading = true);

    final result = await _printer.printText(
      '===========================\n'
      '   POS PRINTER UNITY\n'
      '   Teste de Impressão\n'
      '===========================\n'
      '\n'
      'Package: pos_printer_unity\n'
      'Versão: 0.0.1\n'
      '\n'
      'Impressora unificada para\n'
      'terminais POS.\n'
      '\n'
      '===========================\n',
    );

    _showResult(result);
    setState(() => _isLoading = false);
  }

  Future<void> _printTestBitmap() async {
    setState(() => _isLoading = true);

    try {
      // Load a sample image from assets or generate one
      final ByteData data = await rootBundle.load('assets/test_receipt.png');
      final Uint8List imageBytes = data.buffer.asUint8List();

      final result = await _printer.printBitmap(imageBytes);
      _showResult(result);
    } catch (e) {
      _showResult(PrintResult.failure(message: e.toString()));
    }

    setState(() => _isLoading = false);
  }

  Future<void> _feedPaper() async {
    final result = await _printer.feedPaper(lines: 5);
    _showResult(result);
  }

  void _showResult(PrintResult result) {
    final message = result.success
        ? 'Impressão realizada com sucesso! ✅'
        : 'Erro: ${result.errorMessage}';

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: result.success ? Colors.green : Colors.red,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('POS Printer Unity'), centerTitle: true),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Status card
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  children: [
                    const Icon(Icons.print, size: 48, color: Colors.blue),
                    const SizedBox(height: 12),
                    Text(
                      'Status: $_status',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 4),
                    Text(
                      'Marca: $_brand',
                      style: Theme.of(context).textTheme.bodyMedium,
                    ),
                  ],
                ),
              ),
            ),

            const SizedBox(height: 24),

            // Action buttons
            ElevatedButton.icon(
              onPressed: _isLoading ? null : _initPrinter,
              icon: const Icon(Icons.power_settings_new),
              label: const Text('Inicializar Impressora'),
            ),

            const SizedBox(height: 12),

            ElevatedButton.icon(
              onPressed: _isLoading ? null : _printTestText,
              icon: const Icon(Icons.text_fields),
              label: const Text('Imprimir Texto de Teste'),
            ),

            const SizedBox(height: 12),

            ElevatedButton.icon(
              onPressed: _isLoading ? null : _printTestBitmap,
              icon: const Icon(Icons.image),
              label: const Text('Imprimir Bitmap de Teste'),
            ),

            const SizedBox(height: 12),

            OutlinedButton.icon(
              onPressed: _isLoading ? null : _feedPaper,
              icon: const Icon(Icons.vertical_align_bottom),
              label: const Text('Avançar Papel'),
            ),

            if (_isLoading) ...[
              const SizedBox(height: 24),
              const Center(child: CircularProgressIndicator()),
            ],
          ],
        ),
      ),
    );
  }
}

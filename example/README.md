# pos_printer_unity — Example App

Aplicativo de demonstração para o pacote `pos_printer_unity`.

## Funcionalidades

- Inicializar impressora (auto-detecção ou marca específica)
- Imprimir texto de teste
- Imprimir bitmap (captura de widget como imagem)
- Avançar papel
- Log em tempo real das operações

## Como rodar

```bash
cd example
flutter pub get
flutter run
```

### Na L400 (Positivo)

```bash
flutter run -d <device_id>
```

## Screenshot

O app exibe:
- **Card de status** — verde (pronta) ou laranja (não inicializada)
- **4 botões** — Inicializar, Texto, Bitmap, Avançar Papel
- **Preview do recibo** — como ficará impresso
- **Terminal de log** — todas as operações em tempo real

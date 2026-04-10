# Contributing to pos_printer_unity

Obrigado pelo interesse em contribuir! 🎉

## 🛠️ Configuração do Ambiente

1. Clone o repositório:
   ```bash
   git clone https://github.com/gcezair0/pos_printer_unity.git
   cd pos_printer_unity
   ```

2. Instale as dependências:
   ```bash
   flutter pub get
   ```

3. Rode os testes:
   ```bash
   flutter test
   ```

## 📱 Adicionando Suporte a um Novo Terminal

### 1. Android (Java)

Crie o adapter em `android/src/main/java/com/guilherme/pos_printer/printers/<marca>/`:

```java
public class MinhaMarcaPrinterAdapter implements IPrinterAdapter {
    @Override public void init(PrinterAdapterCallback callback) { /* ... */ }
    @Override public void printBitmap(Bitmap bitmap, PrinterAdapterCallback callback) { /* ... */ }
    @Override public void printText(String text, PrinterAdapterCallback callback) { /* ... */ }
    @Override public void feedPaper(int lines) { /* ... */ }
    @Override public String getStatus() { return "READY"; }
    @Override public String getBrand() { return "MINHA_MARCA"; }
}
```

### 2. Registre no Factory

Em `PrinterFactory.java`, adicione:
- Um `case` no método `create()`
- Detecção de modelo no método `autoDetect()`

### 3. SDK (.aar)

- Coloque o `.aar` em `android/libs/`
- Adicione regras de ProGuard em `proguard-rules.pro`

### 4. Dart

Adicione a marca no enum `PrinterBrand` em `lib/src/models/printer_brand.dart`.

### 5. Testes

Adicione testes para o novo enum value em `test/`.

## 📏 Padrão de Código

- **Dart**: Seguir `flutter_lints`
- **Java**: Seguir convenções Android padrão
- **Commits**: Usar [Conventional Commits](https://www.conventionalcommits.org/)
  - `feat:` nova funcionalidade
  - `fix:` correção de bug
  - `docs:` documentação
  - `chore:` manutenção

## 🔀 Pull Requests

1. Crie um branch: `feat/suporte-pax`
2. Faça suas alterações
3. Rode `flutter test` e `dart format .`
4. Abra o PR com descrição clara

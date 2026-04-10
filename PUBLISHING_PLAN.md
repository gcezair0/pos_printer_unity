# Planejamento de Publicação — pos_printer_unity

## 📌 Status Atual

| Item | Status |
|------|--------|
| Código Dart (API pública) | ✅ Pronto |
| Código Android nativo | ✅ Pronto (Positivo) |
| SDK Positivo (.aar) | ✅ Incluído |
| Testes unitários | ✅ Básicos |
| Example app | ✅ Pronto |
| README.md | ✅ Pronto |
| LICENSE (MIT) | ✅ Pronto |
| CHANGELOG.md | ✅ Pronto |
| .gitignore | ✅ Pronto |

---

## 🚀 Roadmap para Publicação

### Fase 1 — Validação (AGORA)
- [ ] Testar impressão na L400 com o app de teste
- [ ] Confirmar que `init()`, `printText()`, `printBitmap()` e `feedPaper()` funcionam
- [ ] Corrigir bugs encontrados no teste real

### Fase 2 — Preparação do Pacote
- [ ] Rodar `flutter pub publish --dry-run` e corrigir todos os warnings
- [ ] Adicionar `example/README.md` (pub.dev exige)
- [ ] Verificar pontuação no https://pub.dev/packages/preview
- [ ] Garantir que `dartdoc` gera documentação sem erros
- [ ] Adicionar doc comments em todas as classes/métodos públicos (/// format)

### Fase 3 — Qualidade
- [ ] Aumentar cobertura de testes (mock do MethodChannel)
- [ ] Adicionar teste de integração no example app
- [ ] Adicionar CI/CD (GitHub Actions para `flutter test` + `flutter analyze`)

### Fase 4 — Publicação
- [ ] Criar repositório no GitHub
- [ ] Push do código
- [ ] Atualizar URLs no `pubspec.yaml` (homepage, repository, issue_tracker)
- [ ] Autenticar no pub.dev (`dart pub login`)
- [ ] Publicar com `flutter pub publish`

### Fase 5 — Evolução (pós-publicação)
- [ ] v0.1.0 — Adicionar suporte PAX (A910, A920)
- [ ] v0.2.0 — Adicionar suporte Gertec/GPOS (GPOS700, GPOS720)
- [ ] v0.3.0 — Adicionar suporte POSMP (APOS A8OVS, DX8000)
- [ ] v0.4.0 — Adicionar suporte Ingenico/SafraPay
- [ ] v1.0.0 — Release estável com todos os terminais

---

## 📋 Checklist pub.dev

O pub.dev avalia seu pacote com uma pontuação de 0 a 160 pontos:

### Convention (30 pts)
- [x] `pubspec.yaml` com description, version, homepage
- [x] `README.md` presente
- [x] `CHANGELOG.md` presente
- [x] `LICENSE` presente
- [ ] `example/` com código funcional
- [ ] `example/README.md`

### Documentation (10 pts)
- [ ] Todas as classes públicas documentadas com `///`
- [ ] Exemplos de código nos doc comments
- [ ] `dartdoc` sem warnings

### Platform (20 pts)
- [x] Suporte Android declarado no pubspec

### Analysis (50 pts)
- [ ] Zero erros no `flutter analyze`
- [ ] Zero warnings no `flutter analyze`
- [ ] Seguir regras do `flutter_lints`

### Dependency (20 pts)
- [x] Dependências em versões recentes
- [x] Sem dependências deprecated

### Pana (30 pts)
- [ ] `dart pub publish --dry-run` sem erros
- [ ] Formatar código com `dart format`
- [ ] Sem arquivos desnecessários no pacote

---

## 📄 Licença

**Escolhida: MIT License**

### Por que MIT?
- ✅ Permite uso comercial
- ✅ Permite modificação e distribuição
- ✅ Permite uso privado
- ✅ É a licença mais usada no pub.dev (~60% dos pacotes)
- ✅ Compatível com projetos Flutter/Dart
- ✅ Simples e curta

### Alternativas consideradas:
| Licença | Prós | Contras |
|---------|------|---------|
| **MIT** ✅ | Simples, permissiva, padrão | Sem proteção de patente |
| BSD-3 | Similar ao MIT, cláusula de não-endosso | Menos conhecida |
| Apache 2.0 | Proteção de patente explícita | Mais complexa |
| GPL | Copyleft forte | Incompatível com apps comerciais |

---

## 📖 Documentação Necessária

### README.md (✅ Já existe)
- Badges do pub.dev (adicionar após publicação)
- Instalação
- Quick start
- API reference
- Supported terminals
- Como adicionar novos terminais

### CONTRIBUTING.md (criar)
- Como configurar ambiente de desenvolvimento
- Como adicionar suporte a novo terminal
- Padrão de código
- Pull request guidelines

### example/README.md (criar — obrigatório pub.dev)
- Descrição do app de exemplo
- Como rodar

---

## 🔒 Cuidados com os SDKs (.aar)

### ⚠️ IMPORTANTE
Os arquivos `.aar` dos fabricantes podem ter licenças restritivas.
Antes de publicar, verificar:

- [ ] O `positivo.aar` pode ser redistribuído publicamente?
- [ ] Existem termos de uso do SDK da Positivo/XCheng?
- [ ] Se NÃO puder redistribuir: documentar como o usuário obtém o .aar por conta própria

### Opções se não puder incluir o .aar:
1. **Não incluir no pacote** — usuário baixa separadamente
2. **Repositório Maven privado** — referência no build.gradle
3. **Entrar em contato com fabricante** — pedir permissão de redistribuição

---

## 📝 Versionamento (SemVer)

```
MAJOR.MINOR.PATCH

0.0.1  ← Atual (pré-release, apenas Positivo)
0.1.0  ← Primeiro terminal adicional
0.x.0  ← Cada novo terminal
1.0.0  ← API estável, todos os terminais principais
```

### Regras:
- **PATCH** (0.0.x): Bug fixes, melhorias internas
- **MINOR** (0.x.0): Novo terminal, nova feature (sem breaking change)
- **MAJOR** (x.0.0): Breaking changes na API pública

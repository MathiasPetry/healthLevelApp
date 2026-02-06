# HealthLevelApp

Aplicativo Android que calcula o **HealthLevel** do carro a partir de respostas objetivas sobre o estado do veículo e informações de revisão.

## Para que serve

- Avaliar a "saúde" geral do carro em um score de **0 a 100**.
- Fornecer um diagnóstico claro (Excelente, Bom, Regular, Crítico).
- Gerar recomendações personalizadas com base nas respostas.
- Calcular o prazo até a próxima revisão de acordo com a preferência do usuário (por tempo ou por km).

## Tecnologias utilizadas

- **Kotlin**
- **Jetpack Compose** (UI declarativa)
- **AndroidX Navigation Compose**
- **Gradle Kotlin DSL**


### 2) Via terminal (scripts inclusos)

O projeto inclui scripts que instalam SDK/JDK, criam AVD e abrem o app.

```bash
./scripts/run_emulator.sh
```

Para executar:

```bash
./scripts/demo.sh
```

Para abrir o emulador mais rapido (sem reinstalar APK):

```bash
SKIP_INSTALL=1 ./scripts/demo.sh
```

Para encerrar:

```bash
./scripts/stop_emulator.sh
```

> Observação: a primeira execução pode demorar porque o script baixa pacotes grandes.
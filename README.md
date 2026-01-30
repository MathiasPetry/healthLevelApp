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

### Padrões de design aplicados

- **MVVM (Model-View-ViewModel)**
  - ViewModel concentra o estado e regras de tela.
- **Factory** (`FabricaCarro`)
  - Instancia o tipo correto de `Carro`.

## Estrutura do projeto

```
app/src/main/java/com/example/carhealth/
├── apresentacao/
│   ├── HealthLevelApp.kt
│   ├── HealthLevelViewModel.kt
│   ├── InitialFormState.kt
│   ├── MaintenanceState.kt
│   ├── Rotas.kt
│   ├── tema/
│   │   └── TemaHealthLevel.kt
│   └── telas/
│       ├── TelaInicial.kt
│       ├── TelaPerguntas.kt
│       └── TelaResultado.kt
├── dominio/
│   ├── Carro.kt
│   ├── CarroCombustao.kt
│   ├── CarroEletrico.kt
│   ├── CarroHibrido.kt
│   ├── CalculadoraHealthLevel.kt
│   ├── CalculadoraRevisao.kt
│   ├── DadosRevisao.kt
│   ├── DiagnosticoHealthLevel.kt
│   ├── FabricaCarro.kt
│   ├── OpcaoResposta.kt
│   ├── PerguntaAvaliacao.kt
│   ├── PerguntasCarro.kt
│   ├── PreferenciaRevisao.kt
│   ├── ResumoRevisao.kt
│   └── ResultadoHealthLevel.kt
└── AtividadePrincipal.kt
```

## Como executar

### 1) Android Studio

1. Abra o Android Studio.
2. File > Open e selecione a pasta do projeto.
3. Aguarde o Gradle Sync.
4. Rode em um emulador ou dispositivo físico.

### 2) Via terminal (scripts inclusos)

O projeto inclui scripts que instalam SDK/JDK, criam AVD e abrem o app.

```bash
./scripts/run_emulator.sh
```

Para encerrar:

```bash
./scripts/stop_emulator.sh
```

> Observação: a primeira execução pode demorar porque o script baixa pacotes grandes.

### 3) Manual (sem script)

```bash
export JAVA_HOME=/caminho/para/jdk17
export ANDROID_HOME=/caminho/para/Android/Sdk
export ANDROID_SDK_ROOT=$ANDROID_HOME
export PATH="$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"

sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0" "emulator" "system-images;android-34;google_apis;x86_64"
avdmanager create avd -n carhealth-api34 -k "system-images;android-34;google_apis;x86_64" -d "medium_phone"

emulator -avd carhealth-api34
./gradlew :app:installDebug
adb shell monkey -p com.example.carhealth -c android.intent.category.LAUNCHER 1
```
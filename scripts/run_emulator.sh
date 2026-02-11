#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APP_ID="com.example.healthlevel"
AVD_NAME="carhealth-api34"
API_LEVEL="34"
BUILD_TOOLS="34.0.0"
SYSTEM_IMAGE="system-images;android-34;google_apis;x86_64"

ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$ANDROID_HOME}"
ANDROID_AVD_HOME="${ANDROID_AVD_HOME:-$HOME/.config/.android/avd}"
JDK_BASE="${JDK_BASE:-$HOME/.jdks}"

mkdir -p "$ANDROID_HOME" "$ANDROID_AVD_HOME" "$JDK_BASE"

if ! command -v curl >/dev/null 2>&1; then
  echo "curl nao encontrado. Instale curl e tente novamente."
  exit 1
fi

if ! command -v unzip >/dev/null 2>&1 && ! command -v python3 >/dev/null 2>&1; then
  echo "Precisa de unzip ou python3 para extrair os pacotes."
  exit 1
fi

# Instala JDK 17 (Temurin) se nao existir
if ! ls "$JDK_BASE"/jdk-17* >/dev/null 2>&1; then
  echo "Baixando JDK 17 (Temurin)..."
  tmp_tgz="$(mktemp -t temurin17-XXXX.tar.gz)"
  curl -L -o "$tmp_tgz" "https://api.adoptium.net/v3/binary/latest/17/ga/linux/x64/jdk/hotspot/normal/eclipse"
  tar -xzf "$tmp_tgz" -C "$JDK_BASE"
  rm -f "$tmp_tgz"
fi

JAVA_HOME="$(ls -d "$JDK_BASE"/jdk-17* | head -n 1)"
export JAVA_HOME ANDROID_HOME ANDROID_SDK_ROOT ANDROID_AVD_HOME
export PATH="$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"
SDKMANAGER="$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager"
AVDMANAGER="$ANDROID_HOME/cmdline-tools/latest/bin/avdmanager"
EMULATOR_BIN="$ANDROID_HOME/emulator/emulator"

# Instala cmdline-tools se necessario
if [ ! -x "$SDKMANAGER" ]; then
  echo "Instalando Android cmdline-tools..."
  tmp_zip="$(mktemp -t cmdline-tools-XXXX.zip)"
  curl -L -o "$tmp_zip" "https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip"
  mkdir -p "$ANDROID_HOME/cmdline-tools"
  if command -v unzip >/dev/null 2>&1; then
    unzip -q "$tmp_zip" -d "$ANDROID_HOME/cmdline-tools"
  else
    python3 -c "import zipfile,sys; zipfile.ZipFile('$tmp_zip').extractall('$ANDROID_HOME/cmdline-tools')"
  fi
  mv "$ANDROID_HOME/cmdline-tools/cmdline-tools" "$ANDROID_HOME/cmdline-tools/latest"
  rm -f "$tmp_zip"
fi

# Aceita licencas e instala pacotes do SDK
yes | "$SDKMANAGER" --licenses >/tmp/android-licenses.log 2>&1 || true
"$SDKMANAGER" \
  "platform-tools" \
  "platforms;android-$API_LEVEL" \
  "build-tools;$BUILD_TOOLS" \
  "emulator" \
  "$SYSTEM_IMAGE"

if ! command -v adb >/dev/null 2>&1; then
  echo "adb nao encontrado mesmo apos instalar platform-tools."
  exit 1
fi
adb start-server >/dev/null 2>&1 || true

# Gera local.properties
cat > "$PROJECT_DIR/local.properties" <<EOF_LOCAL
sdk.dir=$ANDROID_HOME
EOF_LOCAL

# Cria o AVD se nao existir
if ! "$AVDMANAGER" list avd | grep -q "Name: $AVD_NAME"; then
  echo "Criando AVD $AVD_NAME..."
  echo "no" | "$AVDMANAGER" create avd -n "$AVD_NAME" -k "$SYSTEM_IMAGE" -d "medium_phone"
fi

# Inicia o emulador se nenhum estiver rodando
serial="$(adb devices | awk '/emulator-/{print $1; exit}')"
if [ -z "$serial" ]; then
  echo "Iniciando emulador..."
  emu_args=("-avd" "$AVD_NAME" "-no-snapshot" "-no-boot-anim" "-gpu" "swiftshader_indirect" "-netdelay" "none" "-netspeed" "full" "-no-audio")
  if [ -z "${DISPLAY:-}" ]; then
    echo "DISPLAY nao encontrado. Para abrir janela, rode em uma sessao grafica."
    echo "Se quiser rodar sem janela, use: HEADLESS=1 ./scripts/run_emulator.sh"
    exit 1
  fi
  if [ "${HEADLESS:-0}" = "1" ]; then
    emu_args+=("-no-window")
  fi
  nohup "$EMULATOR_BIN" "${emu_args[@]}" >/tmp/carhealth-emulator.log 2>&1 &
  for _ in $(seq 1 60); do
    serial="$(adb devices | awk '/emulator-/{print $1; exit}')"
    if [ -n "$serial" ]; then
      break
    fi
    sleep 2
  done
fi

if [ -z "$serial" ]; then
  echo "Emulador nao iniciou. Veja /tmp/carhealth-emulator.log"
  exit 1
fi

adb -s "$serial" wait-for-device
boot=""
for _ in $(seq 1 90); do
  boot="$(adb -s "$serial" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')"
  if [ "$boot" = "1" ]; then
    break
  fi
  sleep 2
done

if [ "$boot" != "1" ]; then
  echo "Timeout ao iniciar o emulador."
  exit 1
fi

# Instala e abre o app
cd "$PROJECT_DIR"
./gradlew :app:installDebug
adb -s "$serial" shell monkey -p "$APP_ID" -c android.intent.category.LAUNCHER 1

echo "Pronto! App instalado e aberto no emulador ($serial)."

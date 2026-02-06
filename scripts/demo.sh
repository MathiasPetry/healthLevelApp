#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APP_ID="${APP_ID:-com.example.carhealth}"
MAIN_ACTIVITY="${MAIN_ACTIVITY:-.MainActivity}"
AVD_NAME="${AVD_NAME:-carhealth-api34}"

ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$ANDROID_HOME}"
ANDROID_AVD_HOME="${ANDROID_AVD_HOME:-$HOME/.config/.android/avd}"

ADB="$ANDROID_HOME/platform-tools/adb"
EMULATOR_BIN="$ANDROID_HOME/emulator/emulator"
BOOTSTRAP_SCRIPT="$PROJECT_DIR/scripts/run_emulator.sh"

export ANDROID_HOME ANDROID_SDK_ROOT ANDROID_AVD_HOME
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"

log() {
  printf '[demo] %s\n' "$*" >&2
}

fail() {
  printf '[demo] erro: %s\n' "$*" >&2
  exit 1
}

bootstrap_if_needed() {
  if [ ! -x "$ADB" ] || [ ! -x "$EMULATOR_BIN" ]; then
    log "SDK/Emulator nao encontrados. Rodando bootstrap completo (mais lento)."
    cd "$PROJECT_DIR"
    HEADLESS="${HEADLESS:-0}" "$BOOTSTRAP_SCRIPT"
    exit 0
  fi

  if ! "$EMULATOR_BIN" -list-avds | grep -qx "$AVD_NAME"; then
    log "AVD '$AVD_NAME' nao encontrado. Rodando bootstrap completo (mais lento)."
    cd "$PROJECT_DIR"
    HEADLESS="${HEADLESS:-0}" "$BOOTSTRAP_SCRIPT"
    exit 0
  fi
}

start_emulator_if_needed() {
  "$ADB" start-server >/dev/null 2>&1 || true
  serial="$("$ADB" devices | awk '/emulator-/{print $1; exit}')"

  if [ -n "$serial" ]; then
    log "Emulador ja ativo: $serial"
    printf '%s' "$serial"
    return
  fi

  if [ -z "${DISPLAY:-}" ] && [ "${HEADLESS:-0}" != "1" ]; then
    fail "DISPLAY nao encontrado. Use HEADLESS=1 para rodar sem janela."
  fi

  log "Iniciando emulador '$AVD_NAME'..."
  emu_args=(
    "-avd" "$AVD_NAME"
    "-no-snapshot"
    "-no-boot-anim"
    "-gpu" "swiftshader_indirect"
    "-netdelay" "none"
    "-netspeed" "full"
    "-no-audio"
  )
  if [ "${HEADLESS:-0}" = "1" ]; then
    emu_args+=("-no-window")
  fi
  nohup "$EMULATOR_BIN" "${emu_args[@]}" >/tmp/carhealth-emulator.log 2>&1 &

  for _ in $(seq 1 90); do
    serial="$("$ADB" devices | awk '/emulator-/{print $1; exit}')"
    if [ -n "$serial" ]; then
      printf '%s' "$serial"
      return
    fi
    sleep 2
  done

  fail "Emulador nao iniciou. Verifique /tmp/carhealth-emulator.log"
}

wait_boot_completed() {
  local serial="$1"
  "$ADB" -s "$serial" wait-for-device
  for _ in $(seq 1 120); do
    boot="$("$ADB" -s "$serial" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')"
    if [ "$boot" = "1" ]; then
      return
    fi
    sleep 2
  done
  fail "Timeout aguardando boot do emulador $serial."
}

install_and_launch() {
  local serial="$1"

  local should_install=1
  if [ "${SKIP_INSTALL:-0}" = "1" ]; then
    should_install=0
  fi

  if [ "$should_install" = "0" ] && ! "$ADB" -s "$serial" shell pm list packages | grep -q "package:$APP_ID$"; then
    log "Pacote '$APP_ID' nao encontrado no device; fazendo installDebug."
    should_install=1
  fi

  if [ "$should_install" = "1" ]; then
    log "Instalando app (installDebug)..."
    cd "$PROJECT_DIR"
    ./gradlew :app:installDebug --console=plain
  else
    log "SKIP_INSTALL=1, pulando installDebug."
  fi

  log "Abrindo app..."
  "$ADB" -s "$serial" shell am start -W -n "$APP_ID/$MAIN_ACTIVITY" >/tmp/carhealth-launch.log
  if ! "$ADB" -s "$serial" shell dumpsys activity activities | grep -q "$APP_ID/$MAIN_ACTIVITY"; then
    fail "App nao ficou no topo. Verifique /tmp/carhealth-launch.log"
  fi
}

main() {
  bootstrap_if_needed
  serial="$(start_emulator_if_needed)"
  wait_boot_completed "$serial"
  install_and_launch "$serial"
  log "Pronto. App aberto no emulador ($serial)."
  log "Dica: para abrir ainda mais rapido, use SKIP_INSTALL=1 ./scripts/demo.sh"
}

main "$@"

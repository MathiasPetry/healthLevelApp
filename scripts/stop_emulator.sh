#!/usr/bin/env bash
set -euo pipefail

ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
export PATH="$ANDROID_HOME/platform-tools:$PATH"

serials="$(adb devices | awk '/emulator-/{print $1}')"
if [ -z "$serials" ]; then
  echo "Nenhum emulador em execucao."
  exit 0
fi

for s in $serials; do
  adb -s "$s" emu kill || true
  echo "Emulador $s encerrado."
done

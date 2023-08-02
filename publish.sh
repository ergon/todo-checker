#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "${dir}"

version=$(./print-version.sh)
echo "Publishing ${version}"

./gradlew publish "-Pversion=${version}"

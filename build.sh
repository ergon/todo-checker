#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "${dir}"

version=$(./print-version.sh)
echo "Building ${version}"

./gradlew clean build "-Pversion=${version}"

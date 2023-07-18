#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "${dir}"

echo "Building ${dir}"

version=$(./print-version.sh)

./gradlew clean build "-PbuildVersion=${version}"

#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

# example: 55.0.0-1728-ge0f8e63885
describe_version=$(git describe --abbrev=9 --first-parent)
echo "$describe_version"

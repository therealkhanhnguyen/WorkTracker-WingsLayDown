#!/usr/bin/env bash
set -e

# create branch history file
git log --oneline --decorate --graph --all > branch-history.txt

# zip everything except common junk
zip -r worktracker-d424-submission.zip . \
  -x "*.git*" \
  -x "node_modules/*" \
  -x "dist/*" \
  -x "target/*" \
  -x "*.DS_Store"

echo "Created:"
echo " - branch-history.txt"
echo " - worktracker-d424-submission.zip"
#!/bin/bash
if [ -z "$1" ]; then
  echo "Provide a day number as input argument"
  echo "e.g. \"$0 1\""
  exit 1
fi

cd "$(dirname "$0")"

dayNumber=$1
if [ ${#dayNumber} -lt 2 ]; then
  dayNumber="0$dayNumber"
fi
sed -e "s/xx/$dayNumber/g" "Day_kt.template" > "src/Day$dayNumber.kt"
touch "src/Day$dayNumber.txt"
touch "src/Day${dayNumber}_test.txt"

#!/bin/sh
#
#  Copyright (c) 2016-2016 Marcus Rohrmoser http://mro.name/~me. All rights reserved.
#
#
cd "$(dirname "$0")"

src="JCurlShotPlanner.svg"

inkscape=/Applications/Inkscape.app/Contents/Resources/bin/inkscape

${inkscape} --help >/dev/null 2>&1 || { echo "Inkscape is not installed." && exit 1; }
pngquant --help >/dev/null 2>&1 || { echo "pngquant is not installed." && exit 1; }
optipng -help >/dev/null 2>&1 || { echo "optipng is not installed." && exit 1; }

pngquant="pngquant --skip-if-larger --speed 1"

OPTS=" --export-background='#00ff00ff' --without-gui"

width=150 ; height=300
dst="JCurlShotPlanner-${width}x${height}.png"
"${inkscape}" --export-png="$(pwd)/${dst}" --export-area-page --export-width=${width} --export-height=${height} $OPTS --file="$(pwd)/${src}"
${pngquant} "$(pwd)/${dst}" &

width=300 ; height=300
dst="JCurlShotPlanner-${width}x${height}.png"
"${inkscape}" --export-png="$(pwd)/${dst}" --export-area=17:1,398:213:197,398  --export-width=${width} --export-height=${height} $OPTS --file="$(pwd)/${src}"
${pngquant} "$(pwd)/${dst}" &

# dst=JCurlShotPlanner-o.svg
# cp "${src}" "${dst}"
# # http://stackoverflow.com/a/10492912
# ${inkscape} "$(pwd)/${dst}" \
#   --verb=FileVacuum --verb=FileSave \
#   --verb=FileClose --verb=FileQuit
# ${inkscape} --export-area-page $OPTS --vacuum-defs --export-plain-svg="$(pwd)/${dst}" --file="$(pwd)/${dst}"

wait

for i in *-fs8.png
do
  mv "${i}" "$(basename "${i}" -fs8.png).png"
done

for i in *.png
do
  optipng -o 7 "$i" &
done

wait

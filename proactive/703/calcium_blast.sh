#!/bin/sh

echo
echo --- Calcium example  ---------------------------------------------

workingDir=`dirname $0`
. ${workingDir}/../../env.sh

$JAVACMD org.objectweb.proactive.extensions.calcium.examples.blast.Blast "$@"

echo
echo ------------------------------------------------------------

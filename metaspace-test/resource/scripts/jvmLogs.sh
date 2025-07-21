#!/bin/bash

# Usage: ./jvmLogs.sh <jvm_port> <output_folder>
# Example: ./jvmLogs.sh 12345 ./output

PORT="$1"
OUTPUT_FOLDER="$2"

echo  "Starting JVM Metaspace, Heap Info, Class Hierarchy, and Classloaders Logging for port $PORT, output will be saved in $OUTPUT_FOLDER"

if [[ -z "$PORT" || -z "$OUTPUT_FOLDER" ]]; then
    echo "Usage: $0 <jvm_port> <output_folder>"
    exit 1
fi

mkdir -p "$OUTPUT_FOLDER"

while true; do
    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
    TIMESTAMP_FOLDER="$OUTPUT_FOLDER/$TIMESTAMP"
    mkdir -p "$TIMESTAMP_FOLDER"
    METASPACE_FILE="$TIMESTAMP_FOLDER/metaspace.txt"
    HEAPINFO_FILE="$TIMESTAMP_FOLDER/heapinfo.txt"
    CLASS_HIERARCHY_FILE="$TIMESTAMP_FOLDER/class_hierarchy.txt"
    CLASSLOADERS_FILE="$TIMESTAMP_FOLDER/classloaders.txt"
    jcmd "$PORT" VM.metaspace > "$METASPACE_FILE" 2>&1
    jcmd "$PORT" GC.heap_info > "$HEAPINFO_FILE" 2>&1
    jcmd "$PORT" VM.class_hierarchy > "$CLASS_HIERARCHY_FILE" 2>&1
    jcmd "$PORT" VM.classloaders > "$CLASSLOADERS_FILE" 2>&1
    sleep 300
done
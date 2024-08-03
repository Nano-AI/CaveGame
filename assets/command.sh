#!/bin/bash

# Loop through all .wav files in the current directory
for file in *.wav; do
    # Create a new file name with "_converted" suffix
    new_file="${file%.wav}_converted.wav"

    # Convert the file using FFmpeg
    ffmpeg -i "$file" -ar 44100 -ac 2 -sample_fmt s16 "$new_file"

    echo "Converted $file to $new_file"
done


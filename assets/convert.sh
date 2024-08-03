#!/bin/bash

# Loop through all .mp3 files in the current directory
for file in *.mp3; do
    # Create a new file name with .wav extension
    new_file="${file%.mp3}.wav"

    # Convert the file using FFmpeg
    ffmpeg -i "$file" -ar 44100 -ac 2 -sample_fmt s16 "$new_file"

    echo "Converted $file to $new_file"
done


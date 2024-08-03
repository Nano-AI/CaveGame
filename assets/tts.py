from gtts import gTTS
from pydub import AudioSegment
import os

def text_to_speech(text, output_filename):
    # Convert text to speech using gTTS and save as an MP3 file
    tts = gTTS(text, lang='en')
    mp3_filename = output_filename.replace('.wav', '.mp3')
    tts.save(mp3_filename)

    # Convert MP3 to WAV format
    sound = AudioSegment.from_mp3(mp3_filename)
    sound = sound.set_frame_rate(44100).set_channels(2).set_sample_width(2)  # 16-bit PCM
    sound.export(output_filename, format="wav")

    # Remove the temporary MP3 file
    os.remove(mp3_filename)

if __name__ == "__main__":
    lines = []
    while True:
        try:
            texts = input().replace("\n", "").replace(",", "")
        except EOFError:
            break
        if texts == "" or texts == "quit":
            break
        lines.append(texts)
    index = 0

    for text in lines:
        output_filename = "output" + str(index) + ".wav"
        text_to_speech(text, output_filename)
        index += 1
    print(f"Converted text to speech")

package DictionaryCommandLine.api;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.voicerss.tts.*;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.AudioCodec;
import com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory;
import javax.sound.sampled.*;
import java.io.*;

public class Speech {
    public static void UsualSpeech (String text) {
        VoiceManager vm = VoiceManager.getInstance();
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = vm.getVoice("kevin16");
        if(voice == null ) {
            System.out.println("ERROR!");
            return;
        }
        voice.allocate();
        voice.speak(text);
        voice.deallocate();
    }
    //Voice RSS
    public static void EnglishAPISpeech (String text) throws Exception {
        String APIKey = "dc35ca232f6e4969b7b7c8870c501642";
        VoiceProvider voiceProvider = new VoiceProvider(APIKey);
        VoiceParameters voiceParameters = new VoiceParameters(text,Languages.English_UnitedStates);

        voiceParameters.setCodec(AudioCodec.WAV);
        voiceParameters.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_mono);
        voiceParameters.setBase64(false);
        voiceParameters.setSSML(false);
        voiceParameters.setRate(0);

        byte[] voice = voiceProvider.speech(voiceParameters);

        try (AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(voice))) {
            javax.sound.sampled.AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(format);
            line.start();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = ais.read(buffer)) != -1) {
                line.write(buffer,0,bytesRead);
            }

            line.drain();
            line.stop();
            line.close();
        }
    }
    public static void VietnameseAPISpeech(String text) throws Exception {
        String APIKey = "dc35ca232f6e4969b7b7c8870c501642";
        VoiceProvider voiceProvider = new VoiceProvider(APIKey);
        VoiceParameters voiceParameters = new VoiceParameters(text, Languages.Vietnamese);

        voiceParameters.setCodec(AudioCodec.WAV);
        voiceParameters.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_mono);
        voiceParameters.setBase64(false);
        voiceParameters.setSSML(false);
        voiceParameters.setRate(0);

        byte[] voice = voiceProvider.speech(voiceParameters);

        try (AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(voice))) {
            javax.sound.sampled.AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(format);
            line.start();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = ais.read(buffer)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.stop();
            line.close();
        }
    }
}

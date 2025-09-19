/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7p2;

import javazoom.jl.decoder.*;
import javax.sound.sampled.*;
import java.io.*;

/**
 *
 * @author ljmc2
 */
public class Mp3toWAV {

    public static File convertMp3ToWav(File mp3File) throws Exception {
        File wavFile = new File("temp_songs/" + mp3File.getName().replace(".mp3", ".wav"));
        wavFile.getParentFile().mkdirs();

        try (InputStream in = new BufferedInputStream(new FileInputStream(mp3File))) {
            Bitstream bitstream = new Bitstream(in);
            Decoder decoder = new Decoder();

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                Header header;
                while ((header = bitstream.readFrame()) != null) {
                    SampleBuffer output = (SampleBuffer) decoder.decodeFrame(header, bitstream);
                    short[] buffer = output.getBuffer();
                    for(short s: buffer){
                        baos.write(s & 0xff);
                        baos.write((s >> 8) & 0xff);
                    }
                    bitstream.closeFrame();
                }

                byte[] audioBytes = baos.toByteArray();
                AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
                     AudioInputStream ais = new AudioInputStream(bais, format, audioBytes.length / format.getFrameSize())) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
                }
            }
        }
        return wavFile;
    }
}

package com.ruben.nba.alerts.utils;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class Transcoder {

    public void svnToPng (String originURL, String dest) throws IOException, TranscoderException {
        TranscoderInput transcoderInput = new TranscoderInput(originURL);

        OutputStream outputStream = new FileOutputStream(dest);
        TranscoderOutput transcoderOutput = new TranscoderOutput(outputStream);

        convert(transcoderInput, transcoderOutput);

        outputStream.flush();
        outputStream.close();
    }

    private void convert(TranscoderInput transcoderInput, TranscoderOutput transcoderOutput) throws TranscoderException {
        PNGTranscoder pngTranscoder = new PNGTranscoder();
        pngTranscoder.transcode(transcoderInput, transcoderOutput);
    }


}

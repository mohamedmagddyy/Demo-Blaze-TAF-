package comBlazedemoTests.media;

import comBlazedemoTests.utils.logs.LogsManager;
import comBlazedemoTests.utils.logs.TimeManager;
import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class ScreenRecordManager {

    private static final String RECORD_DIR = "test-output/recordings/";
    private static ScreenRecorder screenRecorder;

    public static void startRecording(String testName) {
        try {
            Files.createDirectories(Paths.get(RECORD_DIR));

            GraphicsConfiguration gc = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            screenRecorder = new ScreenRecorder(
                    gc,
                    gc.getBounds(),
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO,
                            EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24,
                            FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO,
                            EncodingKey, "black",
                            FrameRateKey, Rational.valueOf(30)),
                    null,
                    new File(RECORD_DIR)
            );

            screenRecorder.start();
            LogsManager.info("Screen recording started: " + testName);

        } catch (IOException | AWTException e) {
            LogsManager.error("Failed to start screen recording: " + testName, e);
        }
    }

    public static void stopRecording(String testName) {
        try {
            if (screenRecorder != null) {
                screenRecorder.stop();

                // ✅ Rename last recorded file to testName + timestamp
                File recordDir = new File(RECORD_DIR);
                File[] files = recordDir.listFiles((d, name) -> name.endsWith(".avi"));

                if (files != null && files.length > 0) {
                    File lastFile = files[files.length - 1];
                    File renamedFile = new File(RECORD_DIR + testName + "_" + TimeManager.getTimestamp() + ".avi");
                    lastFile.renameTo(renamedFile);
                    LogsManager.info("Screen recording saved: " + renamedFile.getName());
                }
            }
        } catch (IOException e) {
            LogsManager.error("Failed to stop screen recording: " + testName, e);
        }
    }
}
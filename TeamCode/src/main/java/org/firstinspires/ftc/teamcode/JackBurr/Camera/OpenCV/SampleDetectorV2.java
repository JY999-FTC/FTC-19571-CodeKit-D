package org.firstinspires.ftc.teamcode.JackBurr.Camera.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TeleOp
public class SampleDetectorV2 extends OpMode {
    public SampleDetectorToolkit toolkit;
    public SampleDetectorVisionPortalToolkit visionToolkit;
    public ColorBlobLocatorProcessor red;
    public ColorBlobLocatorProcessor neutral;
    public ColorBlobLocatorProcessor blue;
    public List<ColorBlobLocatorProcessor> processorList = new ArrayList<>();
    public List<ColorBlobLocatorProcessor.Blob> redList = new ArrayList<>();
    public List<ColorBlobLocatorProcessor.Blob> neutralList = new ArrayList<>();
    public List<ColorBlobLocatorProcessor.Blob> blueList = new ArrayList<>();
    public List<SampleDetection> masterList = new ArrayList<>();
    public VisionPortal portal;
    public int MIN_AREA = 500;
    public int MAX_AREA = 20000;
    public Point center;
    @Override
    public void init() {
        telemetry.setMsTransmissionInterval(50);
        toolkit = new SampleDetectorToolkit();
        visionToolkit = new SampleDetectorVisionPortalToolkit();
        red = toolkit.getNewProcessor(ColorRange.RED);
        neutral = toolkit.getNewProcessor(ColorRange.YELLOW);
        blue = toolkit.getNewProcessor(ColorRange.BLUE);
        processorList.add(red);
        processorList.add(neutral);
        processorList.add(blue);
        portal = visionToolkit.createVisionPortal(hardwareMap, processorList, "Webcam 1");
        center = toolkit.getCenter(320, 240);
    }

    @Override
    public void init_loop(){
        masterList.clear();
        redList = toolkit.filterByArea(MIN_AREA, MAX_AREA, red.getBlobs());
        neutralList = toolkit.filterByArea(MIN_AREA, MAX_AREA, neutral.getBlobs());
        blueList = toolkit.filterByArea(MIN_AREA, MAX_AREA, blue.getBlobs());
        masterList = toolkit.addToSampleDetectionList(masterList, ColorRange.RED, redList);
        masterList = toolkit.addToSampleDetectionList(masterList, ColorRange.YELLOW, neutralList);
        masterList = toolkit.addToSampleDetectionList(masterList, ColorRange.BLUE, blueList);
        Iterator<SampleDetection> iterator = masterList.iterator();
        while (iterator.hasNext()) {
            SampleDetection detection = iterator.next();
            if (!detection.exists) {
                iterator.remove();
                continue;
            }
            telemetry.addLine("Detected " + detection.color.toString() + " sample/specimen:");
            telemetry.addLine("\t Width: " + detection.width);
            telemetry.addLine("\t Height: " + detection.height);
            telemetry.addLine("\t Angle: " + detection.angle);
        }
    }

    @Override
    public void loop() {
        portal.stopStreaming();
    }
}

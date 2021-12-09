package org.firstinspires.ftc.teamcode.robots.reachRefactor.vision.providers.dummy;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robots.reachRefactor.vision.Position;
import org.firstinspires.ftc.teamcode.robots.reachRefactor.vision.VisionProvider;
import org.opencv.core.Mat;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDummyProvider implements VisionProvider {
    @Override
    public void initializeVision(HardwareMap hardwareMap) {

    }

    @Override
    public void shutdownVision() {

    }

    @Override
    public abstract Position getPosition();

    @Override
    public void reset() {

    }

    @Override
    public Map<String, Object> getTelemetry(boolean debug) {
        return new HashMap<>();
    }

    @Override
    public abstract String getTelemetryName();

    @Override
    public boolean canSendDashboardImage() {
        return false;
    }

    @Override
    public Mat getDashboardImage() {
        return null;
    }

    @Override
    public void update() {

    }
}

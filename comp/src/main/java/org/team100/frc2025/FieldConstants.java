package org.team100.frc2025;

import org.team100.lib.util.Util;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/** Add your docs here. */
public class FieldConstants {

    public enum FieldSector {
        AB(1),
        CD(2),
        EF(3),
        GH(4),
        IJ(5),
        KL(6);
    
        private final int value; // Field to store the number
    
        // Constructor
        FieldSector(int value) {
            this.value = value;
        }
    
        // Getter to retrieve the value
        public int getValue() {
            return value;
        }

        public static FieldSector fromValue(int value) {
            for (FieldSector sector : FieldSector.values()) {
                if (sector.getValue() == value) {
                    return sector;
                }
            }
            throw new IllegalArgumentException("Invalid sector number: " + value);
        }

    }

    public enum ReefDestination {
        CCW,
        CW,
        CENTER
    }

    public static FieldConstants.FieldSector getSector(Pose2d pose){
        Translation2d target = FieldConstants.getReefCenter().minus(pose.getTranslation());
        Rotation2d targetAngle = target.getAngle();
        double angle = targetAngle.getDegrees();

        Util.println("ANGLEEEE" + angle);

        if(angle >= -30 && angle <= 30){
            return FieldSector.AB;
        } else if(angle >= -90 && angle <-30){
            return FieldSector.KL;
        } else if(angle >= -150 && angle <= -90){
            return FieldSector.IJ;
        } else if(angle >= 150 || angle <= -150){
            return FieldSector.GH;
        } else if(angle >= 90 && angle <= 150){
            return FieldSector.EF;
        } else if(angle >= 30 && angle <= 90){
            return FieldSector.CD;
        } else {
            return FieldSector.AB;
        }
    }

    public static Rotation2d getSectorAngle(FieldSector sector){
        switch(sector){
            case AB:
                return Rotation2d.fromDegrees(180);
            case CD:
                return Rotation2d.fromDegrees(-120);
            case EF:
                return Rotation2d.fromDegrees(-60);
            case GH:
                return Rotation2d.fromDegrees(0);
            case IJ:
                return Rotation2d.fromDegrees(60);
            case KL:
                return Rotation2d.fromDegrees(120);
            default:
                return Rotation2d.fromDegrees(0);
        }
    }

    public static Rotation2d getLandingAngleCCW(FieldSector sector){
        switch(sector){
            case AB:
                return Rotation2d.fromDegrees(-90);
            case CD:
                return Rotation2d.fromDegrees(-30);
            case EF:
                return Rotation2d.fromDegrees(30);
            case GH:
                return Rotation2d.fromDegrees(90);
            case IJ:
                return Rotation2d.fromDegrees(150);
            case KL:
                return Rotation2d.fromDegrees(210);
            default:
                return Rotation2d.fromDegrees(0);
        }
    }

    public static double getDistanceToReefCenter(Pose2d pose){
        Translation2d target = FieldConstants.getReefCenter().minus(pose.getTranslation());
        return target.getNorm();
    }

    public static double getDistanceToReefCenter(Translation2d translation){
        Translation2d target = FieldConstants.getReefCenter().minus(translation);
        return target.getNorm();
    }


    public static Rotation2d angleToReefCenter(Pose2d pose){
        Translation2d target = FieldConstants.getReefCenter().minus(pose.getTranslation());
        Rotation2d targetAngle = target.getAngle();
        return targetAngle;
    }

    public static Translation2d getReefCenter(){ //blue
        return new Translation2d(4.508405, 4.038690);
    }

    public static double getOrbitWaypointRadius(){
        return 2.1; //2.4
    }

    public static double getOrbitDestinationRadius(){
        return 1.8;
    }

    public static double getReefOffset(){
        return 0.1524; //0.1524
    }

    public static Translation2d getOrbitWaypoint(Rotation2d angle){
        Translation2d reefCenter = getReefCenter();
        double x = reefCenter.getX() + getOrbitWaypointRadius() * angle.getCos();
        double y = reefCenter.getY() + getOrbitWaypointRadius() * angle.getSin();

        return new Translation2d(x, y);

    }

    public static Translation2d getOrbitWaypoint(FieldSector sector){
        Translation2d reefCenter = getReefCenter();
        double x = reefCenter.getX() + getOrbitWaypointRadius() * getSectorAngle(sector).getCos();
        double y = reefCenter.getY() + getOrbitWaypointRadius() * getSectorAngle(sector).getSin();

        return new Translation2d(x, y);

    }

    public static Translation2d getOrbitLandingZone(FieldSector destinationSector, ReefDestination destinationPoint){
        Translation2d reefCenter = getReefCenter();
        Rotation2d sectorAngle = getSectorAngle(destinationSector);

        

        double x = reefCenter.getX() + getOrbitDestinationRadius() * sectorAngle.getCos();
        double y = reefCenter.getY() + getOrbitDestinationRadius() * sectorAngle.getSin();


        double dx = 0;
        double dy = 0;

        Rotation2d newRotation = Rotation2d.fromDegrees(sectorAngle.getDegrees() + 90);
        dy = (1.0 * Math.sin(newRotation.getRadians()));
        dx = (1.0 * Math.cos(newRotation.getRadians()));

        
        switch(destinationPoint){
            case CW:
                return new Translation2d(x += dx, y += dy);
            case CCW:
                return new Translation2d(x -= dx, y -= dy);
            case CENTER:
                return new Translation2d(x, y);
        
        }

        return new Translation2d(x, y);

    }

    public static Translation2d getOrbitDestination(FieldSector destinationSector, ReefDestination destinationPoint){
        Translation2d reefCenter = getReefCenter();
        Rotation2d sectorAngle = getSectorAngle(destinationSector);

        

        double x = reefCenter.getX() + getOrbitDestinationRadius() * sectorAngle.getCos();
        double y = reefCenter.getY() + getOrbitDestinationRadius() * sectorAngle.getSin();


        double dx = 0;
        double dy = 0;
        
        Rotation2d newRotation = Rotation2d.fromDegrees(sectorAngle.getDegrees() + 90);
        dy = (getReefOffset() * Math.sin(newRotation.getRadians()));
        dx = (getReefOffset() * Math.cos(newRotation.getRadians()));

        
        switch(destinationPoint){
            case CW:
                return new Translation2d(x += dx, y += dy);
            case CCW:
                return new Translation2d(x -= dx, y -= dy);
            case CENTER:
                return new Translation2d(x, y);
        
        }

        return new Translation2d(x, y);

    }

    public static List<Integer> findShortestPath(int start, int target) {
        List<Integer> pathClockwise = new ArrayList<>();
        List<Integer> pathCounterClockwise = new ArrayList<>();

        // Clockwise path
        int side = start;
        while (side != target) {
            pathCounterClockwise.add(side);
            side = (side % 6) + 1; // Move clockwise
        }
        pathCounterClockwise.add(target); // Add target side

        // Counterclockwise path
        side = start;
        while (side != target) {
            pathClockwise.add(side);
            side = (side - 2 + 6) % 6 + 1; // Move counterclockwise
        }
        pathClockwise.add(target); // Add target side

        // Return the shorter path

        if(pathClockwise.size() < pathCounterClockwise.size()){
            return pathClockwise;
        } else{
            return pathCounterClockwise;
        }
        // return (pathClockwise.size() < pathCounterClockwise.size()) ? pathClockwise : pathCounterClockwise;
    }
}

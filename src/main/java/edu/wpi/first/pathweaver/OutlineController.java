package edu.wpi.first.pathweaver;

import java.util.ArrayList;
import edu.wpi.first.pathweaver.Waypoint.RobotOutline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class OutlineController {
    private static ArrayList<RobotOutline> outlineList = new ArrayList<RobotOutline>();
    private static final Circle smallCircle = new Circle();
    private static final Circle largeCircle = new Circle();
    private static ProjectPreferences.Values values = ProjectPreferences.getInstance().getValues();
    private static String lastGameName = values.getGameName();
    private static boolean circlesSetUp = false;

    public static void setupCircles() {
		smallCircle.setCenterX(
				(Game.fromPrettyName(values.getGameName()).getField().getRealWidth().getValue().doubleValue())/2);
		largeCircle.setCenterX(
				(Game.fromPrettyName(values.getGameName()).getField().getRealWidth().getValue().doubleValue())/2);
		smallCircle.setCenterY(
				(Game.fromPrettyName(values.getGameName()).getField().getRealLength().getValue().doubleValue())/2);
		largeCircle.setCenterY(
				(Game.fromPrettyName(values.getGameName()).getField().getRealLength().getValue().doubleValue())/2);
		smallCircle.setRadius(values.getSmallRange());
		largeCircle.setRadius(values.getLargeRange());
		FxUtils.applySubchildClasses(smallCircle);
		FxUtils.applySubchildClasses(largeCircle);
		smallCircle.getStyleClass().add("greenOutline");
		largeCircle.getStyleClass().add("greenOutline");
		smallCircle.toFront();
		largeCircle.toFront();
		smallCircle.setStrokeWidth(5 / ProjectPreferences.getInstance().getField().getScale());
		largeCircle.setStrokeWidth(5 / ProjectPreferences.getInstance().getField().getScale());
		smallCircle.applyCss();
		largeCircle.applyCss();
        circlesSetUp = true;
	}

    public static void setupRobotOutline(RobotOutline outline) {
        double robotWidth = values.getRobotWidth();
        double robotLength = values.getRobotLength();
        outline.setHeight(robotWidth);
        outline.setWidth(robotLength);
        outline.xProperty().bind(outline.getWaypoint().xProperty().subtract((robotLength/2)));
        outline.yProperty().bind(outline.getWaypoint().yProperty().add((robotWidth/2)).negate());
        FxUtils.applySubchildClasses(outline);
        outline.rotateProperty()
                .bind(Bindings.createObjectBinding(
                        () -> outline.getWaypoint().getTangent() == null ? 0.0
                                : Math.toDegrees(
                                        Math.atan2(-outline.getWaypoint().getTangentY(),
                                                outline.getWaypoint().getTangentX())),
                        outline.getWaypoint().tangentXProperty(), outline.getWaypoint().tangentYProperty()));
        outline.setStrokeWidth(5 / ProjectPreferences.getInstance().getField().getScale());
        outline.getStyleClass().add("robotOutline");
        outlineList.forEach(
            (o) -> {
                Point2D point = new Point2D(o.getX(), o.getY());
                Point2D otherPoint = new Point2D(outline.getX(), outline.getY());
                if(point.equals(otherPoint)) {
                    outline.isInList = true;
                    System.out.println("the outline at (" + 
                            String.format("%.3f", otherPoint.getX()) + " , " + 
                            String.format("%.3f", otherPoint.getY()) +
                            ") is already in the list at index " + outlineList.indexOf(o));
                }
            }
        );
        if(!outline.isInList) {
            outlineList.add(outline);
            outline.isInList = true;
            System.out.println("successfully added outline to group at index " 
                    + outlineList.indexOf(outline));
        }
        outline.setup = true;
    }

    public static boolean getOutlineSetup(RobotOutline outline) {
        return outline.setup;
    }

    public void updateValues(ProjectPreferences.Values newValues) {
		values = newValues;
        updateOutlines();
	}

    public static boolean newPrefValues() {
        ProjectPreferences.Values oldValues = values;
        values = ProjectPreferences.getInstance().getValues();
        return !(oldValues == values);
    }

	public static void updateOutlines() {
        newPrefValues();
        smallCircle.setRadius(values.getSmallRange());
		largeCircle.setRadius(values.getLargeRange());
        // If the game has changed, the field size may have too. Change the circle centers to the center of the new field.
        if(lastGameName != values.getGameName()) {
            smallCircle.setCenterX(
				    (Game.fromPrettyName(values.getGameName()).getField().getRealWidth().getValue().doubleValue())/2);
            largeCircle.setCenterX(
                    (Game.fromPrettyName(values.getGameName()).getField().getRealWidth().getValue().doubleValue())/2);
            smallCircle.setCenterY(
                    (Game.fromPrettyName(values.getGameName()).getField().getRealLength().getValue().doubleValue())/2);
            largeCircle.setCenterY(
                    (Game.fromPrettyName(values.getGameName()).getField().getRealLength().getValue().doubleValue())/2);
            lastGameName = values.getGameName();
        }
	}

    public static ArrayList<RobotOutline> getOutlineList() {
        return outlineList;
    }

	public static Circle getSmallCircle() {
        if(!circlesSetUp) {
            setupCircles();
        }
		return smallCircle;
	}

	public static Circle getBigCircle() {
        if(!circlesSetUp) {
            setupCircles();
        }
		return largeCircle;
	}
}

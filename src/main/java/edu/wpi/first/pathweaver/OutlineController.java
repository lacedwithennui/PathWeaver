package edu.wpi.first.pathweaver;

import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class OutlineController {
    private ArrayList<Rectangle> rectangleGroup = new ArrayList<Rectangle>();
    private ArrayList<Waypoint> waypointGroup = new ArrayList<Waypoint>();
    private static final Circle smallCircle = new Circle();
    private static final Circle largeCircle = new Circle();
    private static ProjectPreferences.Values values = ProjectPreferences.getInstance().getValues();
    private String lastGameName = values.getGameName();

    public OutlineController() {}

    public OutlineController(ArrayList<Waypoint> waypointGroup) {
        this.waypointGroup = waypointGroup;
        setupCircles();
        // setupRobotOutline();
    }

    // public class RobotOutline extends Rectangle {
    //     private Waypoint waypoint;

    //     public RobotOutline(Waypoint waypoint) {
    //         this.waypoint = waypoint;
    //     }

    //     public void setupOutline() {
    //         double robotWidth = values.getRobotWidth();
    //     	double robotLength = values.getRobotLength();
    //     	setHeight(robotWidth);
    //     	setWidth(robotLength);
    //     	translateXProperty().bind(xProperty().subtract((robotLength/2)));
    //     	translateYProperty().bind(yProperty().add((robotWidth/2)).negate());
    //     	FxUtils.applySubchildClasses(this);
    //     	rotateProperty()
    //     			.bind(Bindings.createObjectBinding(
    //     					() -> waypoint.getTangent() == null ? 0.0 : Math.toDegrees(Math.atan2(-waypoint.getTangentY(), waypoint.getTangentX())),
    //                         waypoint.tangentXProperty(), waypoint.tangentYProperty()));
    //     	getStyleClass().add("robotOutline");
    //     }

    //     public Waypoint getWaypoint() {
    //         return waypoint;
    //     }
    // }

    public void setupCircles() {
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
	}

    // private void setupRobotOutline() {
    //     double robotWidth = values.getRobotWidth();
    //     double robotLength = values.getRobotLength();
    //     for(int i = 0; i < waypointGroup.size(); i++) {
    //         Waypoint waypoint = waypointGroup.get(i);
    //         rectangleGroup.add(new Rectangle());
    //         rectangleGroup.get(i).setHeight(robotWidth);
    //         rectangleGroup.get(i).setWidth(robotLength);
    //         rectangleGroup.get(i).translateXProperty().bind(waypoint.xProperty().subtract((robotLength/2)));
    //         rectangleGroup.get(i).translateYProperty().bind(waypoint.yProperty().add((robotWidth/2)).negate());
    //         FxUtils.applySubchildClasses(rectangleGroup.get(i));
    //         rectangleGroup.get(i).rotateProperty()
    //                 .bind(Bindings.createObjectBinding(
    //                         () -> waypoint.getTangent() == null ? 0.0 : Math.toDegrees(Math.atan2(-waypoint.getTangentY(), waypoint.getTangentX())),
    //                         waypoint.tangentXProperty(), waypoint.tangentYProperty()));
    //         rectangleGroup.get(i).getStyleClass().add("robotOutline");
	//     }
    // }

    public void updateValues(ProjectPreferences.Values newValues) {
		values = newValues;
        updateOutlines();
	}

	public static void updateOutlines() {
        double robotWidth = values.getRobotWidth();
        double robotLength = values.getRobotLength();
		// for(Rectangle rectangle : rectangleGroup) {
		// 	rectangle.setHeight(robotWidth);
        //     rectangle.setWidth(robotLength);
        //     // TODO: check if these need changing
        //     // rectangle.translateXProperty().bind(waypoint.xProperty().subtract((robotLength/2)));
        //     // rectangle.translateYProperty().bind(waypoint.yProperty().add((robotWidth/2)).negate());
		// }
        // smallCircle.setRadius(values.getSmallRange());
		// largeCircle.setRadius(values.getLargeRange());
        // // If the game has changed, the field size may have too. Change the circle centers to the center of the new field.
        // if(lastGameName != values.getGameName()) {
        //     smallCircle.setCenterX(
		// 		    (Game.fromPrettyName(values.getGameName()).getField().getRealWidth().getValue().doubleValue())/2);
        //     largeCircle.setCenterX(
        //             (Game.fromPrettyName(values.getGameName()).getField().getRealWidth().getValue().doubleValue())/2);
        //     smallCircle.setCenterY(
        //             (Game.fromPrettyName(values.getGameName()).getField().getRealLength().getValue().doubleValue())/2);
        //     largeCircle.setCenterY(
        //             (Game.fromPrettyName(values.getGameName()).getField().getRealLength().getValue().doubleValue())/2);
        //     lastGameName = values.getGameName();
        // }
	}

    public ArrayList<Waypoint> getWaypointList() {
		return waypointGroup;
	}

    public Group getOutlineGroup() {
        Group outlineGroup = new Group();
        for (Rectangle rectangle : rectangleGroup) {
            outlineGroup.getChildren().add(rectangle);
        }
        return outlineGroup;
    }

    public ArrayList<Rectangle> getOutlineList() {
        return rectangleGroup;
    }

	public static Circle getSmallCircle() {
		return smallCircle;
	}

	public static Circle getBigCircle() {
		return largeCircle;
	}

    public Rectangle getOutline(int index) {
        return rectangleGroup.get(index);
    }

    public Rectangle getOutline(Waypoint wp) {
        return wp.getOutline();
    }
}

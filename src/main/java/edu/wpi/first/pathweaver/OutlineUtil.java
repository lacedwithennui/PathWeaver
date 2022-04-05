package edu.wpi.first.pathweaver;

import edu.wpi.first.pathweaver.Waypoint.RobotOutline;
import javafx.beans.binding.Bindings;
import javafx.scene.shape.Circle;

public class OutlineUtil {
    private static final Circle smallCircle = new Circle();
    private static final Circle largeCircle = new Circle();
    private static ProjectPreferences.Values values = ProjectPreferences.getInstance().getValues();
    private static String lastGameName = values.getGameName();
    private static boolean circlesSetUp = false;

    /**
     * <ul>
     *  <li> Applies the following properties to the OutlineUtil circles: <ul>
     *      <li>Centerpoint</li>
     *      <li>Radius</li>
     *      <li>JavaFX classes</li>
     *      <li>Style classes</li>
     *      <li>line weight</li>
     * </ul>
     * </ul>
     */
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

    /**
     * <ul>
     *  <li> Applies the following properties to a given {@link RobotOutline}: <ul>
     *      <li>Height</li>
     *      <li>Width</li>
     *      <li>x property binding</li>
     *      <li>y property binding</li>
     *      <li>rotate property binding</li>
     *      <li>JavaFX classes</li>
     *      <li>Style classes</li>
     *      <li>line weight</li>
     * </ul>
     * </ul>
     */
    public static void setupRobotOutline(RobotOutline outline) {
        double robotWidth = values.getRobotWidth();
        double robotLength = values.getRobotLength();
        outline.setHeight(robotWidth);
        outline.setWidth(robotLength);
        outline.xProperty().bind(outline.getWaypoint().xProperty().subtract((robotLength/2)));
        outline.yProperty().bind(outline.getWaypoint().yProperty().add((robotWidth/2)).negate());
        outline.rotateProperty()
                .bind(Bindings.createObjectBinding(
                        () -> outline.getWaypoint().getTangent() == null ? 0.0
                                : Math.toDegrees(
                                        Math.atan2(-outline.getWaypoint().getTangentY(),
                                                outline.getWaypoint().getTangentX())),
                        outline.getWaypoint().tangentXProperty(), outline.getWaypoint().tangentYProperty()));
        FxUtils.applySubchildClasses(outline);
        outline.getStyleClass().add("robotOutline");
        outline.setStrokeWidth(5 / ProjectPreferences.getInstance().getField().getScale());
        outline.setup = true;
    }

    /**
     * Gets the project's {@link ProjectPreferences.Values} 
     * @return true if the values in the project are newer than the ones saved in OutlineUtil
     */
    public static boolean newPrefValues() {
        ProjectPreferences.Values oldValues = values;
        values = ProjectPreferences.getInstance().getValues();
        return !(oldValues == values);
    }

    /**
     * Updates the radius and centerpoint properties of the OutlineUtil circles with new ProjectPreferences values
     */
	public static void updateOutlines() {
        newPrefValues();
        smallCircle.setRadius(values.getSmallRange());
		largeCircle.setRadius(values.getLargeRange());
        // If the game has changed, the field size may have too. Change the circle centers to the center of the new field.
        if(lastGameName != values.getGameName()) {
            lastGameName = values.getGameName();
            smallCircle.setCenterX(
				    (Game.fromPrettyName(values.getGameName()).getField().getRealWidth().getValue().doubleValue())/2);
            largeCircle.setCenterX(
                    (Game.fromPrettyName(values.getGameName()).getField().getRealWidth().getValue().doubleValue())/2);
            smallCircle.setCenterY(
                    (Game.fromPrettyName(values.getGameName()).getField().getRealLength().getValue().doubleValue())/2);
            largeCircle.setCenterY(
                    (Game.fromPrettyName(values.getGameName()).getField().getRealLength().getValue().doubleValue())/2);
        }
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

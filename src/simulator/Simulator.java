package simulator;

import javafx.animation.Transition;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import map.Map;

public class Simulator {

	private class AnimationController extends Transition {

		Map m;
		GraphicsContext gc;
		public AnimationController(Map m, GraphicsContext gc) {
			super();
			this.setCycleDuration(Duration.INDEFINITE);
			this.m = m;
			this.gc = gc;
		}
		
		
		@Override
		protected void interpolate(double arg0) {
			// TODO Auto-generated method stub
			m.draw(gc);
		}
	
	}
	
	private AnimationController animationController;
	public static final double MAX_RATE = 4.0;
	public static final double MIN_RATE = 0.25;
	
	public Simulator(Map m, GraphicsContext gc) {
		
		animationController = new AnimationController(m, gc);
		
	}
	
	
	public void doubleRate() {
		animationController.setRate(Math.min(MAX_RATE, animationController.getCurrentRate() * 2));
	}
	
	
	public void halfRate() {
		animationController.setRate(Math.max(MIN_RATE, animationController.getCurrentRate() / 2));
	}
	
	public void pause() {
		animationController.pause();
	}
	
	public void play() {
		animationController.play();
	}
	
	public void stop() {
		animationController.stop();
	}
	
}
	

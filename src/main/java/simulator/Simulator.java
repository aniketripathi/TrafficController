package main.java.simulator;

import javafx.animation.Transition;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import main.java.engine.Updator;
import main.java.entities.Car;
import main.java.map.Map;

public class Simulator {

	private class AnimationController extends Transition {

		Updator updator;
		public AnimationController(Updator updator) {
			super(); 
			this.setCycleDuration(Duration.INDEFINITE);
			
			this.updator = updator;
		}
		
		
		@Override
		protected void interpolate(double arg0) {
			updator.update();
			
			
		}
	
	}
	
	private AnimationController animationController;
	public static final double MAX_RATE = 4.0;
	public static final double MIN_RATE = 0.25;
	
	public Simulator(Updator updator) {
		
		animationController = new AnimationController(updator);
		
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
	

package main.java.data.recorder;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class RoadCount implements ChangeListener<Number> {

	private final int numberOfLanes;
	private ForwardLaneCount[] forwardLanesCount;
	private BackwardLaneCount[] backwardLanesCount;
	private LongProperty total;
	private LongProperty totalGenerated;
	private LongProperty totalDestroyed;

	public RoadCount(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
		forwardLanesCount = new ForwardLaneCount[numberOfLanes];
		backwardLanesCount = new BackwardLaneCount[numberOfLanes];
		total = new SimpleLongProperty();
		totalGenerated = new SimpleLongProperty();
		totalDestroyed = new SimpleLongProperty();

		for (int i = 0; i < numberOfLanes; i++) {
			forwardLanesCount[i] = new ForwardLaneCount();
			backwardLanesCount[i] = new BackwardLaneCount();
			forwardLanesCount[i].getGeneratedCount().getTotalProperty().addListener((obs, oldVal, newVal) -> {
				totalGenerated.set(totalGenerated.get() - oldVal.longValue() + newVal.longValue());
			});
			backwardLanesCount[i].getDestroyedCount().getTotalProperty().addListener((obs, oldVal, newVal) -> {
				totalGenerated.set(totalDestroyed.get() - oldVal.longValue() + newVal.longValue());
			});
		}
		totalGenerated.addListener(this);
		totalDestroyed.addListener(this);

	}

	public void reset() {
		for (int i = 0; i < numberOfLanes; i++) {
			forwardLanesCount[i].reset();
			backwardLanesCount[i].reset();
		}
		total.set(0);
		totalGenerated.set(0);
		totalDestroyed.set(0);

	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	protected LongProperty getTotalProperty() {
		return total;
	}

	public ForwardLaneCount getForwardLaneCount(int i) {
		return forwardLanesCount[i];
	}

	public BackwardLaneCount getBackwardLaneCount(int i) {
		return backwardLanesCount[i];
	}

	protected LongProperty getTotalGeneratedProperty() {
		return totalGenerated;
	}

	protected LongProperty getTotalDestroyedProperty() {
		return totalDestroyed;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		total.set(totalGenerated.get() + totalDestroyed.get());

	}

}

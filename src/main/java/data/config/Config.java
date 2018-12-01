package main.java.data.config;

import java.io.IOException;
import java.nio.file.Path;

import main.java.exceptions.InvalidConfigException;
import main.java.exceptions.ProbabilityException;
import main.java.map.Map;

public class Config {

	private RoadProperty roads[];

	public Config(Config config) {
		roads = new RoadProperty[Map.NUMBER_OF_ROADS];
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			roads[i] = new RoadProperty(config.getRoadProperty(i));
		}
	}

	public Config(int numberOfLanes) {
		roads = new RoadProperty[Map.NUMBER_OF_ROADS];
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			roads[i] = new RoadProperty(numberOfLanes);
		}
	}

	public void reset() {
		for (RoadProperty road : roads) {
			road.reset();
		}
	}

	public RoadProperty getRoadProperty(int index) {
		return roads[index];
	}

	public void validate() throws ProbabilityException {
		for (RoadProperty road : roads)
			road.validate();
	}

	public void loadFromFile(Path file) throws IOException, InvalidConfigException {
		ConfigReader.readFromFile(file, this);

	}

	public void saveToFile(Path file) throws IOException {
		ConfigWriter.writeToPath(file, this);
	}

}

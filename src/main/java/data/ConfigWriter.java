package main.java.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import main.java.map.Map;

public class ConfigWriter {

	public static final String CAR_PROBABILITY = "carProbability";
	public static final String TWO_WHEELER_PROBABILITY = "twoWheelerProbability";
	public static final String HEAVY_VEHICLE_PROBABILITY = "heavyVehicleProbability";
	public static final String END_LANE = "endLane";
	public static final String END_ROAD = "endRoad";
	public static final String LANE_INDEX = "laneIndex";
	public static final String ROAD = "road";
	public static final String ROAD_INDEX = "roadIndex";
	public static final String RATE = "rate";
	public static final String NUMBER_OF_LANES = "numberOfLanes";
	public static final String TERMINAL = ";";
	public static final String SEPARATOR = "=";

	public static List<String> laneWriter(int index, Config.LaneProperty laneProperty) {
		List<String> list = new LinkedList<String>();

		list.add(LANE_INDEX + SEPARATOR + index + TERMINAL);
		list.add(CAR_PROBABILITY + SEPARATOR + laneProperty.getCarProbability() + TERMINAL);
		list.add(TWO_WHEELER_PROBABILITY + SEPARATOR + laneProperty.getTwoWheelerProbability() + TERMINAL);
		list.add(HEAVY_VEHICLE_PROBABILITY + SEPARATOR + laneProperty.getHeavyVehicleProbability() + TERMINAL);
		list.add(RATE + SEPARATOR + laneProperty.getRate() + TERMINAL);
		list.add(END_LANE + TERMINAL);
		return list;
	}

	public static List<String> roadWriter(int index, Config.RoadProperty roadProperty) {

		List<String> list = new LinkedList<String>();
		list.add(ROAD_INDEX + SEPARATOR + index + TERMINAL);
		list.add(NUMBER_OF_LANES + SEPARATOR + roadProperty.getNumberOfLanes() + TERMINAL);

		for (int i = 0; i < roadProperty.getNumberOfLanes(); i++)
			list.addAll(laneWriter(i, roadProperty.getLaneProperty(i)));

		list.add(END_ROAD + TERMINAL + System.lineSeparator());

		return list;

	}

	public static List<String> writeToList(Config config) {
		List<String> list = new LinkedList<String>();

		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			list.addAll(roadWriter(i, config.getRoadProperty(i)));
		}
		return list;
	}

	public static void writeToPath(Path path, Config config) throws IOException {

		if (!Files.exists(path)) {
			Files.createFile(path);
		}

		if (Files.isWritable(path)) {
			Files.write(path, writeToList(config), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} else
			throw new IOException("File is not writable.");

	}

}

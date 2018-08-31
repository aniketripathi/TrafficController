package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;

import data.Config.LaneProperty;
import data.Config.RoadProperty;
import exceptions.InvalidConfigException;

public class ConfigReader {

	private static void validateList(List<String> list) throws InvalidConfigException {

		for (String stm : list) {
			if (!stm.endsWith(ConfigWriter.TERMINAL))
				throw new InvalidConfigException(InvalidConfigException.INVALID_STATEMENT_MESSAGE);
		}
	}

	public static void laneReader(LaneProperty laneProperty, List<String> list) throws InvalidConfigException {
		ListIterator<String> iterator = list.listIterator();

		try {

			while (iterator.hasNext()) {

				String stmt = iterator.next();

				if (stmt.equalsIgnoreCase(ConfigWriter.END_LANE)) {
					break;
				}

				else if (stmt.toLowerCase().startsWith(ConfigWriter.CAR_PROBABILITY.toLowerCase())) {
					double carProbability = Double
							.parseDouble(stmt.substring(stmt.indexOf(ConfigWriter.SEPARATOR) + 1));
					laneProperty.setCarProbability(carProbability);
				}

				else if (stmt.toLowerCase().startsWith(ConfigWriter.TWO_WHEELER_PROBABILITY.toLowerCase())) {
					double twoWheelerProbability = Double
							.parseDouble(stmt.substring(stmt.indexOf(ConfigWriter.SEPARATOR) + 1));
					laneProperty.setTwoWheelerProbability(twoWheelerProbability);
				}

				else if (stmt.toLowerCase().startsWith(ConfigWriter.HEAVY_VEHICLE_PROBABILITY.toLowerCase())) {
					double heavyVehicleProbability = Double
							.parseDouble(stmt.substring(stmt.indexOf(ConfigWriter.SEPARATOR) + 1));
					laneProperty.setHeavyVehicleProbability(heavyVehicleProbability);
				}

				else if (stmt.toLowerCase().startsWith(ConfigWriter.RATE.toLowerCase())) {
					double rate = Double.parseDouble(stmt.substring(stmt.indexOf(ConfigWriter.SEPARATOR) + 1));
					laneProperty.setRate(rate);
				}

			}
		} catch (NumberFormatException e) {
			throw new InvalidConfigException(InvalidConfigException.INVALID_VALUE_MESSAGE);
		}

	}

	public static void roadReader(RoadProperty roadProperty, List<String> list) throws InvalidConfigException {
		ListIterator<String> iterator = list.listIterator();

		try {
			while (iterator.hasNext()) {
				String stmt = iterator.next();
				if (stmt.equalsIgnoreCase(ConfigWriter.END_ROAD)) {
					break;
				}

				if (stmt.toLowerCase().startsWith(ConfigWriter.LANE_INDEX.toLowerCase())) {
					int laneIndex = Integer.parseInt(stmt.substring(stmt.indexOf(ConfigWriter.SEPARATOR) + 1));
					LaneProperty laneProperty = roadProperty.getLaneProperty(laneIndex);
					laneReader(laneProperty, list.subList(iterator.nextIndex(), list.size()));
				}

				else if (stmt.toLowerCase().startsWith(ConfigWriter.RATE.toLowerCase())) {
					double rate = Double.parseDouble(stmt.substring(stmt.indexOf(ConfigWriter.SEPARATOR) + 1));
					roadProperty.setRate(rate);
				}
			}
		} catch (NumberFormatException e) {
			throw new InvalidConfigException(InvalidConfigException.INVALID_VALUE_MESSAGE);
		}
	}

	public static void reader(Config config, List<String> list) throws InvalidConfigException {

		validateList(list);

		ListIterator<String> iterator = list.listIterator();

		while (iterator.hasNext()) {

			String stmt = iterator.next();

			if (stmt.equalsIgnoreCase(ConfigWriter.TOP_ROAD)) {
				roadReader(config.getTopRoadProperty(), list.subList(iterator.nextIndex(), list.size()));
			} else if (stmt.equalsIgnoreCase(ConfigWriter.BOTTOM_ROAD)) {
				roadReader(config.getBottomRoadProperty(), list.subList(iterator.nextIndex(), list.size()));
			}

			else if (stmt.equalsIgnoreCase(ConfigWriter.TOP_ROAD)) {
				roadReader(config.getBottomRoadProperty(), list.subList(iterator.nextIndex(), list.size()));
			}

			else if (stmt.equalsIgnoreCase(ConfigWriter.TOP_ROAD)) {
				roadReader(config.getBottomRoadProperty(), list.subList(iterator.nextIndex(), list.size()));
			}
		}
	}

	public static void readFromFile(Path path, Config config) throws IOException, InvalidConfigException {
		if (Files.exists(path) && Files.isReadable(path)) {
			reader(config, Files.readAllLines(path));
		} else
			throw new IOException("Either file does not exist or is not readable.");

	}
}
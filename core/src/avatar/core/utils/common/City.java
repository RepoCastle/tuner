package avatar.core.utils.common;

import java.util.HashMap;

import avatar.core.model.common.Point;

public class City {
	private String name;
	private Point landmark;
	private double earthRadius;
	private String mapfile;
	
	public City(String name, Point landmark, double earthRadius) {
		this.name = name;
		this.landmark = landmark;
		this.earthRadius = earthRadius;
		this.mapfile = Constant.RegMapDir + "/" + this.name + ".region";
	}
	
	public String getMapfile() {
		return mapfile;
	}

	public String getName() {
		return name;
	}
	public Point getLandmark() {
		return landmark;
	}
	public double getEarthRadius() {
		return earthRadius;
	}
	
	public double distance2size(double distance) {
		return distance * 180 / Math.PI / earthRadius;
	}
	
	public double size2degree(double size) {
		return size * earthRadius * Math.PI / 180;
	}
	
	public static City get(String cityname) {
		return cities.get(cityname);
	}
	

	public static final int RADIUS_SH = 6372774;
	public static final int RADIUS_A = 6372774;
	@SuppressWarnings("serial")
	public static HashMap<String, City> cities = new HashMap<String, City>() {{
		put("shanghai", new City("shanghai", new Point(121.468186, 31.23139), RADIUS_SH));
		put("beijing", new City("beijing", new Point(116.39688, 39.91431), RADIUS_A));
		put("shenzhen", new City("shenzhen", new Point(113.832780,22.592990), RADIUS_A));
	}};
}

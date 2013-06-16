package fr.xebia.yawyl.model;

import java.util.Date;

public class Concert {

	public final String artistName;

	public final Date date;

	public final double longitude;

	public final double latitude;

	public Concert(String artistName, Date date, double longitude, double latitude) {
		this.artistName = artistName;
		this.date = date;
		this.longitude = longitude;
		this.latitude = latitude;
	}

}

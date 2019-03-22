package me.nfdsr.regional.data;

import java.util.Optional;

public class IpinfoResponse {
	public IpinfoResponseError error;
	public String ip;
	public String hostname;
	public String city;
	public String region;
	public String country;
	public String loc;
	public String postal;
	public String org;
	public Boolean bogon;
}


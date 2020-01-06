package pyramid.models.searchdata;

import com.google.gson.annotations.SerializedName;

import pyramid.models.Track;

public class TopTracks {
	
	public TrackData[] track;  
	public TrackSearchData[] trackSearchData;
	public Track[] tracks;
	
	@SerializedName("@attr")
	public ArtistPage attr;




}

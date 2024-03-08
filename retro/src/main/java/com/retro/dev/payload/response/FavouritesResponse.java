package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.FavouritesDTO;


public class FavouritesResponse extends PaginationGenericResponse {

	
	//private Attributes
	////////////////////
	
	private FavouritesDTO favourites;
	private List<FavouritesDTO> favouritesList;
	
	
	//public Attributes
	////////////////////
	public FavouritesDTO getFavourites() {
		return favourites;
	}

	public void setFavourites(FavouritesDTO favourites) {
		this.favourites = favourites;
	}

	public List<FavouritesDTO> getFavouritesList() {
		return favouritesList;
	}

	public void setFavouritesList(List<FavouritesDTO> favouritesList) {
		this.favouritesList = favouritesList;
	}
	

}

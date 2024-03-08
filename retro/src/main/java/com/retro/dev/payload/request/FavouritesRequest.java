package com.retro.dev.payload.request;

import com.retro.dev.dtos.FavouritesDTO;


public class FavouritesRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private FavouritesDTO favourites;
	
	
	
	//public Attributes
	////////////////////
	public FavouritesDTO getFavourites() {
		return favourites;
	}

	public void setFavourites(FavouritesDTO favourites) {
		this.favourites = favourites;
	}

}
